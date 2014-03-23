package idv.qin.core;

import idv.qin.domain.SendMessageBean;
import idv.qin.utils.AuthUtil;
import idv.qin.utils.CacheManager;
import idv.qin.utils.MyBuildConfig;
import idv.qin.utils.MyLog;
import idv.qin.utils.SerializeManage;
import idv.qin.utils.SerializeManage.SubNameManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import android.os.AsyncTask;

public class SendMailService {
	private SendMailConncetionProvider conncetionProvider;
	private File send_mail_folder = CacheManager.getDefalutInstance().getSend_mail_folder();
	
	/**
	 * 锟斤拷锟斤拷锟绞硷拷
	 * @param messageBean
	 * @param address
	 * @throws Exception
	 */
	public void sendMessage(SendMessageBean messageBean) throws Exception{
		
		serializableMail(messageBean);
		
		conncetionProvider = new SendMailConncetionProvider();
		Session session = conncetionProvider.getSession();
		Address[] addresses = parseAddress(messageBean.receiveAddress);
		Message message = parseSendMessageBean(messageBean, session);
		message.setFrom(InternetAddress.parse(messageBean.sendAddress)[0]);
		message.setSubject(messageBean.subject != null ? messageBean.subject : "");
	/*	OutputStream outputStream = new FileOutputStream( new File(send_mail_folder,"aaa.eml") );
		message.writeTo(outputStream);
		outputStream.close();*/
		SendMailManager manager = new SendMailManager(message, addresses);
		manager.execute();
	}

	

	/**
	 * 锟斤拷锟斤拷锟秸硷拷锟剿碉拷址
	 * @param address
	 * @return
	 * @throws AddressException
	 */
	private Address[] parseAddress(String address) throws AddressException {
		if(address == null ){
			return null;
		}
		Address [] addresses_array = null;
		String [] addresArray = address.split(";");
		if(addresArray != null && addresArray.length > 0){
			addresses_array = new Address [addresArray.length];
			for(int i = 0; i < addresArray.length; i++){
				Address addr = new InternetAddress(addresArray[i]);
				addresses_array [i] = addr;
			}
		}
		return addresses_array;
	}
	
	/**
	 * 锟斤拷锟斤拷messagebean 为 message
	 * @param messageBean
	 * @param session
	 * @return
	 * @throws Exception
	 */
	private Message parseSendMessageBean(SendMessageBean messageBean, Session session) throws Exception {
		Message message = new MimeMessage(session);
		Multipart multipart = null;
		if(messageBean.attachBeans != null && messageBean.attachBeans.size() > 0){
			multipart = new MimeMultipart("mixed");
			parseMixedContent(multipart,messageBean);
		}else if(messageBean.innerAttachBeans != null && messageBean.innerAttachBeans.size() > 0){
			multipart = new MimeMultipart("related");
			parseRelatedContent(multipart,messageBean);
		}else {
			multipart = new MimeMultipart("alternative");
			parseAlternativeContent(multipart,messageBean);
		}
		message.setContent(multipart);
		message.saveChanges();
		return message;
	}

	/**
	 * 锟斤拷装锟斤拷锟斤拷 锟斤拷嵌图锟斤拷锟侥憋拷
	 * @param multipart
	 * @param messageBean
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 */
	private void parseMixedContent(Multipart multipart,SendMessageBean messageBean) throws UnsupportedEncodingException, MessagingException {
		//锟斤拷锟斤拷嵌图
		if(messageBean.innerAttachBeans != null && messageBean.innerAttachBeans.size() >0){
			BodyPart bodyPart = new MimeBodyPart();
			Multipart multipart_related = new MimeMultipart("related");
			parseRelatedContent(multipart_related, messageBean);
			bodyPart.setContent(multipart_related);
			multipart.addBodyPart(bodyPart);
		}else if(messageBean.content != null && !"".equals(messageBean.content)){
			parseAlternativeContent(multipart, messageBean);
		}
		for(int i=0; i<messageBean.attachBeans.size(); i++){
			File file = new File(messageBean.attachBeans.get(i).path);
			if(file.exists()){
				BodyPart bodyPart_attach_extra = new MimeBodyPart();
				DataSource source_attachment = new FileDataSource(file);
				DataHandler handler_attachment = new DataHandler(source_attachment);
				bodyPart_attach_extra.setDataHandler(handler_attachment);
				bodyPart_attach_extra.setFileName(MimeUtility.encodeText(file.getName()));
				bodyPart_attach_extra.setDisposition(BodyPart.ATTACHMENT);
				multipart.addBodyPart(bodyPart_attach_extra);
			}
		}
	}



