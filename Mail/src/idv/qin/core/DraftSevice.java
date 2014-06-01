package idv.qin.core;

import idv.qin.domain.MailMessageBean;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.CacheManager;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Handler;

public class DraftSevice extends BaseService {

	private File dir =  CacheManager.getDefalutInstance().getTemp_folder();
	private Context context;
	private Handler handler;
	
	public DraftSevice(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	
	/**
	 * 加载到的数据通过 handler 发送到界面
	 */
	public void loadLocalMailMessageBeans(){
		new Thread(){

			@Override
			public void run() {
				List<MailMessageBean> mailMessageBeans = MessageManager.loadLocalMailMessageBean(dir);
				handler.obtainMessage(BaseFragment.LOCAL_LOAD_SUCCESS, mailMessageBeans).sendToTarget();
			}
			
		}.start();
	}
	
	public void saveMailMessageBeans(List<MailMessageBean> mailMessageBeans){
		MessageManager.saveMailMessageBean(mailMessageBeans, dir);
	}
	
	public boolean deleteTempMessage(String fileName){
		return MessageManager.deleteFile(dir, fileName);
	}
}
