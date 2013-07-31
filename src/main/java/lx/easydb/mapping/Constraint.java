package lx.easydb.mapping;

import lx.easydb.StringHelper;
import lx.easydb.dialect.Dialect;

public abstract class Constraint extends RelationalModel implements IRelationalModel {
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
	
	public String ToString() {
        return getClass().getName() + "(" + table.getName() + getColumns() + ") as " + name;
    }
	
	protected String doToSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema) {
        String constraintString = doToSqlConstraint(dialect, name);
        return StringHelper.createBuilder()
            .append("alter table ")
            .append(table.getQualifiedName(dialect, defaultCatalog, defaultSchema))
            .append(constraintString)
            .toString();
    }
	
	protected String doToSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema) {
        return StringHelper.createBuilder()
            .append("alter table ")
            .append(table.getQualifiedName(dialect, defaultCatalog, defaultSchema))
            .append(" drop constraint ")
            .append(dialect.quote(name))
            .toString();
    }
	
	protected abstract String doToSqlConstraint(Dialect dialect, String constraintName);
}
