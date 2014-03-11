package lx.easydb.test;

import junit.framework.TestCase;
import lx.easydb.mapping.DefaultNamingStrategy;

public class NamingStrategyTest extends TestCase {

	public void testNamingStrategy() {
		DefaultNamingStrategy ns = new DefaultNamingStrategy();
		assertEquals("abc", ns.getTableName("abc"));
		assertEquals("abc", ns.getTableName("Abc"));
		assertEquals("abc", ns.getTableName("org.Abc"));
		assertEquals("a_bc", ns.getTableName("aBc"));
		assertEquals("ab_c", ns.getTableName("abC"));
		assertEquals("abc_de_fg", ns.getTableName("AbcDeFg"));

		assertEquals("rst", ns.getColumnName("rst"));
		assertEquals("rst", ns.getColumnName("Rst"));
		assertEquals("r_st", ns.getColumnName("rSt"));
		assertEquals("rs_t", ns.getColumnName("rsT"));
		assertEquals("rst_uvw", ns.getColumnName("rstUvw"));
	}
}
