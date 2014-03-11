package lx.easydb.dialect;

import lx.easydb.Types;

/**
 * An SQL dialect for MySQL 5.x specific features.
 */
public class MySQL5Dialect extends MySQLDialect {
	
	protected void registerVarcharTypes() {
		registerColumnType(Types.VARCHAR, "longtext");
//		registerColumnType(Types.VARCHAR, 16777215, "mediumtext");
		registerColumnType(Types.VARCHAR, 65535, "varchar($l)");
		registerColumnType(Types.LONGVARCHAR, "longtext");
	}

	public boolean supportsColumnCheck() {
		return false;
	}
}
