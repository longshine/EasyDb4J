package lx.easydb.dialect;

import lx.easydb.Types;
import lx.easydb.dialect.function.SQLFunctionTemplate;
import lx.easydb.dialect.function.StandardSQLFunction;
import lx.easydb.dialect.function.VarArgsSQLFunction;

public class SQLiteDialect extends Dialect {
	
	public SQLiteDialect() {
        registerColumnType(Types.IDENTITY, "integer");
        registerColumnType(Types.BOOLEAN, "integer");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.NUMERIC, "numeric");
        registerColumnType(Types.DECIMAL, "decimal");
        registerColumnType(Types.VARCHAR, "text");
        registerColumnType(Types.CHAR, "char");
        registerColumnType(Types.VARCHAR, "varchar");
        registerColumnType(Types.LONGVARCHAR, "longvarchar");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.BINARY, "blob");
        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.LONGVARBINARY, "blob");
        registerColumnType(Types.NULL, "null");
        registerColumnType(Types.BLOB, "blob");
        registerColumnType(Types.CLOB, "clob");

        registerFunction("concat", new VarArgsSQLFunction(Types.VARCHAR, "", "||", ""));
        registerFunction("mod", new SQLFunctionTemplate(Types.INTEGER, "?1 % ?2"));
        registerFunction("substr", new StandardSQLFunction("substr", Types.VARCHAR));
        registerFunction("substring", new StandardSQLFunction("substr", Types.VARCHAR));
		
        registerFunction("date", new StandardSQLFunction("date"));
    }
	
	public boolean supportsIfExistsBeforeTableName() {
		return true;
	}
	
	public boolean hasPrimaryKeyInIdentityColumn() {
		return true;
	}
	
	public String getIdentityColumnString() {
        return "primary key autoincrement";
    }
	
	protected String getIdentitySelectString() {
        return "select last_insert_rowid();";
    }
	
	public boolean supportsTemporaryTables() {
		return true;
	}
	
	public String getCreateTemporaryTableString() {
		return "create temporary table if not exists";
	}
	
	public String getAddPrimaryKeyConstraintString(String constraintName) {
		throw new UnsupportedOperationException(
				"No add primary key syntax supported by SQLiteDialect");
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
