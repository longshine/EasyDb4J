package lx.easydb.test;

import lx.easydb.ConnectionFactoryBuilder;

public class SQLServerTest extends AbstractDbTest {

	public SQLServerTest() throws Exception {
		super(ConnectionFactoryBuilder.buildSQLServer2005("localhost", "test", "sa", ""));
	}
}
