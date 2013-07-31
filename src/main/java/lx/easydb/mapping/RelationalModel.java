package lx.easydb.mapping;

import java.util.ArrayList;
import java.util.List;

import lx.easydb.dialect.Dialect;

public abstract class RelationalModel implements IRelationalModel {
	private List columns = new ArrayList();
	
	public List getColumns() {
		return columns;
	}
	
	public void addColumn(Column column) {
		if (!columns.contains(column))
			columns.add(column);
	}
	
	public boolean containsColumn(Column column) {
        return columns.contains(column);
    }
	
	public String toSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema) {
		return doToSqlCreate(dialect, defaultCatalog, defaultSchema);
	}
	
	public String toSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema) {
		return doToSqlDrop(dialect, defaultCatalog, defaultSchema);
	}
	
	protected abstract String doToSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema);
	protected abstract String doToSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema);
}
