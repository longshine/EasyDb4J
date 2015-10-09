package lx.easydb.criterion;

import java.util.Iterator;
import java.util.Map;

/**
 * Provides static factory methods to create built-in criterion types.
 * 
 * @author Longshine
 *
 */
public class Clauses {

	private Clauses() { }
	
	/**
	 * Creates an expression of a field.
	 * @param fieldName the name of the field
	 */
	public static FieldExpression field(String fieldName) {
		return new FieldExpression(fieldName);
	}
	
	/**
	 * Creates an expression of a field.
	 * @param fieldName the name of the field
	 * @param tableName the name of the owner table
	 */
	public static FieldExpression field(String fieldName, String tableName) {
		return new FieldExpression(fieldName, tableName);
	}
	
	/**
	 * Creates an expression of a value object.
	 * @param value 
	 */
	public static ValueExpression value(Object value) {
		return new ValueExpression(value);
	}
	
	/**
	 * Creates an expression of a plain string.
	 * @param value a string to be placed in the query
	 */
	public static PlainExpression plain(String value) {
		return new PlainExpression(value);
	}
	
	/**
	 * Creates a "between" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param lower
	 * @param upper
	 */
	public static BetweenExpression between(String fieldName, Object lower, Object upper) {
		return between(field(fieldName), value(lower), value(upper));
	}
	
	/**
	 * Creates a "between" constraint to the named field.
	 * @param field the expression of the field
	 * @param lower
	 * @param upper
	 */
	public static BetweenExpression between(IExpression field, IExpression lower, IExpression upper) {
		return new BetweenExpression(field, lower, upper);
	}
	
	/**
	 * Creates a "in" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param values
	 */
	public static InExpression in(String fieldName, Object[] values) {
		IExpression[] exps = new IExpression[values.length];
		for (int i = 0; i < exps.length; i++) {
			exps[i] = value(values[i]);
		}
		return in(field(fieldName), exps);
	}
	
	/**
	 * Creates a "in" constraint to the named field.
	 * @param field the expression of the field
	 * @param values
	 */
	public static InExpression in(IExpression field, Object[] values) {
		IExpression[] exps = new IExpression[values.length];
		for (int i = 0; i < exps.length; i++) {
			exps[i] = value(values[i]);
		}
		return in(field, exps);
	}
	
	/**
	 * Creates a "in" constraint to the named field.
	 * @param field the expression of the field
	 * @param values
	 */
	public static InExpression in(IExpression field, IExpression[] values) {
		return new InExpression(field, values);
	}
	
	/**
	 * Creates a "is null" constraint to the named field.
	 * @param fieldName the name of the field
	 */
	public static NullExpression isNull(String fieldName) {
		return new NullExpression(field(fieldName));
	}
	
	/**
	 * Creates a "is not null" constraint to the named field.
	 * @param fieldName the name of the field
	 */
	public static NotNullExpression isNotNull(String fieldName) {
		return new NotNullExpression(field(fieldName));
	}
	
	/**
	 * Group expressions together in a single disjunction (A or B or C...)
	 */
	public static Disjunction disjunction() {
		return new Disjunction();
	}
	
	/**
	 * Group expressions together in a single conjunction (A and B and C...)
	 */
	public static Conjunction conjunction() {
		return new Conjunction();
	}
	
	/**
	 * Returns the negation of an expression,
	 */
	public static NotExpression not(IExpression expression) {
		return new NotExpression(expression);
	}
	
	/**
	 * Returns the conjuction of two expressions.
	 */
	public static LogicalExpression and(IExpression left, IExpression right) {
		return new LogicalExpression(left, right, " AND ");
	}

	/**
	 * Returns the disjuction of two expressions.
	 */
	public static LogicalExpression or(IExpression left, IExpression right) {
		return new LogicalExpression(left, right, " OR ");
	}
	
