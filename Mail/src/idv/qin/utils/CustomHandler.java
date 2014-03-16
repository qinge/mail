package idv.qin.utils;

import android.os.Handler;
import android.os.Message;

/**
 * 自定义的 Handler 的抽象子类 需要子类来继承并实现 通过 msg.what 来确定处理是否成功
 * @author qinge
 *
 */
public abstract class CustomHandler extends Handler{
	
	/**
	 * 处理成功标志
	 */
	public static final int HANDLE_SUCCESS = 200;
	/**
	 * 处理失败标志
	 */
	public static final int HANDLE_FAIL = 500;
	
	/*WeakReference<Activity> reference = null;

	public HandlerUtil(Activity activity) {
		reference = new WeakReference<Activity>(activity);
	}*/

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if(msg.what == HANDLE_SUCCESS){
			success(msg);
		}else if(msg.what == HANDLE_FAIL){
			fail(msg);
		}
	}
	
	/**
	 * 当结果正确时候调用此方法 并设置 message.what == 200 
	 */
	public abstract void success(Message msg); 
	/**
	 * 当结果失败时候调用  并设置 message.what == 500 
	 */
	public abstract void fail(Message msg); 
		
}