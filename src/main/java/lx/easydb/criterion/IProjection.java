package lx.easydb.criterion;

import lx.easydb.ICriteria;

public interface IProjection extends IFragment {
	String getAlias();
	void setAlias(String alias);
	boolean isGrouped();
	String toGroupString(ICriteria criteria);
}
