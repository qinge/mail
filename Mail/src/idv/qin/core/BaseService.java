package idv.qin.core;

import idv.qin.utils.MyLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public abstract class BaseService {

	protected void saveObject(File dir, String fileName, Object ...objects){
		for(int i=0  ; i< objects.length; i++ ){
			File file = new File(dir,fileName);
			if(file.exists()){
				continue;
			}
			OutputStream outputStream;
			ObjectOutputStream objectOutputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				objectOutputStream = new ObjectOutputStream(outputStream);
				objectOutputStream.writeObject(objects[i]);
			} catch (Exception e) {
			}finally{
				try {
					if(objectOutputStream != null)
						objectOutputStream.close();
				} catch (Exception e2) {
					MyLog.e("tag", "BaseService -- > BaseService --> run()");
				}
			}
		}
		System.gc();
	}
}
