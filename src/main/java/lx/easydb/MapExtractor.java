package lx.easydb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lx.easydb.mapping.Table;
/**
 * {@link ValueExtractor} for {@link Map} types,
 * extracts fields and puts them in a case-insensitive {@link LinkedHashMap}.
 * 
 * @author Long
 *
 */
public class MapExtractor implements ValueExtractor {

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
	
	public void extract(ResultSet rs, Object item, int index,
			String field) throws SQLException {
		Map map = (Map) item;
		map.put(field, rs.getObject(index));
	}

	protected Object newInstance() {
		return new LinkedHashMap() {
			private static final long serialVersionUID = -5300859616547090379L;

			public Object put(Object key, Object value) {
				return key == null ? super.put(key, value) :
					super.put(key.toString().toUpperCase(), value);
			}
			
			public Object get(Object key) {
				return key == null ? super.get(key) :
					super.get(key.toString().toUpperCase());
			}
		};
	}
}
