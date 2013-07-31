package lx.easydb.criterion;

import lx.easydb.ICriteria;

public class LogicalExpression implements IExpression {
	private IExpression left;
	private IExpression right;
	private String op;
	
	public LogicalExpression(IExpression left, IExpression right, String op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}
	
	public IExpression getLeft() {
		return this.left;
	}
	
	public IExpression getRight() {
		return this.right;
	}
	
	public String getOp() {
		return this.op;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}

	public String toString() {
		return "(" + this.left + this.op + this.right + ")";
	}
}
