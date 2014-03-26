package lx.easydb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lx.easydb.mapping.Table;

/**
 * {@link ValueExtractor} for primitive types.
 * 
 * @author Long
 *
 */
public abstract class PrimitiveExtractor extends AbstractValueExtractor {
	/**
	 * {@link ValueExtractor} for integers.
	 */
	public static final PrimitiveExtractor INTEGER = new IntegerExtractor();
	/**
	 * {@link ValueExtractor} for long integers.
	 */
	public static final PrimitiveExtractor LONG = new LongExtractor();

	public List extract(ResultSet rs, Table table) throws SQLException {
		ArrayList list = new ArrayList();
		while (rs.next()) {
			list.add(extract(rs, 1));
		}
		return list;
	}

	public void extract(ResultSet rs, Object item, int index,
			String field) throws SQLException {
	}
	
	protected abstract Object extract(ResultSet rs, int index) throws SQLException;
	
	static class IntegerExtractor extends PrimitiveExtractor {
		
		public Object extract(ResultSet rs, int index) throws SQLException {
			return Integer.valueOf(rs.getInt(index));
		}
	}
	
	static class LongExtractor extends PrimitiveExtractor {
	
		public Object extract(ResultSet rs, int index) throws SQLException {
			return Long.valueOf(rs.getLong(index));
		}
	}
}
