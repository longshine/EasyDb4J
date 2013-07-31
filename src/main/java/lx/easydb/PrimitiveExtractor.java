package lx.easydb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lx.easydb.mapping.Table;

public abstract class PrimitiveExtractor implements ValueExtractor {

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
}
