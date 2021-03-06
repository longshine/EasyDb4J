package lx.easydb.criterion;

import lx.easydb.ICriteria;
import lx.easydb.StringHelper;

/**
 * @author Longshine
 *
 */
public class Function implements IExpression {
	private String name;
	private IExpression[] args;
	
	public Function(String name, IExpression[] args) {
		this.name = name;
		this.args = args;
	}
	
	public String getName() {
		return this.name;
	}
	
	public IExpression[] getArgs() {
		return this.args;
	}
	
	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toString() {
		return this.name + "(" + StringHelper.toString(this.args) + ")";
	}
}
