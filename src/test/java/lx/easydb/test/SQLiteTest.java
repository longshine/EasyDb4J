package lx.easydb.test;

import lx.easydb.ConnectionFactoryBuilder;

public class SQLiteTest extends AbstractDbTest {

	public SQLiteTest() throws Exception {
		super(ConnectionFactoryBuilder.buildSQLite("test.db", "", ""));
	}
}
