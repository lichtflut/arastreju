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
package org.arastreju.sge.query;

import junit.framework.Assert;

import org.arastreju.sge.model.nodes.ResourceNode;
import org.junit.Test;

import de.lichtflut.infra.exceptions.NotYetImplementedException;

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
	
	static class TestQueryBuilder extends QueryBuilder {
		public QueryResult getResult() {
			throw new NotYetImplementedException();
		}
	}
	
	@Test
	public void testSimple() {
		final QueryBuilder builder = new TestQueryBuilder();
		
		builder.add(new FieldParam("a", 1));

		final QueryExpression root = builder.getRoot();
		Assert.assertTrue(root != null);
		Assert.assertEquals(0, root.getChildren().size());
		Assert.assertEquals(QueryOperator.EQUALS, root.getOperator());
		
	}

	@Test
	public void testPlainQueryBuilder() {
		final QueryBuilder builder = new TestQueryBuilder();
		
		builder
			.beginAnd()
				.add(new FieldParam("a", 1))
				.add(new FieldParam("b", 2))
				.add(new FieldParam("c", 3))
				.beginOr()
					.add(new FieldParam("d1", 1))
					.add(new FieldParam("d2", 2))
					.add(new FieldParam("d3", 3))
				.end()
			.end();

		final QueryExpression root = builder.getRoot();
		Assert.assertTrue(root != null);
		Assert.assertEquals(4, root.getChildren().size());
		Assert.assertEquals(3, root.getChildren().get(3).getChildren().size());
		
	}
	
	@Test
	public void testPrepandQueryBuilder() {
		final QueryBuilder builder = new TestQueryBuilder();
		
		builder.add(new FieldParam("a", 1)).and().add(new FieldParam("b", 2));
		
		final QueryExpression root = builder.getRoot();
		Assert.assertTrue(root != null);
		Assert.assertEquals(2, root.getChildren().size());
		Assert.assertEquals(QueryOperator.AND, root.getOperator());
		
	}
	
	@Test
	public void testComplexPrepandQueryBuilder() {
		final QueryBuilder builder = new TestQueryBuilder();
		
		builder
			.beginAnd()
				.add(new FieldParam("a", 1))
				.add(new FieldParam("b", 2))
				.add(new FieldParam("c", 3))
				.beginOr()
					.add(new FieldParam("d1", 1))
					.add(new FieldParam("d2", 2))
					.add(new FieldParam("d3", 3))
				.end()
			.end()
		.and()
			.add(new FieldParam("e4", 3));
		
		final QueryExpression root = builder.getRoot();
        System.out.println(root);
		Assert.assertTrue(root != null);
		Assert.assertEquals(2, root.getChildren().size());
		Assert.assertEquals(4, root.getChildren().get(0).getChildren().size());
		
	}
	
	@Test
	public void testSimpleNot() {
		final QueryBuilder query = new TestQueryBuilder();
		
		query.add(new FieldParam("a", 1)).and().not().add(new FieldParam("b", 2));
		
		final QueryExpression root = query.getRoot();
		Assert.assertTrue(root != null);
		Assert.assertEquals(2, root.getChildren().size());
	}

    @Test
    public void testStringRepresentation() {
        final QueryBuilder query = new TestQueryBuilder();
        query.beginAnd()
                    .add(new FieldParam("a", 1))
                    .addURI("http://bla")
                    .addValue("hello")
                    .not().addRelation("ns:a")
                    .beginOr()
                        .add(new FieldParam("d1", 1))
                        .add(new FieldParam("d2", 2))
                        .add(new FieldParam("d3", 3))
                    .end()
                .end();

        String queryString = query.toString();
        Assert.assertEquals("AND('a'='1',QN='http://bla',VAL='hello',NOT(REL='ns:a',OR('d1'='1','d2'='2','d3'='3')))",
                queryString.replaceAll(" ", ""));
    }
	
}