	/**
	 * 锟斤拷装锟斤拷嵌图 锟斤拷锟侥憋拷
	 * @param multipart
	 * @param messageBean
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private void parseRelatedContent(Multipart multipart,SendMessageBean messageBean) throws MessagingException,
			UnsupportedEncodingException {
		if(messageBean.content != null && !"".equals(messageBean.content)){
			parseAlternativeContent(multipart, messageBean);
		}
		for(int i=0; i<messageBean.innerAttachBeans.size(); i++){
			BodyPart bodyPart_inner_extra = new MimeBodyPart();
			File extra = new File(messageBean.innerAttachBeans.get(i).path);
			if(extra.exists()){
				DataSource source_extra = new FileDataSource(extra);
				DataHandler handler_extra = new DataHandler(source_extra);
				bodyPart_inner_extra.setDataHandler(handler_extra);
				bodyPart_inner_extra.setHeader("Content-ID", extra.getName()); 
				bodyPart_inner_extra.setFileName(MimeUtility.encodeText(extra.getName()));
				multipart.addBodyPart(bodyPart_inner_extra);
			}
		}
	}



	/**
	 * 锟斤拷装锟侥憋拷锟斤拷锟斤拷
	 * @param multipart
	 * @param messageBean
	 * @throws MessagingException
	 */
	private void parseAlternativeContent(Multipart multipart,SendMessageBean messageBean) throws MessagingException {
		BodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(messageBean.content != null ? messageBean.content : "", "text/html;charset=utf-8");
		multipart.addBodyPart(bodyPart);
	}

	
	/** 锟斤拷锟斤拷锟绞硷拷锟斤拷锟襟到达拷锟斤拷 */
	private void serializableMail(SendMessageBean messageBean){
		SerializeManage.serializeObject(messageBean,
				send_mail_folder, 
				String.valueOf(messageBean.sendDate.getTime()),
				SubNameManager.BIN
			);
	}
	/**
	 * 锟斤拷锟竭筹拷锟叫凤拷锟斤拷锟绞硷拷
	 * @author qinge
	 *
	 */
	private final class SendMailManager extends AsyncTask<Void, Integer, Boolean>{

		private Message message; 
		private Address [] addresses;
		
		
		public SendMailManager(Message message, Address[] addresses) {
			this.message = message;
			this.addresses = addresses;
		}


		@Override
		protected Boolean doInBackground(Void... params) {
			if(message == null || addresses == null || addresses.length == 0){
				return false;
			}
			try {
				Transport transport = conncetionProvider.getSession().getTransport();
				if(transport != null){
					transport.connect(AuthUtil.getUserName(), AuthUtil.getUserPassword());
					transport.sendMessage(message, addresses);
					transport.close();
				}
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		
	}
	
	/**
	 * 锟斤拷锟斤拷锟绞硷拷锟斤拷锟斤拷锟斤拷锟结供锟斤拷
	 * @author qinge
	 *
	 */
	private static class SendMailConncetionProvider {

		private  Properties properties = new Properties();
		private Session session;
		
		public SendMailConncetionProvider() {
			properties.setProperty("mail.transport.protocol", "smtp");
			properties.setProperty("mail.host", "smtp.qq.com");
			properties.setProperty("mail.smtp.auth", "true");
			session = Session.getInstance(properties);
			if(MyBuildConfig.DEBUG){
				session.setDebug(true);
			}
		}
		
		
		/**
		 * 锟斤拷取session
		 * @return
		 * @throws NoSuchProviderException 
		 */
		public Session getSession() throws NoSuchProviderException{
			if(session != null){
				return session;
			}
			return null;
		}
	}
}
