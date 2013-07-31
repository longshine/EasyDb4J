package lx.easydb.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lx.easydb.StringHelper;
import lx.easydb.Types;
import lx.easydb.dialect.Dialect;

public class Table implements IRelationalModel {
	private String name;
	private String schema;
	private String catalog;
	private String comment;
	private PrimaryKey primaryKey;
	private Column idColumn;
	private Map fieldColumnMap = new LinkedHashMap();
	private Map columnFieldMap = new LinkedHashMap();
	//private Map indices = new HashMap();
	private Map uniqueKeys = new HashMap();
	private List checkConstraints = new ArrayList();
	private Class entityClass;
	
	public void setName(String name) {
		this.name = Dialect.unquote(name);
	}
	
	public String getName() {
		return name;
	}

	public void setSchema(String schema) {
		this.schema = Dialect.unquote(schema);
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

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	
	public boolean hasPrimaryKey() {
		return primaryKey != null;
	}
	
	public Column getIdColumn() {
		return idColumn;
	}
	
	public Collection getColumns() {
		return columnFieldMap.values();
	}
	
	public void addColumn(Column column) {
		fieldColumnMap.put(column.getFieldName().toUpperCase(), column);
		columnFieldMap.put(column.getName().toUpperCase(), column);
		if (Types.IDENTITY == column.getDbType())
			idColumn = column;
	}
	
	public Column findColumnByFieldName(String fieldName) {
		fieldName = fieldName.toUpperCase();
        return (Column) fieldColumnMap.get(fieldName);
	}
	
	public Column findColumnByColumnName(String columnName) {
		columnName = columnName.toUpperCase();
        return (Column) columnFieldMap.get(columnName);
	}
	
	public String getQuotedName(Dialect dialect) {
        return dialect.openQuote() + name + dialect.closeQuote();
    }
	
	public String getQuotedSchema(Dialect dialect) {
        return dialect.openQuote() + schema + dialect.closeQuote();
    }
	
	public String getQualifiedName(Dialect dialect, String defaultCatalog, String defaultSchema) {
        String quotedName = getQuotedName(dialect);
        String usedSchema = schema == null ? defaultSchema : getQuotedSchema(dialect);
        String usedCatalog = catalog == null ? defaultCatalog : catalog;
        return qualify(usedCatalog, usedSchema, quotedName);
    }
	
	public static String qualify(String catalog, String schema, String table) {
        StringBuffer sb = StringHelper.createBuilder();
        if (catalog != null && catalog.length() > 0)
            sb.append(catalog).append('.');
        if (schema != null && schema.length() > 0)
            sb.append(schema).append('.');
        return sb.append(table).toString();
    }

	public String toSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema) {
		StringBuffer sb = StringHelper.createBuilder()
        	.append(hasPrimaryKey() ? dialect.getCreateTableString() : dialect.getCreateMultisetTableString())
	        .append(" ")
	        .append(getQualifiedName(dialect, defaultCatalog, defaultSchema))
	        .append(" (");
		boolean hasIdentity = false;
		
		boolean append = false;
		Iterator it = getColumns().iterator();
		while (it.hasNext()) {
			if (append)
				sb.append(", ");
			else
				append = true;
			
			Column column = (Column) it.next();
			
			// column info
            sb.append(column.getQuotedName(dialect))
                .append(" ");

            if (column.getDbType() == Types.IDENTITY) {
                hasIdentity = true;
                if (dialect.hasDataTypeInIdentityColumn())
                    sb.append(column.getSqlType(dialect));
                sb.append(" ").append(dialect.getIdentityColumnString());
            } else {
                sb.append(column.getSqlType(dialect));

                if (column.getDefaultValue() != null)
                    sb.append(" default ").append(column.getDefaultValue());

                if (column.isNullable())
                    sb.append(dialect.getNullColumnString());
                else
                    sb.append(" not null");
            }

            // unique constraint
            if (column.isUnique() &&
                (!column.isNullable() || dialect.supportsNullableUnique())) {
                if (dialect.supportsUnique())
                    sb.append(" unique");
                else {
                    UniqueKey uk = getOrCreateUniqueKey(column.getQuotedName(dialect) + "_");
                    uk.addColumn(column);
                }
            }

            // check constraint
            if (column.getCheckConstraint() != null && dialect.supportsColumnCheck())
                sb.append(" check (")
                    .append(column.getCheckConstraint())
                    .append(")");

            // comment
            if (column.getComment() != null)
                sb.append(dialect.getColumnComment(column.getComment()));
		}
		
		if (hasPrimaryKey() && !(hasIdentity && dialect.hasPrimaryKeyInIdentityColumn()))
            sb.append(", ")
                .append(getPrimaryKey().toSqlConstraintString(dialect));

        if (dialect.supportsUniqueConstraintInCreateAlterTable()) {
        	Iterator itUk = uniqueKeys.values().iterator();
        	while (itUk.hasNext()) {
        		UniqueKey uk = (UniqueKey) itUk.next();
                String constraint = uk.ToSqlConstraintString(dialect);
                if (constraint != null && constraint.length() > 0)
                    sb.append(", ").append(constraint);
        	}
        }

        // table check
        if (dialect.supportsTableCheck()) {
        	Iterator itCheck = checkConstraints.iterator();
        	while (itCheck.hasNext()) {
        		sb.append(", check (").append(itCheck.next()).append(")");
        	}
        }

        sb.append(")");

        if (getComment() != null) {
            sb.append(dialect.getTableComment(getComment()));
        }

        return sb.toString();
	}

