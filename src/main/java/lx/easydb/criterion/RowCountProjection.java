package lx.easydb.criterion;

import java.util.List;

import lx.easydb.ICriteria;

public class RowCountProjection extends SimpleProjection {
	public static List ARGS = java.util.Collections.singletonList( "*" );
	
	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toString() {
		return "count(*)";
	}
}
