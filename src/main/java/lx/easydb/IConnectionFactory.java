package lx.easydb;

import java.sql.SQLException;

import lx.easydb.dialect.Dialect;
import lx.easydb.mapping.Mapping;

/**
 * Represents a factory that builds {@link IConnection}s.
 * 
 * @author Long
 *
 */
public interface IConnectionFactory {
	/**
	 * Gets an opened connection.
	 * @throws SQLException
	 */
	IConnection openConnection() throws SQLException;
	/**
	 * Gets the JDBC driver of this factory.
	 */
	String getDriver();
	/**
	 * Gets the URL of database server of this factory.
	 */
	String getUrl();
	/**
	 * Gets the user of this factory.
	 */
	String getUser();
	/**
	 * Gets the password of this factory.
	 */
	String getPassword();
	/**
	 * Gets the {@link Dialect} of this factory.
	 */
	Dialect getDialect();
	/**
	 * Gets the {@link Mapping} of this factory.
	 */
	Mapping getMapping();
	/**
	 * Registers a {@link ValueBinder} to a specified type.
	 * @param clazz the type of entity to register
	 * @param binder the {@link ValueBinder} to register
	 */
	void registerBinder(Class clazz, ValueBinder binder);
	/**
	 * Gets the {@link ValueBinder} associated with
	 * the specified type.
	 * @param clazz the type of entity
	 * @return the {@link ValueBinder} associated with the type, or null if not found
	 */
	ValueBinder getBinder(Class clazz);
	/**
	 * Registers a {@link ValueExtractor} to a specified type.
	 * @param clazz the type of entity to register
	 * @param binder the {@link ValueExtractor} to register
	 */
	void registerExtractor(Class clazz, ValueExtractor extractor);
	/**
	 * Gets the {@link ValueExtractor} associated with
	 * the specified type.
	 * @param clazz the type of entity
	 * @return the {@link ValueExtractor} associated with the type, or null if not found
	 */
	ValueExtractor getExtractor(Class clazz);
}
