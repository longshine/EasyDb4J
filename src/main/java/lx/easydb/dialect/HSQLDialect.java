package lx.easydb.dialect;

import java.util.List;

import lx.easydb.IConnectionFactory;
import lx.easydb.StringHelper;
import lx.easydb.Types;
import lx.easydb.dialect.function.AvgWithArgumentCastFunction;
import lx.easydb.dialect.function.NoArgSQLFunction;
import lx.easydb.dialect.function.SQLFunctionTemplate;
import lx.easydb.dialect.function.StandardSQLFunction;
import lx.easydb.dialect.function.VarArgsSQLFunction;

public class HSQLDialect extends Dialect {
	/**
	 * version is 18 for 1.8 or 20 for 2.0
	 */
	private int hsqldbVersion = 18;
	
	public HSQLDialect() {
		super();

		try {
			Class props = Class.forName("org.hsqldb.persist.HsqlDatabaseProperties");
			String versionString = (String) props.getDeclaredField(
					"THIS_VERSION").get(null);

			hsqldbVersion = Integer.parseInt(versionString.substring(0, 1)) * 10;
			hsqldbVersion += Integer.parseInt(versionString.substring(2, 3));
		} catch (Throwable e) {
			// must be a very old version
		}

        registerColumnType(Types.IDENTITY, "int");
        
		registerColumnType(Types.BIGINT, "bigint");
		registerColumnType(Types.BINARY, "binary");
		registerColumnType(Types.BIT, "bit");
		registerColumnType(Types.BOOLEAN, "boolean");
		registerColumnType(Types.CHAR, "char($l)");
		registerColumnType(Types.DATE, "date");

		registerColumnType(Types.DECIMAL, "decimal($p,$s)");
		registerColumnType(Types.DOUBLE, "double");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.LONGVARBINARY, "longvarbinary");
		registerColumnType(Types.LONGVARCHAR, "longvarchar");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");
		registerColumnType(Types.VARCHAR, "varchar($l)");
		registerColumnType(Types.VARBINARY, "varbinary($l)");

		if (hsqldbVersion < 20) {
			registerColumnType(Types.NUMERIC, "numeric");
		} else {
			registerColumnType(Types.NUMERIC, "numeric($p,$s)");
		}

		// HSQL has no Blob/Clob support .... but just put these here for now!
		if (hsqldbVersion < 20) {
			registerColumnType(Types.BLOB, "longvarbinary");
			registerColumnType(Types.CLOB, "longvarchar");
		} else {
			registerColumnType(Types.BLOB, "blob");
			registerColumnType(Types.CLOB, "clob");
		}

		registerFunction("avg", new AvgWithArgumentCastFunction("double"));

		registerFunction("ascii", new StandardSQLFunction("ascii", Types.INTEGER));
		registerFunction("char", new StandardSQLFunction("char", Types.CHAR));
		registerFunction("lower", new StandardSQLFunction("lower"));
		registerFunction("upper", new StandardSQLFunction("upper"));
		registerFunction("lcase", new StandardSQLFunction("lcase"));
		registerFunction("ucase", new StandardSQLFunction("ucase"));
		registerFunction("soundex", new StandardSQLFunction("soundex", Types.VARCHAR));
		registerFunction("ltrim", new StandardSQLFunction("ltrim"));
		registerFunction("rtrim", new StandardSQLFunction("rtrim"));
		registerFunction("reverse", new StandardSQLFunction("reverse"));
		registerFunction("space", new StandardSQLFunction("space", Types.VARCHAR));
		registerFunction("rawtohex", new StandardSQLFunction("rawtohex"));
		registerFunction("hextoraw", new StandardSQLFunction("hextoraw"));
		registerFunction("str", new SQLFunctionTemplate(Types.VARCHAR, "cast(?1 as varchar(24))"));
		registerFunction("user", new NoArgSQLFunction("user", Types.VARCHAR));
		registerFunction("database", new NoArgSQLFunction("database", Types.VARCHAR));

