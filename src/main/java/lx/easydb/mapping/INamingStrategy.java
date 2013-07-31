package lx.easydb.mapping;

import lx.easydb.StringHelper;

public interface INamingStrategy {
	String getColumnName(String propertyName);
	String getTableName(String typeName);
}

class DefaultNamingStrategy implements INamingStrategy {
    public String getColumnName(String propertyName) {
        return StringHelper.unqualify(propertyName);
    }

    public String getTableName(String typeName) {
        return StringHelper.unqualify(typeName);
    }
}