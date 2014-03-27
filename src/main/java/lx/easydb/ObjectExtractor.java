package lx.easydb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lx.easydb.mapping.Column;
import lx.easydb.mapping.Table;

/**
 * Base class of {@link ValueExtractor} for objects.
 * Overrides {@link #newInstance()} to create instances and 
 * {@link #extract(ResultSet, Object, int, String)} to
 * extract each field.
 * 
 * @author Long
 *
 */
public abstract class ObjectExtractor implements ValueExtractor {

	public List extract(ResultSet rs, Table table) throws SQLException {
		ArrayList list = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		while (rs.next()) {
			Object obj = newInstance();
			
			for (int i = 1; i <= count; i++) {
				String columnName = rsmd.getColumnName(i), fieldName = null;
				if (table != null)
					fieldName = table.getFieldName(columnName);
				if (fieldName == null)
					fieldName = columnName;
				extract(rs, obj, i, fieldName);
			}
			
			list.add(obj);
		}
		return list;
	}
	
	public void extract(ResultSet rs, Object item,
			int index, Column column) throws SQLException {
		extract(rs, item, index, column.getFieldName());
	}

	protected abstract Object newInstance();
	protected abstract void extract(ResultSet rs, Object item,
			int index, String field) throws SQLException;
}
