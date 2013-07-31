package lx.easydb;

import java.util.Map;

import lx.easydb.dialect.Dialect;

public class ConnectionFactoryBuilder {
	private String driver;
	private String url;
	private String user;
	private String password;
	private Dialect dialect;
	private Map options;

	protected ConnectionFactoryBuilder() {
	}
	
	public IConnectionFactory build() throws Exception {
		return new DefaultConnectionFactory(driver,
				url, user, password, dialect, options);
	}

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
}
