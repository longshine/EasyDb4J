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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapExtractor extends ObjectExtractor {
	public static final int ORIGIN = 0;
	public static final int LOWERCASE = -1;
	public static final int UPPERCASE = 1;
	private int casing;
	
	public MapExtractor() {
		this(ORIGIN);
	}
	
	public MapExtractor(int casing) {
		this.casing = casing;
	}

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
					super.put(casing(key.toString()), value);
			}
			
			public Object get(Object key) {
				return key == null ? super.get(key) :
					super.get(casing(key.toString()));
			}
		};
	}
	
	private String casing(String key) {
		switch (casing) {
		case LOWERCASE:
			return key.toLowerCase();
		case UPPERCASE:
			return key.toUpperCase();
		default:
			return key;
		}
	}

	public int getCasing() {
		return casing;
	}

	public void setCasing(int casing) {
		this.casing = casing;
	}
}
