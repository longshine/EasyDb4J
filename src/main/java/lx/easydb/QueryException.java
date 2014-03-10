package lx.easydb;

/**
 * Exception that occurs in query.
 * 
 * @author Long
 *
 */
public class QueryException extends RuntimeException {
	private static final long serialVersionUID = 3147139684496027115L;

	private String queryString;

	public QueryException(String message) {
		super(message);
	}

	public QueryException(String message, Throwable e) {
		super(message, e);
	}

	public QueryException(String message, String queryString) {
		super(message);
		this.queryString = queryString;
	}

	public QueryException(Exception e) {
		super(e);
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getMessage() {
		String msg = super.getMessage();
		if (queryString != null)
			msg += " [" + queryString + ']';
		return msg;
	}
}
