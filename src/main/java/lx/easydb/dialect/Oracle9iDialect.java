package lx.easydb.dialect;

import lx.easydb.Types;

/**
 * A dialect for Oracle 9i databases.
 */
public class Oracle9iDialect extends Oracle8iDialect {
	
	protected void registerCharacterTypeMappings() {
		registerColumnType(Types.CHAR, "char(1 char)");
		registerColumnType(Types.VARCHAR, 4000, "varchar2($l char)");
		registerColumnType(Types.VARCHAR, "long");
	}

	protected void registerDateTimeTypeMappings() {
		registerColumnType(Types.DATE, "date");
		registerColumnType(Types.TIME, "date");
		registerColumnType(Types.TIMESTAMP, "timestamp");
	}

	public String getPaging(String sql, String order, int total, int offset) {
		sql = sql.trim();
		String forUpdateClause = null;
		boolean isForUpdate = false;
		final int forUpdateIndex = sql.toLowerCase().lastIndexOf( "for update") ;
		if ( forUpdateIndex > -1 ) {
			// save 'for update ...' and then remove it
			forUpdateClause = sql.substring( forUpdateIndex );
			sql = sql.substring( 0, forUpdateIndex-1 );
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 );
		if (offset > 0) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (offset > 0) {
			pagingSelect.append(" ) row_ where rownum <= ")
				.append(offset + total)
				.append(") where rownum_ > ")
				.append(offset);
		}
		else {
			pagingSelect.append(" ) where rownum <= ").append(total);
		}

		if ( isForUpdate ) {
			pagingSelect.append( " " );
			pagingSelect.append( forUpdateClause );
		}

		return pagingSelect.toString();
	}
}
