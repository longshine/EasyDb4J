package lx.easydb.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Long
 *
 */
public interface IMemberMap {
	String getColumnName();
	Class getMemberType();
	Field getField();
	Method getGetter();
	Method getSetter();
}

class SimpleMemberMap implements IMemberMap {
	private final String columnName;
	private final Field field;
	private final Method getter;
	private final Method setter;
	
	public SimpleMemberMap(String columnName, Field field) {
		this.columnName = columnName;
		this.field = field;
		this.getter = this.setter = null;
	}
	
	public SimpleMemberMap(String columnName, Field field,
			Method getter, Method setter) {
		this.columnName = columnName;
		this.field = field;
		this.getter = getter;
		this.setter = setter;
	}

	public String getColumnName() {
		return columnName;
	}

	public Field getField() {
		return field;
	}

	public Class getMemberType() {
		return field.getType();
	}

	public Method getGetter() {
		return getter;
	}

	public Method getSetter() {
		return setter;
	}
}