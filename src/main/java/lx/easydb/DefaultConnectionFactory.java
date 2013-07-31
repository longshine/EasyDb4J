/**
 * Copyright (c) 2011-2013 SmeshLink Technology Corporation.
 * All rights reserved.
 * 
 * This file is part of the SmeshServer, a gateway middleware for WSN.
 * Please see README for more information.
 */
package lx.easydb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import lx.easydb.datasource.C3P0DataSource;
import lx.easydb.datasource.IDataSource;
import lx.easydb.dialect.Dialect;
import lx.easydb.mapping.Mapping;

/**
 * @author smeshlink
 *
 */
public class DefaultConnectionFactory implements IConnectionFactory {
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
		
		binders.put(Map.class, new ValueBinder() {
			public void bind(PreparedStatement st, Object item, int index,
					String field, int sqlType) throws SQLException {
				Map map = (Map) item;
				if (sqlType == Types.EMPTY)
					st.setObject(index, map.get(field));
				else
					st.setObject(index, map.get(field), sqlType);
			}
		});
		
		extractors.put(Map.class, new ObjectExtractor() {
			public void extract(ResultSet rs, Object item, int index,
					String field) throws SQLException {
				Map map = (Map) item;
				map.put(field, rs.getObject(index));
			}
			protected Object newInstance() {
				return new HashMap();
			}
		});
		
		extractors.put(Integer.class, new PrimitiveExtractor() {
			public Object extract(ResultSet rs, int index) throws SQLException {
				return Integer.valueOf(rs.getInt(index));
			}
		});
		
		extractors.put(Long.class, new PrimitiveExtractor() {
			public Object extract(ResultSet rs, int index) throws SQLException {
				return Long.valueOf(rs.getLong(index));
			}
		});
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
		if (binder == null) {
			// TODO reflect binder
		}
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
		if (extractor == null) {
			// TODO reflect extractor
		}
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
