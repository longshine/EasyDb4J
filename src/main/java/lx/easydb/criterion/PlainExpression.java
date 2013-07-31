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
public class PlainExpression implements IExpression {
	private String value;
	
	public PlainExpression(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String toString() {
		return this.value;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
}
