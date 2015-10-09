package lx.easydb.dialect.function;

import java.util.List;

import lx.easydb.IConnectionFactory;
import lx.easydb.QueryException;

public class NoArgSQLFunction implements ISQLFunction {
    private int returnType;
    private boolean hasParenthesesIfNoArguments;
    private String name;

    public NoArgSQLFunction(String name, int returnType) {
        this(name, returnType, true);
    }

    public NoArgSQLFunction(String name, int returnType, boolean hasParenthesesIfNoArguments) {
        this.returnType = returnType;
        this.hasParenthesesIfNoArguments = hasParenthesesIfNoArguments;
        this.name = name;
    }

	public boolean hasArguments() {
		return false;
	}

	public boolean hasParenthesesIfNoArguments() {
		return hasParenthesesIfNoArguments;
	}

    public int getReturnType(int argumentType) {
        return returnType;
    }

	public String render(int argumentType, @SuppressWarnings("rawtypes") List args, IConnectionFactory factory)
			throws QueryException {
		if (args.size() > 0) {
			throw new QueryException("function takes no arguments: " + name);
		}
		return hasParenthesesIfNoArguments ? name + "()" : name;
	}
}
