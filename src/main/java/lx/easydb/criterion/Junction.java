package lx.easydb.criterion;

import java.util.ArrayList;
import java.util.List;

import lx.easydb.ICriteria;
import lx.easydb.StringHelper;

/**
 * @author Longshine
 *
 */
public class Junction implements IExpression {
	@SuppressWarnings("rawtypes")
	private ArrayList expressions = new ArrayList();
	private String op;
	
	public Junction(String op) {
		this.op = op;
	}
	
	@SuppressWarnings("rawtypes")
	public List getExpressions() {
		return this.expressions;
	}
	
	@SuppressWarnings("unchecked")
	public Junction add(IExpression exp) {
		expressions.add(exp);
		return this;
	}
	
	public String getOp() {
		return this.op;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toString() {
		return '(' + StringHelper.join(' ' + op + ' ', expressions.iterator()) + ')';
	}
}
