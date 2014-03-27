package lx.easydb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class MapBinder extends AbstractValueBinder {

	public void bind(PreparedStatement st, Object item, int index,
			String field, int sqlType) throws SQLException {
		Map map = (Map) item;
		setObject(st, index, map.get(field), sqlType);
	}
}
