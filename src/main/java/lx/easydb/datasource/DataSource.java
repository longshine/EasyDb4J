package lx.easydb.datasource;

/**
 * Abstract implementation of {@link IDataSource}.
 * 
 * @author Longshine
 *
 */
public abstract class DataSource implements IDataSource {
	protected String driver;
	protected String url;
	protected String user;
	protected String password;

	public DataSource(String driver, String url, String user, String password)
			throws ClassNotFoundException {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		Class.forName(driver);
	}
}
