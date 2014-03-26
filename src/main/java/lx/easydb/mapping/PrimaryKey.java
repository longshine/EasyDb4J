package lx.easydb.mapping;

import java.util.Iterator;

import lx.easydb.StringHelper;
import lx.easydb.dialect.Dialect;

public class PrimaryKey extends Constraint {
	
	public PrimaryKey() {
		
	}
	
	public PrimaryKey(Column column) {
		addColumn(column);
	}
	
	public PrimaryKey(Column[] columns) {
		for (int i = 0; i < columns.length; i++) {
			addColumn(columns[i]);
		}
	}

	public String toSqlConstraintString(Dialect dialect) {
        StringBuffer sb = StringHelper.createBuilder().append("primary key (");
        return appendColumns(sb, dialect).append(")").toString();
    }
	
	protected String doToSqlConstraint(Dialect dialect, String constraintName) {
		StringBuffer sb = StringHelper.createBuilder()
            .append(dialect.getAddPrimaryKeyConstraintString(constraintName))
            .append("(");
        return appendColumns(sb, dialect).append(")").toString();
    }
	
	private StringBuffer appendColumns(StringBuffer sb, Dialect dialect) {
		boolean append = false;
		Iterator it = getColumns().iterator();
		while (it.hasNext()) {
			if (append)
				sb.append(", ");
			else
				append = true;
			Column column = (Column) it.next();
			sb.append(column.getQuotedName(dialect));
		}
        return sb;
    }
}
