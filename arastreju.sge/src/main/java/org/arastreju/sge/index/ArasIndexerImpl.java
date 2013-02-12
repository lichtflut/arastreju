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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
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
		LOGGER.debug("index(" + node + ")");
		Document doc = new Document();
		doc.add(new Field(IndexFields.QUALIFIED_NAME, node.toURI(), Store.YES, Index.ANALYZED));

		for (Statement stmt : node.getAssociations()) {
			doc.add(makeField(stmt));
			Field f = makeGenField(stmt);
			if (!findValue(doc, f.name(), f.stringValue())) {
				doc.add(f);
			}
		}

		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		try {
			index.getWriter().updateDocument(new Term(IndexFields.QUALIFIED_NAME, normalizeQN(node.toURI())), doc); //creates if nonexistent
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
		LOGGER.debug("index(" + statement + ")");
		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		IndexWriter writer = index.getWriter();
		org.apache.lucene.search.IndexSearcher searcher = index.getSearcher();
		IndexReader reader = searcher.getIndexReader();

		ResourceID subject = statement.getSubject();
		ResourceID pred = statement.getPredicate();

		try {
			Document doc;
			Query q = new TermQuery(new Term(IndexFields.QUALIFIED_NAME, normalizeQN(subject.toURI())));
			TopDocs top = searcher.search(q, 1);
			if (top.totalHits > 0) {
				doc = reader.document(top.scoreDocs[0].doc);
				if (doc.get(pred.toURI()) != null) {
					doc.removeFields(pred.toURI());
				}
			} else {
				doc = new Document();
				doc.add(new Field(IndexFields.QUALIFIED_NAME, subject.toURI(), Store.YES, Index.ANALYZED));
			}

			doc.add(makeField(statement));

			Field genField = makeGenField(statement);
			if (!findValue(doc, genField.name(), genField.stringValue())) {
				doc.add(genField);
			}

			writer.updateDocument(new Term(IndexFields.QUALIFIED_NAME, normalizeQN(subject.toURI())), doc);
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
		LOGGER.debug("remove(" + qn + ")");
		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		try {
			index.getWriter().deleteDocuments(new Term(IndexFields.QUALIFIED_NAME, normalizeQN(qn.toURI())));
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

		/* default field is 'qn' as this is the only field common to all resources.
		 * (not that we're going to need a default field, anyway.) */
		QueryParser qp = new QueryParser(Version.LUCENE_35, IndexFields.QUALIFIED_NAME, new LowercaseWhitespaceAnalyzer(Version.LUCENE_35));
		qp.setAllowLeadingWildcard(true); //such queries should be avoided where possible nevertheless

		TopDocs top;
		List<QualifiedName> resultList = new LinkedList<QualifiedName>();
		try {
			/* we can use searcher.search(String, Collector) if we need all them results */
			top = searcher.search(qp.parse(query), MAX_RESULTS);
			for (int i = 0; i < top.totalHits; i++) {
				Document hit = reader.document(top.scoreDocs[i].doc);
				resultList.add(new QualifiedName(hit.get(IndexFields.QUALIFIED_NAME)));
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

	public void dump() {
		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		org.apache.lucene.search.IndexSearcher searcher = index.getSearcher();
		IndexReader reader = searcher.getIndexReader();

		try {
			TopDocs top = searcher.search(new MatchAllDocsQuery(), 100);
			for (int i = 0; i < top.totalHits; i++) {
				Document doc = reader.document(top.scoreDocs[i].doc);
				LOGGER.info("---Document--- id: " + top.scoreDocs[i].doc);
				List<Fieldable> fields = doc.getFields();
				for (Fieldable f : fields) {
					LOGGER.info("\tField: name='" + f.name() + "', val='" + f.stringValue() + "'");
				}

			}
		} catch (IOException e) {
			String msg = "caught IOException while dumping index";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}

	}

	// ----------------------------------------------------

	private Field makeGenField(Statement stmt) {
		Field f;

		if (stmt.getObject().isResourceNode()) {
			f = new Field(IndexFields.RESOURCE_RELATION, stmt.getObject().asResource().toURI(), Store.YES, Index.ANALYZED);
		} else {
			f = new Field(IndexFields.RESOURCE_VALUE, stmt.getObject().asValue().getStringValue(), Store.YES, Index.ANALYZED); //analyzed, right?
		}

		return f;
	}

	private Field makeField(Statement stmt) {
		Field f;

		if (stmt.getObject().isResourceNode()) {
			f = new Field(stmt.getPredicate().toURI(), stmt.getObject().asResource().toURI(), Store.YES, Index.ANALYZED);
		} else {
			/* This replicates the behaviour of the old neo index, for now.
			 * TODO: Should probably use different sorts of fields  (like
			 * NumericField) where applicable to leverage more of lucenes functionality */
			f = new Field(stmt.getPredicate().toURI(), stmt.getObject().asValue().getStringValue(), Store.YES, Index.ANALYZED); //analyzed, right?
		}

		return f;
	}

	private boolean findValue(Document doc, String fieldName, String val) {
		String[] vals = doc.getValues(fieldName);
		for (String v : vals) {
			if (v.equals(val)) {
				return true;
			}
		}

		return false;
	}

	/* this is applied whenever we search for a qn.
	 * XXX do we actually want case-insensitive search on URI?
	 * LuceneQueryBuilder.normalizeValue() sort of enforces/suggests this. */
	private String normalizeQN(String qn) {
		return qn.toLowerCase();
	}

	/* no more calls to this object after close() */
	public void close() {
		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		LuceneIndex.drop(conversationContext.getPrimaryContext());
		try {
			index.getReader().close();
			index.getWriter().close();
		} catch (IOException e) {
			String msg = "caught IOException while closing reader/writer";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	public void clear() {
		LuceneIndex index = LuceneIndex.forContext(conversationContext.getPrimaryContext());
		try {
			index.getWriter().deleteAll();
			index.getWriter().commit();
		} catch (IOException e) {
			String msg = "caught IOException while clearing index";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}
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

	public static void drop(Context ctx) {
		synchronized (indexMap) {
			indexMap.remove(ctx == null ? nullCtxDummy : ctx);
		}
	}

	/* create/open index for context ctx */
	private LuceneIndex(String indexRoot, Context ctx) throws IOException {
		String path = indexRoot + File.separatorChar + new sun.misc.BASE64Encoder().encode(ctx.toURI().getBytes());
		LOGGER.debug("creating LuceneIndex, root='" + indexRoot + "'; ctx='" + ctx.toString() + "'; (path='" + path + "')");
		this.dir = FSDirectory.open(new File(path));

		IndexWriterConfig cfg = new IndexWriterConfig(Version.LUCENE_35, new LowercaseWhitespaceAnalyzer(Version.LUCENE_35));
		this.writer = new IndexWriter(dir, cfg);
		//		writer.setInfoStream(System.err);

		if (!IndexReader.indexExists(dir)) {
			/* cause the index to be created, this saves us a couple indexExists() checks */
			Document dummyDoc = new Document();
			dummyDoc.add(new Field("dummy_key", "dummy_value", Store.NO, Index.NOT_ANALYZED));
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
