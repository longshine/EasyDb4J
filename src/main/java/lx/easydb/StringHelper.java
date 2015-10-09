package lx.easydb;

import java.util.Iterator;

public final class StringHelper {
	
	private StringHelper() {
	}
	
	public static StringBuffer createBuilder() {
		return new StringBuffer();
	}
	
	public static String unqualify(String qualifiedName) {
        int loc = qualifiedName.lastIndexOf(".");
        return (loc < 0) ? qualifiedName : qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
    }
	
	public static String replaceOnce(String template, String placeholder, String replacement) {
		if ( template == null ) {
			return template; // returning null!
		}
        int loc = template.indexOf( placeholder );
		if ( loc < 0 ) {
			return template;
		} else {
			return new StringBuffer( template.substring( 0, loc ) )
					.append( replacement )
					.append( template.substring( loc + placeholder.length() ) )
					.toString();
		}
	}
	
	public static String join(String seperator, @SuppressWarnings("rawtypes") Iterator objects) {
		StringBuffer sb = new StringBuffer();
		if (objects.hasNext())
			sb.append(objects.next());
		while (objects.hasNext()) {
			sb.append(seperator).append(objects.next());
		}
		return sb.toString();
	}
	
	public static String join(String seperator, Object[] objects) {
		int len = objects.length;
		StringBuffer sb = new StringBuffer(len * 2);
		if (len > 0) {
			sb.append(objects[0]);
			for (int i = 1; i < len; i++) {
				sb.append(seperator).append(objects[i]);
			}
		}
		return sb.toString();
	}
	
	public static String toString(Object[] objects) {
		return join(", ", objects);
	}
}
