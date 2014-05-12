package idv.qin.core;

import idv.qin.domain.MailMessageBean;
import idv.qin.domain.MailMessageBean.MailHeadBean;
import idv.qin.mail.MainActivity;
import idv.qin.utils.CacheManager;
import idv.qin.utils.MyBuildConfig;
import idv.qin.utils.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import android.os.AsyncTask;
import android.os.Handler;

import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sun.mail.pop3.POP3Folder;

public class ReceiveMailService {
	
	public static final String RECEIVEMAIL_SERVICE_FLAG  = "ReceiveMailService";
	private LinkedList<MailMessageBean> beans = new LinkedList<MailMessageBean>();
	private MainActivity mainActivity;
	private Handler handler;
	
	
	public ReceiveMailService(MainActivity mainActivity, Handler handler) {
		this.mainActivity = mainActivity;
		this.handler = handler;
	}


	public void getHeadMessage(int start , int end) throws Exception{
		ReceiverMailManager mailManager = new ReceiverMailManager(start, end);
		mailManager.execute();
	}
	
	private final class ReceiverMailManager extends AsyncTask<Void, Integer, Void>{
		private  Properties props;
		private  Folder folder;
		private POP3Folder pop3Folder;
		private  Session session;
		private  Store store;
		private  int start;
		private  int end;
		

		public ReceiverMailManager(int start, int end) throws Exception {
			this.start = start;
			this.end = end;
			props = new Properties();
			props.setProperty("mail.store.protocol", "pop3");
			props.setProperty("mail.host", "pop.qq.com");
			session = Session.getInstance(props);
			if(MyBuildConfig.DEBUG)
				session.setDebug(true);
		}



		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.currentThread().sleep(3000);
				store = session.getStore();
				store.connect("pop.qq.com", "1241304515@qq.com", "005440054400");
				folder = store.getFolder("inbox");
				if(!folder.isOpen())
					folder.open(Folder.READ_ONLY);
				
				int message_count = folder.getMessageCount();
				if(end > message_count){
					end = message_count;
				}
				if(start <= 0 ){
					start = 1;
				}
				Message[] messages = folder.getMessages(start, end);
				if(messages == null || messages.length <= 0 ){
					return null;
				}
				pop3Folder= (com.sun.mail.pop3.POP3Folder)store.getFolder("inbox");
				pop3Folder.open(Folder.READ_ONLY);
				for(int i= 0; i< messages.length ;i++){
					MailMessageBean bean = new MailMessageBean();
					bean.mailHead = parseMessageHead(pop3Folder, (MimeMessage)messages[i]);
					beans.addLast(bean);
				}
				handler.obtainMessage(200, beans).sendToTarget();
				new Thread(new ReceiveMailService.SaveMessageHead2Disk(beans)).start();
			} catch (Exception e) {
				MyLog.d(RECEIVEMAIL_SERVICE_FLAG, e != null ? e.getMessage()+"" : "ReceiveMailService-->ReceiverMailManager" +
						"-->doInBackground()");
			}finally{
				try {
					if(pop3Folder != null){
						pop3Folder.close(false);
					}
					if(folder != null){
						folder.close(false);
					}
					if(store != null){
						store.close();
					}
				} catch (MessagingException e) {
				} // 不删除打了删除标记的有邮件
			}
			
			return null;
		}



		/**
		 * 
		 * @param message
		 * @return
		 * @throws MessagingException 
		 * @throws UnsupportedEncodingException 
		 */
		private MailHeadBean parseMessageHead( POP3Folder pop3Folder, MimeMessage message) 
				throws MessagingException, UnsupportedEncodingException {
			if(pop3Folder== null || message == null){
				return null;
			}
			if(!pop3Folder.isOpen()){
				pop3Folder.open(Folder.READ_ONLY);
			}
			MailMessageBean.MailHeadBean headBean = new MailMessageBean.MailHeadBean();
			headBean.from = MimeUtility.decodeText(message.getFrom()[0].toString());
			headBean.sendDate = message.getSentDate();
			headBean.sendDate.toLocaleString();
			headBean.subject = message.getSubject()+"";
			headBean.uid = pop3Folder.getUID(message);
			return headBean;
		}
		
	}
	
	/**
	 * 保存邮件消息头字段到磁盘 保存路径为 收件箱/head/...
	 * @author qinge
	 *
	 */
	private final class SaveMessageHead2Disk implements Runnable{
		LinkedList<MailMessageBean> beans;
		public SaveMessageHead2Disk(LinkedList<MailMessageBean> beans) {
			this.beans = beans;
		}

		@Override
		public void run() {
			if(beans == null || beans.size() <=0 ){
				return ;
			}
			File fileDir = new File(CacheManager.getDefalutInstance().getReceiver_mail_folder(), "head");
			if(!fileDir.exists()){
				fileDir.mkdirs();
			}
			for(int i=beans.size()-1 ; i>=0; i-- ){
				File file = new File(fileDir, beans.get(i).mailHead.uid);
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
						MyLog.e(RECEIVEMAIL_SERVICE_FLAG, "ReceiveMailService -- > SaveMessage2Disk --> run()");
					}
				}
			}
			System.gc();
		}
		
	}
	
	/**
	 * 加载本地缓存 消息头字段
	 * @return
	 */
	public static LinkedList<MailMessageBean> loadLocalMessageHeads(){
		File fileDir = new File(CacheManager.getDefalutInstance().getReceiver_mail_folder(), "head");
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		File [] files = fileDir.listFiles();
		if(files != null && files.length >0){
			LinkedList<MailMessageBean> mailMessageBeans = new LinkedList<MailMessageBean>();
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
			return mailMessageBeans;
		}
		return null;
	}
	
	// 测试 Mac 上传消息
	
}
