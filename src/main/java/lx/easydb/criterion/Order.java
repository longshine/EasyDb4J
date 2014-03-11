package lx.easydb.criterion;

import lx.easydb.ICriteria;

/**
 * Represents an order imposed upon a <tt>Criteria</tt> result set.
 * 
 * @author Longshine
 *
 */
public class Order implements IFragment {
	private IExpression expression;
	private boolean ascending;
	
	public Order(IExpression expression, boolean ascending) {
		this.expression = expression;
		this.ascending = ascending;
	}

	public boolean isAscending() {
		return this.ascending;
	}
	
	public IExpression getExpression() {
		return expression;
	}

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toString() {
		return expression + " " + (ascending ? "ASC" : "DESC");
	}
	
	/**
	 * Ascending order.
	 * @param fieldName
	 */
	public static Order asc(String fieldName) {
		return new Order(Clauses.field(fieldName), true);
	}
	
	/**
	 * Ascending order.
	 * @param expression
	 */
	public static Order asc(IExpression expression) {
		return new Order(expression, true);
	}
	
	/**
	 * Descending order.
	 * @param fieldName
	 */
	public static Order desc(String fieldName) {
		return new Order(Clauses.field(fieldName), false);
	}
	
	/**
	 * Descending order.
	 * @param expression
	 */
	public static Order desc(IExpression expression) {
		return new Order(expression, false);
	}
}
