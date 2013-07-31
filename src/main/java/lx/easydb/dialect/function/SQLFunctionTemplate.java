package lx.easydb.dialect.function;

import java.util.List;

import lx.easydb.IConnectionFactory;

public class SQLFunctionTemplate implements ISQLFunction {
	private final int type;
	private final TemplateRenderer renderer;
	private final boolean hasParenthesesIfNoArgs;

	public SQLFunctionTemplate(int type, String template) {
		this(type, template, true);
	}

	public SQLFunctionTemplate(int type, String template,
			boolean hasParenthesesIfNoArgs) {
		this.type = type;
		this.renderer = new TemplateRenderer(template);
		this.hasParenthesesIfNoArgs = hasParenthesesIfNoArgs;
	}

	public String render(int argumentType, List args, IConnectionFactory factory) {
		return renderer.render(args, factory);
	}
	
	public int getReturnType(int argumentType) {
		return type;
	}
	
	public boolean hasArguments() {
		return renderer.getAnticipatedNumberOfArguments() > 0;
	}
	
	public boolean hasParenthesesIfNoArguments() {
		return hasParenthesesIfNoArgs;
	}
	
	public String toString() {
		return renderer.getTemplate();
	}
}
