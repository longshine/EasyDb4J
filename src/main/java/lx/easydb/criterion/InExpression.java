package lx.easydb.criterion;

import lx.easydb.ICriteria;
import lx.easydb.StringHelper;

/**
 * @author Longshine
 *
 */
public class InExpression implements IExpression {
	private IExpression exp;
	private IExpression[] values;
	
	public InExpression(IExpression expression, IExpression[] values) {
		this.exp = expression;
		this.values = values;
	}
	
	public IExpression getExpression() {
		return this.exp;
	}
	
	public IExpression[] getValues() {
		return this.values;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}

	public String toString() {
		return this.exp + " IN (" + StringHelper.toString(values) + ')';
	}
}
