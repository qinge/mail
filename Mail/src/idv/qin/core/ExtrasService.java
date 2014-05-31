package idv.qin.core;

import idv.qin.domain.AttachBean;
import idv.qin.domain.ExtraType;
import idv.qin.domain.MailMessageBean;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.CacheManager;
import idv.qin.utils.ExtraTypeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

public class ExtrasService extends BaseService{
	private File dir =  CacheManager.getDefalutInstance().getExtras_folder();
	private Context context;
	private Handler handler;
	
	public ExtrasService(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}
	
	/**
	 * 对外暴露的加载数据的方法
	 */
	public void loadExtras(){
		new ExtrasManager().execute();
	}
	
	
	
	private final class ExtrasManager extends AsyncTask<Void, Integer, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			handler.obtainMessage(BaseFragment.REMOTE_LOAD_SUCCESS, getExtras()).sendToTarget();
			return null;
		}
		
	}

	/**
	 * 获取附件信息
	 * @return
	 */
	private List<AttachBean> getExtras(){
		File [] files = dir.listFiles();
		if(files != null && files.length > 0){
			List<AttachBean> attachBeans = new ArrayList<AttachBean>();
			for(File file : files){
				if(file.isDirectory()){
					continue ;
				}else{
					AttachBean bean = new AttachBean();
					bean.name = file.getName();
					bean.path = file.getAbsolutePath();
					bean.extraType = ExtraTypeUtil.parseExtraType(bean.path);
					attachBeans.add(bean);
				}
			}
			return attachBeans;
		}
		return null;
	}
	
	/**
	 * 删除某个附件
	 * @param attachBean
	 * @return
	 */
	public boolean deleteExtra(AttachBean attachBean){
		if(attachBean == null){
			return false;
		}
		String name = attachBean.name;
		File file = new File(dir, name);
		if(file.exists()){
			file.delete();
			return true;
		}
		file = null;
		return false;
	}
	
	public void saveExtra(AttachBean attachBean){
		if(attachBean == null){
			return ;
		}
		String name = attachBean.name+"."+attachBean.extraType;
		File file = new File(dir, name);
		if(!file.exists()){
			
		}
	}
	
	
	public void saveAttachBean(AttachBean attachBean){
		if(attachBean == null){
			return ;
		}
		String fileName = attachBean.name+"."+attachBean.extraType;
		super.saveObject(dir, fileName, attachBean);
		
	}

}
