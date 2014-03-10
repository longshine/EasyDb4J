package lx.easydb.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			assertEquals(conn.executeUpdate("create table t(i int)"), 0);
			assertEquals(conn.executeUpdate("insert into t values (?)",
					new Object[] { new Integer(1) }), 1);
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
			assertEquals(conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" },
					new User("skywalker", 13)), 1);
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
			assertEquals(conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" }, list), 2);
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
			assertEquals(conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" }, user), 1);
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
			assertEquals(conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" }, list), 2);
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
	
	public void testQueryMap() {
		IConnection conn = open();
		
		try {
			conn.executeUpdate("create table t(name varchar(32), age int)");
			assertEquals(conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" },
					new User[] { new User("skywalker", 13), new User("vader", 23)}), 2);
			
			List list = conn.query("select * from t");
			assertEquals(list.size(), 2);
			Map map = (Map) list.get(0);
			assertEquals(map.get("name"), "skywalker");
			assertEquals(map.get("age"), new Integer(13));
			
			list = conn.query("select * from t where name = ?", new String[] { "name" }, list.get(1));
			assertEquals(list.size(), 1);
			map = (Map) list.get(0);
			assertEquals(map.get("name"), "vader");
			assertEquals(map.get("age"), new Integer(23));
			
			list = conn.query("select * from t where name = ?", new Object[] { "skywalker" });
			assertEquals(list.size(), 1);
			map = (Map) list.get(0);
			assertEquals(map.get("name"), "skywalker");
			assertEquals(map.get("age"), new Integer(13));
		} catch (SQLException ex) {
			fail(ex.getMessage());
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testQueryEntityMap() {
		IConnection conn = open();
		
		try {
			conn.executeUpdate("create table t(name varchar(32), age int)");
			assertEquals(conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" },
					new User[] { new User("skywalker", 13), new User("vader", 23)}), 2);
			
			List list = conn.query("User", "select * from t");
			assertEquals(list.size(), 2);
			Map map = (Map) list.get(0);
			assertEquals(map.get("name"), "skywalker");
			assertEquals(map.get("age"), new Integer(13));
			
			list = conn.query("User", "select * from t where name = ?", new String[] { "name" }, list.get(1));
			assertEquals(list.size(), 1);
			map = (Map) list.get(0);
			assertEquals(map.get("name"), "vader");
			assertEquals(map.get("age"), new Integer(23));
		} catch (SQLException ex) {
			fail(ex.getMessage());
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testQueryEntityStrongType() {
		IConnection conn = open();
		
		try {
			conn.executeUpdate("create table t(name varchar(32), age int)");
			assertEquals(conn.executeUpdate("insert into t (name,age) values (?, ?)",
					new String[] { "name", "age" },
					new User[] { new User("skywalker", 13), new User("vader", 23)}), 2);
			
			List list = conn.query(User.class, "select * from t");
			assertEquals(list.size(), 2);
			User user = (User) list.get(0);
			assertEquals(user.getName(), "skywalker");
			assertEquals(user.getAge(), 13);
			
			factory.getMapping().registerTable("user", factory.getMapping().findTable(User.class));
			
			list = conn.query("user", "select * from t where name = ?", new String[] { "name" }, list.get(1));
			assertEquals(list.size(), 1);
			user = (User) list.get(0);
			assertEquals(user.getName(), "vader");
			assertEquals(user.getAge(), 23);
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
	
	static class User {
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
