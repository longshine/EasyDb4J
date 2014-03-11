package lx.easydb.criterion;

/**
 * @author Longshine
 *
 */
public interface ISelect extends IFragment {
	String getAlias();
	boolean isDistinct();
}
