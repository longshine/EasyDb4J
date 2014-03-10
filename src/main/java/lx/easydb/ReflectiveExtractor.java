package lx.easydb;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lx.easydb.mapping.Table;

/**
 * A {@link ValueExtractor} that use reflection.
 * 
 * @author Long
 *
 */
public class ReflectiveExtractor implements ValueExtractor {
	private static final Class[] EMPTY_CLASSES = new Class[0];
	private static final Object[] EMPTY_PARAMS = new Object[0];
	
	public List extract(ResultSet rs, Table table) throws SQLException {
		ArrayList list = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		while (rs.next()) {
			Object obj = newInstance(table);
			
			for (int i = 1; i <= count; i++) {
				String columnName = rsmd.getColumnName(i);
				extract(rs, obj, i, table == null ? columnName : table.getFieldName(columnName));
			}
			
			list.add(obj);
		}
		return list;
	}

	public void extract(ResultSet rs, Object item, int index, String field)
			throws SQLException {
		Field fieldObj;
		try {
			fieldObj = item.getClass().getDeclaredField(field);
		} catch (NoSuchFieldException e1) {
			fieldObj = null;
		}
		if (fieldObj == null)
			return;
		
		String setterName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
		Object value = rs.getObject(index);
		try {
			Method setterMethod = item.getClass().getMethod(setterName, new Class[] { fieldObj.getType() });
			setterMethod.setAccessible(true);
			value = setterMethod.invoke(item, new Object[] { value });
		} catch (Exception e) {
			return;
		}
	}
	
	protected Object newInstance(Table table) throws QueryException {
		try {
			Constructor c = table.getEntityClass().getConstructor(EMPTY_CLASSES);
			c.setAccessible(true);
			return c.newInstance(EMPTY_PARAMS);
		} catch (ReflectiveOperationException e) {
			throw new QueryException(e);
		}
	}
}
