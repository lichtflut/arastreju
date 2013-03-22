package org.arastreju.sge.spi.impl;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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

    public static final String PID = "pid";

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneBasedNodeKeyTable.class);

    public static final String QN = "qn";

    // ----------------------------------------------------

    private final IndexWriterConfig config =
            new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));

    private final Directory dir;

    private final IndexWriter writer;

    // ----------------------------------------------------

    public static LuceneBasedNodeKeyTable<NumericPhysicalNodeID> forNumericIDs(String baseDir) throws IOException {
        return new LuceneBasedNodeKeyTable<NumericPhysicalNodeID>(baseDir) {
            @Override
            protected NumericPhysicalNodeID createID(Document doc) {
                NumericField field = (NumericField) doc.getFieldable(PID);
                return new NumericPhysicalNodeID(field.getNumericValue());
            }

            @Override
            protected void setID(Document doc, NumericPhysicalNodeID id) {
                NumericField field = new NumericField(PID, Field.Store.YES, false);
                field.setLongValue(id.asLong());
                doc.add(field);
            }
        };
    }

    // ----------------------------------------------------

    protected LuceneBasedNodeKeyTable(String baseDir) throws IOException {
        final File indexDir = new File(baseDir, "__qn_index");
        LOGGER.info("Creating node key table index in {}.", indexDir);
        boolean created = indexDir.mkdir();
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
        try {
            IndexReader reader = reader();
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs result = searcher.search(query, 2);
            searcher.close();
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

    private IndexReader reader() throws IOException {
        return IndexReader.open(dir, true);
    }

    private IndexWriter writer() throws IOException {
        return writer;
    }

}
