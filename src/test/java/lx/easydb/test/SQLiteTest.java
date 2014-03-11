package lx.easydb.test;

import lx.easydb.ConnectionFactoryBuilder;

public class SQLiteTest extends AbstractDbTest {

	public SQLiteTest() throws Exception {
		super(ConnectionFactoryBuilder.newBuilder(
				"org.sqlite.JDBC",
				"jdbc:sqlite:test.db",
				"", "",
				"lx.easydb.dialect.SQLiteDialect", null).build());
	}
}
