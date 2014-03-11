package lx.easydb.criterion;

/**
 * Provides static factory methods to create built-in projection types.
 * 
 * @author Long
 *
 */
public class Projections {

	private Projections() { }
	
	/**
	 * Creates a new projection list.
	 */
    public static ProjectionList list() {
        return new ProjectionList();
    }
    
    /**
     * Create a distinct projection from a projection.
     */
    public static IProjection distinct(IProjection projection) {
        return new Distinct(projection);
    }
    
    /**
     * Create a distinct projection from a property.
     */
    public static IProjection distinct(String property) {
        return new Distinct(property(property));
    }

    /**
     * The query row count, ie. <code>count(*)</code>
     */
    public static IProjection rowCount() {
        return new RowCountProjection();
    }

    /**
     * A property value count.
     */
    public static IProjection count(String fieldName) {
        return new CountProjection(Clauses.field(fieldName));
    }
    
    /**
     * A property value count.
     */
    public static IProjection count(String fieldName, String alias) {
    	CountProjection c = new CountProjection(Clauses.field(fieldName));
    	c.setAlias(alias);
    	return c;
    }

    /**
     * A distinct property value count.
     */
    public static IProjection countDistinct(String fieldName) {
    	CountProjection p = new CountProjection(Clauses.field(fieldName));
    	p.setDistinct(true);
    	return p;
    }

    /**
     * A property average value.
     */
    public static IProjection avg(String fieldName) {
        return avg(fieldName, null);
    }
    
    /**
     * A property average value.
     */
    public static IProjection avg(String fieldName, String alias) {
    	AggregateProjection p = new AggregateProjection("avg", Clauses.field(fieldName));
    	p.setAlias(alias);
    	return p;
    }

    /**
     * A property maximum value.
     */
    public static IProjection max(String fieldName) {
    	return max(fieldName, null);
    }
    
    /**
     * A property maximum value.
     */
    public static IProjection max(String fieldName, String alias) {
    	AggregateProjection p = new AggregateProjection("max", Clauses.field(fieldName));
    	p.setAlias(alias);
    	return p;
    }

    /**
     * A property minimum value.
     */
    public static IProjection min(String fieldName) {
        return min(fieldName, null);
    }
    
    /**
     * A property minimum value.
     */
    public static IProjection min(String fieldName, String alias) {
    	AggregateProjection p = new AggregateProjection("min", Clauses.field(fieldName));
    	p.setAlias(alias);
    	return p;
    }

    /**
     * A property value sum.
     */
    public static IProjection sum(String fieldName) {
        return new AggregateProjection("sum", Clauses.field(fieldName));
    }
    
    /**
     * A property value sum.
     */
    public static IProjection sum(String fieldName, String alias) {
    	AggregateProjection p = new AggregateProjection("sum", Clauses.field(fieldName));
    	p.setAlias(alias);
    	return p;
    }

    /**
     * A grouping property value.
     */
    public static IProjection groupProperty(String propertyName) {
        return new PropertyProjection(propertyName, true);
    }

    /**
     * A projected property value.
     */
    public static IProjection property(String propertyName) {
        return new PropertyProjection(propertyName);
    }

    /**
     * A projected expression.
     */
    public static IProjection expression(IExpression exp) {
    	ExpressionProjection p = new ExpressionProjection();
    	p.setExpression(exp);
    	return p;
    }

    /**
     * A grouping projected expression.
     */
    public static IProjection groupExpression(IExpression exp) {
    	return groupExpression(exp, null);
    }
    
    /**
     * A grouping projected expression.
     */
    public static IProjection groupExpression(IExpression exp, String alias) {
    	ExpressionProjection p = new ExpressionProjection();
    	p.setExpression(exp);
    	p.setGrouped(true);
    	p.setAlias(alias);
    	return p;
    }
    
    /**
	 * Assign an alias to a projection, by wrapping it
	 */
	public static IProjection alias(IProjection projection, String alias) {
		projection.setAlias(alias);
		return projection;
	}
}