	/**
	 * Creates a "greater than" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 */
	public static SimpleExpression gt(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), ">");
	}
	
	/**
	 * Creates a "less than" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 */
	public static SimpleExpression lt(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), "<");
	}
	
	/**
	 * Creates a "greater than or equal" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 */
	public static SimpleExpression ge(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), ">=");
	}
	
	/**
	 * Creates a "greater than or equal" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 */
	public static SimpleExpression le(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), "<=");
	}

	/**
	 * Creates a "equal" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 */
	public static SimpleExpression eq(String fieldName, Object value) {
		return eq(field(fieldName), value(value));
	}
	
	/**
	 * Creates a "equal" constraint to the named field.
	 * @param field the expression of the field
	 * @param value
	 */
	public static SimpleExpression eq(IExpression field, IExpression value) {
		return new SimpleExpression(field, value, "=");
	}
	
	/**
	 * Creates a "not equal" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 */
	public static SimpleExpression ne(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), "<>");
	}
	
	/**
	 * Creates an "equal" constraint to two properties.
	 */
	public static SimpleExpression eqProperty(String propertyName, String otherPropertyName) {
		return new SimpleExpression(field(propertyName), field(otherPropertyName), "=");
	}
	
	/**
	 * Creates a "not equal" constraint to two properties.
	 */
	public static SimpleExpression neProperty(String propertyName, String otherPropertyName) {
		return new SimpleExpression(field(propertyName), field(otherPropertyName), "<>");
	}
	
	/**
	 * Creates a "less than" constraint to two properties.
	 */
	public static SimpleExpression ltProperty(String propertyName, String otherPropertyName) {
		return new SimpleExpression(field(propertyName), field(otherPropertyName), "<");
	}
	
	/**
	 * Creates a "less than or equal" constraint to two properties
	 */
	public static SimpleExpression leProperty(String propertyName, String otherPropertyName) {
		return new SimpleExpression(field(propertyName), field(otherPropertyName), "<=");
	}
	
	/**
	 * Creates a "greater than" constraint to two properties.
	 */
	public static SimpleExpression gtProperty(String propertyName, String otherPropertyName) {
		return new SimpleExpression(field(propertyName), field(otherPropertyName), ">");
	}
	
	/**
	 * Creates a "greater than or equal" constraint to two properties.
	 */
	public static SimpleExpression geProperty(String propertyName, String otherPropertyName) {
		return new SimpleExpression(field(propertyName), field(otherPropertyName), ">=");
	}
	
	/**
	 * Creates a "like" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 */
	public static LikeExpression like(String fieldName, String value) {
		return new LikeExpression(field(fieldName), value(value));
	}
	
	/**
	 * Creates a "like" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 * @param matchMode
	 */
	public static LikeExpression like(String fieldName, String value, MatchMode matchMode) {
		return new LikeExpression(field(fieldName), value(value), matchMode);
	}
	
	/**
	 * Creates a "ilike" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 */
	public static IlikeExpression ilike(String fieldName, String value) {
		return new IlikeExpression(field(fieldName), value(value));
	}
	
	/**
	 * Creates a "ilike" constraint to the named field.
	 * @param fieldName the name of the field
	 * @param value
	 * @param matchMode
	 */
	public static IlikeExpression ilike(String fieldName, String value, MatchMode matchMode) {
		return new IlikeExpression(field(fieldName), value(value), matchMode);
	}
	
	/**
	 * Creates an "equals" constraint to each property in the
	 * key set of a <tt>Map</tt>
	 * @param fieldNameValues a map from property names to values
	 */
	@SuppressWarnings("rawtypes")
	public static Conjunction allEq(Map fieldNameValues) {
		Conjunction conj = conjunction();
		for (Iterator it = fieldNameValues.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry me = (Map.Entry) it.next();
			conj.add(eq((String) me.getKey(), me.getValue()));
		}
		return conj;
	}
	
	public static IExpression function(String function, String fieldName) {
		return function(function, new IExpression[] { field(fieldName) });
	}
	
	public static IExpression function(String function, IExpression[] args) {
		return new Function(function, args);
	}
	
	public static IExpression mod(String fieldName, Object value) {
		return function("mod", new IExpression[] { field(fieldName), value(value) });
	}
	
	public static IExpression minus(IExpression left, IExpression right) {
        return new SimpleExpression(left, right, "-");
    }
}
