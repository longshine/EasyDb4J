package lx.easydb.criterion;

import lx.easydb.ICriteria;

/**
 * Represents a fragment of an SQL statement.
 * 
 * @author Longshine
 *
 */
public interface IFragment {
	/**
	 * Renders the SQL fragment.
	 * @param criteria the {@link ICriteria} which this belongs to
	 * @return the generated SQL fragment
	 */
	String render(ICriteria criteria);
}
