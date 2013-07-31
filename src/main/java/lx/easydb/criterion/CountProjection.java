package lx.easydb.criterion;

import java.util.ArrayList;
import java.util.List;

import lx.easydb.ICriteria;

public class CountProjection extends AggregateProjection {
	private boolean distinct;
	
	public CountProjection(IExpression expression) {
		super("count", expression);
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}
	
	public String toString() {
		if (distinct)
            return "distinct " + super.toString();
        else
            return super.toString();
	}
	
	public List buildFunctionParameterList(ICriteria criteria) {
		List list = new ArrayList();
		if (distinct)
			list.add("distinct");
		list.add(getExpression().render(criteria));
		return list;
	}
}
