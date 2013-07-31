package lx.easydb;

import java.sql.SQLException;

import lx.easydb.dialect.Dialect;
import lx.easydb.mapping.Mapping;

public interface IConnectionFactory {
	IConnection openConnection() throws SQLException;
	String getDriver();
	String getUrl();
	String getUser();
	String getPassword();
	Dialect getDialect();
	Mapping getMapping();
	void registerBinder(Class clazz, ValueBinder binder);
	ValueBinder getBinder(Class clazz);
	void registerExtractor(Class clazz, ValueExtractor extractor);
	ValueExtractor getExtractor(Class clazz);
}
