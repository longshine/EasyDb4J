package lx.easydb.dialect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import lx.easydb.MappingException;
import lx.easydb.StringHelper;
import lx.easydb.Types;
import lx.easydb.dialect.function.CastFunction;
import lx.easydb.dialect.function.ISQLFunction;
import lx.easydb.dialect.function.SQLFunctionTemplate;
import lx.easydb.dialect.function.StandardAnsiSqlAggregationFunctions;
import lx.easydb.dialect.function.StandardSQLFunction;
import lx.easydb.mapping.Column;

public abstract class Dialect {
	static final String QUOTES = "`\"[";

	private TypeNames typeNames = new TypeNames();
	private final Map sqlFunctions = new HashMap();

	public static boolean isQuoted(String s) {
		return s != null && s.length() > 0 && QUOTES.indexOf(s.charAt(0)) > -1;
	}

	public static String unquote(String s) {
		return isQuoted(s) ? s.substring(1, s.length() - 1) : s;
	}
	
	protected Dialect() {
		StandardAnsiSqlAggregationFunctions.registerFunctions(sqlFunctions);
		
		// standard sql92 functions (can be overridden by subclasses)
		registerFunction("substring", new SQLFunctionTemplate(Types.VARCHAR, "substring(?1, ?2, ?3)"));
		registerFunction("locate", new SQLFunctionTemplate(Types.INTEGER, "locate(?1, ?2, ?3)"));
		registerFunction("trim", new SQLFunctionTemplate(Types.VARCHAR, "trim(?1 ?2 ?3 ?4)"));
		registerFunction("length", new StandardSQLFunction("length", Types.INTEGER));
		registerFunction("bit_length", new StandardSQLFunction("bit_length", Types.INTEGER));
		registerFunction("coalesce", new StandardSQLFunction("coalesce"));
		registerFunction("nullif", new StandardSQLFunction("nullif"));
		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("mod", new StandardSQLFunction("mod", Types.INTEGER));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", Types.DOUBLE));
		registerFunction("upper", new StandardSQLFunction("upper"));
		registerFunction("lower", new StandardSQLFunction("lower"));
		registerFunction("cast", new CastFunction());
		registerFunction("extract", new SQLFunctionTemplate(Types.INTEGER, "extract(?1 ?2 ?3)"));

		//map second/minute/hour/day/month/year to ANSI extract(), override on subclasses
		registerFunction("second", new SQLFunctionTemplate(Types.INTEGER, "extract(second from ?1)"));
		registerFunction("minute", new SQLFunctionTemplate(Types.INTEGER, "extract(minute from ?1)"));
		registerFunction("hour", new SQLFunctionTemplate(Types.INTEGER, "extract(hour from ?1)"));
		registerFunction("day", new SQLFunctionTemplate(Types.INTEGER, "extract(day from ?1)"));
		registerFunction("month", new SQLFunctionTemplate(Types.INTEGER, "extract(month from ?1)"));
		registerFunction("year", new SQLFunctionTemplate(Types.INTEGER, "extract(year from ?1)"));

		registerFunction("str", new SQLFunctionTemplate(Types.VARCHAR, "cast(?1 as char)"));
	}
	
	public ISQLFunction findFunction(String functionName) {
		return (ISQLFunction) sqlFunctions.get(functionName);
    }
	
	public String getTypeName(int type) {
		String result = typeNames.get(type);
        if (result == null)
            throw new MappingException("No dialect mapping for type: " + type);
        return result;
	}
	
	public String getTypeName(int type, int length, int precision, int scale) {
        String result = typeNames.get(type, length, precision, scale);
        if (result == null)
            throw new MappingException("No dialect mapping for type: " + type + ", length: " + length);
        return result;
    }

