package lx.easydb.dialect.function;

import java.util.List;

import lx.easydb.IConnectionFactory;
import lx.easydb.QueryException;

/**
 * Provides support routines for the functions as used
 * in the various SQL Dialects.
 */
public interface ISQLFunction {
	boolean hasArguments();
	boolean hasParenthesesIfNoArguments();
	int getReturnType(int firstArgumentType);
	String render(int firstArgumentType, List arguments, IConnectionFactory factory) throws QueryException;
}
