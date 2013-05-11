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
package org.arastreju.sge.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.arastreju.sge.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 *  Holds together everything needed to operate a lucene index.
 *  also takes care of mapping contexts to indexes and providing them
 *  to ConversationIndex.
 * </p>
 *
 * <p>
 *  Created Feb 01, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
public class ContextIndex {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextIndex.class);

    // ----------------------------------------------------

	private final Directory dir;

	private final IndexWriter writer;

    // ----------------------------------------------------

    /**
     * Constructor.
     * @param baseDirectory The base directory of the indices.
     * @param ctx The context to be used.
     * @throws IOException When index cannot be obtained.
     */
	ContextIndex(String baseDirectory, Context ctx) throws IOException {
		String path = baseDirectory + File.separatorChar + new sun.misc.BASE64Encoder().encode(ctx.toURI().getBytes());
		LOGGER.debug("creating ContextIndex, root='" + baseDirectory + "'; ctx='" + ctx.toString() + "'; (path='" + path + "')");
		this.dir = FSDirectory.open(new File(path));

		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_35, new LowercaseWhitespaceAnalyzer(Version.LUCENE_35));
		this.writer = new IndexWriter(dir, cfg);

        ensureIndexExists();
    }

	public IndexReader createReader() throws IOException {
        return IndexReader.open(writer, false);
    }

	public IndexWriter getWriter() {
		return writer;
	}

    public void close() throws IOException {
        writer.close();
        dir.close();
    }

    // ----------------------------------------------------

    private void ensureIndexExists() throws IOException {
        if (!IndexReader.indexExists(dir)) {
            /* cause the index to be created, this saves us a couple indexExists() checks */
            Document dummyDoc = new Document();
            dummyDoc.add(new Field("dummy_key", "dummy_value", Field.Store.NO, Field.Index.NOT_ANALYZED));
            writer.addDocument(dummyDoc);
            writer.commit();
            writer.deleteAll();
            writer.commit();
        }
    }

}
