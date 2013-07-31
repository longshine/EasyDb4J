package lx.easydb.dialect;

import lx.easydb.Types;
import lx.easydb.dialect.function.NoArgSQLFunction;
import lx.easydb.dialect.function.SQLFunctionTemplate;
import lx.easydb.dialect.function.StandardSQLFunction;
import lx.easydb.dialect.function.VarArgsSQLFunction;

/**
 * An abstract base class for Sybase and MS SQL Server dialects.
 */
public abstract class AbstractTransactSQLDialect extends Dialect {

	public AbstractTransactSQLDialect() {
        registerColumnType(Types.IDENTITY, "int");
		registerColumnType(Types.BIT, "tinyint"); // Sybase BIT type does not support null values
		registerColumnType(Types.BIGINT, "numeric(19,0)");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.TINYINT, "tinyint");
		registerColumnType(Types.INTEGER, "int");
		registerColumnType(Types.CHAR, "char(1)");
		registerColumnType(Types.VARCHAR, "varchar($l)");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.DOUBLE, "double precision");
		registerColumnType(Types.DATE, "datetime");
		registerColumnType(Types.TIME, "datetime");
		registerColumnType(Types.TIMESTAMP, "datetime");
		registerColumnType(Types.VARBINARY, "varbinary($l)");
		registerColumnType(Types.NUMERIC, "numeric($p,$s)");
		registerColumnType(Types.BLOB, "image");
		registerColumnType(Types.CLOB, "text");

		registerFunction("ascii", new StandardSQLFunction("ascii", Types.INTEGER));
		registerFunction("char", new StandardSQLFunction("char", Types.CHAR));
		registerFunction("len", new StandardSQLFunction("len", Types.BIGINT));
		registerFunction("lower", new StandardSQLFunction("lower"));
		registerFunction("upper", new StandardSQLFunction("upper"));
		registerFunction("str", new StandardSQLFunction("str", Types.VARCHAR));
		registerFunction("ltrim", new StandardSQLFunction("ltrim"));
		registerFunction("rtrim", new StandardSQLFunction("rtrim"));
		registerFunction("reverse", new StandardSQLFunction("reverse"));
		registerFunction("space", new StandardSQLFunction("space", Types.VARCHAR));

		registerFunction("user", new NoArgSQLFunction("user", Types.VARCHAR));

		registerFunction("current_timestamp", new NoArgSQLFunction("getdate", Types.TIMESTAMP));
		registerFunction("current_time", new NoArgSQLFunction("getdate", Types.TIME));
		registerFunction("current_date", new NoArgSQLFunction("getdate", Types.DATE));

		registerFunction("getdate", new NoArgSQLFunction("getdate", Types.TIMESTAMP));
		registerFunction("getutcdate", new NoArgSQLFunction("getutcdate", Types.TIMESTAMP));
		registerFunction("day", new StandardSQLFunction("day", Types.INTEGER));
		registerFunction("month", new StandardSQLFunction("month", Types.INTEGER));
		registerFunction("year", new StandardSQLFunction("year", Types.INTEGER));
		registerFunction("datename", new StandardSQLFunction("datename", Types.VARCHAR));

		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("sign", new StandardSQLFunction("sign", Types.INTEGER));

		registerFunction("acos", new StandardSQLFunction("acos", Types.DOUBLE));
		registerFunction("asin", new StandardSQLFunction("asin", Types.DOUBLE));
		registerFunction("atan", new StandardSQLFunction("atan", Types.DOUBLE));
		registerFunction("cos", new StandardSQLFunction("cos", Types.DOUBLE));
		registerFunction("cot", new StandardSQLFunction("cot", Types.DOUBLE));
		registerFunction("exp", new StandardSQLFunction("exp", Types.DOUBLE));
		registerFunction("log", new StandardSQLFunction( "log", Types.DOUBLE));
		registerFunction("log10", new StandardSQLFunction("log10", Types.DOUBLE));
		registerFunction("sin", new StandardSQLFunction("sin", Types.DOUBLE));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", Types.DOUBLE));
		registerFunction("tan", new StandardSQLFunction("tan", Types.DOUBLE));
		registerFunction("pi", new NoArgSQLFunction("pi", Types.DOUBLE));
		registerFunction("square", new StandardSQLFunction("square"));
		registerFunction("rand", new StandardSQLFunction("rand", Types.FLOAT));

		registerFunction("radians", new StandardSQLFunction("radians", Types.DOUBLE));
		registerFunction("degrees", new StandardSQLFunction("degrees", Types.DOUBLE));

		registerFunction("round", new StandardSQLFunction("round"));
		registerFunction("ceiling", new StandardSQLFunction("ceiling"));
		registerFunction("floor", new StandardSQLFunction("floor"));

		registerFunction("isnull", new StandardSQLFunction("isnull"));

		registerFunction("concat", new VarArgsSQLFunction(Types.VARCHAR, "(", "+", ")"));

		registerFunction("length", new StandardSQLFunction("len", Types.INTEGER));
		registerFunction("trim", new SQLFunctionTemplate(Types.VARCHAR, "ltrim(rtrim(?1))"));
	}
	
	public String getIdentitySelectString() {
		return "select @@identity";
	}
	
	public String getIdentityColumnString() {
		return "identity not null"; //starts with 1, implicitly
	}
}
