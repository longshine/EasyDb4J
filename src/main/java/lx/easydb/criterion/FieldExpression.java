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
public class FieldExpression implements IExpression {
	private String filedName;
	private String tableName;
	
	public FieldExpression(String fieldName) {
		this(fieldName, null);
	}
	
	public FieldExpression(String fieldName, String tableName) {
		this.filedName = fieldName;
		this.tableName = tableName;
	}
	
	public String getFieldName() {
		return this.filedName;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	
	public String toString() {
		if (null == this.tableName || 0 == this.tableName.length())
			return this.filedName;
		else
			return this.tableName + "." + this.filedName;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
}