	public String toSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema) {
		StringBuffer sb = StringHelper.createBuilder().append("drop table ");
        if (dialect.supportsIfExistsBeforeTableName())
            sb.append("if exists ");
        sb.append(getQualifiedName(dialect, defaultCatalog, defaultSchema))
            .append(dialect.getCascadeConstraintsString());
        if (dialect.supportsIfExistsAfterTableName())
            sb.append(" if exists");
        return sb.toString();
	}
	
	public String toSqlInsert(Dialect dialect, String defaultCatalog,
			String defaultSchema, List paramList) {
		StringBuffer sbSql = StringHelper.createBuilder();
		StringBuffer sbParameters = StringHelper.createBuilder();

        sbSql.append("insert into ")
        	.append(getQualifiedName(dialect, defaultCatalog, defaultSchema))
        	.append(" (");

        boolean append = false;
		Iterator it = getColumns().iterator();
		while (it.hasNext()) {
			if (append) {
				sbSql.append(", ");
				sbParameters.append(", ");
			}
			Column column = (Column) it.next();
			if (column.getDbType() == Types.IDENTITY)
				append = false;
			else {
	            sbSql.append(column.getQuotedName(dialect));
	            sbParameters.append(dialect.paramPrefix());//.append(column.getFieldName());
	            paramList.add(column);
	            append = true;
			}
		}

        sbSql.append(") values (");
        sbSql.append(sbParameters);
        sbSql.append(")");

        return sbSql.toString();
    }
	
	public String toSqlSelect(Dialect dialect, String defaultCatalog,
			String defaultSchema, boolean useKeys, List paramList) {
		StringBuffer sbSql = StringHelper.createBuilder().append("select ");
		
		boolean append = false;
		Iterator it = getColumns().iterator();
		while (it.hasNext()) {
			if (append)
				sbSql.append(", ");
			else
				append = true;
			Column column = (Column) it.next();
			sbSql.append(column.getQuotedName(dialect));
		}
	
	    sbSql.append(" from ").append(getQualifiedName(dialect, defaultCatalog, defaultSchema));
	
	    if (useKeys && hasPrimaryKey()) {
	        sbSql.append(" where ");
	        append = false;
	        it = getPrimaryKey().getColumns().iterator();
	        while (it.hasNext()) {
				if (append)
					sbSql.append(" and ");
				else
					append = true;
				Column column = (Column) it.next();
				sbSql.append(column.getQuotedName(dialect)).append(" = ")
                	.append(dialect.paramPrefix());//.append(column.FieldName);
				paramList.add(column);
			}
	    }
	
	    return sbSql.toString();
	}
	
	public String toSqlUpdate(Dialect dialect, String defaultCatalog,
			String defaultSchema, List paramList) {
		StringBuffer sbSql = StringHelper.createBuilder()
            .append("update ").append(getQualifiedName(dialect, defaultCatalog, defaultSchema))
            .append(" set ");
		
		boolean append = false;
		Iterator it = getColumns().iterator();
		while (it.hasNext()) {
			if (append)
				sbSql.append(", ");
			Column column = (Column) it.next();
			if (hasPrimaryKey() && getPrimaryKey().containsColumn(column)) {
				append = false;
			} else if (!column.isUpdatable()) {
				append = false;
			} else {
				sbSql.append(column.getQuotedName(dialect))
					.append(" = ").append(dialect.paramPrefix());
					//.append(column.getFieldName());
				paramList.add(column);
				append = true;
			}
		}
		
        if (hasPrimaryKey()) {
            sbSql.append(" where ");
            append = false;
            it = getPrimaryKey().getColumns().iterator();
            while (it.hasNext()) {
    			if (append)
    				sbSql.append(" and ");
    			else
    				append = true;
    			Column column = (Column) it.next();
    			sbSql.append(column.getQuotedName(dialect))
	                .append(" = ").append(dialect.paramPrefix());
	                //.append(column.getFieldName());
				paramList.add(column);
            }
        }
        return sbSql.toString();
    }
	
	public String toSqlDelete(Dialect dialect, String defaultCatalog,
			String defaultSchema, List paramList) {
        StringBuffer sbSql = StringHelper.createBuilder()
            .append("delete from ").append(getQualifiedName(dialect, defaultCatalog, defaultSchema));
        if (hasPrimaryKey()) {
            sbSql.append(" where ");
            
            boolean append = false;
    		Iterator it = getPrimaryKey().getColumns().iterator();
    		while (it.hasNext()) {
    			if (append)
    				sbSql.append(" and ");
    			else
    				append = true;
    			Column column = (Column) it.next();
    			sbSql.append(column.getQuotedName(dialect))
	                .append(" = ").append(dialect.paramPrefix());
	                //.append(column.getFieldName());
				paramList.add(column);
    		}
        }
        return sbSql.toString();
    }
	
	public UniqueKey getOrCreateUniqueKey(String keyName) {
        UniqueKey uk = (UniqueKey) uniqueKeys.get(keyName);
        if (uk == null) {
            uk = new UniqueKey();
            uk.setName(keyName);
            uk.setTable(this);
            uniqueKeys.put(keyName, uk);
        }
        return uk;
    }
	
	public String getFieldName(String columnName) {
        Column column = findColumnByColumnName(columnName);
        return column == null ? columnName : column.getFieldName();
    }

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	public Class getEntityClass() {
		return entityClass;
	}
}
