package lx.easydb.mapping;

import lx.easydb.dialect.Dialect;

public class Column {
	public static final int DEFAULT_LENGTH = 255;
	public static final int DEFAULT_PRECISION = 19;
	public static final int DEFAULT_SCALE = 2;
	
	private String name;
	private String fieldName;
	private int length = DEFAULT_LENGTH;
	private int precision = DEFAULT_PRECISION;
	private int scale = DEFAULT_SCALE;
	private int dbType;
	private boolean nullable = true;
	private boolean unique;
	private String comment;
	private String defaultValue;
	private String checkConstraint;
	private String sqlType;
	private boolean updatable = true;
	private IMemberMap memberInfo;
	
	public Column(String columnName, String fieldName, int dbType) {
		this.name = columnName;
		this.fieldName = fieldName;
		this.dbType = dbType;
	}
	
	public void setName(String name) {
		this.name = Dialect.unquote(name);
	}
	
	public String getName() {
		return name;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getPrecision() {
		return precision;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getScale() {
		return scale;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	public int getDbType() {
		return dbType;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isNullable() {
		return nullable;
	}
	
	public String getQuotedName(Dialect dialect) {
        return (dialect.openQuote() + name + dialect.closeQuote());
    }

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setCheckConstraint(String checkConstraint) {
		this.checkConstraint = checkConstraint;
	}

	public String getCheckConstraint() {
		return checkConstraint;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getSqlType() {
		return sqlType;
	}
	
	public String getSqlType(Dialect dialect) {
        return sqlType == null ? dialect.getTypeName(dbType, length, precision, scale) : sqlType;
    }

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	
	public IMemberMap getMemberInfo() {
		return memberInfo;
	}
	
	public void setMemberInfo(IMemberMap memberInfo) {
		this.memberInfo = memberInfo;
	}
}
