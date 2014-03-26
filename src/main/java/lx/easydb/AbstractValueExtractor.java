package lx.easydb;

import java.sql.ResultSet;
import java.sql.SQLException;

import lx.easydb.mapping.Column;

/**
 * @author Long
 *
 */
public abstract class AbstractValueExtractor implements ValueExtractor {

	public void extract(ResultSet rs, Object item, int index, Column column)
			throws SQLException {
		extract(rs, item, index, column.getFieldName());
	}
}
