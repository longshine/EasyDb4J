/**
 * Copyright (c) 2011 SmeshLink Technology Corporation.
 * All rights reserved.
 * 
 * This file is part of the SmeshServer, a gateway middleware for WSN.
 * Please see README for more information.
 */
package lx.easydb.criterion;

import lx.easydb.ICriteria;

/**
 * @author Longshine
 *
 */
public class Order implements IFragment {
	private IExpression expression;
	private boolean ascending;
	
	public Order(IExpression expression, boolean ascending) {
		this.expression = expression;
		this.ascending = ascending;
	}

	public boolean isAscending() {
		return this.ascending;
	}
	
	public IExpression getExpression() {
		return expression;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toString() {
		return expression + " " + (ascending ? "ASC" : "DESC");
	}
	
	public static Order asc(String fieldName) {
		return new Order(Clauses.field(fieldName), true);
	}
	
	public static Order asc(IExpression expression) {
		return new Order(expression, true);
	}
	
	public static Order desc(String fieldName) {
		return new Order(Clauses.field(fieldName), false);
	}
}
