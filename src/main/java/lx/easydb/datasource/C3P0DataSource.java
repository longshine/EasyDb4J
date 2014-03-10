package lx.easydb.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * An {@link IDataSource} which use {@link ComboPooledDataSource}
 * of C3P0.
 * 
 * @author Longshine
 *
 */
public class C3P0DataSource extends DataSource {
	private ComboPooledDataSource dataSource;
	
	public C3P0DataSource(String driver, String url, String user, String password, Map options) throws Exception {
		super(driver, url, user, password);
		this.dataSource = new ComboPooledDataSource();
		this.dataSource.setDriverClass(driver);
		this.dataSource.setJdbcUrl(url);
		this.dataSource.setUser(user);
		this.dataSource.setPassword(password);
		//this.dataSource.setAcquireRetryAttempts(2);
		//this.dataSource.setAcquireRetryDelay(5000);
		
		setMinPoolSize(readInt(options, "minPoolSize", 1));
		setMaxPoolSize(readInt(options, "maxPoolSize", 15));
		setInitialPoolSize(readInt(options, "initialPoolSize", 1));
		// A connection can exist 1 day at most.
		setMaxConnectionAge(readInt(options, "maxConnectionAge", 86400));
		setAcquireIncrement(readInt(options, "acquireIncrement", 1));
	}
	
	public void setMinPoolSize(int minPoolSize) {
		this.dataSource.setMinPoolSize(minPoolSize);
	}
	
	public void setMaxPoolSize(int maxPoolSize) {
		this.dataSource.setMaxPoolSize(maxPoolSize);
	}
	
	public void setInitialPoolSize(int initialPoolSize) {
		this.dataSource.setInitialPoolSize(initialPoolSize);
	}
	
	public void setMaxConnectionAge(int maxConnectionAge) {
		this.dataSource.setMaxConnectionAge(maxConnectionAge);
	}
	
	public void setAcquireIncrement(int acquireIncrement) {
		this.dataSource.setAcquireIncrement(acquireIncrement);
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection() throws SQLException {
		return this.dataSource.getConnection();
	}
	
	private static int readInt(Map map, String key, int def) {
		int result = def;
		if (map != null && map.containsKey(key)) {
			try {
				result = Integer.parseInt((String) map.get(key));
			} catch (Exception e) {
				result = def;
			}
		}
		return result;
	}
}
