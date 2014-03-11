package lx.easydb.dialect;

import lx.easydb.Types;
import lx.easydb.dialect.function.AnsiTrimEmulationFunction;
import lx.easydb.dialect.function.AvgWithArgumentCastFunction;
import lx.easydb.dialect.function.NoArgSQLFunction;
import lx.easydb.dialect.function.SQLFunctionTemplate;
import lx.easydb.dialect.function.StandardSQLFunction;
import lx.easydb.dialect.function.VarArgsSQLFunction;

/**
 * An SQL dialect for DB2.
 */
public class DB2Dialect extends Dialect {

	public DB2Dialect() {
		registerColumnType(Types.BIT, "smallint");
		registerColumnType(Types.BIGINT, "bigint");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.TINYINT, "smallint");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.CHAR, "char(1)");
		registerColumnType(Types.VARCHAR, "varchar($l)");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.DOUBLE, "double");
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");
		registerColumnType(Types.VARBINARY, "varchar($l) for bit data");
		registerColumnType(Types.NUMERIC, "numeric($p,$s)");
		registerColumnType(Types.BLOB, "blob($l)");
		registerColumnType(Types.CLOB, "clob($l)");
		registerColumnType(Types.LONGVARCHAR, "long varchar");
		registerColumnType(Types.LONGVARBINARY, "long varchar for bit data");

		registerFunction( "avg", new AvgWithArgumentCastFunction( "double"));

		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("absval", new StandardSQLFunction("absval"));
		registerFunction("sign", new StandardSQLFunction("sign", Types.INTEGER));

		registerFunction("ceiling", new StandardSQLFunction("ceiling"));
		registerFunction("ceil", new StandardSQLFunction("ceil"));
		registerFunction("floor", new StandardSQLFunction("floor"));
		registerFunction("round", new StandardSQLFunction("round"));

		registerFunction("acos", new StandardSQLFunction("acos", Types.DOUBLE));
		registerFunction("asin", new StandardSQLFunction("asin", Types.DOUBLE));
		registerFunction("atan", new StandardSQLFunction("atan", Types.DOUBLE));
		registerFunction("cos", new StandardSQLFunction("cos", Types.DOUBLE));
		registerFunction("cot", new StandardSQLFunction("cot", Types.DOUBLE));
		registerFunction("degrees", new StandardSQLFunction("degrees", Types.DOUBLE));
		registerFunction("exp", new StandardSQLFunction("exp", Types.DOUBLE));
		registerFunction("float", new StandardSQLFunction("float", Types.DOUBLE));
		registerFunction("hex", new StandardSQLFunction("hex", Types.VARCHAR));
		registerFunction("ln", new StandardSQLFunction("ln", Types.DOUBLE));
		registerFunction("log", new StandardSQLFunction("log", Types.DOUBLE));
		registerFunction("log10", new StandardSQLFunction("log10", Types.DOUBLE));
		registerFunction("radians", new StandardSQLFunction("radians", Types.DOUBLE));
		registerFunction("rand", new NoArgSQLFunction("rand", Types.DOUBLE));
		registerFunction("sin", new StandardSQLFunction("sin", Types.DOUBLE));
		registerFunction("soundex", new StandardSQLFunction("soundex", Types.VARCHAR));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", Types.DOUBLE));
		registerFunction("stddev", new StandardSQLFunction("stddev", Types.DOUBLE));
		registerFunction("tan", new StandardSQLFunction("tan", Types.DOUBLE));
		registerFunction("variance", new StandardSQLFunction("variance", Types.DOUBLE));

		registerFunction("julian_day", new StandardSQLFunction("julian_day", Types.INTEGER));
		registerFunction("microsecond", new StandardSQLFunction("microsecond", Types.INTEGER));
		registerFunction("midnight_seconds", new StandardSQLFunction("midnight_seconds", Types.INTEGER));
		registerFunction("minute", new StandardSQLFunction("minute", Types.INTEGER));
		registerFunction("month", new StandardSQLFunction("month", Types.INTEGER));
		registerFunction("monthname", new StandardSQLFunction("monthname", Types.VARCHAR));
		registerFunction("quarter", new StandardSQLFunction("quarter", Types.INTEGER));
		registerFunction("hour", new StandardSQLFunction("hour", Types.INTEGER));
		registerFunction("second", new StandardSQLFunction("second", Types.INTEGER));
		registerFunction("current_date", new NoArgSQLFunction("current date", Types.DATE, false));
		registerFunction("date", new StandardSQLFunction("date", Types.DATE));
		registerFunction("day", new StandardSQLFunction("day", Types.INTEGER));
		registerFunction("dayname", new StandardSQLFunction("dayname", Types.VARCHAR));
		registerFunction("dayofweek", new StandardSQLFunction("dayofweek", Types.INTEGER));
		registerFunction("dayofweek_iso", new StandardSQLFunction("dayofweek_iso", Types.INTEGER));
		registerFunction("dayofyear", new StandardSQLFunction("dayofyear", Types.INTEGER));
		registerFunction("days", new StandardSQLFunction("days", Types.BIGINT));
		registerFunction("current_time", new NoArgSQLFunction("current time", Types.TIME, false));
		registerFunction("time", new StandardSQLFunction("time", Types.TIME));
		registerFunction("current_timestamp", new NoArgSQLFunction("current timestamp", Types.TIMESTAMP, false));
		registerFunction("timestamp", new StandardSQLFunction("timestamp", Types.TIMESTAMP));
		registerFunction("timestamp_iso", new StandardSQLFunction("timestamp_iso", Types.TIMESTAMP));
		registerFunction("week", new StandardSQLFunction("week", Types.INTEGER));
		registerFunction("week_iso", new StandardSQLFunction("week_iso", Types.INTEGER));
		registerFunction("year", new StandardSQLFunction("year", Types.INTEGER));

		registerFunction("double", new StandardSQLFunction("double", Types.DOUBLE));
		registerFunction("varchar", new StandardSQLFunction("varchar", Types.VARCHAR));
		registerFunction("real", new StandardSQLFunction("real", Types.FLOAT));
		registerFunction("bigint", new StandardSQLFunction("bigint", Types.BIGINT));
		registerFunction("char", new StandardSQLFunction("char", Types.CHAR));
		registerFunction("integer", new StandardSQLFunction("integer", Types.INTEGER));
		registerFunction("smallint", new StandardSQLFunction("smallint", Types.SMALLINT));

		registerFunction("digits", new StandardSQLFunction("digits", Types.VARCHAR));
		registerFunction("chr", new StandardSQLFunction("chr", Types.CHAR));
		registerFunction("upper", new StandardSQLFunction("upper"));
		registerFunction("lower", new StandardSQLFunction("lower"));
		registerFunction("ucase", new StandardSQLFunction("ucase"));
		registerFunction("lcase", new StandardSQLFunction("lcase"));
		registerFunction("ltrim", new StandardSQLFunction("ltrim"));
		registerFunction("rtrim", new StandardSQLFunction("rtrim"));
		registerFunction("substr", new StandardSQLFunction("substr", Types.VARCHAR));
		registerFunction("posstr", new StandardSQLFunction("posstr", Types.INTEGER));

		registerFunction("substring", new StandardSQLFunction("substr", Types.VARCHAR));
		registerFunction("bit_length", new SQLFunctionTemplate(Types.INTEGER, "length(?1)*8"));
		registerFunction("trim", new AnsiTrimEmulationFunction());

		registerFunction("concat", new VarArgsSQLFunction(Types.VARCHAR, "", "||", ""));

		registerFunction("str", new SQLFunctionTemplate(Types.VARCHAR, "rtrim(char(?1))"));
	}

	public String getLowercaseFunction() {
		return "lcase";
	}

	public String getIdentitySelectString() {
		return "values identity_val_local()";
	}
	
	public String getIdentityColumnString() {
		return "generated by default as identity"; //not null ... (start with 1) is implicit
	}

	public boolean supportsTemporaryTables() {
		return true;
	}

	public String getCreateTemporaryTableString() {
		return "declare global temporary table";
	}

	public String getPaging(String sql, String order, int total, int offset) {
		int startOfSelect = sql.toLowerCase().indexOf("select");

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100)
				.append(sql.substring(0, startOfSelect))	// add the comment
				.append("select * from ( select ") 			// nest the main query in an outer select
				.append(getRowNumber(sql)); 				// add the rownnumber bit into the outer query select list

		if ( hasDistinct(sql) ) {
			pagingSelect.append(" row_.* from ( ")			// add another (inner) nested select
					.append(sql.substring(startOfSelect) ) // add the main query
					.append(" ) as row_"); 					// close off the inner nested select
		} else {
			pagingSelect.append(sql.substring(startOfSelect + 6)); // add the main query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");

		//add the restriction to the outer select
		if (offset > 0) {
			pagingSelect
				.append("between ")
				.append(offset + 1)
				.append(" and ")
				.append(offset + total);
		} else {
			pagingSelect.append("<= ").append(total);
		}

		return pagingSelect.toString();
	}

	/**
	 * Render the <tt>rownumber() over ( .... ) as rownumber_,</tt> 
	 * bit, that goes in the select list
	 */
	private String getRowNumber(String sql) {
		StringBuffer rownumber = new StringBuffer(50)
			.append("rownumber() over(");

		int orderByIndex = sql.toLowerCase().indexOf("order by");

		if ( orderByIndex>0 && !hasDistinct(sql) ) {
			rownumber.append( sql.substring(orderByIndex));
		}

		rownumber.append(") as rownumber_,");

		return rownumber.toString();
	}
	
	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct") >= 0;
	}
}
