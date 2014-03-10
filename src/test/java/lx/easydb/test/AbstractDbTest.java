package lx.easydb.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;
import lx.easydb.IConnection;
import lx.easydb.IConnectionFactory;

public abstract class AbstractDbTest extends TestCase {
	private IConnectionFactory factory;
	
	public AbstractDbTest(IConnectionFactory factory) {
		this.factory = factory;
	}

	public void testConnection() {
		try {
			IConnection conn = factory.openConnection();
			conn.close();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
	
	public void testExecuteDirectSQL() {
		IConnection conn = open();
		try {
			conn.executeUpdate("create table t(i int)");
			conn.executeUpdate("insert into t values (?)",
					new Object[] { new Integer(1) });
			ResultSet rs = conn.executeQuery("select * from t");
			assertTrue(rs.next());
			assertEquals(rs.getInt(1), 1);
			rs.close();
			
			assertEquals(conn.executeScalar("select * from t"), new Integer(1));
		} catch (SQLException ex) {
			fail(ex.getMessage());
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testExecuteStrongType() {
		IConnection conn = open();
		
		try {
			conn.executeUpdate("create table t(name varchar(32), age int)");
			conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" },
					new User("skywalker", 13));
			ResultSet rs = conn.executeQuery("select * from t");
			assertTrue(rs.next());
			assertEquals(rs.getString(1), "skywalker");
			assertEquals(rs.getInt(2), 13);
			rs.close();
		} catch (SQLException ex) {
			fail(ex.getMessage());
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testExecuteMultipleStrongType() {
		IConnection conn = open();
		
		ArrayList list = new ArrayList();
		list.add(new User("skywalker", 13));
		list.add(new User("vader", 23));
		try {
			conn.executeUpdate("create table t(name varchar(32), age int)");
			conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" }, list);
			Object total = conn.executeScalar("select sum(age) from t");
			assertTrue(total instanceof Number);
			assertEquals(((Number) total).intValue(), 36);
		} catch (SQLException ex) {
			fail(ex.getMessage());
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testExecuteMap() {
		IConnection conn = open();
		
		HashMap user = new HashMap();
		user.put("name", "skywalker");
		user.put("age", new Integer(13));
		
		try {
			conn.executeUpdate("create table t(name varchar(32), age int)");
			conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" }, user);
			ResultSet rs = conn.executeQuery("select * from t");
			assertTrue(rs.next());
			assertEquals(rs.getString(1), "skywalker");
			assertEquals(rs.getInt(2), 13);
			rs.close();
		} catch (SQLException ex) {
			fail(ex.getMessage());
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testExecuteMultipleMap() {
		IConnection conn = open();
		
		ArrayList list = new ArrayList();
		HashMap user = new HashMap();
		user.put("name", "skywalker");
		user.put("age", new Integer(13));
		list.add(user);
		user = new HashMap();
		user.put("name", "vader");
		user.put("age", new Integer(23));
		list.add(user);
		
		try {
			conn.executeUpdate("create table t(name varchar(32), age int)");
			conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" }, list);
			Object total = conn.executeScalar("select sum(age) from t");
			assertTrue(total instanceof Number);
			assertEquals(((Number) total).intValue(), 36);
		} catch (SQLException ex) {
			fail(ex.getMessage());
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	protected IConnection open() {
		try {
			return factory.openConnection();
		} catch (SQLException ex) {
			fail(ex.getMessage());
			return null;
		}
	}
	
	protected void close(IConnection conn) {
		try {
			conn.close();
		} catch (SQLException ex) {
			fail(ex.getMessage());
		}
	}
	
	class User {
		private String name;
		private int age;
		
		public User() {
		}
		
		public User(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
}
