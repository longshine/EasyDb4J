package lx.easydb.dialect;

import java.util.List;

import lx.easydb.IConnectionFactory;
import lx.easydb.Types;
import lx.easydb.dialect.function.NoArgSQLFunction;
import lx.easydb.dialect.function.StandardSQLFunction;

/**
 * An SQL dialect for MySQL (prior to 5.x).
 */
public class MySQLDialect extends Dialect {
	
	public MySQLDialect() {
        registerColumnType(Types.IDENTITY, "int");
		registerColumnType(Types.BIT, "bit");
		registerColumnType(Types.BIGINT, "bigint");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.CHAR, "char(1)");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.DOUBLE, "double precision");
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "datetime");
		registerColumnType(Types.VARBINARY, "longblob");
		registerColumnType(Types.VARBINARY, 16777215, "mediumblob");
		registerColumnType(Types.VARBINARY, 65535, "blob");
		registerColumnType(Types.VARBINARY, 255, "tinyblob");
		registerColumnType(Types.LONGVARBINARY, "longblob");
		registerColumnType(Types.LONGVARBINARY, 16777215, "mediumblob");
		registerColumnType(Types.NUMERIC, "decimal($p,$s)");
		registerColumnType(Types.BLOB, "longblob");
		// registerColumnType( Types.BLOB, 16777215, "mediumblob");
		// registerColumnType( Types.BLOB, 65535, "blob");
		registerColumnType(Types.CLOB, "longtext");
		// registerColumnType( Types.CLOB, 16777215, "mediumtext");
		// registerColumnType( Types.CLOB, 65535, "text");
		registerVarcharTypes();

		registerFunction("ascii", new StandardSQLFunction("ascii", Types.INTEGER));
		registerFunction("bin", new StandardSQLFunction("bin", Types.VARCHAR));
		registerFunction("char_length", new StandardSQLFunction("char_length", Types.BIGINT));
		registerFunction("character_length", new StandardSQLFunction("character_length", Types.BIGINT));
		registerFunction("lcase", new StandardSQLFunction("lcase"));
		registerFunction("lower", new StandardSQLFunction("lower"));
		registerFunction("ltrim", new StandardSQLFunction("ltrim"));
		registerFunction("ord", new StandardSQLFunction("ord", Types.INTEGER));
		registerFunction("quote", new StandardSQLFunction("quote"));
		registerFunction("reverse", new StandardSQLFunction("reverse"));
		registerFunction("rtrim", new StandardSQLFunction("rtrim"));
		registerFunction("soundex", new StandardSQLFunction("soundex"));
		registerFunction("space", new StandardSQLFunction("space", Types.VARCHAR));
		registerFunction("ucase", new StandardSQLFunction("ucase"));
		registerFunction("upper", new StandardSQLFunction("upper"));
		registerFunction("unhex", new StandardSQLFunction("unhex", Types.VARCHAR));

		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("sign", new StandardSQLFunction("sign", Types.INTEGER));

		registerFunction("acos", new StandardSQLFunction("acos", Types.DOUBLE));
		registerFunction("asin", new StandardSQLFunction("asin", Types.DOUBLE));
		registerFunction("atan", new StandardSQLFunction("atan", Types.DOUBLE));
		registerFunction("cos", new StandardSQLFunction("cos", Types.DOUBLE));
		registerFunction("cot", new StandardSQLFunction("cot", Types.DOUBLE));
		registerFunction("crc32", new StandardSQLFunction("crc32", Types.BIGINT));
		registerFunction("exp", new StandardSQLFunction("exp", Types.DOUBLE));
		registerFunction("ln", new StandardSQLFunction("ln", Types.DOUBLE));
		registerFunction("log", new StandardSQLFunction("log", Types.DOUBLE));
		registerFunction("log2", new StandardSQLFunction("log2", Types.DOUBLE));
		registerFunction("log10", new StandardSQLFunction("log10", Types.DOUBLE));
		registerFunction("pi", new NoArgSQLFunction("pi", Types.DOUBLE));
		registerFunction("rand", new NoArgSQLFunction("rand", Types.DOUBLE));
		registerFunction("sin", new StandardSQLFunction("sin", Types.DOUBLE));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", Types.DOUBLE));
		registerFunction("tan", new StandardSQLFunction("tan", Types.DOUBLE));

		registerFunction("radians", new StandardSQLFunction("radians", Types.DOUBLE));
		registerFunction("degrees", new StandardSQLFunction("degrees", Types.DOUBLE));

		registerFunction("ceiling", new StandardSQLFunction("ceiling", Types.INTEGER));
		registerFunction("ceil", new StandardSQLFunction("ceil", Types.INTEGER));
		registerFunction("floor", new StandardSQLFunction("floor", Types.INTEGER));
		registerFunction("round", new StandardSQLFunction("round"));

		registerFunction("datediff", new StandardSQLFunction("datediff", Types.INTEGER));
		registerFunction("timediff", new StandardSQLFunction("timediff", Types.TIME));
		registerFunction("date_format", new StandardSQLFunction("date_format", Types.VARCHAR));

		registerFunction("curdate", new NoArgSQLFunction("curdate", Types.DATE));
		registerFunction("curtime", new NoArgSQLFunction("curtime", Types.TIME));
		registerFunction("current_date", new NoArgSQLFunction("current_date", Types.DATE, false));
		registerFunction("current_time", new NoArgSQLFunction("current_time", Types.TIME, false));
		registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", Types.TIMESTAMP, false));
		registerFunction("date", new StandardSQLFunction("date", Types.DATE));
		registerFunction("day", new StandardSQLFunction("day", Types.INTEGER));
		registerFunction("dayofmonth", new StandardSQLFunction("dayofmonth", Types.INTEGER));
		registerFunction("dayname", new StandardSQLFunction("dayname", Types.VARCHAR));
		registerFunction("dayofweek", new StandardSQLFunction("dayofweek", Types.INTEGER));
		registerFunction("dayofyear", new StandardSQLFunction("dayofyear", Types.INTEGER));
		registerFunction("from_days", new StandardSQLFunction("from_days", Types.DATE));
		registerFunction("from_unixtime", new StandardSQLFunction("from_unixtime", Types.TIMESTAMP));
		registerFunction("hour", new StandardSQLFunction("hour", Types.INTEGER));
		registerFunction("last_day", new StandardSQLFunction("last_day", Types.DATE));
		registerFunction("localtime", new NoArgSQLFunction("localtime", Types.TIMESTAMP));
		registerFunction("localtimestamp", new NoArgSQLFunction("localtimestamp", Types.TIMESTAMP));
		registerFunction("microseconds", new StandardSQLFunction("microseconds", Types.INTEGER));
		registerFunction("minute", new StandardSQLFunction("minute", Types.INTEGER));
		registerFunction("month", new StandardSQLFunction("month", Types.INTEGER));
		registerFunction("monthname", new StandardSQLFunction("monthname", Types.VARCHAR));
		registerFunction("now", new NoArgSQLFunction("now", Types.TIMESTAMP));
		registerFunction("quarter", new StandardSQLFunction("quarter", Types.INTEGER));
		registerFunction("second", new StandardSQLFunction("second", Types.INTEGER));
		registerFunction("sec_to_time", new StandardSQLFunction("sec_to_time", Types.TIME));
		registerFunction("sysdate", new NoArgSQLFunction("sysdate", Types.TIMESTAMP));
		registerFunction("time", new StandardSQLFunction("time", Types.TIME));
		registerFunction("timestamp", new StandardSQLFunction("timestamp", Types.TIMESTAMP));
		registerFunction("time_to_sec", new StandardSQLFunction("time_to_sec", Types.INTEGER));
		registerFunction("to_days", new StandardSQLFunction("to_days", Types.BIGINT));
		registerFunction("unix_timestamp", new StandardSQLFunction("unix_timestamp", Types.BIGINT));
		registerFunction("utc_date", new NoArgSQLFunction("utc_date", Types.VARCHAR));
		registerFunction("utc_time", new NoArgSQLFunction("utc_time", Types.VARCHAR));
		registerFunction("utc_timestamp", new NoArgSQLFunction("utc_timestamp", Types.VARCHAR));
		registerFunction("week", new StandardSQLFunction("week", Types.INTEGER));
		registerFunction("weekday", new StandardSQLFunction("weekday", Types.INTEGER));
		registerFunction("weekofyear", new StandardSQLFunction("weekofyear", Types.INTEGER));
		registerFunction("year", new StandardSQLFunction("year", Types.INTEGER));
		registerFunction("yearweek", new StandardSQLFunction("yearweek", Types.INTEGER));

		registerFunction("hex", new StandardSQLFunction("hex", Types.VARCHAR));
		registerFunction("oct", new StandardSQLFunction("oct", Types.VARCHAR));

		registerFunction("octet_length", new StandardSQLFunction("octet_length", Types.BIGINT));
		registerFunction("bit_length", new StandardSQLFunction("bit_length", Types.BIGINT));

		registerFunction("bit_count", new StandardSQLFunction("bit_count", Types.BIGINT));
		registerFunction("encrypt", new StandardSQLFunction("encrypt", Types.VARCHAR));
		registerFunction("md5", new StandardSQLFunction("md5", Types.VARCHAR));
		registerFunction("sha1", new StandardSQLFunction("sha1", Types.VARCHAR));
		registerFunction("sha", new StandardSQLFunction("sha", Types.VARCHAR));

		registerFunction("concat", new StandardSQLFunction( "concat", Types.VARCHAR));
		
		registerFunction("date", new StandardSQLFunction("date_format") {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			protected String doRender(int firstArgumentType, List arguments, IConnectionFactory factory) {
				arguments.add("'%Y-%m-%d'");
				return super.doRender(firstArgumentType, arguments, factory);
			}
		});
	}

	protected void registerVarcharTypes() {
		registerColumnType(Types.VARCHAR, "longtext");
		// registerColumnType( Types.VARCHAR, 16777215, "mediumtext");
		// registerColumnType( Types.VARCHAR, 65535, "text");
		registerColumnType(Types.VARCHAR, 255, "varchar($l)");
		registerColumnType(Types.LONGVARCHAR, "longtext");
	}
	
	public char closeQuote() {
		return '`';
	}

	public char openQuote() {
		return '`';
	}
	
	public String getIdentitySelectString() {
		return "select last_insert_id()";
	}

	public String getIdentityColumnString() {
		return "not null auto_increment"; //starts with 1, implicitly
	}
	
	public boolean supportsIfExistsBeforeTableName() {
		return true;
	}
	
	public String getTableComment(String comment) {
		return " comment='" + comment + "'";
	}

	public String getColumnComment(String comment) {
		return " comment '" + comment + "'";
	}
	
	public String getCastTypeName(int code) {
		if (code == Types.INTEGER) {
			return "signed";
		} else if (code == Types.VARCHAR) {
			return "char";
		} else if (code == Types.VARBINARY) {
			return "binary";
		} else {
			return super.getCastTypeName(code);
		}
	}
	
	public String getPaging(String sql, String order, int total, int offset) {
		StringBuffer sb = new StringBuffer(sql);
		if (order != null && order.length() > 0)
			sb.append(" ").append(order);
		sb.append(" LIMIT ")
			.append(total);
		if (offset > 0) {
			sb.append(" OFFSET ");
			sb.append(offset);
		}
		return sb.toString();
	}
}
