package idv.qin.utils;

import android.os.Handler;
import android.os.Message;

public abstract class HandlerUtil extends Handler{
	/*WeakReference<Activity> reference = null;

	public HandlerUtil(Activity activity) {
		reference = new WeakReference<Activity>(activity);
	}*/

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if(msg.what == 200){
			hookSuccess(msg);
		}else if(msg.what == 500){
			hookFail(msg);
		}
	}
	
	/**
	 * 当结果正确时候调用此方法 并设置 message.what == 200 
	 */
	public abstract void hookSuccess(Message msg); 
	/**
	 * 当结果失败时候调用  并设置 message.what == 500 
	 */
	public abstract void hookFail(Message msg); 
		
}