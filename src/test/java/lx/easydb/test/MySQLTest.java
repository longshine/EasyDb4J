package lx.easydb.test;

import lx.easydb.ConnectionFactoryBuilder;

public class MySQLTest extends AbstractDbTest {

	public MySQLTest() throws Exception {
		super(ConnectionFactoryBuilder.buildMySQL("localhost", "test", "root", ""));
	}
}
