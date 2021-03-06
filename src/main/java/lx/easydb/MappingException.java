package lx.easydb;

/**
 * Exception that occurs in O-R mappings.
 * 
 * @author Long
 *
 */
public class MappingException extends RuntimeException {
	private static final long serialVersionUID = 5965616730899166269L;
	
	public MappingException(String message) {
		super(message);
	}

	public MappingException(String message, Throwable e) {
		super(message, e);
	}
}
