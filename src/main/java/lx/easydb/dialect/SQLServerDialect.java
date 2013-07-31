package lx.easydb.dialect;

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
	
	public String getPaging(String sql, String order, int limit, int offset) {
		if (offset > 0) {
			if (order == null || order.length() == 0)
				throw new IllegalArgumentException("An order should be specified for paging query.");
			sql = new StringBuffer(sql.length() + order.length() + 9)
				.append(sql)
				.append(" ").append(order)
				.insert(getAfterSelectInsertPoint(sql), " top " + (limit + offset))
				.toString();
			String anotherOrderby = order.toUpperCase();
			if (anotherOrderby.indexOf(" DESC") > -1)
				anotherOrderby = anotherOrderby.replaceFirst(" DESC", " ASC");
	        else if (anotherOrderby.indexOf(" ASC") > -1)
	            anotherOrderby = anotherOrderby.replaceFirst(" ASC", " DESC");
	        else
	            anotherOrderby += " DESC";
			// NOTE This may not work properly when the total count of records < (limit + offset)
			return new StringBuffer("SELECT * FROM (SELECT top ")
				.append(limit)
				.append(" * FROM (")
				.append(sql)
				.append(") t1 ")
				.append(anotherOrderby)
				.append(") t2 ")
				.append(order)
				.toString();
		} else {
			StringBuffer sb = new StringBuffer(sql.length() + (order == null ? 0 : order.length()) + 9)
				.append(sql);
			if (order != null && order.length() > 0)
				sb.append(" ").append(order);
			return sb.insert(getAfterSelectInsertPoint(sql), " top " + limit)
				.toString();
		}
	}
	
	static int getAfterSelectInsertPoint(String sql) {
		sql = sql.toLowerCase();
		int selectIndex = sql.indexOf("select");
		final int selectDistinctIndex = sql.indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}
}
