package lx.easydb.datasource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides a method to acquire {@link Connection}s.
 * 
 * @author Longshine
 *
 */
public interface IDataSource {
	/**
	 * Gets a {@link Connection} from the data source.
	 * @throws SQLException
	 */
	Connection getConnection() throws SQLException;
}
