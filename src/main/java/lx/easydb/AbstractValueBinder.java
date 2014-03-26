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
		int dbType = column.getDbType();
		if (dbType == Types.IDENTITY)
			dbType = Types.EMPTY;
		bind(st, item, index, column.getFieldName(), dbType);
	}
}
