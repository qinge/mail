package idv.qin.core;

import idv.qin.domain.MailMessageBean;
import idv.qin.utils.CustomComparator;
import idv.qin.utils.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 提供 MimeMessage 保存 恢复等方法
 * @author qinge
 *
 */
public class MessageManager {

	/**
	 * 保存数据到指定文件夹
	 * @param beans
	 * @param dir
	 */
	public static void saveMailMessageBean(List<MailMessageBean> beans, File dir){
		new Thread(new SaveMessageHead2Disk(beans, dir)).start();
	}
	
	
	/**
	 * 保存邮件消息头字段到磁盘 保存路径为
	 * @author qinge
	 *
	 */
	private static final class SaveMessageHead2Disk implements Runnable{
		private List<MailMessageBean> beans = null;
		private File fileDir = null;
		public SaveMessageHead2Disk(List<MailMessageBean> beans, File fileDir) {
			this.beans = beans;
			this.fileDir = fileDir;
		}

		@Override
		public void run() {
			if(beans == null || beans.size() <=0 ){
				return ;
			}
			
			if(!fileDir.exists()){
				fileDir.mkdirs();
			}
			for(int i=0  ; i< beans.size(); i++ ){
				File file = new File(fileDir, beans.get(i).mailHead.uid);
				if(file.exists()){
					continue;
				}
				OutputStream outputStream;
				ObjectOutputStream objectOutputStream = null;
				try {
					outputStream = new FileOutputStream(file);
					objectOutputStream = new ObjectOutputStream(outputStream);
					objectOutputStream.writeObject(beans.get(i));
				} catch (Exception e) {
				}finally{
					try {
						if(objectOutputStream != null)
							objectOutputStream.close();
					} catch (Exception e2) {
						MyLog.e("tag", "ReceiveMailService -- > SaveMessage2Disk --> run()");
					}
				}
			}
			System.gc();
		}
		
	}
	
	/**
	 * 从指定文件夹下加载 MailMessageBean 加载的数据已经根据邮件收发时间从晚到早排序过(新邮件会排再前面)
	 * @param fileDir
	 * @return
	 */
	public static List<MailMessageBean> loadLocalMailMessageBean(File fileDir){
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		File [] files = fileDir.listFiles();
		if(files != null && files.length >0){
			List<MailMessageBean> mailMessageBeans = new ArrayList<MailMessageBean>();
			for(File file : files){
				if(file.isDirectory()){
					continue ;
				}else{
					ObjectInputStream objectInputStream  = null;
					try {
						FileInputStream inputStream = new FileInputStream(file);
						objectInputStream = new ObjectInputStream(inputStream);
						MailMessageBean bean = (MailMessageBean) objectInputStream.readObject();
						mailMessageBeans.add(bean);
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
			Collections.sort(mailMessageBeans, new CustomComparator());
			return mailMessageBeans;
		}
		return null;
	}
	
	/**
	 * 删除邮件消息
	 * @return
	 */
	public static boolean deleteMessage(File dir , MailMessageBean messageBean){
		if(messageBean == null || dir == null || !dir.exists()){
			return false;
		}
		File file = new File(dir, messageBean.mailHead.uid);
		if(file.exists()){
			file.delete();
		}
		file = null;
		return true;
	}
	
	
	public static boolean deleteFile(File dir , String fileName){
		File file = new File(dir, fileName);
		if(file.exists()){
			file.delete();
			file = null;
			return true;
		}
		return false;

	}
}
