package lx.easydb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import lx.easydb.mapping.Column;

/**
 * Provides method to read parameters from an object
 * into a {@link PreparedStatement}.
 * 
 * @author Long
 *
 */
public interface ValueBinder {
	/**
	 * Reads a parameter named <code>field</code> from
	 * the specified object, and set it to the given
	 * {@link PreparedStatement}.
	 * @param st the {@link PreparedStatement} to bind
	 * @param item the source object from which to read parameter's value
	 * @param index the index of the parameter in the statement
	 * @param field the name of the parameter
	 * @param sqlType the type of the parameter defined in {@link Types}
	 * @throws SQLException
	 */
	void bind(PreparedStatement st, Object item, int index, String field,
			int sqlType) throws SQLException;
	/**
	 * Reads a parameter mapped by <code>column</code> from
	 * the specified object, and set it to the given
	 * {@link PreparedStatement}.
	 * @param st the {@link PreparedStatement} to bind
	 * @param item the source object from which to read parameter's value
	 * @param index the index of the parameter in the statement
	 * @param column the column of the parameter
	 * @throws SQLException
	 */
	void bind(PreparedStatement st, Object item, int index, Column column)
			throws SQLException;
}
