package lx.easydb.dialect.function;

import java.util.ArrayList;
import java.util.List;

import lx.easydb.IConnectionFactory;

public class TemplateRenderer {
	private final String template;
	private final String[] chunks;
	private final int[] paramIndexes;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TemplateRenderer(String template) {
		this.template = template;

		List chunkList = new ArrayList();
		List paramList = new ArrayList();
		StringBuffer chunk = new StringBuffer(10);
		StringBuffer index = new StringBuffer(2);

		for (int i = 0; i < template.length(); ++i) {
			char c = template.charAt(i);
			if (c == '?') {
				chunkList.add(chunk.toString());
				chunk.delete(0, chunk.length());

				while (++i < template.length()) {
					c = template.charAt(i);
					if (Character.isDigit(c)) {
						index.append(c);
					} else {
						chunk.append(c);
						break;
					}
				}

				paramList.add(Integer.valueOf(index.toString()));
				index.delete(0, index.length());
			} else {
				chunk.append(c);
			}
		}

		if (chunk.length() > 0) {
			chunkList.add(chunk.toString());
		}

		chunks = (String[]) chunkList.toArray(new String[chunkList.size()]);
		paramIndexes = new int[paramList.size()];
		for (int i = 0; i < paramIndexes.length; ++i) {
			paramIndexes[i] = ((Integer) paramList.get(i)).intValue();
		}
	}

	public String getTemplate() {
		return template;
	}

	public int getAnticipatedNumberOfArguments() {
		return paramIndexes.length;
	}

	public String render(@SuppressWarnings("rawtypes") List args, IConnectionFactory factory) {
		int numberOfArguments = args.size();
		if (getAnticipatedNumberOfArguments() > 0 && numberOfArguments != getAnticipatedNumberOfArguments()) {
			// log.warn(
			// "Function template anticipated {} arguments, but {} arguments encountered",
			// getAnticipatedNumberOfArguments(), numberOfArguments );
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < chunks.length; ++i) {
			if (i < paramIndexes.length) {
				final int index = paramIndexes[i] - 1;
				final Object arg = index < numberOfArguments ? args.get(index) : null;
				if (arg != null) {
					buf.append(chunks[i]).append(arg);
				}
			} else {
				buf.append(chunks[i]);
			}
		}
		return buf.toString();
	}
}
