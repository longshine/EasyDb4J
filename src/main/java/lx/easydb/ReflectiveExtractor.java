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
		
		int sqlType = Types.get(fieldObj.getType());
		
		String setterName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
		Object value = getValue(rs, index, sqlType);
		try {
			Method setterMethod = item.getClass().getMethod(setterName, new Class[] { fieldObj.getType() });
			setterMethod.setAccessible(true);
			setterMethod.invoke(item, new Object[] { value });
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

	private Object getValue(ResultSet rs, int index, int sqlType) throws SQLException {
		switch (sqlType) {
		case Types.INTEGER:
			return new Integer(rs.getInt(index));
		case Types.BIGINT:
			return new Long(rs.getLong(index));
		case Types.SMALLINT:
			return new Short(rs.getShort(index));
		case Types.DOUBLE:
			return new Double(rs.getDouble(index));
		case Types.FLOAT:
			return new Float(rs.getFloat(index));
		case Types.TINYINT:
			return new Byte(rs.getByte(index));
		case Types.DECIMAL:
			return rs.getBigDecimal(index);
		case Types.BOOLEAN:
			return new Boolean(rs.getBoolean(index));
		case Types.VARCHAR:
			return rs.getString(index);
		case Types.BINARY:
			return rs.getBytes(index);
		case Types.TIMESTAMP:
			return rs.getTimestamp(index);
		case Types.TIME:
			return rs.getTime(index);
		case Types.DATE:
			return rs.getDate(index);
		default:
			return rs.getObject(index);
		}
	}
}
