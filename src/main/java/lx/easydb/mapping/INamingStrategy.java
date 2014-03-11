package lx.easydb.mapping;

/**
 * Provides methods to name tables and columns
 * by type names and property names.
 * 
 * @author Long
 *
 */
public interface INamingStrategy {
	/**
	 * Maps a property name to a column name.
	 * @param propertyName
	 */
	String getColumnName(String propertyName);
	/**
	 * Maps a type name to a table name. 
	 * @param typeName
	 */
	String getTableName(String typeName);
}
