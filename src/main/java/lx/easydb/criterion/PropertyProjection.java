package lx.easydb.criterion;

import lx.easydb.ICriteria;

public class PropertyProjection extends SimpleProjection {
	private boolean grouped;
	private String propertyName;
	
	public PropertyProjection(String propertyName) {
		this(propertyName, false);
	}
	
	public PropertyProjection(String propertyName, boolean grouped) {
        this.propertyName = propertyName;
        this.grouped = grouped;
    }

	public String render(ICriteria criteria) {
		return ((ICriteriaRender) criteria).toSqlString(this);
	}
	
	public String toGroupString(ICriteria criteria) {
		if (grouped)
			return Clauses.field(propertyName).render(criteria);
		else
			return super.toGroupString(criteria);
	}
	
	public boolean isGrouped() {
		return grouped;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}
}
