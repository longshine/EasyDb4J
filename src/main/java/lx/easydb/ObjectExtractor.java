package lx.easydb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lx.easydb.mapping.Table;

public abstract class ObjectExtractor implements ValueExtractor {

	public List extract(ResultSet rs, Table table) throws SQLException {
		ArrayList list = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		while (rs.next()) {
			Object obj = newInstance();
			
			for (int i = 1; i <= count; i++) {
				String columnName = rsmd.getColumnName(i);
				extract(rs, obj, i, table.getFieldName(columnName));
			}
			
			list.add(obj);
		}
		return list;
	}

	protected abstract Object newInstance();
}
