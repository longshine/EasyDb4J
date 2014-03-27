package lx.easydb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import lx.easydb.mapping.Column;

/**
 * @author Long
 *
 */
public abstract class AbstractValueBinder implements ValueBinder {

	public void bind(PreparedStatement st, Object item, int index, Column column)
			throws SQLException {
		bind(st, item, index, column.getFieldName(), column.getDbType());
	}
	
	protected void setObject(PreparedStatement st, int parameterIndex,
			Object x, int sqlType) throws SQLException {
		if (sqlType == Types.EMPTY
				|| sqlType == Types.IDENTITY
				|| sqlType == Types.NULL)
			st.setObject(parameterIndex, x);
		else
			st.setObject(parameterIndex, x, sqlType);
	}
}
