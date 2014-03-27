package lx.easydb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link ValueExtractor} for {@link Map} types,
 * extracts fields and puts them in a case-insensitive {@link LinkedHashMap}.
 * 
 * @author Long
 *
 */
public class MapExtractor extends ObjectExtractor {

	public void extract(ResultSet rs, Object item, int index,
			String field) throws SQLException {
		Map map = (Map) item;
		map.put(field, rs.getObject(index));
	}

	protected Object newInstance() {
		return new LinkedHashMap() {
			private static final long serialVersionUID = -5300859616547090379L;

			public Object put(Object key, Object value) {
				return key == null ? super.put(key, value) :
					super.put(key.toString().toUpperCase(), value);
			}
			
			public Object get(Object key) {
				return key == null ? super.get(key) :
					super.get(key.toString().toUpperCase());
			}
		};
	}
}
