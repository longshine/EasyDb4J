/**
 * Copyright (c) 2011 SmeshLink Technology Corporation.
 * All rights reserved.
 * 
 * This file is part of the SmeshServer, a gateway middleware for WSN.
 * Please see README for more information.
 */
package lx.easydb.datasource;

/**
 * @author Longshine
 *
 */
public abstract class DataSource implements IDataSource {
	protected String driver = null;
	protected String url = null;
	protected String user = null;
	protected String password = null;

	public DataSource(String driver, String url, String user, String password) throws ClassNotFoundException {
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		Class.forName(driver);
	}
}
