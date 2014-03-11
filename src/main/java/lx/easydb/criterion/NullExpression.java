package lx.easydb.criterion;

import lx.easydb.ICriteria;

/**
 * @author Longshine
 *
 */
public class NullExpression implements IExpression {
	private IExpression exp;
	
	public NullExpression(IExpression expression) {
		this.exp = expression;
	}
	
	public IExpression getExpression() {
		return this.exp;
	}
	
	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toString() {
		return this.exp + " IS NULL";
	}
}
