package lx.easydb;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import lx.easydb.criterion.Criteria;
import lx.easydb.mapping.Column;
import lx.easydb.mapping.Table;

public interface IConnection extends Connection {
	Connection getConnection();
	int executeUpdate(String sql) throws SQLException;
	int executeUpdate(String sql, Object[] params) throws SQLException;
	int executeUpdate(String sql, Object[] params, int[] sqlTypes) throws SQLException;

	boolean existTable(String entity);
	void createTable(String entity) throws SQLException;
	void dropTable(String entity) throws SQLException;
	long insert(String entity, Object item) throws SQLException;
	boolean update(String entity, Object item) throws SQLException;
	boolean delete(String entity, Object item) throws SQLException;
	Object find(String entity, Object id) throws SQLException, QueryException;
	
	boolean existTable(Class clazz);
	void createTable(Class clazz) throws SQLException;
	void dropTable(Class clazz) throws SQLException;
	long insert(Class clazz, Object item) throws SQLException;
	boolean update(Class clazz, Object item) throws SQLException;
	boolean delete(Class clazz, Object item) throws SQLException;
	Object find(Class clazz, Object id) throws SQLException, QueryException;
	
	List query(String entity, String sql, Collection paramNames, Collection paramTypes, Object item) throws SQLException;
	List query(Class clazz, String sql, Collection paramNames, Collection paramTypes, Object item) throws SQLException;

	ICriteria createCriteria(String entity);
	ICriteria createCriteria(Class clazz);
}

class ConnectionWrapper implements IConnection {
	private Connection connection;
	private IConnectionFactory factory;
	
