package idv.qin.utils;

import idv.qin.doamin.SendMessageBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *  use class to serialize object
 * @author qinge
 *
 */
public class SerializeManage {
	

	private SerializeManage(){}
	/**
	 * @param   obj 
	 * @param  dir
	 * @param file_name 
	 * @param   sub_name 
	 */
	public static void serializeObject(final Object obj, final File dir, final String file_name, final SubNameManager subNameManager){
		new Thread(){

			@Override
			public void run() {
				File file = null;
				OutputStream output = null;
				ObjectOutputStream objectOutputStream = null;
				try {
					if(!dir.exists()){
						dir.mkdirs();
					}
					if(subNameManager != null ){
						file = new File(dir,file_name+"."+subNameManager.toLowerCase());
					}else{
						file = new File(dir,file_name);
					}
					output = new FileOutputStream(file);
					objectOutputStream= new ObjectOutputStream(output);
					objectOutputStream.writeObject(obj);
					objectOutputStream.flush();
				} catch (Exception e) {
					MyLog.e("SerializeManage-->serializeObject(...)", e.getMessage());
				}finally{
					try {
						if(objectOutputStream != null){
							objectOutputStream.close();
							objectOutputStream = null;
						}
						if(output != null){
							output.close();
							output = null;
						}
						file = null;
					} catch (Exception e2) {
						MyLog.e("SerializeManage-->serializeObject(...)", e2.getMessage());
					}
				}
			}
			
		}.start();
	}
	
	
	public static List<SendMessageBean> recoverMessageBean(File dir, SubNameManager sub_name){
		if(!dir.exists() || dir.listFiles() == null || dir.listFiles().length == 0){
			return null;
		}
		List<SendMessageBean> beans = new ArrayList<SendMessageBean>();
		for(File file : dir.listFiles()){
			if(file.isDirectory()){
				continue ;
			}else{
				if(file.getName().contains(sub_name.toLowerCase())){
					inflateData2Collection(file , beans);
				}
			}
		}
		return beans;
	}
	
	private static void inflateData2Collection(File file , List<SendMessageBean> beans) {
		try {
			
		} catch (Exception e) {
			MyLog.e("SerializeManage-->inflateData2Collection(...)", e.getMessage());
		}finally{
			
		}
	}

	public static enum SubNameManager{
		BIN {
			@Override
			public String toLowerCase() {
				// TODO Auto-generated method stub
				return "bin";
			}
		}, 
		OBJ {
			@Override
			public String toLowerCase() {
				// TODO Auto-generated method stub
				return "obj";
			}
		}, 
		PNG {
			@Override
			public String toLowerCase() {
				// TODO Auto-generated method stub
				return "png";
			}
		},
		JPEG {
			@Override
			public String toLowerCase() {
				// TODO Auto-generated method stub
				return "jpeg";
			}
		},
		MP3 {
			@Override
			public String toLowerCase() {
				// TODO Auto-generated method stub
				return "mp3";
			}
		},
		AVI {
			@Override
			public String toLowerCase() {
				// TODO Auto-generated method stub
				return "avi";
			}
		},
		RMVB {
			@Override
			public String toLowerCase() {
				// TODO Auto-generated method stub
				return "rmvb";
			}
		},
		NONE {
			@Override
			public String toLowerCase() {
				// TODO Auto-generated method stub
				return "none";
			}
		};
		public abstract String toLowerCase();
	}
}
