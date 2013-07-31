package lx.easydb.dialect.function;

import java.util.List;

import lx.easydb.IConnectionFactory;
import lx.easydb.QueryException;

public class CastFunction implements ISQLFunction {
	
	public boolean hasArguments() {
		return true;
	}

	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	public int getReturnType(int columnType) {
		return columnType; // this is really just a guess, unless the caller properly identifies the 'type' argument here
	}

	public String render(int columnType, List args, IConnectionFactory factory) throws QueryException {
		if ( args.size()!=2 ) {
			throw new QueryException("cast() requires two arguments");
		}
		return "cast(" + args.get(0) + " as " + factory.getDialect().getCastTypeName((String) args.get(1)) + ')';
	}
}
