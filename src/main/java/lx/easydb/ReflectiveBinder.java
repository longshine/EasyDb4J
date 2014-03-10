package lx.easydb;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A {@link ValueBinder} that use reflection.
 * 
 * @author Long
 *
 */
public class ReflectiveBinder implements ValueBinder {
	private static final Object[] EMPTY_PARAMS = new Object[0];

	public void bind(PreparedStatement st, Object item, int index,
			String field, int sqlType) throws SQLException {
		String getterName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
		Object value;
		try {
			Method getterMethod = item.getClass().getMethod(getterName, new Class[] { });
			getterMethod.setAccessible(true);
			value = getterMethod.invoke(item, EMPTY_PARAMS);
		} catch (Exception e) {
			return;
		}
		
		if (sqlType == Types.EMPTY)
			st.setObject(index, value);
		else
			st.setObject(index, value, sqlType);
	}
}
