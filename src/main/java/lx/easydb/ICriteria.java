package lx.easydb;

import java.sql.SQLException;
import java.util.List;

import lx.easydb.criterion.IExpression;
import lx.easydb.criterion.IProjection;
import lx.easydb.criterion.Order;

public interface ICriteria {
	void setParameterized(boolean parameterized);
	ICriteria add(IExpression condition);
	ICriteria addOrder(Order order);
	List list() throws SQLException;
	List list(int total, int offset) throws SQLException;
	int count() throws SQLException;
	Object single() throws SQLException;
	ICriteria setProjection(IProjection projection);
}
