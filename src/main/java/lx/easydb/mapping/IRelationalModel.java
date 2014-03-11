package lx.easydb.mapping;

import lx.easydb.dialect.Dialect;

/**
 * Represents a relational model.
 * 
 * @author Long
 *
 */
public interface IRelationalModel {
	/**
	 * Gets a SQL statement for creating this model.
	 * @param dialect
	 * @param defaultCatalog
	 * @param defaultSchema
	 */
	String toSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema);
	/**
	 * Gets a SQL statement for dropping this model.
	 * @param dialect
	 * @param defaultCatalog
	 * @param defaultSchema
	 */
	String toSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema);
}
