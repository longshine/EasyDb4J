/**
 * Copyright (c) 2011 SmeshLink Technology Corporation.
 * All rights reserved.
 * 
 * This file is part of the SmeshServer, a gateway middleware for WSN.
 * Please see README for more information.
 */
package lx.easydb.datasource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Longshine
 *
 */
public interface IDataSource {
	Connection getConnection() throws SQLException;
}
