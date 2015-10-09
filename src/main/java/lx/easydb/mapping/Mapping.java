package lx.easydb.mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import lx.easydb.MappingException;
import lx.easydb.Types;

/**
 * Stores O-R mappings.
 * 
 * @author Long
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
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
	 * Finds the {@link lx.easydb.mapping.Table} mapped to the specified entity.
	 * @param entity
	 */
	public lx.easydb.mapping.Table findTable(String entity) {
		return (lx.easydb.mapping.Table) tables.get(entity);
	}
	
	/**
	 * Gets the {@link lx.easydb.mapping.Table} mapped to the specified entity.
	 * @param entity
	 * @exception MappingException if no table mapped to the entity is found
	 */
	public lx.easydb.mapping.Table getTable(String entity) throws MappingException {
		if (!tables.containsKey(entity))
			throw new MappingException("Unknown mapping for " + entity);
		return (lx.easydb.mapping.Table) tables.get(entity);
	}
	
	/**
	 * Gets the {@link lx.easydb.mapping.Table} mapped to the specified class.
	 * @param clazz
	 * @exception MappingException if no table mapped to the class is found
	 */
	public lx.easydb.mapping.Table getTable(Class clazz) throws MappingException {
		return getTable(clazz.getName());
	}
	
	/**
	 * Finds the {@link lx.easydb.mapping.Table} mapped to the specified class.
	 * @param clazz
	 */
	public lx.easydb.mapping.Table findTable(Class clazz) {
		lx.easydb.mapping.Table table = findTable(clazz.getName());
		if (table == null) {
			table = new lx.easydb.mapping.Table(clazz, namingStrategy);
			registerTable(clazz, table);
		}
		return table;
	}
	
	/**
	 * Mapping an entity to a {@link lx.easydb.mapping.Table}. 
	 * @param entity
	 * @param table
	 */
	public void registerTable(String entity, lx.easydb.mapping.Table table) {
		if (table.getEntityClass() == null)
			table.setEntityClass(Map.class);
		tables.put(entity, table);
	}
	
	/**
	 * Mapping a class to a {@link lx.easydb.mapping.Table}. 
	 * @param clazz
	 * @param table
	 */
	public void registerTable(Class clazz, lx.easydb.mapping.Table table) {
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
	
	/**
	 * Indicates the properties of a table.
	 * @author Long
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Table {
		/**
		 * Returns the name of this table.
		 * @return the name of this table
		 */
		String name();
	}

	/**
	 * Indicates the details of the column mapped with this field.
	 * @author Long
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.METHOD })
	public static @interface Column {
		/**
		 * Returns the name of this column.
		 * @return the name of this column
		 */
		String name() default "";
		/**
		 * Returns the type of this column.
		 * @return the type of this column
		 */
		int type() default Types.EMPTY;
		/**
		 * Indicates whether this column should be included when updating entities.
		 * @return true if this column should be included when updating entities, otherwise false.
		 */
		boolean updatable() default true;
	}

	/**
	 * Indicates that this field is the primary key or one of primary keys. 
	 * @author Long
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.METHOD })
	public static @interface PrimaryKey {
		
	}

	/**
	 * Indicates that this field should be ignored and not be persisted.
	 * @author Long
	 */
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.METHOD })
	public static @interface Ignore {
		
	}
}
