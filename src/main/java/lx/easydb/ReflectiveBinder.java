package lx.easydb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lx.easydb.mapping.Column;
import lx.easydb.mapping.IMemberMap;

/**
 * A {@link ValueBinder} that use reflection.
 * 
 * @author Long
 *
 */
public class ReflectiveBinder extends AbstractValueBinder {
	private static final Object[] EMPTY_PARAMS = new Object[0];

	public void bind(PreparedStatement st, Object item, int index,
			String field, int sqlType) throws SQLException {
		Field fieldObj;
		try {
			fieldObj = item.getClass().getDeclaredField(field);
		} catch (NoSuchFieldException e1) {
			fieldObj = null;
		}
		
		Object value = null;
		
		if (fieldObj != null
				&& Modifier.isPublic(fieldObj.getModifiers())
				&& !Modifier.isStatic(fieldObj.getModifiers())
				&& !Modifier.isFinal(fieldObj.getModifiers())) {
			try {
				boolean accessible = fieldObj.isAccessible();
				fieldObj.setAccessible(true);
				value = fieldObj.get(item);
				fieldObj.setAccessible(accessible);
			} catch (IllegalAccessException e) {
				// ignore
			}
		} else {
			String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
			try {
				Method getterMethod = item.getClass().getMethod(getterName, new Class[] { });
				boolean accessible = getterMethod.isAccessible();
				getterMethod.setAccessible(true);
				value = getterMethod.invoke(item, EMPTY_PARAMS);
				getterMethod.setAccessible(accessible);
			} catch (Exception e) {
				// ignore
			}
		}
		
		if (sqlType == Types.EMPTY)
			st.setObject(index, value);
		else
			st.setObject(index, value, sqlType);
	}
	
	public void bind(PreparedStatement st, Object item, int index, Column column)
			throws SQLException {
		IMemberMap mi = column.getMemberInfo();
		if (mi == null) {
			super.bind(st, item, index, column);
			return;
		}
		
		Object value = null;
		Method getterMethod = mi.getGetter();
		Field fieldObj = mi.getField();
		
		if (getterMethod != null) {
			try {
				boolean accessible = getterMethod.isAccessible();
				getterMethod.setAccessible(true);
				value = getterMethod.invoke(item, EMPTY_PARAMS);
				getterMethod.setAccessible(accessible);
			} catch (Exception e) {
				// ignore
			}
		} else if (fieldObj != null
				&& Modifier.isPublic(fieldObj.getModifiers())
				&& !Modifier.isStatic(fieldObj.getModifiers())
				&& !Modifier.isFinal(fieldObj.getModifiers())) {
			try {
				boolean accessible = fieldObj.isAccessible();
				fieldObj.setAccessible(true);
				value = fieldObj.get(item);
				fieldObj.setAccessible(accessible);
			} catch (IllegalAccessException e) {
				// ignore
			}
		}
		
		int dbType = column.getDbType();
		if (dbType == Types.IDENTITY)
			dbType = Types.EMPTY;
		
		if (dbType == Types.EMPTY)
			st.setObject(index, value);
		else
			st.setObject(index, value, dbType);
	}
}
