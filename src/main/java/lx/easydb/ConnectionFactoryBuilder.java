package lx.easydb;

import java.util.Map;

import lx.easydb.dialect.Dialect;
import lx.easydb.dialect.HSQLDialect;
import lx.easydb.dialect.MySQLDialect;
import lx.easydb.dialect.SQLServer2005Dialect;
import lx.easydb.dialect.SQLServer2008Dialect;
import lx.easydb.dialect.SQLServer2012Dialect;
import lx.easydb.dialect.SQLiteDialect;

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

	/**
	 * Builds a {@link IConnectionFactory} for MySQL.
	 * @param server the MySQL server name
	 * @param database the database to connect
	 * @param user the name of user
	 * @param password the password of user
	 * @return
	 * @throws Exception if <code>com.mysql.jdbc.Driver</code> is not found
	 */
	public static IConnectionFactory buildMySQL(String server, String database,
			String user, String password) throws Exception {
		return newBuilder(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://" + server + "/" + database,
				user, password,
				new MySQLDialect(), null).build();
	}
	
	/**
	 * Builds a {@link IConnectionFactory} for SQLite.
	 * @param dbFile the file name of the database
	 * @param user the name of user
	 * @param password the password of user
	 * @return
	 * @throws Exception if <code>org.sqlite.JDBC</code> driver is not found
	 */
	public static IConnectionFactory buildSQLite(String dbFile,
			String user, String password) throws Exception {
		return newBuilder(
				"org.sqlite.JDBC",
				"jdbc:sqlite:" + dbFile,
				user, password,
				new SQLiteDialect(), null).build();
	}
	
	/**
	 * Builds a {@link IConnectionFactory} for HSQLDB in-file db.
	 * @param dbFile the file name of the database
	 * @param user the name of user
	 * @param password the password of user
	 * @return
	 * @throws Exception if <code>org.hsqldb.jdbcDriver</code> is not found
	 */
	public static IConnectionFactory buildHSQLDB(String dbFile,
			String user, String password) throws Exception {
		return newBuilder(
				"org.hsqldb.jdbcDriver",
				"jdbc:hsqldb:file:" + dbFile,
				user, password,
				new HSQLDialect(), null).build();
	}
	
	/**
	 * Builds a {@link IConnectionFactory} for SQL Server.
	 * @param server the server name of the SQL Server
	 * @param database the database to connect
	 * @param user the name of user
	 * @param password the password of user
	 * @param dialect the dialect to use
	 * @return
	 * @throws Exception if <code>com.microsoft.sqlserver.jdbc.SQLServerDriver<code> is not found
	 */
	public static IConnectionFactory buildSQLServer(String server, String database,
			String user, String password, Dialect dialect) throws Exception {
		return newBuilder(
				"com.microsoft.sqlserver.jdbc.SQLServerDriver",
				"jdbc:sqlserver://" + server + ";database=" + database,
				user, password,
				dialect, null).build();
	}
	
	/**
	 * Builds a {@link IConnectionFactory} for SQL Server 2005.
	 * @param server the server name of the SQL Server
	 * @param database the database to connect
	 * @param user the name of user
	 * @param password the password of user
	 * @return
	 * @throws Exception if <code>com.microsoft.sqlserver.jdbc.SQLServerDriver<code> is not found
	 */
	public static IConnectionFactory buildSQLServer2005(String server, String database,
			String user, String password) throws Exception {
		return buildSQLServer(server, database, user, password,
				new SQLServer2005Dialect());
	}
	
	/**
	 * Builds a {@link IConnectionFactory} for SQL Server 2008.
	 * @param server the server name of the SQL Server
	 * @param database the database to connect
	 * @param user the name of user
	 * @param password the password of user
	 * @return
	 * @throws Exception if <code>com.microsoft.sqlserver.jdbc.SQLServerDriver<code> is not found
	 */
	public static IConnectionFactory buildSQLServer2008(String server, String database,
			String user, String password) throws Exception {
		return buildSQLServer(server, database, user, password,
				new SQLServer2008Dialect());
	}
	
	/**
	 * Builds a {@link IConnectionFactory} for SQL Server 2012.
	 * @param server the server name of the SQL Server
	 * @param database the database to connect
	 * @param user the name of user
	 * @param password the password of user
	 * @return
	 * @throws Exception if <code>com.microsoft.sqlserver.jdbc.SQLServerDriver<code> is not found
	 */
	public static IConnectionFactory buildSQLServer2012(String server, String database,
			String user, String password) throws Exception {
		return buildSQLServer(server, database, user, password,
				new SQLServer2012Dialect());
	}
}