		registerFunction("sysdate", new NoArgSQLFunction("sysdate", Types.DATE, false));
		registerFunction("current_date", new NoArgSQLFunction("current_date", Types.DATE, false));
		registerFunction("curdate", new NoArgSQLFunction("curdate", Types.DATE));
		registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", Types.TIMESTAMP, false));
		registerFunction("now", new NoArgSQLFunction("now", Types.TIMESTAMP));
		registerFunction("current_time", new NoArgSQLFunction("current_time", Types.TIME, false));
		registerFunction("curtime", new NoArgSQLFunction("curtime", Types.TIME));
		registerFunction("day", new StandardSQLFunction("day", Types.INTEGER));
		registerFunction("dayofweek", new StandardSQLFunction("dayofweek", Types.INTEGER));
		registerFunction("dayofyear", new StandardSQLFunction("dayofyear", Types.INTEGER));
		registerFunction("dayofmonth", new StandardSQLFunction("dayofmonth", Types.INTEGER));
		registerFunction("month", new StandardSQLFunction("month", Types.INTEGER));
		registerFunction("year", new StandardSQLFunction("year", Types.INTEGER));
		registerFunction("week", new StandardSQLFunction("week", Types.INTEGER));
		registerFunction("quarter", new StandardSQLFunction("quarter", Types.INTEGER));
		registerFunction("hour", new StandardSQLFunction("hour", Types.INTEGER));
		registerFunction("minute", new StandardSQLFunction("minute", Types.INTEGER));
		registerFunction("second", new StandardSQLFunction("second", Types.INTEGER));
		registerFunction("dayname", new StandardSQLFunction("dayname", Types.VARCHAR));
		registerFunction("monthname", new StandardSQLFunction("monthname", Types.VARCHAR));

		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("sign", new StandardSQLFunction("sign", Types.INTEGER));

		registerFunction("acos", new StandardSQLFunction("acos", Types.DOUBLE));
		registerFunction("asin", new StandardSQLFunction("asin", Types.DOUBLE));
		registerFunction("atan", new StandardSQLFunction("atan", Types.DOUBLE));
		registerFunction("cos", new StandardSQLFunction("cos", Types.DOUBLE));
		registerFunction("cot", new StandardSQLFunction("cot", Types.DOUBLE));
		registerFunction("exp", new StandardSQLFunction("exp", Types.DOUBLE));
		registerFunction("log", new StandardSQLFunction("log", Types.DOUBLE));
		registerFunction("log10", new StandardSQLFunction("log10", Types.DOUBLE));
		registerFunction("sin", new StandardSQLFunction("sin", Types.DOUBLE));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", Types.DOUBLE));
		registerFunction("tan", new StandardSQLFunction("tan", Types.DOUBLE));
		registerFunction("pi", new NoArgSQLFunction("pi", Types.DOUBLE));
		registerFunction("rand", new StandardSQLFunction("rand", Types.FLOAT));

		registerFunction("radians", new StandardSQLFunction("radians", Types.DOUBLE));
		registerFunction("degrees", new StandardSQLFunction("degrees", Types.DOUBLE));
		registerFunction("roundmagic", new StandardSQLFunction("roundmagic"));

		registerFunction("ceiling", new StandardSQLFunction("ceiling"));
		registerFunction("floor", new StandardSQLFunction("floor"));

		// Multi-param dialect functions...
		registerFunction("mod", new StandardSQLFunction("mod", Types.INTEGER));

		// function templates
		registerFunction("concat", new VarArgsSQLFunction(Types.VARCHAR, "(", "||", ")"));
		
		registerFunction("date", new StandardSQLFunction("to_char") {
			protected String doRender(int firstArgumentType, List arguments, IConnectionFactory factory) {
				arguments.add("'yyyy-mm-dd'");
				return super.doRender(firstArgumentType, arguments, factory);
			}
		});
	}
	
	public String getIdentityColumnString() {
		return "generated by default as identity (start with 1)"; //not null is implicit
    }
	
	protected String getIdentitySelectString() {
		return "call identity()";
    }
	
	public boolean supportsIfExistsAfterTableName() {
		return true;
	}
	
	public boolean supportsColumnCheck() {
		return hsqldbVersion >= 20;
	}
	
	public String getPaging(String sql, String order, int total, int offset) {
		StringBuffer sb = StringHelper.createBuilder()
			.append(sql)
			.append(" LIMIT ")
			.append(total);
		if (offset > 0) {
			sb.append(" OFFSET ");
			sb.append(offset);
		}
		return sb.toString();
	}
	
	public String getCreateTemporaryTableString() {
		if (hsqldbVersion < 20) {
			return "create global temporary table";
		} else {
			return "declare local temporary table";
		}
	}
}