	public ConnectionWrapper(Connection connection, IConnectionFactory factory) {
		this.connection = connection;
		this.factory = factory;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public int executeUpdate(String sql) throws SQLException {
		return executeUpdate(sql, (Object[])null, null);
	}
	
	public int executeUpdate(String sql, Object[] params) throws SQLException {
		return executeUpdate(sql, params, null);
	}
	
	public int executeUpdate(String sql, Object[] params, int[] sqlTypes) throws SQLException {
		PreparedStatement st = null;
		
		try {
			st = connection.prepareStatement(sql);
			
			if (params != null) {
				if (sqlTypes != null && sqlTypes.length == params.length) {
					for (int i = 0; i < params.length; i++) {
						st.setObject(i + 1, params[i], sqlTypes[i]);
					}
				} else {
					for (int i = 0; i < params.length; i++) {
						st.setObject(i + 1, params[i]);
					}
				}
			}
			
			return st.executeUpdate();
		} finally {
			if (st != null)
				st.close();
		}
	}
	
	public int executeUpdate(String sql, Collection paramColumns, Object item) throws SQLException {
		PreparedStatement st = null;
		try {
			st = connection.prepareStatement(sql);
			
			if (paramColumns != null) {
				ValueBinder binder = factory.getBinder(item.getClass());
				int index = 1;
				Iterator it = paramColumns.iterator();
				while (it.hasNext()) {
					Column column = (Column) it.next();
					binder.bind(st, item, index++, column.getFieldName(), column.getDbType());
				}
			}
			
			return st.executeUpdate();
		} finally {
			if (st != null)
				st.close();
		}
	}
	
	public List query(String entity, String sql, Collection paramColumns, Object item) throws SQLException {
		Table table = factory.getMapping().findTable(entity);
		return queryInternal(table.getEntityClass(), table, sql, paramColumns, item);
	}

	public List query(String entity, String sql, Collection paramNames, Collection paramTypes, Object item) throws SQLException {
		Table table = factory.getMapping().findTable(entity);
		return queryInternal(table.getEntityClass(), table, sql, paramNames, paramTypes, item);
	}
	
	public List query(Class clazz, String sql, Collection paramNames, Collection paramTypes, Object item) throws SQLException {
		Table table = factory.getMapping().findTable(clazz.getName());
		return queryInternal(clazz, table, sql, paramNames, paramTypes, item);
	}
	
	public ICriteria createCriteria(String entity) {
		return new Criteria(entity, this, factory);
	}
	
	public ICriteria createCriteria(Class clazz) {
		return new Criteria(clazz.getName(), this, factory);
	}
	
	private List queryInternal(Class clazz, Table table, PreparedStatement st) throws SQLException {
		ValueExtractor extractor = factory.getExtractor(clazz);
		ResultSet rs = st.executeQuery();
		return extractor.extract(rs, table);
	}
	
	private List queryInternal(Class clazz, Table table, String sql,
			Collection paramColumns, Object item) throws SQLException {
		PreparedStatement st = null;
		try {
			st = connection.prepareStatement(sql);
			
			if (paramColumns != null) {
				ValueBinder binder = factory.getBinder(item.getClass());
				int index = 1;
				Iterator it = paramColumns.iterator();
				while (it.hasNext()) {
					Column column = (Column) it.next();
					binder.bind(st, item, index++, column.getFieldName(), column.getDbType());
				}
			}
			
			return queryInternal(clazz, table, st);
		} finally {
			if (st != null)
				st.close();
		}
	}
	
	private List queryInternal(Class clazz, Table table, String sql,
			Collection paramNames, Collection paramTypes, Object item)
			throws SQLException {
		PreparedStatement st = null;
		try {
			st = connection.prepareStatement(sql);
			
			if (paramNames != null) {
				ValueBinder binder = factory.getBinder(item.getClass());
				int index = 1;
				Iterator itName = paramNames.iterator();
				if (paramTypes != null) {
					Iterator itType = paramTypes.iterator();
					while (itName.hasNext() && itType.hasNext()) {
						binder.bind(st, item, index++, (String) itName.next(), ((Integer) itType.next()).intValue());
					}
				} else {
					while (itName.hasNext()) {
						binder.bind(st, item, index++, (String) itName.next(), Types.EMPTY);
					}
				}
			}
			
			return queryInternal(clazz, table, st);
		} finally {
			if (st != null)
				st.close();
		}
	}

	public Object executeScalar(String sql) throws SQLException {
		Object result = null;
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				result = rs.getObject(1);
			rs.close();
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}
	
	public boolean existTable(String entity) {
		return existTable(factory.getMapping().getTable(entity));
	}
	
	public boolean existTable(Class clazz) {
		return existTable(factory.getMapping().getTable(clazz));
	}

	public void createTable(String entity) throws SQLException {
		createTable(factory.getMapping().getTable(entity));
	}
	
	public void createTable(Class clazz) throws SQLException {
		createTable(factory.getMapping().getTable(clazz));
	}

	public void dropTable(String entity) throws SQLException {
		dropTable(factory.getMapping().getTable(entity));
	}

	public void dropTable(Class clazz) throws SQLException {
		dropTable(factory.getMapping().getTable(clazz));
	}
	
	public boolean delete(String entity, Object item) throws SQLException {
		return delete(factory.getMapping().getTable(entity), item);
	}
	
	public boolean delete(Class clazz, Object item) throws SQLException {
		return delete(factory.getMapping().getTable(clazz), item);
	}

	public long insert(String entity, Object item) throws SQLException {
		return insert(factory.getMapping().getTable(entity), item);
	}

	public long insert(Class clazz, Object item) throws SQLException {
		return insert(factory.getMapping().getTable(clazz), item);
	}

	public boolean update(String entity, Object item) throws SQLException {
		return update(factory.getMapping().getTable(entity), item);
	}

	public boolean update(Class clazz, Object item) throws SQLException {
		return update(factory.getMapping().getTable(clazz), item);
	}

	public Object find(String entity, Object id) throws SQLException, QueryException {
		return find(factory.getMapping().getTable(entity), id);
	}
	
	public Object find(Class clazz, Object id) throws SQLException, QueryException {
		return find(factory.getMapping().getTable(clazz), id);
	}

	private boolean existTable(Table table) {
		try {
			executeScalar("select 1 from " + table.getQualifiedName(factory.getDialect(), factory.getMapping().getCatalog(), factory.getMapping().getSchema()));
		} catch (SQLException ex) {
			return false;
		}
		return true;
	}
	
	private void createTable(Table table) throws SQLException {
		String sql = table.toSqlCreate(factory.getDialect(),
				factory.getMapping().getCatalog(), factory.getMapping().getSchema());
		executeUpdate(sql);
	}
	
	private void dropTable(Table table) throws SQLException {
		String sql = table.toSqlDrop(factory.getDialect(),
				factory.getMapping().getCatalog(), factory.getMapping().getSchema());
		executeUpdate(sql);
	}

	private long insert(Table table, Object item) throws SQLException {
		ArrayList paramColumns = new ArrayList();
		String sql = table.toSqlInsert(factory.getDialect(), factory.getMapping().getCatalog(),
				factory.getMapping().getSchema(), paramColumns);
		
		executeUpdate(sql, paramColumns, item);
		
		// select id
		long id = 0L;
		Column idCol = table.getIdColumn();
		if (idCol != null) {
			ValueExtractor extractor = factory.getExtractor(item.getClass());
			if (extractor != null) {
				String idSelectSql = factory.getDialect().getIdentitySelectString(table.getName(),
						idCol.getName(), idCol.getDbType());
				Statement st = null;
				try {
					st = connection.createStatement();
					ResultSet rs = st.executeQuery(idSelectSql);
					if (rs.next()) {
						extractor.extract(rs, item, 1, idCol.getFieldName());
						id = rs.getLong(1);
					}
					rs.close();
				} catch (SQLException ex) {
					// ignore
				} finally {
					if (st != null)
						st.close();
				}
			}
		}
		
		return id;
	}
	
	private boolean update(Table table, Object item) throws SQLException {
		if (!table.hasPrimaryKey())
			throw new QueryException("The type " + table.getName() + " has no primary-key property.");
		
		ArrayList paramColumns = new ArrayList();
		String sql = table.toSqlUpdate(factory.getDialect(),
				factory.getMapping().getCatalog(), factory.getMapping().getSchema(), paramColumns);
		
		return executeUpdate(sql, paramColumns, item) > 0;
	}

	private boolean delete(Table table, Object item) throws SQLException {
		if (!table.hasPrimaryKey())
			throw new QueryException("The type " + table.getName() + " has no primary-key property.");

		ArrayList paramColumns = new ArrayList();
		String sql = table.toSqlDelete(factory.getDialect(),
				factory.getMapping().getCatalog(), factory.getMapping().getSchema(), paramColumns);
		
		return executeUpdate(sql, paramColumns, item) > 0;
	}

	private Object find(Table table, Object id) throws SQLException, QueryException {
		if (!table.hasPrimaryKey())
			throw new QueryException("The type " + table.getName() + " has no primary-key property.");
		
		Object params;
		if (Number.class.isAssignableFrom(id.getClass())
				|| String.class.isAssignableFrom(id.getClass())
				|| Boolean.class.isAssignableFrom(id.getClass())) {
			HashMap map = new HashMap();
			map.put(((Column)table.getPrimaryKey().getColumns().get(0)).getFieldName(), id);
			params = map;
		} else {
			params = id;
		}
		
		ArrayList paramList = new ArrayList();
		String sql = table.toSqlSelect(factory.getDialect(), factory.getMapping().getCatalog(),
				factory.getMapping().getSchema(), true, paramList);
		
		List list = queryInternal(table.getEntityClass(), table, sql, paramList, params);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public void clearWarnings() throws SQLException {
		connection.clearWarnings();
	}

	public void close() throws SQLException {
		connection.close();
	}

	public void commit() throws SQLException {
		connection.commit();
	}

	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return connection.createArrayOf(typeName, elements);
	}

	public Blob createBlob() throws SQLException {
		return connection.createBlob();
	}

	public Clob createClob() throws SQLException {
		return connection.createClob();
	}

	public NClob createNClob() throws SQLException {
		return connection.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return connection.createSQLXML();
	}

	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return connection.createStatement(resultSetType, resultSetConcurrency);
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return connection.createStruct(typeName, attributes);
	}

