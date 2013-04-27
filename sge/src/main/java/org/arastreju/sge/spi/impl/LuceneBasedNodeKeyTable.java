/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arastreju.sge.spi.impl;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.arastreju.sge.naming.QualifiedName;
import org.arastreju.sge.persistence.NodeKeyTable;
import org.arastreju.sge.spi.PhysicalNodeID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 *  Node key table based on a lucene index.
 * </p>
 *
 * <p>
 *  Created Mar 22, 2013
 * </p>
 *
 * @author Oliver Tigges
 */
public abstract class LuceneBasedNodeKeyTable<T extends PhysicalNodeID> implements NodeKeyTable<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneBasedNodeKeyTable.class);

    public static final String PID = "pid";

    public static final String QN = "qn";

    // ----------------------------------------------------

    private final Directory dir;

    private final IndexWriter writer;

    // ----------------------------------------------------

    public static LuceneBasedNodeKeyTable<NumericPhysicalNodeID> forNumericIDs(String baseDir) throws IOException {
        return new LuceneBasedNodeKeyTable<NumericPhysicalNodeID>(baseDir) {
            @Override
            protected NumericPhysicalNodeID createID(Document doc) {
                IndexableField field = doc.getField(PID);
                return new NumericPhysicalNodeID(field.numericValue());
            }

            @Override
            protected void setID(Document doc, NumericPhysicalNodeID id) {
                LongField field = new LongField(PID, id.asLong(), Field.Store.YES);
                doc.add(field);
            }
        };
    }

    // ----------------------------------------------------

    protected LuceneBasedNodeKeyTable(String baseDir) throws IOException {
        final File indexDir = new File(baseDir, "__qn_index");
        LOGGER.info("Creating node key table index in {}.", indexDir);
        boolean created = indexDir.mkdir();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, new StandardAnalyzer(Version.LUCENE_35));
        this.dir = FSDirectory.open(indexDir);
        this.writer = new IndexWriter(dir, config);
        if (created) {
            LOGGER.info("Index directory newly created. Starting initialization.");
            writer().commit();
        }
    }

    // ----------------------------------------------------

    @Override
    public T lookup(QualifiedName qualifiedName) {
        TermQuery query = new TermQuery(new Term(QN, qualifiedName.toURI()));
        IndexReader reader = reader();
        try {

            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs result = searcher.search(query, 2);

            if (result.scoreDocs.length == 1) {
                Document document = reader.document(result.scoreDocs[0].doc);
                return createID(document);
            } else if (result.scoreDocs.length == 0) {
                return null;
            } else {
                throw new IllegalStateException("Found more than one document for qualified name: "
                        + qualifiedName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not query by qualified name.", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close index reader.", e);
            }
        }
    }

    @Override
    public synchronized void put(QualifiedName qn, T physicalID) {
        Document doc = new Document();
        doc.add(new Field(QN, qn.toURI(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        setID(doc, physicalID);
        try {
            final IndexWriter writer = writer();
            writer.addDocument(doc);
            writer.commit();
        } catch (IOException e) {
            throw new RuntimeException("Could not map phyiscal ID to qualified name in index.", e);
        }
    }

    @Override
    public void remove(QualifiedName qn) {
        try {
            final IndexWriter writer = writer();
            writer.deleteDocuments(new Term(QN, qn.toURI()));
            writer.commit();
        } catch (IOException e) {
            throw new RuntimeException("Could not remove qualified name from index.", e);
        }
    }

    @Override
    public void shutdown() throws IOException {
        writer.close();
        dir.close();
    }

    // ----------------------------------------------------

    protected abstract T createID(Document doc);

    protected abstract void setID(Document doc, T id);

    // ----------------------------------------------------

    private IndexReader reader() {
        try {
            return DirectoryReader.open(dir);
        } catch (IOException e) {
            throw new RuntimeException("Unable to obtain an index reader.", e);
        }
    }

    private IndexWriter writer() throws IOException {
        return writer;
    }

}
