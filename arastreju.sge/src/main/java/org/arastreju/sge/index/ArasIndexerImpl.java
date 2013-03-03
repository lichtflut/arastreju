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
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.inferencing.Inferencer;
import org.arastreju.sge.model.Statement;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.naming.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private List<Inferencer> inferencers = new ArrayList<Inferencer>();

	private final ConversationContext conversationContext;

	private final IndexProvider provider;

	// ----------------------------------------------------

	public ArasIndexerImpl(ConversationContext cc, IndexProvider provider) {
		this.conversationContext = cc;
		this.provider = provider;
	}

	// ----------------------------------------------------

	/**
	 * Add a soft inferencer.
	 * @param inferencer The inferencer.
	 * @return This.
	 */
	public ArasIndexerImpl add(Inferencer... inferencer) {
		Collections.addAll(inferencers, inferencer);
		return this;
	}

	// ----------------------------------------------------

	/**
	 * Index this node with all it's statements, regarding the current primary context.
	 * If the node already has been indexed, it will be updated.
	 * @param node The node to index.
	 */
	@Override
	public void index(ResourceNode node) {
		LOGGER.debug("Indexing ({})", node);

		Document doc = createDocument(node);
		LuceneIndex index = provider.forContext(conversationContext.getPrimaryContext());
		try {
			index.getWriter().updateDocument(new Term(IndexFields.QUALIFIED_NAME, normalizeQN(node.toURI())), doc); //creates if nonexistent
//			index.getWriter().commit(); // XXX to be revised when transactions enter the play
		} catch (IOException e) {
			String msg = "caught IOException while indexing resource " + node.toURI();
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
		LuceneIndex index = provider.forContext(conversationContext.getPrimaryContext());
		try {
			index.getWriter().deleteDocuments(new Term(IndexFields.QUALIFIED_NAME, normalizeQN(qn.toURI())));
//			index.getWriter().commit();
		} catch (IOException e) {
			String msg = "caught IOException while removing " + qn.toURI();
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	@Override
	public IndexSearchResult search(String query) {
		LOGGER.debug("search(" + query + ")");
		LuceneIndex index = provider.forContext(conversationContext.getPrimaryContext());
		org.apache.lucene.search.IndexSearcher searcher = index.getSearcher();

		/* default field is 'qn' as this is the only field common to all resources.
		 * (not that we're going to need a default field, anyway.) */
		QueryParser qp = new QueryParser(Version.LUCENE_35, IndexFields.QUALIFIED_NAME, new LowercaseWhitespaceAnalyzer(Version.LUCENE_35));
		qp.setAllowLeadingWildcard(true); //such queries should be avoided where possible nevertheless

		List<QualifiedName> resultList;
		try {
			/* we can use searcher.search(String, Collector) if we need all them results */
			AllHitsCollector collector = new AllHitsCollector();
			searcher.search(qp.parse(query), collector);

			resultList = collector.getList();
		} catch (IOException e) {
			String msg = "caught IOException while processing query '" + query + "'";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		} catch (ParseException e) {
			String msg = "caught ParseException while processing query '" + query + "'";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}

		return new FixedIndexSearchResult(resultList);
	}

	public void dump() {
		LuceneIndex index = provider.forContext(conversationContext.getPrimaryContext());
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

	private Document createDocument(ResourceNode node) {
		Document doc = new Document();
		doc.add(new Field(IndexFields.QUALIFIED_NAME, node.toURI(), Store.YES, Index.ANALYZED));

		Set<Statement> asserted = node.getAssociations();
		Set<Statement> inferred = new HashSet<Statement>();
		for (Statement stmt : asserted) {
			for (Inferencer inferencer : inferencers) {
				inferencer.addInferenced(stmt, inferred);
			}
			addFields(doc, stmt);
		}
		for (Statement stmt : inferred) {
			addFields(doc, stmt);
		}
		return doc;
	}

	private void addFields(Document doc, Statement stmt) {
		doc.add(makeField(stmt));
		Field f = makeGenField(stmt);
		if (!findValue(doc, f.name(), f.stringValue())) {
			doc.add(f);
		}
	}

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
		LuceneIndex index = provider.forContext(conversationContext.getPrimaryContext());
		provider.release(conversationContext.getPrimaryContext());
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
		LuceneIndex index = provider.forContext(conversationContext.getPrimaryContext());
		try {
			index.getWriter().deleteAll();
//			index.getWriter().commit();
		} catch (IOException e) {
			String msg = "caught IOException while clearing index";
			LOGGER.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}
}
