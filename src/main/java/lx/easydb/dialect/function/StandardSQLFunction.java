package lx.easydb.dialect.function;

import java.util.List;

import lx.easydb.IConnectionFactory;
import lx.easydb.Types;

public class StandardSQLFunction implements ISQLFunction {
	private final String name;
	private final int registeredType;

	public StandardSQLFunction(String name) {
		this(name, Types.EMPTY);
	}

	public StandardSQLFunction(String name, int registeredType) {
		this.name = name;
		this.registeredType = registeredType;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return registeredType;
	}

	public boolean hasArguments() {
		return true;
	}

	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	public int getReturnType(int firstArgumentType) {
		return registeredType == Types.EMPTY ? firstArgumentType : registeredType;
	}

	public String render(int firstArgumentType, List arguments, IConnectionFactory factory) {
		return doRender(firstArgumentType, arguments, factory);
	}

	public String toString() {
		return name;
	}
	
	protected String doRender(int firstArgumentType, List arguments, IConnectionFactory factory) {
		StringBuffer buf = new StringBuffer();
		buf.append(name).append('(');
		for (int i = 0; i < arguments.size(); i++) {
			buf.append(arguments.get(i));
			if (i < arguments.size() - 1) {
				buf.append(", ");
			}
		}
		return buf.append(')').toString();
	}
}
