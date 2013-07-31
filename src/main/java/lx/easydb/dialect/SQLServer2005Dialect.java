package lx.easydb.dialect;

/**
 * A dialect for Microsoft SQL Server 2005
 * 
 * @author Longshine
 */
public class SQLServer2005Dialect extends SQLServerDialect {
	
	public String getPaging(String sql, String order, int limit, int offset) {
		if (offset > 0) {
			if (order == null || order.length() == 0)
				throw new IllegalArgumentException("An order should be specified for paging query.");
			int fromIndex = getBeforeFromInsertPoint(sql);
			sql = new StringBuffer(sql.length() + order.length() + 32)
				.append(sql)
				.insert(fromIndex, ",ROW_NUMBER() OVER (" + order + ") AS RowNum")
				.toString();
			return new StringBuffer(sql.substring(0, fromIndex))
				.append(" FROM (")
	            .append(sql)
	            .append(") t1 WHERE t1.RowNum BETWEEN ")
	            .append(offset + 1)
	            .append(" AND ")
	            .append(limit + offset)
	            .toString();
		} else {
			return super.getPaging(sql, order, limit, offset);
		}
	}
	
	static int getBeforeFromInsertPoint(String sql) {
		int fromIndex = sql.toLowerCase().indexOf(" from ");
        return fromIndex;
    }
}
