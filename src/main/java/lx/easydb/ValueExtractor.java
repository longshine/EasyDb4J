package lx.easydb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import lx.easydb.mapping.Table;

public interface ValueExtractor {
	List extract(ResultSet rs, Table table) throws SQLException;
	void extract(ResultSet rs, Object item, int index, String field) throws SQLException;
}
