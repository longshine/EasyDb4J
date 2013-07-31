package lx.easydb.mapping;

import java.util.Iterator;

import lx.easydb.StringHelper;
import lx.easydb.dialect.Dialect;

public class UniqueKey extends Constraint {
	
	public String ToSqlConstraintString(Dialect dialect) {
		StringBuffer sb = StringHelper.createBuilder().append("unique (");
        boolean hadNullableColumn = appendColumns(sb, dialect);
        //do not add unique constraint on DB not supporting unique and nullable columns
        return (!hadNullableColumn || dialect.supportsNullableUnique()) ?
            sb.append(")").toString() :
            null;
    }
	
	protected String doToSqlConstraint(Dialect dialect, String constraintName) {
		StringBuffer sb = StringHelper.createBuilder().append(dialect.getAddUniqueKeyConstraintString(constraintName))
            .append("(");
        boolean hadNullableColumn = appendColumns(sb, dialect);
        //do not add unique constraint on DB not supporting unique and nullable columns
        return (!hadNullableColumn || dialect.supportsNullableUnique()) ?
            sb.append(")").toString() :
            null;
    }
	
	protected String DoToSqlCreate(Dialect dialect, String defaultCatalog, String defaultSchema) {
        if (isGenerated(dialect)) {
            if (dialect.supportsUniqueConstraintInCreateAlterTable())
                return super.doToSqlCreate(dialect, defaultCatalog, defaultSchema);
            else
                return Index.buildSqlCreateIndexString(dialect, getName(), getTable(), getColumns(), true, defaultCatalog, defaultSchema);
        }
        else
            return null;
    }
	
	protected String doToSqlDrop(Dialect dialect, String defaultCatalog, String defaultSchema) {
        if (isGenerated(dialect)) {
            if (dialect.supportsUniqueConstraintInCreateAlterTable())
                return super.doToSqlDrop(dialect, defaultCatalog, defaultSchema);
            else
                return Index.buildSqlDropIndexString(dialect, getTable(), getName(), defaultCatalog, defaultSchema);
        }
        else
            return null;
    }
	
	private boolean isGenerated(Dialect dialect) {
		if (dialect.supportsNullableUnique())
            return true;

		Iterator it = getColumns().iterator();
		while (it.hasNext()) {
			Column column = (Column) it.next();
			if (column.isNullable())
                return false;
		}

        return true;
    }
	
	private boolean appendColumns(StringBuffer sb, Dialect dialect)
    {
		boolean hadNullableColumn = false;
		
		boolean append = false;
		Iterator it = getColumns().iterator();
		while (it.hasNext()) {
			if (append)
				sb.append(", ");
			else
				append = true;
			Column column = (Column) it.next();
			if (!hadNullableColumn && column.isNullable())
                hadNullableColumn = true;
            sb.append(column.getQuotedName(dialect));
		}

        return hadNullableColumn;
    }
}
