package lx.easydb.test;

import lx.easydb.ConnectionFactoryBuilder;

public class MySQLTest extends AbstractDbTest {

	public MySQLTest() throws Exception {
		super(ConnectionFactoryBuilder.newBuilder(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost/test",
				"root", "asdf",
				"lx.easydb.dialect.MySQLDialect", null).build());
	}
}
