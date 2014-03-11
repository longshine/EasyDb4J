package lx.easydb.criterion;

import lx.easydb.ICriteria;

/**
 * @author Longshine
 *
 */
public class IlikeExpression implements IExpression {
	private IExpression expression;
	private IExpression value;
	private MatchMode matchMode;
	
	public IlikeExpression(IExpression expression, IExpression value) {
		this(expression, value, MatchMode.EXACT);
	}
	
	public IlikeExpression(IExpression expression, IExpression value, MatchMode matchMode) {
		this.expression = expression;
		this.value = value;
		this.matchMode = matchMode;
	}
	
	public IExpression getExpression() {
		return expression;
	}
	
	public IExpression getValue() {
		return this.value;
	}

	public MatchMode getMatchMode() {
		return matchMode;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}

	public String toString() {
		return expression + " ILIKE " + matchMode.toMatchString(value.toString());
	}
}
