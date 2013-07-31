package lx.easydb.criterion;

import lx.easydb.ICriteria;

public abstract class SimpleProjection implements IProjection {
	private String alias;
	
	public String getAlias() {
		return alias;
	}

	public boolean isGrouped() {
		return false;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String toGroupString(ICriteria criteria) {
		throw new UnsupportedOperationException("not a grouping projection");
	}

	public abstract String render(ICriteria criteria);
}
