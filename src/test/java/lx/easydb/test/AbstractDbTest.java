package lx.easydb.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import lx.easydb.AbstractValueBinder;
import lx.easydb.IConnection;
import lx.easydb.IConnectionFactory;
import lx.easydb.ObjectExtractor;
import lx.easydb.Types;
import lx.easydb.criterion.Clauses;
import lx.easydb.criterion.Order;
import lx.easydb.criterion.Projections;
import lx.easydb.mapping.Column;
import lx.easydb.mapping.PrimaryKey;
import lx.easydb.mapping.Table;

public abstract class AbstractDbTest extends TestCase {
	private IConnectionFactory factory;
	
	public AbstractDbTest(IConnectionFactory factory) {
		this.factory = factory;
	}

	public void testConnection() throws SQLException {
		IConnection conn = factory.openConnection();
		conn.close();
	}
	
	public void testExecuteDirectSQL() throws SQLException {
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
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testExecuteStrongType() throws SQLException {
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
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testExecuteMultipleStrongType() throws SQLException {
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
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testExecuteMap() throws SQLException {
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
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testExecuteMultipleMap() throws SQLException {
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
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testQueryMap() throws SQLException {
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
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testQueryEntityMap() throws SQLException {
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
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testQueryEntityStrongType() throws SQLException {
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
		} finally {
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testCRUD() throws SQLException {
		IConnection conn = open();
		
		Table table = factory.getMapping().findTable(User.class);
		table.getColumns().clear();
		Column idCol = addColumn(table, "id", "id", Types.IDENTITY);
		addColumn(table, "name", "name", Types.VARCHAR);
		addColumn(table, "age", "age", Types.INTEGER);
		PrimaryKey pk = new PrimaryKey();
		pk.addColumn(idCol);
		table.setPrimaryKey(pk);
		
		try {
			conn.createTable(User.class);
			
			User u1 = new User("skywalker", 13);
			User u2 = new User("vader", 23);
			long id1 = conn.insert(User.class, u1);
			long id2 = conn.insert(User.class, u2);
			assertEquals(id1, u1.getId());
			assertEquals(id2, u2.getId());
			assertTrue(id2 == id1 + 1);
			
			User user = (User) conn.find(User.class, new Long(id2));
			assertNotNull(user);
			assertEquals(user.getName(), "vader");
			assertEquals(user.getAge(), 23);
			
			conn.delete(User.class, user);
			user = (User) conn.find(User.class, new Long(id2));
			assertNull(user);
			
			user = (User) conn.find(User.class, new Long(id1));
			assertNotNull(user);
			assertEquals(user.getName(), "skywalker");
			user.setName("vader");
			assertTrue(conn.update(User.class, user));
			
			user = (User) conn.find(User.class, new Long(id1));
			assertNotNull(user);
			assertEquals(user.getName(), "vader");
			assertEquals(user.getAge(), 13);
		} finally {
			try {
				conn.dropTable(User.class);
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testCustomBinderAndExtractor() throws SQLException {
		IConnection conn = open();
		
		factory.registerBinder(User.class, new AbstractValueBinder() {

			public void bind(PreparedStatement st, Object item, int index,
					String field, int sqlType) throws SQLException {
				User u = (User) item;
				if ("f1".equals(field)) {
					st.setString(index, u.getName());
				} else if ("f2".equals(field)) {
					st.setInt(index, u.getAge());
				}
			}
		});
		
		factory.registerExtractor(User.class, new ObjectExtractor() {

			public void extract(ResultSet rs, Object item, int index,
					String field) throws SQLException {
				User u = (User) item;
				if ("f1".equalsIgnoreCase(field)) {
					u.setName(rs.getString(index));
				} else if ("f2".equalsIgnoreCase(field)) {
					// mess up id with age deliberately
					u.setId(rs.getInt(index));
				}
			}

			protected Object newInstance() {
				return new User();
			}
		});
		
		try {
			conn.executeUpdate("create table t(f1 varchar(32) not null, f2 int not null)");
			
			assertEquals(conn.executeUpdate("insert into t (f1, f2) values (?, ?)",
					new String[] { "f1", "f2" },
					new User[] { new User("skywalker", 13), new User("vader", 23)}), 2);
			
			List list = conn.query(User.class, "select * from t");
			assertEquals(list.size(), 2);
			User user = (User) list.get(0);
			assertEquals(user.getName(), "skywalker");
			// age and id are swapped in the custom extractor
			assertEquals(user.getAge(), 0);
			assertEquals(user.getId(), 13);
			
			// unregister binder
			factory.registerBinder(User.class, null);
			try {
				conn.executeUpdate("insert into t (f1, f2) values (?, ?)",
						new String[] { "f1", "f2" },
						new User[] { new User("skywalker", 13), new User("vader", 23)});
				fail("Should not be here");
			} catch (SQLException e) {
				// should occur since no parameter is specified
				assertNotNull(e);
			}
		} finally {
			factory.registerBinder(User.class, null);
			factory.registerExtractor(User.class, null);
			
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testCriteria() throws SQLException {
		IConnection conn = open();
		
		Table table = factory.getMapping().findTable(User.class);
		table.getColumns().clear();
		Column idCol = addColumn(table, "id", "id", Types.IDENTITY);
		addColumn(table, "name", "name", Types.VARCHAR);
		addColumn(table, "age", "age", Types.INTEGER);
		PrimaryKey pk = new PrimaryKey();
		pk.addColumn(idCol);
		table.setPrimaryKey(pk);
		
		try {
			conn.createTable(User.class);
			
			long id1 = conn.insert(User.class, new User("skywalker", 13));
			long id2 = conn.insert(User.class, new User("vader", 23));
			long id3 = conn.insert(User.class, new User("padme", 18));
			
			User user = (User) conn.createCriteria(User.class)
				.add(Clauses.eq("name", "skywalker"))
				.single();
			assertNotNull(user);
			assertEquals(user.getId(), id1);
			assertEquals(user.getName(), "skywalker");
			assertEquals(user.getAge(), 13);
			
			List list = conn.createCriteria(User.class)
				.add(Clauses.between("id", new Long(id2), new Long(id3)))
				.addOrder(Order.asc("age"))
				.list();
			assertEquals(2, list.size());
			user = (User) list.get(0);
			assertNotNull(user);
			assertEquals(id3, user.getId());
			assertEquals("padme", user.getName());
			
			user = (User) list.get(1);
			assertNotNull(user);
			assertEquals(id2, user.getId());
			assertEquals("vader", user.getName());
			
			// test projection
			user = (User) conn.createCriteria(User.class)
				.setProjection(Projections.sum("age", "age"))
				.single();
			assertNotNull(user);
			assertNull(user.getName());
			assertEquals(54, user.getAge());
			
			// test paging
			list = conn.createCriteria(User.class)
					.addOrder(Order.asc("age"))
					.list(1, 2);
			assertEquals(1, list.size());
			user = (User) list.get(0);
			assertNotNull(user);
			assertEquals("vader", user.getName());
		} finally {
			try {
				conn.dropTable(User.class);
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testQueryPrimaryKey() throws SQLException {
		IConnection conn = open();
		
		factory.getMapping().registerTable(User.class, new Table(User.class, factory.getMapping().getNamingStrategy()));
		
		try {
			User user = new User();
			user.setId(2);
			user.setName("test");
			user.setAge(18);
			conn.createTable(User.class);
			long id = conn.insert(User.class, user);
			assertEquals(id, 0);
			user = (User) conn.find(User.class, new Integer(2));
			assertNotNull(user);
			assertEquals(user.getId(), 2);
			assertEquals(user.getName(), "test");
			assertEquals(user.getAge(), 18);
		} finally {
			try {
				conn.dropTable(User.class);
			} catch (Exception ex) { }
			
			try {
				conn.executeUpdate("drop table t");
			} catch (Exception ex) { }
			
			close(conn);
		}
	}
	
	public void testPublicField() throws SQLException {
		IConnection conn = open();
		
		try {
			ObjectWithPublicField o1 = new ObjectWithPublicField();
			o1.setId(2);
			o1.name = "public";
			conn.createTable(ObjectWithPublicField.class);
			conn.insert(ObjectWithPublicField.class, o1);
			ObjectWithPublicField o2 = (ObjectWithPublicField) conn.find(ObjectWithPublicField.class, new Integer(2));
			assertNotNull(o2);
			assertEquals(o2.getId(), o1.getId());
			assertEquals(o2.name, o1.name);
		} finally {
			try {
				conn.dropTable(ObjectWithPublicField.class);
			} catch (Exception ex) { }
			
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
	
	private static Column addColumn(Table table, String columnName, String fieldName, int dbType) {
		Column column = new Column(columnName, fieldName, dbType);
		table.addColumn(column);
		return column;
	}
}

class User {
	private int id;
	private String name;
	private int age;
	
	public User() {
	}
	
	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

class ObjectWithPublicField {
	private int id;
	public String name;
	
	public ObjectWithPublicField() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}