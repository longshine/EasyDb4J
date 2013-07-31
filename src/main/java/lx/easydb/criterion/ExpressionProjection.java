package lx.easydb.criterion;

import lx.easydb.ICriteria;

public class ExpressionProjection implements IProjection {
	private boolean grouped;
	private String alias;
	private IExpression expression;
	
	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toGroupString(ICriteria criteria) {
        if (grouped)
            return expression.render(criteria);
        else
            return "";
    }

	public void setGrouped(boolean grouped) {
		this.grouped = grouped;
	}

	public boolean isGrouped() {
		return grouped;
	}

	public void setExpression(IExpression expression) {
		this.expression = expression;
	}

	public IExpression getExpression() {
		return expression;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}
}