	public boolean getAutoCommit() throws SQLException {
		return connection.getAutoCommit();
	}

	public String getCatalog() throws SQLException {
		return connection.getCatalog();
	}

	public Properties getClientInfo() throws SQLException {
		return connection.getClientInfo();
	}

	public String getClientInfo(String name) throws SQLException {
		return connection.getClientInfo(name);
	}

	public int getHoldability() throws SQLException {
		return connection.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return connection.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException {
		return connection.getTransactionIsolation();
	}

	public Map getTypeMap() throws SQLException {
		return connection.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException {
		return connection.getWarnings();
	}

	public boolean isClosed() throws SQLException {
		return connection.isClosed();
	}

	public boolean isReadOnly() throws SQLException {
		return connection.isReadOnly();
	}

	public boolean isValid(int timeout) throws SQLException {
		return connection.isValid(timeout);
	}

	public String nativeSQL(String sql) throws SQLException {
		return connection.nativeSQL(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return connection.prepareCall(sql);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return connection.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return connection.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return connection.prepareStatement(sql, columnNames);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint(savepoint);
	}

	public void rollback() throws SQLException {
		connection.rollback();
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		connection.rollback(savepoint);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}

	public void setCatalog(String catalog) throws SQLException {
		connection.setCatalog(catalog);
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		connection.setClientInfo(properties);
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		connection.setClientInfo(name, value);
	}

	public void setHoldability(int holdability) throws SQLException {
		connection.setHoldability(holdability);		
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		connection.setReadOnly(readOnly);
	}

	public Savepoint setSavepoint() throws SQLException {
		return connection.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return connection.setSavepoint(name);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation(level);
	}

	public void setTypeMap(Map arg0) throws SQLException {
		connection.setTypeMap(arg0);
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		return connection.isWrapperFor(arg0);
	}

	public void setSchema(String schema) throws SQLException {
		connection.setSchema(schema);
	}

	public String getSchema() throws SQLException {
		return connection.getSchema();
	}

	public void abort(java.util.concurrent.Executor executor) throws SQLException {
		connection.abort(executor);
	}

	public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds)
			throws SQLException {
		connection.setNetworkTimeout(executor, milliseconds);
	}

	public int getNetworkTimeout() throws SQLException {
		return connection.getNetworkTimeout();
	}

	public Object unwrap(Class iface) throws SQLException {
		return connection.unwrap(iface);
	}
}