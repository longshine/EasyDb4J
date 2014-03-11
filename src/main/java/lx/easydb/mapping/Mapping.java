package lx.easydb.mapping;

import java.util.HashMap;
import java.util.Map;

import lx.easydb.MappingException;

/**
 * Stores O-R mappings.
 * 
 * @author Long
 *
 */
public class Mapping {
	private Map tables = new HashMap();
	private String schema;
	private String catalog;
	private INamingStrategy namingStrategy = new DefaultNamingStrategy();
	
	/**
	 * Gets the naming strategy used for tables.
	 */
	public INamingStrategy getNamingStrategy() {
		return namingStrategy;
	}
	
	/**
	 * Sets the naming strategy used for tables.
	 */
	public void setNamingStrategy(INamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}
	
	/**
	 * Finds the {@link Table} mapped to the specified entity.
	 * @param entity
	 */
	public Table findTable(String entity) {
		return (Table) tables.get(entity);
	}
	
	/**
	 * Gets the {@link Table} mapped to the specified entity.
	 * @param entity
	 * @exception MappingException if no table mapped to the entity is found
	 */
	public Table getTable(String entity) throws MappingException {
		if (!tables.containsKey(entity))
			throw new MappingException("Unknown mapping for " + entity);
		return (Table) tables.get(entity);
	}
	
	/**
	 * Gets the {@link Table} mapped to the specified class.
	 * @param clazz
	 * @exception MappingException if no table mapped to the class is found
	 */
	public Table getTable(Class clazz) throws MappingException {
		return getTable(clazz.getName());
	}
	
	/**
	 * Finds the {@link Table} mapped to the specified class.
	 * @param clazz
	 */
	public Table findTable(Class clazz) {
		Table table = findTable(clazz.getName());
		if (table == null) {
			table = new Table(clazz, namingStrategy);
			registerTable(clazz, table);
		}
		return table;
	}
	
	/**
	 * Mapping an entity to a {@link Table}. 
	 * @param entity
	 * @param table
	 */
	public void registerTable(String entity, Table table) {
		if (table.getEntityClass() == null)
			table.setEntityClass(Map.class);
		tables.put(entity, table);
	}
	
	/**
	 * Mapping a class to a {@link Table}. 
	 * @param clazz
	 * @param table
	 */
	public void registerTable(Class clazz, Table table) {
		if (table.getEntityClass() == null)
			table.setEntityClass(clazz);
		tables.put(clazz.getName(), table);
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getSchema() {
		return schema;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getCatalog() {
		return catalog;
	}
}
