package lx.easydb;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import lx.easydb.datasource.C3P0DataSource;
import lx.easydb.datasource.IDataSource;
import lx.easydb.dialect.Dialect;
import lx.easydb.mapping.Mapping;

/**
 * Default implementation of {@link IConnectionFactory}.
 * 
 * @author smeshlink
 *
 */
public class DefaultConnectionFactory implements IConnectionFactory {
	private static final ReflectiveBinder reflectiveBinder = new ReflectiveBinder();
	private static final ReflectiveExtractor reflectiveExtractor = new ReflectiveExtractor();
	private String driver;
	private String url;
	private String user;
	private String password;
	private Dialect dialect;
	private IDataSource dataSource;
	private Mapping mapping = new Mapping();
	private HashMap binders = new HashMap();
	private HashMap extractors = new HashMap();
	
	public DefaultConnectionFactory(IDataSource dataSource, Dialect dialect) {
		this.dataSource = dataSource;
		this.dialect = dialect;
		
		binders.put(Map.class, new MapBinder());
		
		extractors.put(Map.class, new MapExtractor());
		extractors.put(Integer.class, PrimitiveExtractor.INTEGER);
		extractors.put(Long.class, PrimitiveExtractor.LONG);
	}
	
	public DefaultConnectionFactory(String driver, String url, String user,
			String password, Dialect dialect, Map options) throws Exception {
		this(new C3P0DataSource(driver, url, user, password, options), dialect);
	}
	
	public IConnection openConnection() throws SQLException {
		return new ConnectionWrapper(dataSource.getConnection(), this);
	}
	
	public void registerBinder(Class clazz, ValueBinder binder) {
		binders.put(clazz, binder);
	}
	
	public ValueBinder getBinder(Class clazz) {
		ValueBinder binder;
		if (Map.class.isAssignableFrom(clazz))
			binder = (ValueBinder) binders.get(Map.class);
		else
			binder = (ValueBinder) binders.get(clazz);
		if (binder == null)
			binder = reflectiveBinder;
		return binder;
	}
	
	public void registerExtractor(Class clazz, ValueExtractor extractor) {
		extractors.put(clazz, extractor);
	}
	
	public ValueExtractor getExtractor(Class clazz) {
		ValueExtractor extractor;
		if (Map.class.isAssignableFrom(clazz))
			extractor = (ValueExtractor) extractors.get(Map.class);
		else
			extractor = (ValueExtractor) extractors.get(clazz);
		if (extractor == null)
			extractor = reflectiveExtractor;
		return extractor;
	}

	public Dialect getDialect() {
		return dialect;
	}
	
	public Mapping getMapping() {
		return mapping;
	}

	public String getDriver() {
		return driver;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}
}
