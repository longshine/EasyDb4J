package lx.easydb.test;

import java.util.Map;

import lx.easydb.ConnectionFactoryBuilder;
import lx.easydb.MapExtractor;

public class HSQLDBTest extends AbstractDbTest {

	public HSQLDBTest() throws Exception {
		super(ConnectionFactoryBuilder.newBuilder(
				"org.hsqldb.jdbcDriver",
				"jdbc:hsqldb:file:test",
				"SA", "",
				"lx.easydb.dialect.HSQLDialect", null).build());
		
		/**
		 *  HSQLDB uppercases column names,
		 *  so register Map with a case-insensitive MapExtractor.
		 */
		factory.registerExtractor(Map.class, new MapExtractor(MapExtractor.UPPERCASE));
	}
}
