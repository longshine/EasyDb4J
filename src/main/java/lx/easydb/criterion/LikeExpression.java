package lx.easydb.criterion;

import lx.easydb.ICriteria;

/**
 * @author Longshine
 *
 */
public class LikeExpression implements IExpression {
	private IExpression expression;
	private IExpression value;
	private MatchMode matchMode;
	private String escapeChar;
	private boolean ignoreCase;
	
	public LikeExpression(IExpression expression, IExpression value) {
		this(expression, value, MatchMode.EXACT, null, false);
	}
	
	public LikeExpression(IExpression expression, IExpression value, MatchMode matchMode) {
		this(expression, value, matchMode, null, false);
	}
	
	public LikeExpression(IExpression expression, IExpression value,
			MatchMode matchMode, String escapeChar, boolean ignoreCase) {
		this.expression = expression;
		this.value = value;
		this.matchMode = matchMode;
		this.escapeChar = escapeChar;
		this.ignoreCase = ignoreCase;
	}
	
	public IExpression getExpression() {
		return expression;
	}
	
	public IExpression getValue() {
		return value;
	}
	
	public String getEscapeChar() {
		return escapeChar;
	}
	
	public MatchMode getMatchMode() {
		return matchMode;
	}
	
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}

	public String toString() {
		return expression + " LIKE " + matchMode.toMatchString(value.toString());
	}
}
