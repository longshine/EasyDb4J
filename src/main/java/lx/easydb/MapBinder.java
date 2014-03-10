package lx.easydb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class MapBinder implements ValueBinder {

	public void bind(PreparedStatement st, Object item, int index,
			String field, int sqlType) throws SQLException {
		Map map = (Map) item;
		if (sqlType == Types.EMPTY)
			st.setObject(index, map.get(field));
		else
			st.setObject(index, map.get(field), sqlType);
	}
}
