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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  Holds together everything needed to operate a lucene index.
 *  also takes care of mapping contexts to indexes and providing them
 *  to ArasIndexerImpl.
 * </p>
 *
 * <p>
 *  Created Feb 01, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
class LuceneIndex {

	private static final Logger LOGGER = LoggerFactory.getLogger(LuceneIndex.class);

	private final Directory dir;
	private final IndexWriter writer;
	private IndexReader reader;
	private org.apache.lucene.search.IndexSearcher searcher; //XXX name collision with our interface

	/* create/open index for context ctx */
	LuceneIndex(String indexRoot, Context ctx) throws IOException {
		String path = indexRoot + File.separatorChar + new sun.misc.BASE64Encoder().encode(ctx.toURI().getBytes());
		LOGGER.debug("creating LuceneIndex, root='" + indexRoot + "'; ctx='" + ctx.toString() + "'; (path='" + path + "')");
		this.dir = FSDirectory.open(new File(path));

		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_35, new LowercaseWhitespaceAnalyzer(Version.LUCENE_35));
		this.writer = new IndexWriter(dir, cfg);
		//		writer.setInfoStream(System.err);

		if (!IndexReader.indexExists(dir)) {
			/* cause the index to be created, this saves us a couple indexExists() checks */
			Document dummyDoc = new Document();
			dummyDoc.add(new Field("dummy_key", "dummy_value", Field.Store.NO, Field.Index.NOT_ANALYZED));
			writer.addDocument(dummyDoc);
			writer.commit();
			writer.deleteAll();
			writer.commit();
		}
		this.reader = IndexReader.open(dir, true);
		this.searcher = new org.apache.lucene.search.IndexSearcher(reader);
	}

	public Directory getDir() {
		return dir;
	}

	public org.apache.lucene.search.IndexSearcher getSearcher() {
		refreshReader();
		return searcher;
	}

	public IndexReader getReader() {
		refreshReader();
		return reader;
	}

	public IndexWriter getWriter() {
		return writer;
	}

	private void refreshReader() {
		try {
			if (reader.isCurrent()) {
				return;
			}
			IndexReader nr;
			nr = IndexReader.openIfChanged(reader, true);
			if (nr != null) {
				searcher.close();
				reader.close();
				reader = nr;
				searcher = new org.apache.lucene.search.IndexSearcher(reader);
			}
		} catch (IOException e) {
			String msg = "caught IOException while refreshing IndexReader";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
}
