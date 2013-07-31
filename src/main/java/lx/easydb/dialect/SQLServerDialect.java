package lx.easydb.dialect;

import lx.easydb.StringHelper;
import lx.easydb.Types;
import lx.easydb.dialect.function.AnsiTrimEmulationFunction;
import lx.easydb.dialect.function.SQLFunctionTemplate;
import lx.easydb.dialect.function.StandardSQLFunction;

/**
 * A dialect for Microsoft SQL Server 2000 and 2005
 * 
 * @author smeshlink
 */
public class SQLServerDialect extends AbstractTransactSQLDialect {

	public SQLServerDialect() {
		registerColumnType(Types.VARBINARY, "image");
		registerColumnType(Types.VARBINARY, 8000, "varbinary($l)");
		registerColumnType(Types.LONGVARBINARY, "image");
		registerColumnType(Types.LONGVARCHAR, "text");

		registerFunction("second", new SQLFunctionTemplate(Types.INTEGER, "datepart(second, ?1)"));
		registerFunction("minute", new SQLFunctionTemplate(Types.INTEGER, "datepart(minute, ?1)"));
		registerFunction("hour", new SQLFunctionTemplate(Types.INTEGER, "datepart(hour, ?1)"));
		registerFunction("locate", new StandardSQLFunction("charindex", Types.INTEGER ));

		registerFunction("extract", new SQLFunctionTemplate(Types.INTEGER, "datepart(?1, ?3)"));
		registerFunction("mod", new SQLFunctionTemplate(Types.INTEGER, "?1 % ?2"));
		registerFunction("bit_length", new SQLFunctionTemplate(Types.INTEGER, "datalength(?1) * 8"));

		registerFunction("trim", new AnsiTrimEmulationFunction());
	}
	
	public char closeQuote() {
		return ']';
	}

	public char openQuote() {
		return '[';
	}
	
	public String getPaging(String sql, String order, int total, int offset) {
		if (offset > 0) {
			String anotherOrderby = order.toUpperCase();
			// FIXME method replace is in java 1.5
			if (anotherOrderby.contains(" DESC"))
	            anotherOrderby = anotherOrderby.replace(" DESC", " ASC");
	        else if (anotherOrderby.contains(" ASC"))
	            anotherOrderby = anotherOrderby.replace(" ASC", " DESC");
	        else
	            anotherOrderby += " DESC";
			StringBuffer sb = StringHelper.createBuilder();
			sb.append("SELECT * FROM (SELECT top ");
			sb.append(total);
			sb.append(" * FROM (SELECT top ");
			sb.append(total + offset);
			sb.append(" * FROM (");
			sb.append(sql);
			sb.append(") t1 ");
			sb.append(order);
			sb.append(") t2 ");
			sb.append(anotherOrderby);
			sb.append(") t3 ");
			sb.append(order);
			return sb.toString();
		}
		else {
			return "SELECT TOP " + total + sql.substring(6);
		}
	}
}
