package lx.easydb.datasource;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import lx.easydb.ReflectHelper;

/**
 * An {@link IDataSource} which holds a {@link Connection}
 * as a singleton. It never closes the inner connection really.
 * 
 * @author Longshine
 *
 */
public class SingletonDataSource extends DataSource {
	private Connection conn;
	
	public SingletonDataSource(String driver, String url, String user, String password) throws ClassNotFoundException, SQLException {
		super(driver, url, user, password);
		this.conn = new SingletonConnection(java.sql.DriverManager.getConnection(this.url, this.user, this.password));
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized final Connection getConnection() throws SQLException {
		return this.conn;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void finalize() throws Throwable {
		close();
	}
	
	public void close() throws Throwable {
		((SingletonConnection)this.conn).close(true);
	}

	private class SingletonConnection implements Connection {
		private Connection conn;
		
		public SingletonConnection(Connection conn) {
			this.conn = conn;
		}

		public void clearWarnings() throws SQLException {
			this.conn.clearWarnings();
		}

		public void close() throws SQLException {
			//this.conn.close();
		}
		
		public void close(boolean closing) throws SQLException {
			if (closing)
				this.conn.close();
		}

		public void commit() throws SQLException {
			this.conn.commit();
		}

		public Array createArrayOf(String typeName, Object[] elements)
				throws SQLException {
			return this.conn.createArrayOf(typeName, elements);
		}

		public Blob createBlob() throws SQLException {
			return this.conn.createBlob();
		}

		public Clob createClob() throws SQLException {
			return this.conn.createClob();
		}

		public NClob createNClob() throws SQLException {
			return this.conn.createNClob();
		}

		public SQLXML createSQLXML() throws SQLException {
			return this.conn.createSQLXML();
		}

		public Statement createStatement() throws SQLException {
			return this.conn.createStatement();
		}

		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			return this.conn.createStatement(resultSetType, resultSetConcurrency);
		}

		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return this.conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return this.conn.createStruct(typeName, attributes);
		}

		public boolean getAutoCommit() throws SQLException {
			return this.conn.getAutoCommit();
		}

		public String getCatalog() throws SQLException {
			return this.conn.getCatalog();
		}

		public Properties getClientInfo() throws SQLException {
			return this.conn.getClientInfo();
		}

		public String getClientInfo(String name) throws SQLException {
			return this.conn.getClientInfo(name);
		}

		public int getHoldability() throws SQLException {
			return this.conn.getHoldability();
		}

		public DatabaseMetaData getMetaData() throws SQLException {
			return this.conn.getMetaData();
		}

		public int getTransactionIsolation() throws SQLException {
			return this.conn.getTransactionIsolation();
		}

		public Map getTypeMap() throws SQLException {
			return this.conn.getTypeMap();
		}

		public SQLWarning getWarnings() throws SQLException {
			return this.conn.getWarnings();
		}

		public boolean isClosed() throws SQLException {
				return this.conn.isClosed();
		}

		public boolean isReadOnly() throws SQLException {
			return this.conn.isReadOnly();
		}

		public boolean isValid(int timeout) throws SQLException {
			return this.conn.isValid(timeout);
		}

		public String nativeSQL(String sql) throws SQLException {
			return this.conn.nativeSQL(sql);
		}

		public CallableStatement prepareCall(String sql) throws SQLException {
			return this.conn.prepareCall(sql);
		}

		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
			return this.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return this.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public PreparedStatement prepareStatement(String sql) throws SQLException {
			return this.conn.prepareStatement(sql);
		}

		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return this.conn.prepareStatement(sql, autoGeneratedKeys);
		}

		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return this.conn.prepareStatement(sql, columnIndexes);
		}

		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return this.conn.prepareStatement(sql, columnNames);
		}

		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
			return this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			this.conn.releaseSavepoint(savepoint);
		}

		public void rollback() throws SQLException {
			this.conn.rollback();
		}

		public void rollback(Savepoint savepoint) throws SQLException {
			this.conn.rollback(savepoint);
		}

		public void setAutoCommit(boolean autoCommit) throws SQLException {
			this.conn.setAutoCommit(autoCommit);
		}

		public void setCatalog(String catalog) throws SQLException {
			this.conn.setCatalog(catalog);
		}

		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			this.conn.setClientInfo(properties);
		}

		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			this.conn.setClientInfo(name, value);
		}

		public void setHoldability(int holdability) throws SQLException {
			this.conn.setHoldability(holdability);
		}

		public void setReadOnly(boolean readOnly) throws SQLException {
			this.conn.setReadOnly(readOnly);
		}

		public Savepoint setSavepoint() throws SQLException {
			return this.conn.setSavepoint();
		}

		public Savepoint setSavepoint(String name) throws SQLException {
			return this.conn.setSavepoint(name);
		}

		public void setTransactionIsolation(int level) throws SQLException {
			this.conn.setTransactionIsolation(level);
		}

		public void setTypeMap(Map arg0) throws SQLException {
			this.conn.setTypeMap(arg0);
		}

		public boolean isWrapperFor(Class arg0) throws SQLException {
			return this.conn.isWrapperFor(arg0);
		}

		public Object unwrap(Class iface) throws SQLException {
			return conn.unwrap(iface);
		}

		public void setSchema(String schema) throws SQLException {
			ReflectHelper.invokeSilent(conn, "setSchema", new Class[] { String.class }, new Object[] { schema });
		}

		public String getSchema() throws SQLException {
			return (String) ReflectHelper.invokeSilent(conn, "getSchema", null, null);
		}

		public void abort(java.util.concurrent.Executor executor) throws SQLException {
			ReflectHelper.invokeSilent(conn, "abort",
					new Class[] { java.util.concurrent.Executor.class }, new Object[] { executor });
		}

		public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds)
				throws SQLException {
			ReflectHelper.invokeSilent(conn, "setNetworkTimeout",
					new Class[] { java.util.concurrent.Executor.class, int.class },
					new Object[] { executor, new Integer(milliseconds) });
		}

		public int getNetworkTimeout() throws SQLException {
			return ((Integer) ReflectHelper.invokeSilent(conn, "getNetworkTimeout", null, null)).intValue();
		}
	}
}
