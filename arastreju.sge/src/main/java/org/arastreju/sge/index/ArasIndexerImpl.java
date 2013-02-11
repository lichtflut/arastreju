/*
 * Copyright (C) 2013 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 *
 * The Arastreju-Neo4j binding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arastreju.sge.index;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.context.Context;
import org.arastreju.sge.context.SimpleContextID;
import org.arastreju.sge.model.ResourceID;
import org.arastreju.sge.model.SimpleResourceID;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *  Indexer implementation using Lucene
 * </p>
 *
 * <p>
 * 	Created Feb 01, 2013
 * </p>
 *
 * @author Timo Buhrmester
 */
public class ArasIndexerImpl implements IndexUpdator, IndexSearcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(ArasIndexerImpl.class);

	/* turn this into a configuration setting */
	private static final int MAX_RESULTS = 100;

	private final ConversationContext conversationContext;

	public ArasIndexerImpl(ConversationContext cc) {
		conversationContext = cc;
	}

	/**
	 * Index this node with all it's statements, regarding the current primary context.
	 * If the node already has been indexed, it will be updated.
	 * @param node The node to index.
	 */
	@Override
	public void index(ResourceNode node) {
		LOGGER.debug("LUCENEINDEX: index(" + node + ")");
		Document doc = new Document();
		doc.add(new Field("uri", node.toURI(), Store.YES, Index.NOT_ANALYZED));

		for (Statement stmt : node.getAssociations()) {
			doc.add(makeField(stmt));
		}

		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		try {
			index.getWriter().updateDocument(new Term("uri", node.toURI()), doc); //creates if nonexistent
			index.getWriter().commit(); // XXX to be revised when transactions enter the play
		} catch (IOException e) {
			String msg = "caught IOException while indexing resource " + node.toURI();
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	/**
	 * Add this statement to the index.
	 * @param statement The statement to index.
	 */
	@Override
	public void index(Statement statement) {
		LOGGER.debug("LUCENEINDEX: index(" + statement + ")");
		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		IndexWriter writer = index.getWriter();
		org.apache.lucene.search.IndexSearcher searcher = index.getSearcher();
		IndexReader reader = searcher.getIndexReader();

		ResourceID subject = statement.getSubject();
		ResourceID pred = statement.getPredicate();

		try {
			Document doc;
			Query q = new TermQuery(new Term("uri", subject.toURI()));
			TopDocs top = searcher.search(q, 1);
			if (top.totalHits > 0) {
				doc = reader.document(top.scoreDocs[0].doc);
				if (doc.get(pred.toURI()) != null) {
					doc.removeFields(pred.toURI());
				}
			} else {
				doc = new Document();
				doc.add(new Field("uri", subject.toURI(), Store.YES, Index.NOT_ANALYZED));
			}

			doc.add(makeField(statement));

			writer.updateDocument(new Term("uri", subject.toURI()), doc);
			writer.commit();
		} catch (IOException e) {
			String msg = "caught IOException while indexing statement " + statement;
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	/**
	 * Remove the resource identified by the qualified name form the index.
	 * @param qn The qualified name.
	 */
	@Override
	public void remove(QualifiedName qn) {
		LOGGER.debug("LUCENEINDEX: remove(" + qn + ")");
		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		try {
			LOGGER.debug("deleting c");
			index.getWriter().deleteDocuments(new Term("uri", qn.toURI()));
			LOGGER.debug("committing c");
			index.getWriter().commit();
		} catch (IOException e) {
			String msg = "caught IOException while removing " + qn.toURI();
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	@Override
	public Iterable<QualifiedName> search(String query) {
		LOGGER.debug("search(" + query + ")");
		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		org.apache.lucene.search.IndexSearcher searcher = index.getSearcher();
		IndexReader reader = searcher.getIndexReader();

		/* default field is 'uri' as this is the only field common to all resources.
		 * (not that we're going to need a default field, anyway.) */
		QueryParser qp = new QueryParser(Version.LUCENE_35, "uri", new StandardAnalyzer(Version.LUCENE_35));

		TopDocs top;
		List<QualifiedName> resultList = new LinkedList<QualifiedName>();
		try {
			/* we can use searcher.search(String, Collector) if we need all them results */
			top = searcher.search(qp.parse(query), MAX_RESULTS);
			for (int i = 0; i < top.totalHits; i++) {
				Document hit = reader.document(top.scoreDocs[i].doc);
				resultList.add(new QualifiedName(hit.get("uri")));
			}
		} catch (IOException e) {
			String msg = "caught IOException while processing query '" + query + "'";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		} catch (ParseException e) {
			String msg = "caught ParseException while processing query '" + query + "'";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}

		return resultList;
	}

	// ----------------------------------------------------

	private Field makeField(Statement stmt) {
		Field f;

		if (stmt.getObject().isResourceNode()) {
			f = new Field(stmt.getPredicate().toURI(), stmt.getObject().asResource().toURI(), Store.YES, Index.NOT_ANALYZED);
		} else {
			/* This replicates the behaviour of the old neo index, for now.
			 * TODO: Should probably use different sorts of fields  (like
			 * NumericField) where applicable to leverage more of lucenes functionality */
			f = new Field(stmt.getPredicate().toURI(), stmt.getObject().asValue().getStringValue(), Store.YES, Index.ANALYZED); //analyzed, right?
		}

		return f;
	}
}


/* holds together everything needed to operate a lucene index.
 * also takes care of mapping contexts to indexes and providing them
 * to ArasIndexerImpl */
class LuceneIndex {
	private static final Logger LOGGER = LoggerFactory.getLogger(LuceneIndex.class);

	/* stores one index per context */
	private static final Map<Context, LuceneIndex> indexMap = new HashMap<Context, LuceneIndex>();

	/* placeholder - to be turned into a proper configuration setting */
	private static String indexRoot = "/tmp/myindex";

	private final Directory dir;
	private final IndexWriter writer;
	private IndexReader reader;
	private org.apache.lucene.search.IndexSearcher searcher; //XXX name collision with our interface

	private static final Context nullCtxDummy = new SimpleContextID(new SimpleResourceID().getQualifiedName());

	/* create index if nonexistent */
	public static LuceneIndex forContext(Context ctx) {
		if (ctx == null)
			ctx = nullCtxDummy;
		LuceneIndex index;
		synchronized (indexMap) {
			if ((index = indexMap.get(ctx)) == null) {
				try {
					indexMap.put(ctx, (index = new LuceneIndex(indexRoot, ctx)));
				} catch (IOException e) {
					String msg = "caught IOException while creating index for context " + ctx.toURI();
					LOGGER.error(msg, e);
					throw new RuntimeException(msg, e);
				}
			}
		}

		return index;
	}

	/* create/open index for context ctx */
	private LuceneIndex(String indexRoot, Context ctx) throws IOException {
		String path = indexRoot + File.separatorChar + new sun.misc.BASE64Encoder().encode(ctx.toURI().getBytes());
		this.dir = FSDirectory.open(new File(path));

		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
		this.writer = new IndexWriter(dir, cfg);
		//		writer.setInfoStream(System.err);

		/* add a dummy document to a fresh index so as to avoid having to
		 * check indexExists() every time
		 */
		if (!IndexReader.indexExists(dir)) {
			Document dummyDoc = new Document();
			dummyDoc.add(new Field("dummy_key", "dummy_value", Store.NO, Index.NOT_ANALYZED));
			writer.addDocument(dummyDoc);
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
			if (reader.isCurrent())
				return;
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
