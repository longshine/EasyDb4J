package lx.easydb.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

/**
 * Represents a relational table, with {@link Column}s and {@link Constraint}s.
 * @author Long
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
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
	
	public Table() {
		
	}
	
	public Table(Class clazz, INamingStrategy namingStrategy) {
		String typeName = null;
		Mapping.Table tableAnn = (Mapping.Table) clazz.getAnnotation(Mapping.Table.class);
		if (tableAnn != null)
			typeName = tableAnn.name();
		if (typeName == null)
			typeName = namingStrategy.getTableName(clazz.getName());
		
		setName(typeName);
		setEntityClass(clazz);
		
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			
			if (Modifier.isStatic(field.getModifiers())
					|| Modifier.isFinal(field.getModifiers()))
				continue;

			String fieldName = field.getName();
			Method getter = null, setter = null;
			if (!Modifier.isPublic(field.getModifiers())) {
				String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				
				try {
					getter = clazz.getMethod(getterName, new Class[] { });
				} catch (Exception e) {
					continue;
				}
				
				String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					setter = clazz.getMethod(setterName, new Class[] { field.getType() });
				} catch (Exception e) {
					continue;
				}
			}
			
			if (getAnnotation(Mapping.Ignore.class, field, getter, setter) != null)
				continue;
			
			Mapping.Column colAnn = getAnnotation(Mapping.Column.class, field, getter, setter);
			String colName = colAnn == null || colAnn.name() == null || colAnn.name().length() == 0 ?
					namingStrategy.getColumnName(fieldName) : colAnn.name();
			int colType = colAnn == null || colAnn.type() == Types.EMPTY ?
					Types.get(field.getType()) : colAnn.type();
			
			Column column = new Column(colName, fieldName, colType);
			column.setMemberInfo(new SimpleMemberMap(column.getName(), field, getter, setter));
			if (colAnn != null)
				column.setUpdatable(colAnn.updatable());
			addColumn(column);
			
			if (getAnnotation(Mapping.PrimaryKey.class, field, getter, setter) != null) {
				PrimaryKey pk = getPrimaryKey();
				if (pk == null) {
					pk = new PrimaryKey();
					setPrimaryKey(pk);
				}
				pk.addColumn(column);
			}
		}
		
		if (getPrimaryKey() == null) {
			Column idCol = findColumnByFieldName("id");
			if (idCol != null)
				setPrimaryKey(new PrimaryKey(idCol));
		}
	}
	
	private static <T extends Annotation> T getAnnotation(Class<T> annotationClass,
			Field field, Method getter, Method setter) {
		T a = field.getAnnotation(annotationClass);
		if (a == null && getter != null)
			a = getter.getAnnotation(annotationClass);
		if (a == null && setter != null)
			a = setter.getAnnotation(annotationClass);
		return a;
	}
	
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
		
		int oldLen = sbSql.length();
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
		
		if (sbSql.length() == oldLen)
			// nothing to update
			return null;
		
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
        return column == null ? null : column.getFieldName();
    }

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	public Class getEntityClass() {
		return entityClass;
	}
}
