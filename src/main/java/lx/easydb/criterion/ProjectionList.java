package lx.easydb.criterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lx.easydb.ICriteria;
import lx.easydb.StringHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ProjectionList implements IProjection {
	private List projections = new ArrayList();

	public String getAlias() {
		return null;
	}

	public boolean isGrouped() {
		for (int i = 0; i < getLength(); i++) {
			if (getProjection(i).isGrouped())
				return true;
		}
		return false;
	}

	public void setAlias(String alias) {
	}
	
	public int getLength() {
		return projections.size();
	}
	
	public IProjection getProjection(int i) {
		return (IProjection) projections.get(i);
	}
	
	public ProjectionList add(IProjection projection) {
		projections.add(projection);
		return this;
	}
	
	public ProjectionList add(IProjection projection, String alias) {
        projection.setAlias(alias);
        projections.add(projection);
        return this;
    }

	public String toGroupString(ICriteria criteria) {
		List groups = new ArrayList(projections.size());
		Iterator it = projections.iterator();
		while (it.hasNext()) {
			IProjection p = (IProjection) it.next();
			if (p.isGrouped())
				groups.add(p.toGroupString(criteria));
		}
		
		StringBuffer sb = StringHelper.createBuilder();
		boolean append = false;
		it = groups.iterator();
		while (it.hasNext()) {
			if (append)
				sb.append(", ");
			else
				append = true;
			sb.append(it.next());
		}
		return sb.toString();
	}

	public String render(ICriteria criteria) {
		StringBuffer sb = StringHelper.createBuilder();
		boolean append = false;
		Iterator it = projections.iterator();
		while (it.hasNext()) {
			if (append)
				sb.append(", ");
			else
				append = true;
			IProjection p = (IProjection) it.next();
			sb.append(p.render(criteria));
		}
		return sb.toString();
	}
}
