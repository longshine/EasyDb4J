package lx.easydb;

import java.util.Map;

import lx.easydb.dialect.Dialect;

/**
 * Builder that builds connection factories.
 * 
 * @author Long
 *
 */
public class ConnectionFactoryBuilder {
	private String driver;
	private String url;
	private String user;
	private String password;
	private Dialect dialect;
	private Map options;

	protected ConnectionFactoryBuilder() {
	}
	
	/**
	 * Builds a factory with given properties.
	 * @throws Exception if the JDBC driver is not found
	 */
	public IConnectionFactory build() throws Exception {
		return new DefaultConnectionFactory(driver,
				url, user, password, dialect, options);
	}
	
	/**
	 * Sets the type of JDBC driver.
	 * @return itself
	 */
	public ConnectionFactoryBuilder setDriver(String driver) {
		this.driver = driver;
		return this;
	}
	
	/**
	 * Sets the URL of database server.
	 * @return itself
	 */
	public ConnectionFactoryBuilder setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * Sets the name of user.
	 * @return itself
	 */
	public ConnectionFactoryBuilder setUser(String user) {
		this.user = user;
		return this;
	}
	
	/**
	 * Sets the password of user.
	 * @return itself
	 */
	public ConnectionFactoryBuilder setPassword(String password) {
		this.password = password;
		return this;
	}
	
	/**
	 * Sets the {@link Dialect} to use.
	 * @return itself
	 */
	public ConnectionFactoryBuilder setDialect(Dialect dialect) {
		this.dialect = dialect;
		return this;
	}
	
	/**
	 * Sets the map of other options.
	 * @return itself
	 */
	public ConnectionFactoryBuilder setOptions(Map options) {
		this.options = options;
		return this;
	}
	
	/**
	 * Creates a empty builder.
	 */
	public static ConnectionFactoryBuilder newBuilder() {
		return new ConnectionFactoryBuilder();
	}

	/**
	 * Creates a builder with a map of options.
	 * @param map the map of options
	 * @throws Exception if fail to initialize the dialect given in the map
	 */
	public static ConnectionFactoryBuilder newBuilder(Map map) throws Exception {
		ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
		builder.driver = (String) map.get("driver");
		builder.url = (String) map.get("url");
		builder.user = (String) map.get("user");
		builder.password = (String) map.get("password");
		builder.options = map;
		String dialect = (String) map.get("dialect");
		if (dialect != null && dialect.length() > 0) {
			builder.dialect = (Dialect) Class.forName(dialect).newInstance();
		}
		return builder;
	}
	
	/**
	 * Creates a builder.
	 * @param driver the type of JDBC driver
	 * @param url the URL of database server
	 * @param user the name of user
	 * @param password the password of user
	 * @param dialect the type of {@link Dialect}
	 * @param options the map of other options
	 * @throws Exception if fail to initialize the dialect
	 */
	public static ConnectionFactoryBuilder newBuilder(String driver, String url,
			String user, String password, String dialect, Map options) throws Exception {
		ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
		builder.driver = driver;
		builder.url = url;
		builder.user = user;
		builder.password = password;
		builder.options = options;
		if (dialect != null && dialect.length() > 0) {
			builder.dialect = (Dialect) Class.forName(dialect).newInstance();
		}
		return builder;
	}
	
	/**
	 * Creates a builder.
	 * @param driver the JDBC driver
	 * @param url the URL of database server
	 * @param user the name of user
	 * @param password the password of user
	 * @param dialect the {@link Dialect} to use
	 * @param options the map of other options
	 */
	public static ConnectionFactoryBuilder newBuilder(String driver, String url,
			String user, String password, Dialect dialect, Map options) {
		ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
		builder.driver = driver;
		builder.url = url;
		builder.user = user;
		builder.password = password;
		builder.options = options;
		builder.dialect = dialect;
		return builder;
	}
}
