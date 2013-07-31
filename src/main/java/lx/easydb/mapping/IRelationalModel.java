package lx.easydb.mapping;

import lx.easydb.dialect.Dialect;

public interface IRelationalModel {
	String toSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema);
	String toSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema);
}
