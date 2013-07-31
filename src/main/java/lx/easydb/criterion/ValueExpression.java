/**
 * Copyright (c) 2011 SmeshLink Technology Corporation.
 * All rights reserved.
 * 
 * This file is part of the SmeshServer, a gateway middleware for WSN.
 * Please see README for more information.
 */
package lx.easydb.criterion;

import java.util.Date;

import lx.easydb.ICriteria;

/**
 * @author Longshine
 *
 */
public class ValueExpression implements IExpression {
	private Object value;
	
	public ValueExpression(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public String toString() {
		if (this.value instanceof String || this.value instanceof Date)
			return "'" + this.value.toString() + "'";
		else
			return this.value.toString();
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
}
