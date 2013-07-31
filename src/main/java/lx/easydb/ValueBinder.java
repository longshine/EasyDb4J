package lx.easydb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ValueBinder {
	void bind(PreparedStatement st, Object item, int index, String field,
			int sqlType) throws SQLException;
}
