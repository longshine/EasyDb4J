package lx.easydb;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lx.easydb.mapping.Column;
import lx.easydb.mapping.IMemberMap;
import lx.easydb.mapping.Table;

/**
 * A {@link ValueExtractor} that use reflection.
 * 
 * @author Long
 *
 */
public class ReflectiveExtractor implements ValueExtractor {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List extract(ResultSet rs, Table table) throws SQLException {
		ArrayList list = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		while (rs.next()) {
			Object obj = newInstance(table);
			
			for (int i = 1; i <= count; i++) {
				String columnName = rsmd.getColumnName(i);
				Column column = null;
				if (table != null)
					column = table.findColumnByColumnName(columnName);
				if (column == null)
					extract(rs, obj, i, columnName);
				else
					extract(rs, obj, i, column);
			}
			
			list.add(obj);
		}
		return list;
	}
	
	public void extract(ResultSet rs, Object item, int index, Column column) throws SQLException {
		IMemberMap mi = column.getMemberInfo();
		if (mi == null) {
			extract(rs, item, index, column.getFieldName());
			return;
		}
		
		Field fieldObj = mi.getField();
		if (fieldObj == null) {
			extract(rs, item, index, column.getFieldName());
			return;
		}
		
		int sqlType = Types.get(fieldObj.getType());
		Object value = getValue(rs, index, sqlType);
		Method setterMethod = mi.getSetter();
		
		if (setterMethod != null) {
			try {
				boolean accessible = setterMethod.isAccessible();
				setterMethod.setAccessible(true);
				setterMethod.invoke(item, new Object[] { value });
				setterMethod.setAccessible(accessible);
			} catch (Exception e) {
				// ignore
			}
		} else if (Modifier.isPublic(fieldObj.getModifiers())
				&& !Modifier.isStatic(fieldObj.getModifiers())
				&& !Modifier.isFinal(fieldObj.getModifiers())) {
			try {
				boolean accessible = fieldObj.isAccessible();
				fieldObj.setAccessible(true);
				fieldObj.set(item, value);
				fieldObj.setAccessible(accessible);
			} catch (IllegalAccessException e) {
				// ignore
			}
		}
	}

	public void extract(ResultSet rs, Object item, int index, String field)
			throws SQLException {
		Field fieldObj;
		try {
			fieldObj = item.getClass().getDeclaredField(field);
		} catch (NoSuchFieldException e) {
			return;
		}
		
		int sqlType = Types.get(fieldObj.getType());
		Object value = getValue(rs, index, sqlType);
		
		if (Modifier.isPublic(fieldObj.getModifiers())
				&& !Modifier.isStatic(fieldObj.getModifiers())
				&& !Modifier.isFinal(fieldObj.getModifiers())) {
			try {
				boolean accessible = fieldObj.isAccessible();
				fieldObj.setAccessible(true);
				fieldObj.set(item, value);
				fieldObj.setAccessible(accessible);
			} catch (IllegalAccessException e) {
				// ignore
			}
		} else {
			String setterName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
			
			try {
				Method setterMethod = item.getClass().getMethod(setterName, new Class[] { fieldObj.getType() });
				boolean accessible = setterMethod.isAccessible();
				setterMethod.setAccessible(true);
				setterMethod.invoke(item, new Object[] { value });
				setterMethod.setAccessible(accessible);
			} catch (Exception e) {
				// ignore
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object newInstance(Table table) throws QueryException {
		try {
			Constructor c = table.getEntityClass().getConstructor(ReflectHelper.EMPTY_CLASSES);
			c.setAccessible(true);
			return c.newInstance(ReflectHelper.EMPTY_PARAMS);
		} catch (Exception e) {
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
