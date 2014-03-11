package lx.easydb.dialect.function;

import lx.easydb.Types;

/**
 * A {@link ISQLFunction} implementation that emulates the ANSI SQL trim function
 * on dialects which do not support the full definition.  However, this function
 * definition does assume the availability of ltrim, rtrim, and replace functions
 * which it uses in various combinations to emulate the desired ANSI trim()
 * functionality.
 *
 * @author Steve Ebersole
 */
public class AnsiTrimEmulationFunction extends AbstractAnsiTrimEmulationFunction {
	public static final String LTRIM = "ltrim";
	public static final String RTRIM = "rtrim";
	public static final String REPLACE = "replace";
	public static final String SPACE_PLACEHOLDER = "${space}$";

	public static final String LEADING_SPACE_TRIM_TEMPLATE = LTRIM + "(?1)";
	public static final String TRAILING_SPACE_TRIM_TEMPLATE = RTRIM + "(?1)";
	public static final String BOTH_SPACE_TRIM_TEMPLATE = LTRIM + "(" + RTRIM + "(?1))";
	public static final String BOTH_SPACE_TRIM_FROM_TEMPLATE = LTRIM + "(" + RTRIM + "(?2))"; //skip the FROM keyword in params

	/**
	 * A template for the series of calls required to trim non-space chars from the beginning of text.
	 * <p/>
	 * NOTE : essentially we:</ol>
	 * <li>replace all space chars with the text '${space}$'</li>
	 * <li>replace all the actual replacement chars with space chars</li>
	 * <li>perform left-trimming (that removes any of the space chars we just added which occur at the beginning of the text)</li>
	 * <li>replace all space chars with the replacement char</li>
	 * <li>replace all the '${space}$' text with space chars</li>
	 * </ol>
	 */
	public static final String LEADING_TRIM_TEMPLATE =
			REPLACE + "(" +
				REPLACE + "(" +
					LTRIM + "(" +
						REPLACE + "(" +
							REPLACE + "(" +
								"?1," +
								"' '," +
								"'" + SPACE_PLACEHOLDER + "'" +
							")," +
							"?2," +
							"' '" +
						")" +
					")," +
					"' '," +
					"?2" +
				")," +
				"'" + SPACE_PLACEHOLDER + "'," +
				"' '" +
			")";

	/**
	 * A template for the series of calls required to trim non-space chars from the end of text.
	 * <p/>
	 * NOTE: essentially the same series of calls as outlined in {@link #LEADING_TRIM_TEMPLATE} except that here,
	 * instead of left-trimming the added spaces, we right-trim them to remove them from the end of the text.
	 */
	public static final String TRAILING_TRIM_TEMPLATE =
			REPLACE + "(" +
				REPLACE + "(" +
					RTRIM + "(" +
						REPLACE + "(" +
							REPLACE + "(" +
								"?1," +
								"' '," +
								"'" + SPACE_PLACEHOLDER + "'" +
							")," +
							"?2," +
							"' '" +
						")" +
					")," +
					"' '," +
					"?2" +
				")," +
				"'" + SPACE_PLACEHOLDER + "'," +
				"' '" +
			")";

	/**
	 * A template for the series of calls required to trim non-space chars from both the beginning and the end of text.
	 * <p/>
	 * NOTE: again, we have a series of calls that is essentially the same as outlined in {@link #LEADING_TRIM_TEMPLATE}
	 * except that here we perform both left (leading) and right (trailing) trimming.
	 */
	public static final String BOTH_TRIM_TEMPLATE =
			REPLACE + "(" +
				REPLACE + "(" +
					LTRIM + "(" +
						RTRIM + "(" +
							REPLACE + "(" +
								REPLACE + "(" +
									"?1," +
									"' '," +
									"'" + SPACE_PLACEHOLDER + "'" +
								")," +
								"?2," +
								"' '" +
							")" +
						")" +
					")," +
					"' '," +
					"?2" +
				")," +
				"'" + SPACE_PLACEHOLDER + "'," +
				"' '" +
			")";

