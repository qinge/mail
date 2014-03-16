package idv.qin.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
/**
 * 输入法管理工具类
 * @author qinge
 *
 */
public class InputMethodUtil {
	private static InputMethodManager mInputMethodManager;  

	
	/**
	 * call this method by myapplication class init <br>
	 * it mean you don't call this method 
	 * @param context
	 */
	public static void getInputMethodManager(Context context){
		mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	/**
	 * 隐藏输入法
	 * @param v 随意传入一个 view 既可
	 */
	public static void HideInputMethod(View v){
		if(mInputMethodManager != null){
			mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}
}
