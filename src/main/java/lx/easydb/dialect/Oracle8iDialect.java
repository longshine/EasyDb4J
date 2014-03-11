package lx.easydb.dialect;

import lx.easydb.Types;
import lx.easydb.dialect.function.NoArgSQLFunction;
import lx.easydb.dialect.function.SQLFunctionTemplate;
import lx.easydb.dialect.function.StandardSQLFunction;
import lx.easydb.dialect.function.VarArgsSQLFunction;

/**
 * A dialect for Oracle 8i.
 */
public class Oracle8iDialect extends Dialect {

	public Oracle8iDialect() {
		registerCharacterTypeMappings();
		registerNumericTypeMappings();
		registerDateTimeTypeMappings();
		registerLargeObjectTypeMappings();

		registerReverseHibernateTypeMappings();

		registerFunctions();
	}

	protected void registerCharacterTypeMappings() {
		registerColumnType(Types.CHAR, "char(1)");
		registerColumnType(Types.VARCHAR, 4000, "varchar2($l)");
		registerColumnType(Types.VARCHAR, "long");
	}

	protected void registerNumericTypeMappings() {
		registerColumnType(Types.BIT, "number(1,0)");
		registerColumnType(Types.BIGINT, "number(19,0)");
		registerColumnType(Types.SMALLINT, "number(5,0)");
		registerColumnType(Types.TINYINT, "number(3,0)");
		registerColumnType(Types.INTEGER, "number(10,0)");

		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.DOUBLE, "double precision");
		registerColumnType(Types.NUMERIC, "number($p,$s)");
		registerColumnType(Types.DECIMAL, "number($p,$s)");
	}

	protected void registerDateTimeTypeMappings() {
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "date");
		registerColumnType(Types.TIMESTAMP, "date");
	}

	protected void registerLargeObjectTypeMappings() {
		registerColumnType(Types.VARBINARY, 2000, "raw($l)");
		registerColumnType(Types.VARBINARY, "long raw");

		registerColumnType(Types.BLOB, "blob");
		registerColumnType(Types.CLOB, "clob");

		registerColumnType(Types.LONGVARCHAR, "long");
		registerColumnType(Types.LONGVARBINARY, "long raw");		
	}

	protected void registerReverseHibernateTypeMappings() {
	}

	protected void registerFunctions() {
		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("sign", new StandardSQLFunction("sign", Types.INTEGER));

		registerFunction("acos", new StandardSQLFunction("acos", Types.DOUBLE));
		registerFunction("asin", new StandardSQLFunction("asin", Types.DOUBLE));
		registerFunction("atan", new StandardSQLFunction("atan", Types.DOUBLE));
		registerFunction("cos", new StandardSQLFunction("cos", Types.DOUBLE));
		registerFunction("cosh", new StandardSQLFunction("cosh", Types.DOUBLE));
		registerFunction("exp", new StandardSQLFunction("exp", Types.DOUBLE));
		registerFunction("ln", new StandardSQLFunction("ln", Types.DOUBLE));
		registerFunction("sin", new StandardSQLFunction("sin", Types.DOUBLE));
		registerFunction("sinh", new StandardSQLFunction("sinh", Types.DOUBLE));
		registerFunction("stddev", new StandardSQLFunction("stddev", Types.DOUBLE));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", Types.DOUBLE));
		registerFunction("tan", new StandardSQLFunction("tan", Types.DOUBLE));
		registerFunction("tanh", new StandardSQLFunction("tanh", Types.DOUBLE));
		registerFunction("variance", new StandardSQLFunction("variance", Types.DOUBLE));

		registerFunction("round", new StandardSQLFunction("round"));
		registerFunction("trunc", new StandardSQLFunction("trunc"));
		registerFunction("ceil", new StandardSQLFunction("ceil"));
		registerFunction("floor", new StandardSQLFunction("floor"));

		registerFunction("chr", new StandardSQLFunction("chr", Types.CHAR));
		registerFunction("initcap", new StandardSQLFunction("initcap"));
		registerFunction("lower", new StandardSQLFunction("lower"));
		registerFunction("ltrim", new StandardSQLFunction("ltrim"));
		registerFunction("rtrim", new StandardSQLFunction("rtrim"));
		registerFunction("soundex", new StandardSQLFunction("soundex"));
		registerFunction("upper", new StandardSQLFunction("upper"));
		registerFunction("ascii", new StandardSQLFunction("ascii", Types.INTEGER));

		registerFunction("to_char", new StandardSQLFunction("to_char", Types.VARCHAR));
		registerFunction("to_date", new StandardSQLFunction("to_date", Types.TIMESTAMP));

		registerFunction("current_date", new NoArgSQLFunction("current_date", Types.DATE, false));
		registerFunction("current_time", new NoArgSQLFunction("current_timestamp", Types.TIME, false));
		registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", Types.TIMESTAMP, false));

		registerFunction("last_day", new StandardSQLFunction("last_day", Types.DATE));
		registerFunction("sysdate", new NoArgSQLFunction("sysdate", Types.DATE, false));
		registerFunction("systimestamp", new NoArgSQLFunction("systimestamp", Types.TIMESTAMP, false));
		registerFunction("uid", new NoArgSQLFunction("uid", Types.INTEGER, false));
		registerFunction("user", new NoArgSQLFunction("user", Types.VARCHAR, false));

		registerFunction("rowid", new NoArgSQLFunction("rowid", Types.BIGINT, false));
		registerFunction("rownum", new NoArgSQLFunction("rownum", Types.BIGINT, false));

		// Multi-param string dialect functions...
		registerFunction("concat", new VarArgsSQLFunction(Types.VARCHAR, "", "||", ""));
		registerFunction("instr", new StandardSQLFunction("instr", Types.INTEGER));
		registerFunction("instrb", new StandardSQLFunction("instrb", Types.INTEGER));
		registerFunction("lpad", new StandardSQLFunction("lpad", Types.VARCHAR));
		registerFunction("replace", new StandardSQLFunction("replace", Types.VARCHAR));
		registerFunction("rpad", new StandardSQLFunction("rpad", Types.VARCHAR));
		registerFunction("substr", new StandardSQLFunction("substr", Types.VARCHAR));
		registerFunction("substrb", new StandardSQLFunction("substrb", Types.VARCHAR));
		registerFunction("translate", new StandardSQLFunction("translate", Types.VARCHAR));

		registerFunction("substring", new StandardSQLFunction("substr", Types.VARCHAR ));
		registerFunction("locate", new SQLFunctionTemplate( Types.INTEGER, "instr(?2,?1)" ));
		registerFunction("bit_length", new SQLFunctionTemplate( Types.INTEGER, "vsize(?1)*8" ));
		//registerFunction("coalesce", new NvlFunction());

		// Multi-param numeric dialect functions...
		registerFunction("atan2", new StandardSQLFunction("atan2", Types.FLOAT));
		registerFunction("log", new StandardSQLFunction("log", Types.INTEGER));
		registerFunction("mod", new StandardSQLFunction("mod", Types.INTEGER));
		registerFunction("nvl", new StandardSQLFunction("nvl"));
		registerFunction("nvl2", new StandardSQLFunction("nvl2"));
		registerFunction("power", new StandardSQLFunction("power", Types.FLOAT));

		// Multi-param date dialect functions...
		registerFunction("add_months", new StandardSQLFunction("add_months", Types.DATE));
		registerFunction("months_between", new StandardSQLFunction("months_between", Types.FLOAT));
		registerFunction("next_day", new StandardSQLFunction("next_day", Types.DATE));

		registerFunction("str", new StandardSQLFunction("to_char", Types.VARCHAR));
	}

	public String getPaging(String sql, String order, int total, int offset) {
		sql = sql.trim();
		boolean isForUpdate = false;
		if ( sql.toLowerCase().endsWith(" for update") ) {
			sql = sql.substring( 0, sql.length()-11 );
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		if (offset > 0) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (offset > 0) {
			pagingSelect
				.append(" ) row_ ) where rownum_ <= ")
				.append(offset + total)
				.append(" and rownum_ > ")
				.append(offset);
		}
		else {
			pagingSelect.append(" ) where rownum <= ").append(total);
		}

		if ( isForUpdate ) {
			pagingSelect.append( " for update");
		}

		return pagingSelect.toString();
	}

	public String getCascadeConstraintsString() {
		return " cascade constraints";
	}

	public boolean supportsTemporaryTables() {
		return true;
	}

	public String getCreateTemporaryTableString() {
		return "create global temporary table";
	}
}
