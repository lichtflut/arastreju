/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.bindings.neo4j.query;

import org.arastreju.bindings.neo4j.index.NeoIndex;
import org.arastreju.bindings.neo4j.index.ResourceIndex;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.query.QueryBuilder;
import org.arastreju.sge.query.QueryExpression;
import org.arastreju.sge.query.QueryParam;
import org.arastreju.sge.query.QueryResult;

import de.lichtflut.infra.exceptions.NotYetSupportedException;

/**
 * <p>
 *  Query Builder specific for Neo4j and Lucene.
 * </p>
 *
 * <p>
 * 	Created Nov 7, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class NeoQueryBuilder extends QueryBuilder {
	
	private final ResourceIndex index;
	
	// -----------------------------------------------------
	
	/**
	 * @param index
	 */
	public NeoQueryBuilder(final ResourceIndex index) {
		this.index = index;
	}
	
	// -----------------------------------------------------

	/** 
	 * {@inheritDoc}
	 */
	public QueryResult getResult() {
		final String queryString = toQueryString();
		return index.search(queryString);
	}

	/** 
	 * {@inheritDoc}
	 */
	public ResourceNode getSingleNode() {
		final String queryString = toQueryString();
		final QueryResult result = index.search(queryString);
		return result.getSingleNode();
	}
	
	// -----------------------------------------------------
	
	protected String toQueryString() {
		final StringBuilder sb = new StringBuilder();
		append(getRoot(), sb);
		return sb.toString();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	protected QueryExpression getRoot() {
		return super.getRoot();
	}
	
	// -----------------------------------------------------

	private void append(final QueryExpression exp, final StringBuilder sb) {
		if (exp.isLeaf()) {
			appendLeaf(exp.getQueryParam(), sb);
		} else {
			sb.append("(");
			boolean first = true;
			for (QueryExpression child : exp.getChildren()) {
				if (first) {
					first = false;
				} else {
					sb.append(" " + exp.getOperator().name() + " ");
				}
				append(child, sb);
			}
			sb.append(")");
		}
	}
	
	private void appendLeaf(final QueryParam param, final StringBuilder sb) {
		switch(param.getOperator()) {
		case EQUALS:
			sb.append(normalizeKey(param.getName()) + ":");
			break;
		case HAS_URI:
			sb.append(NeoIndex.INDEX_KEY_RESOURCE_URI + ":");
			break;
		case HAS_VALUE:
			sb.append(NeoIndex.INDEX_KEY_RESOURCE_VALUE + ":");
			break;
		default:
			throw new NotYetSupportedException(param.getOperator());
		}
		sb.append(normalizeValue(param.getValue().toString()));
	}
	
	private String normalizeKey(final String key) {
		return key.replaceAll(":", "\\\\:");
	}
	
	private String normalizeValue(final String key) {
		return key.trim().toLowerCase().replaceAll(":", "\\\\:");
	}
	
}
