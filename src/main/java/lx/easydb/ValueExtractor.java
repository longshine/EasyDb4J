package lx.easydb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import lx.easydb.mapping.Column;
import lx.easydb.mapping.Table;

/**
 * Provides methods to read values from a {@link ResultSet}
 * and fill up objects of entities.
 * 
 * @author Long
 *
 */
public interface ValueExtractor {
	/**
	 * Reads a list of entities from a {@link ResultSet}.
	 * @param rs the {@link ResultSet} to read
	 * @param table the mapping of {@link Table} of returned entities
	 * @return a list of objects
	 * @throws SQLException
	 */
	List extract(ResultSet rs, Table table) throws SQLException;
	/**
	 * Reads a property named <code>field</code> from a {@link ResultSet}
	 * and set it to the given object.
	 * @param rs the {@link ResultSet} to read
	 * @param item the object to set
	 * @param index the index of the field in the {@link ResultSet}
	 * @param field the name of the field
	 * @throws SQLException
	 */
	void extract(ResultSet rs, Object item, int index, String field) throws SQLException;
	/**
	 * Reads a property named <code>field</code> from a {@link ResultSet}
	 * and set it to the given object.
	 * @param rs the {@link ResultSet} to read
	 * @param item the object to set
	 * @param index the index of the field in the {@link ResultSet}
	 * @param column the column of the field
	 * @throws SQLException
	 */
	void extract(ResultSet rs, Object item, int index, Column column) throws SQLException;
}
