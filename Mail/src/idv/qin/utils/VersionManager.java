package idv.qin.utils;

import android.os.Build;

/**
 *  system version manager
 * @author qin
 *
 */
public class VersionManager {

	private VersionManager(){}
	
	/**
	 * obtain system version
	 * @return int {@linkplain Build.VERSION.SDK_INT}
	 */
	public static int getCurrentVersion(){
		return Build.VERSION.SDK_INT;
		
	}
}