	public String getCastTypeName(String type) {
		int t = Types.OTHER;
		
		if (type == null)
            t = Types.OTHER;
        else if ("int".equalsIgnoreCase(type)
            || "integer".equalsIgnoreCase(type))
            t = Types.INTEGER;
        else if ("uint".equalsIgnoreCase(type)
            || "unsigned".equalsIgnoreCase(type))
            t = Types.INTEGER;
        else if ("float".equalsIgnoreCase(type)
            || "double".equalsIgnoreCase(type))
            t = Types.DOUBLE;
        else if ("binary".equalsIgnoreCase(type))
            t = Types.BINARY;
        else if ("string".equalsIgnoreCase(type))
            t = Types.VARCHAR;

        return getCastTypeName(t);
	}
	
	public String quote(String name) {
        return openQuote() + unquote(name) + closeQuote();
    }
	
	public String paramPrefix() {
		return "?";
	}
	
	public char openQuote() { 
		return '"';
	}
	
	public char closeQuote() { 
		return '"';
	}
	
	/**
	 * Do we need to qualify index names with the schema name?
	 */
	public boolean qualifyIndexName() {
		return true;
	}
	
	/**
	 * The keyword used to specify a nullable column.
	 */
	public String getNullColumnString() {
		return "";
	}
	
	/**
	 * Does this dialect support the <code>UNIQUE</code> column syntax?
	 */
	public boolean supportsUnique() {
		return false;
	}
	
	/**
     * Does this dialect support adding Unique constraints via create and alter table ?
     */
	public boolean supportsUniqueConstraintInCreateAlterTable() {
		return true;
	}
	
	/**
	 * Does this dialect support the <code>UNIQUE</code> column syntax in nullable columns?
	 */
	public boolean supportsNullableUnique() {
		return true;
	}
	
	/**
	 * Whether this dialect have an primary key clause added to the data type or a completely separate identity data type.
	 */
	public boolean hasPrimaryKeyInIdentityColumn() {
		return false;
	}
	
	/**
	 * Whether this dialect have an Identity clause added to the data type or a completely separate identity data type.
	 */
	public boolean hasDataTypeInIdentityColumn() {
		return true;
	}
	
	/**
	 * Get the select command to use to retrieve the last generated IDENTITY
	 * value for a particular table
	 *
	 * @param table The table into which the insert was done
	 * @param column The PK column.
	 * @param type The {@link java.sql.Types} type code.
	 * @return The appropriate select command
	 * @throws MappingException If IDENTITY generation is not supported.
	 */
	public String getIdentitySelectString(String table, String column, int type) throws MappingException {
		return getIdentitySelectString();
	}
	
	/**
	 * Get the select command to use to retrieve the last generated IDENTITY
	 * value.
	 *
	 * @return The appropriate select command
	 * @throws MappingException If IDENTITY generation is not supported.
	 */
	protected String getIdentitySelectString() throws MappingException {
		throw new MappingException( "Dialect does not support identity key generation" );
	}
	
	/**
	 * Does this dialect support column-level check constraints?
	 */
	public boolean supportsColumnCheck() {
		return true;
	}
	
	/**
	 * Does this dialect support table-level check constraints?
	 */
	public boolean supportsTableCheck() {
		return true;
	}
	
	/**
	 * Does this dialect support "if exists" syntax before table name?
	 */
	public boolean supportsIfExistsBeforeTableName() {
		return false;
	}
	
	/**
	 * Does this dialect support "if exists" syntax after table name?
	 */
	public boolean supportsIfExistsAfterTableName() {
		return false;
	}
	
	/**
	 * Completely optional cascading drop clause
	 */
	public String getCascadeConstraintsString() {
		return "";
	}
	
	/**
	 * Command used to create a table.
	 */
	public String getCreateTableString() {
		return "create table";
	}
	
	/**
	 * Slight variation on CreateTableString, used to create a table when there is no primary key and duplicate rows are expected.
	 */
	public String getCreateMultisetTableString() {
		return getCreateTableString();
	}
	
	/**
	 * Does this dialect support temporary tables?
	 */
	public boolean supportsTemporaryTables() {
		return false;
	}
	
