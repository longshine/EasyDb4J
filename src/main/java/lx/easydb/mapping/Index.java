package lx.easydb.mapping;

import java.util.Iterator;
import java.util.List;

import lx.easydb.StringHelper;
import lx.easydb.dialect.Dialect;

public class Index extends RelationalModel implements IRelationalModel {
	private String name;
	private Table table;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Table getTable() {
		return table;
	}
	
	public String toString() {
        return getClass().getName() + "(" + name + ")";
    }
	
	@SuppressWarnings("rawtypes")
	protected static String buildSqlCreateIndexString(Dialect dialect, String name, Table table, List columns, boolean unique, String defaultCatalog, String defaultSchema) {
        String tableQualifiedName = table.getQualifiedName(dialect, defaultCatalog, defaultSchema);
        StringBuffer sb = StringHelper.createBuilder()
            .append("create")
            .append(unique ? " unique" : "")
            .append(" index ")
            .append(dialect.qualifyIndexName() ? qualify(tableQualifiedName, dialect.quote(name)) : dialect.quote(name))
            .append(" on ")
            .append(tableQualifiedName)
            .append(" (");
        
        boolean append = false;
		Iterator it = columns.iterator();
		while (it.hasNext()) {
			if (append)
				sb.append(", ");
			else
				append = true;
			Column column = (Column) it.next();
			sb.append(column.getQuotedName(dialect));
		}

        return sb.append(")").toString();
    }
	
	protected static String buildSqlDropIndexString(Dialect dialect, Table table, String name, String defaultCatalog, String defaultSchema) {
        return "drop index " + (dialect.qualifyIndexName() ? qualify(table.getQualifiedName(dialect, defaultCatalog, defaultSchema), dialect.quote(name)) : dialect.quote(name));
    }
	
	protected String doToSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema) {
        return buildSqlCreateIndexString(dialect, name, table, getColumns(), false, defaultCatalog, defaultSchema);
    }
	
	protected String doToSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema) {
        return buildSqlDropIndexString(dialect, table, name, defaultCatalog, defaultSchema);
    }
	
	private static String qualify(String prefix, String name) {
		return StringHelper.createBuilder().append(prefix)
			.append('.').append(name).toString();
	}
}
