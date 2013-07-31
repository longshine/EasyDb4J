package lx.easydb.dialect;

/**
 * A dialect for Microsoft SQL Server 2012
 * 
 * @author Longshine
 */
public class SQLServer2012Dialect extends SQLServerDialect {
	
	public String getPaging(String sql, String order, int limit, int offset) {
		StringBuffer sb = new StringBuffer(sql);
		if (order != null && order.length() > 0)
			sb.append(" ").append(order);
		if (offset > 0) {
			sb.append(" OFFSET ")
	            .append(offset)
	            .append(" ROWS");
		}
		return sb.append(" FETCH NEXT ")
	        .append(limit)
	        .append(" ROWS ONLY")
	        .toString();
	}
}
