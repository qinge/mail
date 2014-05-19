package idv.qin.core;

import idv.qin.domain.MailMessageBean;
import idv.qin.mail.MainActivity;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.CacheManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

/**
 * 垃圾箱数据提供类
 * 
 * @author qinge
 * 
 */
public class RubblishBxoService {

	private List<MailMessageBean> beans = new ArrayList<MailMessageBean>();
	private MainActivity mainActivity;
	private Handler handler;
	private  int start;
	private  int end;
	
	public RubblishBxoService(MainActivity mainActivity, Handler handler) {
		this.mainActivity = mainActivity;
		this.handler = handler;
		
	}
	
	/**
	 * 数据装载完成 handler 将会发送消息和数据
	 * @param start
	 * @param end
	 */
	public void getPagingData(int start, int end){
		this.start = start;
		this.end = end;
		new RubblishManager().execute();
	}

	private final class RubblishManager extends AsyncTask<Void, Integer, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			File fileDir = CacheManager.getDefalutInstance().getRubblish_folder();
			List<MailMessageBean> beans = MessageManager.loadLocalMailMessageBean(fileDir);
			if(handler != null){
				Message.obtain(handler, BaseFragment.LOCAL_LOAD_SUCCESS, beans).sendToTarget();
			}
			return null;
		}
		
	}
	
}
