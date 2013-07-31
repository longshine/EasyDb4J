/**
 * Copyright (c) 2011 SmeshLink Technology Corporation.
 * All rights reserved.
 * 
 * This file is part of the SmeshServer, a gateway middleware for WSN.
 * Please see README for more information.
 */
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
