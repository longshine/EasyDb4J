package lx.easydb.dialect;

import lx.easydb.Types;
import lx.easydb.dialect.function.VarArgsSQLFunction;

/**
 * An SQL dialect for Interbase.
 */
public class InterbaseDialect extends Dialect {

	public InterbaseDialect() {
		super();
		registerColumnType(Types.BIT, "smallint");
		registerColumnType(Types.BIGINT, "numeric(18,0)");
		registerColumnType(Types.SMALLINT, "smallint");
		registerColumnType(Types.TINYINT, "smallint");
		registerColumnType(Types.INTEGER, "integer");
		registerColumnType(Types.CHAR, "char(1)");
		registerColumnType(Types.VARCHAR, "varchar($l)");
		registerColumnType(Types.FLOAT, "float");
		registerColumnType(Types.DOUBLE, "double precision");
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "time");
		registerColumnType(Types.TIMESTAMP, "timestamp");
		registerColumnType(Types.VARBINARY, "blob");
		registerColumnType(Types.NUMERIC, "numeric($p,$s)");
		registerColumnType(Types.BLOB, "blob");
		registerColumnType(Types.CLOB, "blob sub_type 1");
		
		registerFunction("concat", new VarArgsSQLFunction(Types.VARCHAR, "(","||",")"));
	}

	public String getPaging(String sql, String order, int total, int offset) {
		StringBuffer sb = new StringBuffer(sql.length() + 15)
			.append(sql);
		if (offset > 0) {
			sb.append(" rows ")
				.append(offset)
				.append(" to ")
				.append(offset + total);
		} else {
			sb.append(" rows ").append(total);
		}
		return sb.toString();
	}
}