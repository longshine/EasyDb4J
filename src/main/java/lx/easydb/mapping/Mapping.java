package lx.easydb.mapping;

import java.util.HashMap;
import java.util.Map;

import lx.easydb.MappingException;

public class Mapping {
	private Map tables = new HashMap();
	private String schema;
	private String catalog;
	
	public Table findTable(String entity) {
		return (Table) tables.get(entity);
	}
	
	public Table getTable(String entity) throws MappingException {
		if (!tables.containsKey(entity))
			throw new MappingException("Unknown mapping for " + entity);
		return (Table) tables.get(entity);
	}
	
	public Table getTable(Class clazz) throws MappingException {
		return getTable(clazz.getName());
	}
	
	public Table findTable(Class clazz) {
		return findTable(clazz.getName());
	}
	
	public void registerTable(String entity, Table table) {
		if (table.getEntityClass() == null)
			table.setEntityClass(Map.class);
		tables.put(entity, table);
	}
	
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
