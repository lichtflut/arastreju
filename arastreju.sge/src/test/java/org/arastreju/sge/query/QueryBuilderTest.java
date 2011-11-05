/*
 * Copyright (C) 2011 lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.arastreju.sge.query;

import junit.framework.Assert;

import org.junit.Test;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created Nov 4, 2011
 * </p>
 *
 * @author Oliver Tigges
 */
public class QueryBuilderTest {
	
	@Test
	public void testSimple() {
		final QueryBuilder builder = new QueryBuilder();
		
		builder.add(new QueryParam("a", 1));
		final Query query = builder.query();
		
		Assert.assertTrue(query.getRoot() != null);
		Assert.assertEquals(0, query.getRoot().getChildren().size());
		Assert.assertEquals(QueryOperator.EQUALS, query.getRoot().getOperator());
		
	}

	@Test
	public void testPlainQueryBuilder() {
		final QueryBuilder builder = new QueryBuilder();
		
		builder
			.beginAnd()
				.add(new QueryParam("a", 1))
				.add(new QueryParam("b", 2))
				.add(new QueryParam("c", 3))
				.beginOr()
					.add(new QueryParam("d1", 1))
					.add(new QueryParam("d2", 2))
					.add(new QueryParam("d3", 3))
				.end()
			.end();
		final Query query = builder.query();
		
		Assert.assertTrue(query.getRoot() != null);
		Assert.assertEquals(4, query.getRoot().getChildren().size());
		Assert.assertEquals(3, query.getRoot().getChildren().get(3).getChildren().size());
		
	}
	
	@Test
	public void testPrepandQueryBuilder() {
		final QueryBuilder builder = new QueryBuilder();
		
		builder.add(new QueryParam("a", 1)).and().add(new QueryParam("b", 2));
		final Query query = builder.query();
		
		Assert.assertTrue(query.getRoot() != null);
		Assert.assertEquals(2, query.getRoot().getChildren().size());
		Assert.assertEquals(QueryOperator.AND, query.getRoot().getOperator());
		
	}
	
	@Test
	public void testComplexPrepandQueryBuilder() {
		final QueryBuilder builder = new QueryBuilder();
		
		builder
			.beginAnd()
				.add(new QueryParam("a", 1))
				.add(new QueryParam("b", 2))
				.add(new QueryParam("c", 3))
				.beginOr()
					.add(new QueryParam("d1", 1))
					.add(new QueryParam("d2", 2))
					.add(new QueryParam("d3", 3))
				.end()
			.end()
		.and()
			.add(new QueryParam("e4", 3));
		final Query query = builder.query();
		
		Assert.assertTrue(query.getRoot() != null);
		Assert.assertEquals(2, query.getRoot().getChildren().size());
		Assert.assertEquals(4, query.getRoot().getChildren().get(0).getChildren().size());
		
	}
	
}
