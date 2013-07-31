package lx.easydb.dialect;

import java.util.List;

import lx.easydb.IConnectionFactory;
import lx.easydb.QueryException;
import lx.easydb.Types;
import lx.easydb.dialect.function.ISQLFunction;
import lx.easydb.dialect.function.NoArgSQLFunction;
import lx.easydb.dialect.function.SQLFunctionTemplate;
import lx.easydb.dialect.function.StandardSQLFunction;
import lx.easydb.dialect.function.VarArgsSQLFunction;

public class PostgreSQLDialect extends Dialect {
	
	public PostgreSQLDialect() {
        registerColumnType(Types.IDENTITY, "int");
		registerColumnType(Types.BIT, "bool");
		registerColumnType(Types.BIGINT, "int8");
		registerColumnType(Types.SMALLINT, "int2");
		registerColumnType(Types.TINYINT, "int2");
		registerColumnType(Types.INTEGER, "int4");
		registerColumnType(Types.CHAR, "char(1)");
		registerColumnType(Types.VARCHAR, "varchar($l)");
		registerColumnType(Types.FLOAT, "float4");
		registerColumnType(Types.DOUBLE, "float8");
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");
		registerColumnType(Types.VARBINARY, "bytea");
		registerColumnType(Types.LONGVARCHAR, "text");
		registerColumnType(Types.LONGVARBINARY, "bytea");
		registerColumnType(Types.CLOB, "text");
		registerColumnType(Types.BLOB, "oid");
		registerColumnType(Types.NUMERIC, "numeric($p, $s)");
		registerColumnType(Types.OTHER, "uuid");

		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("sign", new StandardSQLFunction("sign", Types.INTEGER));

		registerFunction("acos", new StandardSQLFunction("acos", Types.DOUBLE));
		registerFunction("asin", new StandardSQLFunction("asin", Types.DOUBLE));
		registerFunction("atan", new StandardSQLFunction("atan", Types.DOUBLE));
		registerFunction("cos", new StandardSQLFunction("cos", Types.DOUBLE));
		registerFunction("cot", new StandardSQLFunction("cot", Types.DOUBLE));
		registerFunction("exp", new StandardSQLFunction("exp", Types.DOUBLE));
		registerFunction("ln", new StandardSQLFunction("ln", Types.DOUBLE));
		registerFunction("log", new StandardSQLFunction("log", Types.DOUBLE));
		registerFunction("sin", new StandardSQLFunction("sin", Types.DOUBLE));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", Types.DOUBLE));
		registerFunction("cbrt", new StandardSQLFunction("cbrt", Types.DOUBLE));
		registerFunction("tan", new StandardSQLFunction("tan", Types.DOUBLE));
		registerFunction("radians", new StandardSQLFunction("radians", Types.DOUBLE));
		registerFunction("degrees", new StandardSQLFunction("degrees", Types.DOUBLE));

		registerFunction("stddev", new StandardSQLFunction("stddev", Types.DOUBLE));
		registerFunction("variance", new StandardSQLFunction("variance", Types.DOUBLE));

		registerFunction("random", new NoArgSQLFunction("random", Types.DOUBLE));

		registerFunction("round", new StandardSQLFunction("round"));
		registerFunction("trunc", new StandardSQLFunction("trunc"));
		registerFunction("ceil", new StandardSQLFunction("ceil"));
		registerFunction("floor", new StandardSQLFunction("floor"));

		registerFunction("chr", new StandardSQLFunction("chr", Types.CHAR));
		registerFunction("lower", new StandardSQLFunction("lower"));
		registerFunction("upper", new StandardSQLFunction("upper"));
		registerFunction("substr", new StandardSQLFunction("substr", Types.VARCHAR));
		registerFunction("initcap", new StandardSQLFunction("initcap"));
		registerFunction("to_ascii", new StandardSQLFunction("to_ascii"));
		registerFunction("quote_ident", new StandardSQLFunction("quote_ident", Types.VARCHAR));
		registerFunction("quote_literal", new StandardSQLFunction("quote_literal", Types.VARCHAR));
		registerFunction("md5", new StandardSQLFunction("md5"));
		registerFunction("ascii", new StandardSQLFunction("ascii", Types.INTEGER));
		registerFunction("char_length", new StandardSQLFunction("char_length", Types.BIGINT));
		registerFunction("bit_length", new StandardSQLFunction("bit_length", Types.BIGINT));
		registerFunction("octet_length", new StandardSQLFunction("octet_length", Types.BIGINT));

		registerFunction("age", new StandardSQLFunction("age"));
		registerFunction("current_date", new NoArgSQLFunction("current_date", Types.DATE, false));
		registerFunction("current_time", new NoArgSQLFunction("current_time", Types.TIME, false));
		registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", Types.TIMESTAMP, false));
		registerFunction("date_trunc", new StandardSQLFunction( "date_trunc", Types.TIMESTAMP ));
		registerFunction("localtime", new NoArgSQLFunction("localtime", Types.TIME, false));
		registerFunction("localtimestamp", new NoArgSQLFunction("localtimestamp", Types.TIMESTAMP, false));
		registerFunction("now", new NoArgSQLFunction("now", Types.TIMESTAMP));
		registerFunction("timeofday", new NoArgSQLFunction("timeofday", Types.VARCHAR));

		registerFunction("current_user", new NoArgSQLFunction("current_user", Types.VARCHAR, false));
		registerFunction("session_user", new NoArgSQLFunction("session_user", Types.VARCHAR, false));
		registerFunction("user", new NoArgSQLFunction("user", Types.VARCHAR, false));
		registerFunction("current_database", new NoArgSQLFunction("current_database", Types.VARCHAR, true));
		registerFunction("current_schema", new NoArgSQLFunction("current_schema", Types.VARCHAR, true));
		
		registerFunction("to_char", new StandardSQLFunction("to_char", Types.VARCHAR));
		registerFunction("to_date", new StandardSQLFunction("to_date", Types.DATE));
		registerFunction("to_timestamp", new StandardSQLFunction("to_timestamp", Types.TIMESTAMP));
		registerFunction("to_number", new StandardSQLFunction("to_number", Types.NUMERIC));

		registerFunction("concat", new VarArgsSQLFunction( Types.VARCHAR, "(","||",")" ));

		registerFunction("locate", new PositionSubstringFunction());

		registerFunction("str", new SQLFunctionTemplate(Types.VARCHAR, "cast(?1 as varchar)"));
		
		registerFunction("date", new StandardSQLFunction("to_date"));
	}
	
