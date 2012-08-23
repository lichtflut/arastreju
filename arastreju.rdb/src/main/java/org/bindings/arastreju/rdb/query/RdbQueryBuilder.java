/*
 * Copyright 2012 by lichtflut Forschungs- und Entwicklungsgesellschaft mbH
 */
package org.bindings.arastreju.rdb.query;

import java.util.List;

import org.arastreju.bindings.rdb.SQL;
import org.arastreju.bindings.rdb.jdbc.Column;
import org.arastreju.bindings.rdb.jdbc.ConnectionWraper;
import org.arastreju.bindings.rdb.jdbc.SQLQueryBuilder;
import org.arastreju.bindings.rdb.tx.JdbcTxProvider;
import org.arastreju.sge.ConversationContext;
import org.arastreju.sge.model.nodes.ResourceNode;
import org.arastreju.sge.query.QueryBuilder;
import org.arastreju.sge.query.QueryException;
import org.arastreju.sge.query.QueryExpression;
import org.arastreju.sge.query.QueryOperator;
import org.arastreju.sge.query.QueryParam;
import org.arastreju.sge.query.QueryResult;
import org.arastreju.sge.query.ValueParam;

import de.lichtflut.infra.exceptions.NotYetSupportedException;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

/**
 * <p>
 *  [DESCRIPTION]
 * </p>
 *
 * <p>
 * 	Created 21.08.2012
 * </p>
 *
 * @author Raphael Esterle

 */
public class RdbQueryBuilder extends QueryBuilder {
	
	private final JdbcTxProvider tx;
	
	public RdbQueryBuilder(ConnectionWraper cw){
		tx = new JdbcTxProvider(cw);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public QueryResult getResult() {
		System.out.println(toSQL(getRoot()));
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ResourceNode getSingleNode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String SQLOperator(QueryOperator operator){
		switch(operator){
			case AND:
				return SQL.OR.value();
			case OR:
				return SQL.OR.value();
			case NOT:;
				return SQL.NOT.value();
			case HAS_RELATION:
				return "relation";
			case HAS_URI:
				return "uri";
			case HAS_VALUE:
				return "value";
		default:
			break;
		}
		return "";
	}
	
	private String getType(Object o){
		if(o.getClass().equals(ValueParam.class))
			return Column.OBJECT.value();
		return o.getClass().toString();
	}
	
	private String toSQL(QueryExpression root){
		System.out.println(root.toString());
		StringBuilder sb = new StringBuilder();
		if(root.isLeaf()){
			QueryParam param = root.getQueryParam();
			if(QueryOperator.NOT.equals(root.getOperator())){
				sb.append(" "+SQL.NOT.value()+" ");
			}
			appendLeaf(param, sb);
			sb.append(SQL.WHITESP.value());
		}else{
			String operator = SQLOperator(root.getOperator());
			sb.append(SQL.BRACKET_OPEN.value());
			List<QueryExpression> children = root.getChildren();
			for(int i=0; i<children.size(); i++){
				sb.append(toSQL(children.get(i)));
				if(i<children.size()-1)
					sb.append(operator);
					sb.append(SQL.WHITESP.value());
			}
			sb.append(SQL.BRACKET_CLOSE.value());
		}
		return sb.toString();
	}
	
	private void appendLeaf(final QueryParam param, final StringBuilder sb) {
		boolean bc = false;
		String value = normalizeValue(param.getValue());
		if (value == null || value.length() == 0) {
			throw new QueryException("Invalid query value: " + param);
		}
		switch(param.getOperator()) {
		case EQUALS:
			sb.append(SQL.BRACKET_OPEN.value()).
			append(Column.PREDICATE.value()).
			append(SQL.EQUALS.value()).
			append(SQL.QUOTE.value()).
			append(param.getName()).
			append(SQL.QUOTE.value()).
			append(SQL.WHITESP.value()).
			append(SQL.AND.value()).
			append(SQL.WHITESP.value()).
			append(Column.OBJECT.value()).
			append(SQL.EQUALS.value());
			bc=true;
			
			//sb.append(normalizeKey(param.getName())).append(SQL.EQUALS.value());
			break;
		case NOT:
			System.out.println("NOOOT");
			break;
		case HAS_URI:
			sb.append(Column.SUBJECT.value() + SQL.EQUALS.value());
			break;
		case HAS_VALUE:
			sb.append(Column.OBJECT.value() + SQL.EQUALS.value());
			break;
		case HAS_RELATION:
			sb.append(Column.PREDICATE.value() + SQL.EQUALS.value());
			break;
		case SUB_QUERY:
			sb.append(param.getValue());
			// abort here!
			return;
		default:
			throw new NotYetSupportedException(param.getOperator());
		}
		sb.append(value);
		if(bc)
			sb.append(SQL.BRACKET_CLOSE.value());
	}
	
	private String normalizeKey(final String key) {
		return key;//.replaceAll(":", "\\\\:");
	}
	
	private String normalizeValue(final Object value) {
		if (value == null) {
			return null;
		}
		String normalized = SQL.QUOTE.value()+value.toString().trim().toLowerCase()+SQL.QUOTE.value();
		
		return normalized.replaceAll(":", "\\\\:");
	}
	
}
