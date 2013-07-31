package lx.easydb.criterion;

import java.util.ArrayList;
import java.util.List;

import lx.easydb.ICriteria;

public class AggregateProjection extends SimpleProjection {
	private final String functionName;
	private final IExpression expression;
	
	public AggregateProjection(String functionName, IExpression expression) {
		this.functionName = functionName;
		this.expression = expression;
    }
	
	public String getFunctionName() {
		return functionName;
	}
	
	public IExpression getExpression() {
		return expression;
	}
	
	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toString() {
    	return functionName + "(" + expression + ')';
	}
	
	public List buildFunctionParameterList(ICriteria criteria) {
		List list = new ArrayList();
		list.add(expression.render(criteria));
		return list;
	}
}
