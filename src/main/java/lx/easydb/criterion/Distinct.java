package lx.easydb.criterion;

import lx.easydb.ICriteria;

public class Distinct implements IProjection {
	private final IProjection projection;
	
	public Distinct(IProjection projection) {
        this.projection = projection;
    }

	public String getAlias() {
		return projection.getAlias();
	}

	public boolean isGrouped() {
		return projection.isGrouped();
	}

	public void setAlias(String alias) {
		projection.setAlias(alias);
	}

	public String toGroupString(ICriteria criteria) {
		return projection.toGroupString(criteria);
	}

	public String render(ICriteria criteria) {
		return "distinct " + projection.render(criteria);
	}
	
	public String toString() {
		return "distinct " + projection.toString();
	}
}