	/**
	 * Command used to create a temporary table.
	 */
	public String getCreateTemporaryTableString() {
		return "create table";
	}
	
	/**
	 * Command used to drop a temporary table.
	 */
	public String getDropTemporaryTableString() {
		return "drop table";
	}
	
	/**
	 * Gets the name of the SQL function that transforms a string to lowercase.
	 */
	public String getLowercaseFunction() {
		return "lower";
	}
	
	/**
	 * The syntax used during DDL to define a column as being an IDENTITY of
	 * a particular type.
	 *
	 * @param type The {@link java.sql.Types} type code.
	 * @return The appropriate DDL fragment.
	 * @throws MappingException If IDENTITY generation is not supported.
	 */
	public String getIdentityColumnString(int type) throws MappingException {
		return getIdentityColumnString();
	}
	
	/**
	 * Gets the syntax used during DDL to define a column as being an IDENTITY of a particular type.
	 */
	public String getIdentityColumnString() {
		throw new MappingException("Dialect does not support identity key generation");
	}
	
	/**
	 * Gets the syntax used to add a primary key constraint to a table.
	 */
	public String getAddPrimaryKeyConstraintString(String constraintName) {
        return " add constraint " + quote(constraintName) + " primary key ";
    }
	
	/**
	 * Gets the syntax used to add a unique key constraint to a table.
	 */
	public String getAddUniqueKeyConstraintString(String constraintName) {
        return " add constraint " + quote(constraintName) + " unique ";
    }
	
	/**
	 * Gets the syntax used to add comment on a column.
	 */
	public String getColumnComment(String comment) {
        return "";
    }
	
	/**
	 * Gets the syntax used to add comment on a table.
	 */
	public String getTableComment(String comment) {
        return "";
    }
	
	/**
	 * Gets paging SQL.
	 */
	public abstract String getPaging(String sql, String order, int total, int offset);
	
	/**
	 * Get the name of the database type appropriate for casting operations (via the CAST() SQL function) for the given <see cref="LX.EasyDb.DbType"/>.
	 */
	public String getCastTypeName(int type) {
        return getTypeName(type, Column.DEFAULT_LENGTH, Column.DEFAULT_PRECISION, Column.DEFAULT_SCALE);
    }
	
	protected void registerFunction(String name, ISQLFunction function) {
		sqlFunctions.put(name, function);
	}
	
	protected void registerColumnType(int type, String name) {
        typeNames.put(type, name);
    }
	
	protected void registerColumnType(int type, int capacity, String name) {
		typeNames.put(type, capacity, name);
    }

	static class TypeNames {
		private HashMap weighted = new HashMap();
		private HashMap defaults = new HashMap();

		public String get(int typecode) {
			return (String) defaults.get(new Integer(typecode));
		}

		public String get(int typecode, int size, int precision, int scale) {
			Map map = (Map) weighted.get(new Integer(typecode));
			if (map != null && map.size() > 0) {
				// iterate entries ordered by capacity to find first fit
				Iterator entries = map.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry entry = (Map.Entry) entries.next();
					if (size <= ((Integer) entry.getKey()).intValue()) {
						return replace((String) entry.getValue(), size,
								precision, scale);
					}
				}
			}
			return replace(get(typecode), size, precision, scale);
		}

		public void put(int typecode, int capacity, String value) {
			TreeMap map = (TreeMap) weighted.get(new Integer(typecode));
			if (map == null) {// add new ordered map
				map = new TreeMap();
				weighted.put(new Integer(typecode), map);
			}
			map.put(new Integer(capacity), value);
		}

		public void put(int typecode, String value) {
			defaults.put(new Integer(typecode), value);
		}

		private static String replace(String type, int size, int precision, int scale) {
			type = StringHelper.replaceOnce(type, "$s", Integer.toString(scale));
			type = StringHelper.replaceOnce(type, "$l", Integer.toString(size));
			return StringHelper.replaceOnce(type, "$p", Integer.toString(precision));
		}
	}
}
