package lx.easydb.dialect.function;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lx.easydb.IConnectionFactory;
import lx.easydb.Types;

public class StandardAnsiSqlAggregationFunctions {
	public static final StandardSQLFunction Count = new CountFunction();
	public static final StandardSQLFunction Avg = new AvgFunction();
	public static final StandardSQLFunction Max = new MaxFunction();
	public static final StandardSQLFunction Min = new MinFunction();
	public static final StandardSQLFunction Sum = new SumFunction();

	public static void registerFunctions(Map functionMap) {
		functionMap.put(Avg.getName(), Avg);
		functionMap.put(Count.getName(), Count);
		functionMap.put(Max.getName(), Max);
		functionMap.put(Min.getName(), Min);
		functionMap.put(Sum.getName(), Sum);
	}

	/**
	 * Definition of a standard ANSI SQL compliant <tt>COUNT</tt> function
	 */
	protected static class CountFunction extends StandardSQLFunction {
		
		public CountFunction() {
			super("count", Types.BIGINT);
		}

		protected String doRender(int firstArgumentType, List arguments, IConnectionFactory factory) {
			if (arguments.size() > 1) {
				if ("distinct".equalsIgnoreCase(arguments.get(0).toString())) {
					return renderCountDistinct(arguments);
				}
			}
			return super.doRender(firstArgumentType, arguments, factory);
		}

		private String renderCountDistinct(List arguments) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("count(distinct ");
			String sep = "";
			Iterator itr = arguments.iterator();
			itr.next(); // intentionally skip first
			while (itr.hasNext()) {
				buffer.append(sep).append(itr.next());
				sep = ", ";
			}
			return buffer.append(")").toString();
		}
	}

	/**
	 * Definition of a standard ANSI SQL compliant <tt>AVG</tt> function
	 */
	protected static class AvgFunction extends StandardSQLFunction {

		public AvgFunction() {
			super("avg", Types.DOUBLE);
		}
	}

	protected static class MaxFunction extends StandardSQLFunction {

		public MaxFunction() {
			super("max");
		}
	}

	protected static class MinFunction extends StandardSQLFunction {

		public MinFunction() {
			super("min");
		}
	}

	protected static class SumFunction extends StandardSQLFunction {

		public SumFunction() {
			super("sum");
		}
	}
}
