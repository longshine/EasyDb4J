package lx.easydb.test;

import lx.easydb.ConnectionFactoryBuilder;

public class HSQLDBTest extends AbstractDbTest {

	public HSQLDBTest() throws Exception {
		super(ConnectionFactoryBuilder.newBuilder(
				"org.hsqldb.jdbcDriver",
				"jdbc:hsqldb:file:test",
				"SA", "",
				"lx.easydb.dialect.HSQLDialect", null).build());
	}
}
