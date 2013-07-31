package lx.easydb.dialect;

import lx.easydb.Types;
import lx.easydb.dialect.function.NoArgSQLFunction;

/**
 * A dialect for Microsoft SQL Server 2008 with JDBC Driver 3.0 and above
 */
public class SQLServer2008Dialect extends SQLServerDialect {
	
	public SQLServer2008Dialect() {
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "datetime2");

		registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", Types.TIMESTAMP, false));
	}
}
