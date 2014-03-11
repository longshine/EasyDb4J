package lx.easydb.mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lx.easydb.StringHelper;

/**
 * The this_is_a_table style of naming.
 * 
 * @author Long
 *
 */
public class DefaultNamingStrategy implements INamingStrategy {
	static final Pattern pattern = Pattern.compile("[A-Z]+(?=[a-z0-9]|$)");
	
    public String getColumnName(String propertyName) {
        return normalize(propertyName);
    }

    public String getTableName(String typeName) {
        return normalize(StringHelper.unqualify(typeName));
    }
    
    private String normalize(String name) {
    	Matcher m = pattern.matcher(name);
    	if (m.find()) {
    		StringBuffer sb = new StringBuffer();
            do {
                m.appendReplacement(sb, m.start() > 0 ? ("_" + m.group().toLowerCase()) : m.group().toLowerCase());
            } while (m.find());
            m.appendTail(sb);
            return sb.toString();
    	} else {
    		return name;
    	}
    }
}
