package idv.qin.utils;

/**
 * 该类提供普通到工具方法 比如 判空等...
 * @author qinge
 *
 */
public class CommonUtil {

	private CommonUtil(){}
	
	/**
	 * 判断空
	 * @param s
	 * @return true  空 ，<br> false : 非空
	 */
	public static boolean isEmpty(String s){
		return null == s || "".equals(s.trim());
	}
}