	private final ISQLFunction leadingSpaceTrim;
	private final ISQLFunction trailingSpaceTrim;
	private final ISQLFunction bothSpaceTrim;
	private final ISQLFunction bothSpaceTrimFrom;

	private final ISQLFunction leadingTrim;
	private final ISQLFunction trailingTrim;
	private final ISQLFunction bothTrim;

	/**
	 * Constructs a new AnsiTrimEmulationFunction using {@link #LTRIM}, {@link #RTRIM}, and {@link #REPLACE}
	 * respectively.
	 *
	 * @see #AnsiTrimEmulationFunction(String,String,String)
	 */
	public AnsiTrimEmulationFunction() {
		this( LTRIM, RTRIM, REPLACE );
	}

	/**
	 * Constructs a <tt>trim()</tt> emulation function definition using the specified function calls.
	 *
	 * @param ltrimFunctionName The <tt>left trim</tt> function to use.
	 * @param rtrimFunctionName The <tt>right trim</tt> function to use.
	 * @param replaceFunctionName The <tt>replace</tt> function to use.
	 */
	public AnsiTrimEmulationFunction(String ltrimFunctionName, String rtrimFunctionName, String replaceFunctionName) {
		leadingSpaceTrim = new SQLFunctionTemplate(
				Types.VARCHAR,
				LEADING_SPACE_TRIM_TEMPLATE.replaceAll( LTRIM, ltrimFunctionName )
		);

		trailingSpaceTrim = new SQLFunctionTemplate(
				Types.VARCHAR,
				TRAILING_SPACE_TRIM_TEMPLATE.replaceAll( RTRIM, rtrimFunctionName )
		);

		bothSpaceTrim = new SQLFunctionTemplate(
				Types.VARCHAR,
				BOTH_SPACE_TRIM_TEMPLATE.replaceAll( LTRIM, ltrimFunctionName )
						.replaceAll( RTRIM, rtrimFunctionName )
		);

		bothSpaceTrimFrom = new SQLFunctionTemplate(
				Types.VARCHAR,
				BOTH_SPACE_TRIM_FROM_TEMPLATE.replaceAll( LTRIM, ltrimFunctionName )
						.replaceAll( RTRIM, rtrimFunctionName )
		);

		leadingTrim = new SQLFunctionTemplate(
				Types.VARCHAR,
				LEADING_TRIM_TEMPLATE.replaceAll( LTRIM, ltrimFunctionName )
						.replaceAll( RTRIM, rtrimFunctionName )
						.replaceAll( REPLACE,replaceFunctionName )
		);

		trailingTrim = new SQLFunctionTemplate(
				Types.VARCHAR,
				TRAILING_TRIM_TEMPLATE.replaceAll( LTRIM, ltrimFunctionName )
						.replaceAll( RTRIM, rtrimFunctionName )
						.replaceAll( REPLACE,replaceFunctionName )
		);

		bothTrim = new SQLFunctionTemplate(
				Types.VARCHAR,
				BOTH_TRIM_TEMPLATE.replaceAll( LTRIM, ltrimFunctionName )
						.replaceAll( RTRIM, rtrimFunctionName )
						.replaceAll( REPLACE,replaceFunctionName )
		);
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISQLFunction resolveBothSpaceTrimFunction() {
		return bothSpaceTrim;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISQLFunction resolveBothSpaceTrimFromFunction() {
		return bothSpaceTrimFrom;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISQLFunction resolveLeadingSpaceTrimFunction() {
		return leadingSpaceTrim;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISQLFunction resolveTrailingSpaceTrimFunction() {
		return trailingSpaceTrim;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISQLFunction resolveBothTrimFunction() {
		return bothTrim;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISQLFunction resolveLeadingTrimFunction() {
		return leadingTrim;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISQLFunction resolveTrailingTrimFunction() {
		return trailingTrim;
	}
}

