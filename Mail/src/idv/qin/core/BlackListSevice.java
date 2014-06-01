package idv.qin.core;

import idv.qin.domain.BlackUserBean;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.CacheManager;
import idv.qin.utils.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

public class BlackListSevice extends BaseService{
	private File dir = CacheManager.getDefalutInstance().getBlack_user_dir();
	private Context context;
	private Handler handler;
	public BlackListSevice(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}
	
	public void loadBlackUserInfo(){
		new Thread(){

			@Override
			public void run() {
				try {
					File [] files = dir.listFiles();
					if(files != null && files.length > 0){
						List<BlackUserBean> beans = new ArrayList<BlackUserBean>();
						for(File file : files){
							if(file.isDirectory()){
								continue;
							}else{
								ObjectInputStream objectInputStream  = null;
								try {
									FileInputStream inputStream = new FileInputStream(file);
									objectInputStream = new ObjectInputStream(inputStream);
									BlackUserBean bean = (BlackUserBean) objectInputStream.readObject();
									beans.add(bean);
								} catch (Exception e) {
								}finally{
									try {
										if(objectInputStream != null){
											objectInputStream.close();
										}
									} catch (Exception e2) {
									}
								}
							}
						}
						handler.obtainMessage(BaseFragment.LOCAL_LOAD_SUCCESS, beans).sendToTarget();
					}else{
						handler.obtainMessage(BaseFragment.LOCAL_LOAD_SUCCESS, null).sendToTarget();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		}.start();
	}

	public boolean deleteBlackUser(String userName){
		File file = new File(dir, userName);
		if(file.exists()){
			file.delete();
		}
		return false;
	}
	
	public void saveBlackUser(BlackUserBean bean){
		if(bean == null){
			return ;
		}
		OutputStream outputStream;
		ObjectOutputStream objectOutputStream = null;
		try {
			File file = new File(dir, bean.userName);
			outputStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(bean);
		} catch (Exception e) {
		}finally{
			try {
				if(objectOutputStream != null)
					objectOutputStream.close();
			} catch (Exception e2) {
				MyLog.e("tag", "ReceiveMailService -- > SaveMessage2Disk --> run()");
			}
		}
		System.gc();
	}
	
}
