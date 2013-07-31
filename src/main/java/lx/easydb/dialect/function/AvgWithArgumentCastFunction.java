package lx.easydb.dialect.function;

import lx.easydb.Types;

/**
 * Some databases strictly return the type of the of the aggregation value for <tt>AVG</tt> which is
 * problematic in the case of averaging integers because the decimals will be dropped.  The usual workaround
 * is to cast the integer argument as some form of double/decimal.
 *
 * @author Steve Ebersole
 */
public class AvgWithArgumentCastFunction extends StandardAnsiSqlAggregationFunctions.AvgFunction {
	private final String castType;

	public AvgWithArgumentCastFunction(String castType) {
		this.castType = castType;
	}

	protected String renderArgument(String argument, int firstArgumentJdbcType) {
		if (firstArgumentJdbcType == Types.DOUBLE
				|| firstArgumentJdbcType == Types.FLOAT) {
			return argument;
		} else {
			return "cast(" + argument + " as " + castType + ")";
		}
	}
}