	public char openQuote() {
        return '`';
    }
	
	public char closeQuote() {
		return '`';
	}
	
	public String getIdentitySelectString(String table, String column, int type) {
		return new StringBuffer().append("select currval('")
			.append(table)
			.append('_')
			.append(column)
			.append("_seq')")
			.toString();
	}
	
	public String getIdentityColumnString(int type) {
		return type == Types.BIGINT ?
			"bigserial not null" :
			"serial not null";
	}
	
	public boolean hasDataTypeInIdentityColumn() {
		return false;
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

	/**
	 * Emulation of <tt>locate()</tt> on PostgreSQL
	 * @author Gavin King
	 */
	static class PositionSubstringFunction implements ISQLFunction {
		
		public boolean hasArguments() {
			return true;
		}

		public boolean hasParenthesesIfNoArguments() {
			return true;
		}

		public int getReturnType(int firstArgumentType) {
			return Types.INTEGER;
		}

		public String render(int firstArgumentType, List args, IConnectionFactory factory) throws QueryException {
			boolean threeArgs = args.size() > 2;
			Object pattern = args.get(0);
			Object string = args.get(1);
			Object start = threeArgs ? args.get(2) : null;

			StringBuffer buf = new StringBuffer();
			if (threeArgs) buf.append('(');
			buf.append("position(").append( pattern ).append(" in ");
			if (threeArgs) buf.append( "substring(");
			buf.append( string );
			if (threeArgs) buf.append( ", " ).append( start ).append(')');
			buf.append(')');
			if (threeArgs) buf.append('+').append( start ).append("-1)");
			return buf.toString();
		}
	}
}
