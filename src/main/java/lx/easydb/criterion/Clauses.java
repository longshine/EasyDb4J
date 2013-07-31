/**
 * Copyright (c) 2011 SmeshLink Technology Corporation.
 * All rights reserved.
 * 
 * This file is part of the SmeshServer, a gateway middleware for WSN.
 * Please see README for more information.
 */
package lx.easydb.criterion;

/**
 * @author Longshine
 *
 */
public class Clauses {
	public static IExpression field(String fieldName) {
		return new FieldExpression(fieldName);
	}
	
	public static IExpression field(String fieldName, String tableName) {
		return new FieldExpression(fieldName, tableName);
	}
	
	public static IExpression value(Object value) {
		return new ValueExpression(value);
	}
	
	public static IExpression plain(String value) {
		return new PlainExpression(value);
	}
	
	public static IExpression between(String fieldName, Object lower, Object upper) {
		return between(field(fieldName), value(lower), value(upper));
	}
	
	public static IExpression between(IExpression field, IExpression lower, IExpression upper) {
		return new BetweenExpression(field, lower, upper);
	}
	
	public static IExpression in(String fieldName, Object[] values) {
		IExpression[] exps = new IExpression[values.length];
		for (int i = 0; i < exps.length; i++) {
			exps[i] = value(values[i]);
		}
		return in(field(fieldName), exps);
	}
	
	public static IExpression in(IExpression field, Object[] values) {
		IExpression[] exps = new IExpression[values.length];
		for (int i = 0; i < exps.length; i++) {
			exps[i] = value(values[i]);
		}
		return in(field, exps);
	}
	
	public static IExpression in(IExpression field, IExpression[] values) {
		return new InExpression(field, values);
	}
	
	public static IExpression isNull(String fieldName) {
		return new NullExpression(field(fieldName));
	}
	
	public static IExpression isNotNull(String fieldName) {
		return new NotNullExpression(field(fieldName));
	}
	
	public static Junction disjunction() {
		return new Disjunction();
	}
	
	public static Junction conjunction() {
		return new Conjunction();
	}
	
	public static IExpression not(IExpression expression) {
		return new NotExpression(expression);
	}
	
	public static IExpression and(IExpression left, IExpression right) {
		return new LogicalExpression(left, right, " AND ");
	}
	
	public static IExpression or(IExpression left, IExpression right) {
		return new LogicalExpression(left, right, " OR ");
	}
	
	public static IExpression gt(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), ">");
	}
	
	public static IExpression lt(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), "<");
	}
	
	public static IExpression ge(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), ">=");
	}
	
	public static IExpression le(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), "<=");
	}
	
	public static IExpression eq(String fieldName, Object value) {
		return eq(field(fieldName), value(value));
	}
	
	public static IExpression eq(IExpression field, IExpression value) {
		return new SimpleExpression(field, value, "=");
	}
	
	public static IExpression ne(String fieldName, Object value) {
		return new SimpleExpression(field(fieldName), value(value), "<>");
	}
	
	public static IExpression like(String fieldName, String value) {
		return new LikeExpression(field(fieldName), value(value));
	}
	
	public static IExpression max(String fieldName) {
		return new Function("MAX", new IExpression[] { field(fieldName) });
	}
	
	public static IExpression min(String fieldName) {
		return new Function("MIN", new IExpression[] { field(fieldName) });
	}
	
	public static IExpression count(String fieldName) {
		return new Function("COUNT", new IExpression[] { field(fieldName) });
	}
	
	public static IExpression sum(String fieldName) {
		return new Function("SUM", new IExpression[] { field(fieldName) });
	}
	
	public static IExpression avg(String fieldName) {
		return new Function("AVG", new IExpression[] { field(fieldName) });
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
