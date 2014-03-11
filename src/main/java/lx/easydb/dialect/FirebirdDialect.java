package lx.easydb.dialect;

/**
 * An SQL dialect for Firebird.
 */
public class FirebirdDialect extends InterbaseDialect {

	public String getPaging(String sql, String order, int total, int offset) {
		return new StringBuffer(sql.length() + 20)
			.append(sql)
			.insert(6, offset > 0 ? (" first " + total + " skip " + offset) : (" first " + total))
			.toString();
	}
}
