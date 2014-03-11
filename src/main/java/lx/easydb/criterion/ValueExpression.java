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
