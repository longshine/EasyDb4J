package lx.easydb.criterion;

/**
 * @author Longshine
 *
 */
public interface IQuery extends IFragment {
	void addSelect(ISelect select);
	void addFrom(IFrom from);
	void addCondition(IExpression condition);
	void addOrder(IOrder order);
	void addGroup(IExpression group);
}
