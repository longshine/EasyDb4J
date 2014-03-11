package lx.easydb.mapping;

import java.util.ArrayList;
import java.util.List;

import lx.easydb.dialect.Dialect;

/**
 * Base class of {@link IRelationalModel}s.
 * 
 * @author Long
 *
 */
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
	
	/**
	 * @see #toSqlCreate(Dialect, String, String)
	 */
	protected abstract String doToSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema);
	/**
	 * @see #toSqlDrop(Dialect, String, String)
	 */
	protected abstract String doToSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema);
}
