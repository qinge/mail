package idv.qin.core;

import idv.qin.domain.AttachBean;
import idv.qin.domain.MailMessageBean;
import idv.qin.domain.MailMessageBean.MailContentBean;
import idv.qin.domain.MailMessageBean.MailHeadBean;
import idv.qin.mail.MainActivity;
import idv.qin.mail.fragmet.inbox.InboxFragment;
import idv.qin.utils.CacheManager;
import idv.qin.utils.CustomComparator;
import idv.qin.utils.ExtraTypeUtil;
import idv.qin.utils.MyBuildConfig;
import idv.qin.utils.MyLog;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import android.os.AsyncTask;
import android.os.Handler;

import com.sun.mail.pop3.POP3Folder;

public class InboxService {
	
	public static final String RECEIVEMAIL_SERVICE_FLAG  = "InboxService";
	private List<MailMessageBean> beans = new ArrayList<MailMessageBean>();
	private MainActivity mainActivity;
	private Handler handler;
	
	
	public InboxService(MainActivity mainActivity, Handler handler) {
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
				for(int i= 0; i< messages.length ;i++){
					if(!pop3Folder.isOpen()){
						pop3Folder.open(Folder.READ_ONLY);
					}
					MailMessageBean bean = new MailMessageBean();
					bean.mailHead = parseMessageHead(pop3Folder, (MimeMessage)messages[i]);
					bean.mailContent = parseMessageContent((MimeMessage)messages[i]);
					beans.add(bean);
				}
				Collections.sort(beans, new CustomComparator());
				handler.obtainMessage(InboxFragment.REMOTE_LOAD_SUCCESS, beans).sendToTarget();
				File fileDir = CacheManager.getDefalutInstance().getReceiver_mail_folder();
				MessageManager.saveMailMessageBean(beans, fileDir);
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
	
	
	private MailContentBean parseMessageContent(MimeMessage mimeMessage) {
		MailContentBean contentBean = new MailContentBean();
		try {
			contentBean.content = getContent(mimeMessage);
			System.out.println(contentBean.content);
			System.out.println("-----++++++++++------ \n");
			contentBean.attachmentBeans = parseMailAttachment(mimeMessage);
		} catch (Exception e) {
		}
		return contentBean;
	}
	
	private static String getContent(Part part)throws Exception{
		if(part.isMimeType("text/html")){
			 return part.getContent().toString();
		 }else if(part.getContent() instanceof Multipart){
			 Multipart multipart = (Multipart) part.getContent();
			 for(int i=0; i<multipart.getCount(); i++){       	 
				 String content = getContent(multipart.getBodyPart(i));
				 if(content != null) 
					 return content;
			 }
		 }
		 return null;
	 }
	
	  private static List<AttachBean> parseMailAttachment(Part part) throws Exception {
	    	List<AttachBean> attachmentBeans = new ArrayList<AttachBean>();
	    	AttachBean attachmentBean ;
	        if (part.isMimeType("multipart/*")) {   
	            Multipart mp = (Multipart) part.getContent();   
	            for (int i = 0; i < mp.getCount(); i++) {   
	                BodyPart mpart = mp.getBodyPart(i);   
	                String disposition = mpart.getDisposition();   
	                if ((disposition != null)&& ((disposition.equalsIgnoreCase(Part.ATTACHMENT)) 
	                		/*||(disposition.equalsIgnoreCase(Part.INLINE))*/)){
	                	attachmentBean = new AttachBean();
	                	attachmentBean.name = MimeUtility.decodeText(mpart.getFileName());
	                	attachmentBean.extraType = ExtraTypeUtil.parseExtraType(mpart.getContentType());
//	                	attachmentBean.path = saveAttachment2Disk(mpart);
	                	attachmentBeans.add(attachmentBean);
	                	attachmentBean = null;
	                }else if (mpart.isMimeType("multipart/*")) {   
	                	parseMailAttachment((Part) mpart);   
	                } 
	            }   
	        } else if (part.isMimeType("message/rfc822")) {   
	        	 parseMailAttachment((Part) part.getContent());   
	        }   
	        return attachmentBeans;   
	    }
	 
	
	public List<MailMessageBean> loadLocalMailMessageBeans(){
		File fileDir = CacheManager.getDefalutInstance().getReceiver_mail_folder();
		List<MailMessageBean> mailMessageBeans = MessageManager.loadLocalMailMessageBean(fileDir);
		return mailMessageBeans;
	}
	
	public void saveMailMessageBeans(List<MailMessageBean> mailMessageBeans){
		File fileDir = CacheManager.getDefalutInstance().getReceiver_mail_folder();
		MessageManager.saveMailMessageBean(mailMessageBeans, fileDir);
	}
	
	// 测试 Mac 上传消息
	
	public boolean deleteLocalData(final List<MailMessageBean> beans){
		// 1 将数据存入垃圾箱中
		File dir = CacheManager.getDefalutInstance().getRubblish_folder();
		MessageManager.saveMailMessageBean(beans, dir);
		// 2 从收件箱中删除
		try {
			new Thread(){

				@Override
				public void run() {
					File fileDir = CacheManager.getDefalutInstance().getReceiver_mail_folder();
					for(int i =0 ; i < beans.size() ; i++){
						File file = new File(fileDir, beans.get(i).mailHead.uid);
						if(file.exists()){
							file.delete();
						}
					}
				}
				
			}.start();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
