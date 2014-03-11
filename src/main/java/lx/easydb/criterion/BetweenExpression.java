package lx.easydb.criterion;

import lx.easydb.ICriteria;
import lx.easydb.StringHelper;

/**
 * @author Longshine
 *
 */
public class BetweenExpression implements IExpression {
	private IExpression exp;
	private IExpression upper;
	private IExpression lower;
	
	public BetweenExpression(IExpression expression, IExpression lower, IExpression upper) {
		this.exp = expression;
		this.upper = upper;
		this.lower = lower;
	}
	
	public IExpression getExpression() {
		return this.exp;	
	}
	
	public IExpression getUpper() {
		return this.upper;
	}
	
	public IExpression getLower() {
		return this.lower;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toString() {
		return StringHelper.createBuilder()
				.append(this.exp)
				.append(" BETWEEN ")
				.append(this.lower.toString())
				.append(" AND ")
				.append(this.upper.toString())
				.toString();
	}
}
