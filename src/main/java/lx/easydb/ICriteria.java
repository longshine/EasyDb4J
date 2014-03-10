package lx.easydb;

import java.sql.SQLException;
import java.util.List;

import lx.easydb.criterion.IExpression;
import lx.easydb.criterion.IProjection;
import lx.easydb.criterion.Order;

/**
 * Represents a criteria query object.
 * 
 * @author Long
 *
 */
public interface ICriteria {
	/**
	 * Sets a value deciding how to deal with parameters in query,
	 * with parameterized query or direct SQL.
	 * The default value is <code>true</code>.
	 * @param parameterized
	 */
	void setParameterized(boolean parameterized);
	/**
	 * Gets a value indicating how to deal with parameters in query.
	 */
	boolean isParameterized();
	/**
	 * Adds a {@link IExpression} to constrain the results
	 * to be retrieved.
	 * @param condition the {@link IExpression} object representing
	 * the restriction to be applied
	 * @return itself
	 * @see {@link lx.easydb.criterion.Clauses Clauses}
	 */
	ICriteria add(IExpression condition);
	/**
	 * Adds an {@link Order ordering} to the result set.
	 * @param order the {@link Order order} object representing
	 * an ordering to be applied to the results
	 * @return itself
	 */
	ICriteria addOrder(Order order);
	/**
	 * Gets the results.
	 * @return the list of matched query results
	 * @throws SQLException
	 */
	List list() throws SQLException;
	/**
	 * Gets the results.
	 * @param total the total count of returned results
	 * @param offset the start index of query results
	 * @return the list of matched query results
	 * @throws SQLException
	 */
	List list(int total, int offset) throws SQLException;
	/**
	 * Counts the results.
	 * @return the count of matched query results
	 * @throws SQLException
	 */
	int count() throws SQLException;
	/**
	 * Gets a single result.
	 * @return the first item in matched query results, or null if none
	 * @throws SQLException
	 */
	Object single() throws SQLException;
	/**
	 * Used to specify that the query results will be a projection
	 * (scalar in nature).
	 * @param projection the {@link IProjection} representing the overall "shape"
	 * of the query results
	 * @return itself
	 * @see {@link lx.easydb.criterion.Projections Projections}
	 */
	ICriteria setProjection(IProjection projection);
}
