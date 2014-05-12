package idv.qin.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.mail.Flags;

/**
 * @author qinge
 *
 */
public class MailMessageBean implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MailHeadBean mailHead;
	public MailContentBean mailContent;

	/**
	 * message head bean
	 * @author qinge
	 *
	 */
	public static class MailHeadBean implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String uid; // uid
		public String from; // 发件人
		public String subject; // 主题
		public Date sendDate; // 发送时间
		public Flags flags ; // 标志
		/**
		 * 包含附件
		 */
		public boolean haveExtras;
		
		
		
		public MailHeadBean() {
		}

/*
		public MailHeadBean(String uid , String address, String subject, Date sendDate,
				Flags flags) {
			this.uid = uid;
			this.from = address;
			this.subject = subject;
			this.sendDate = sendDate;
			this.flags = flags;
		}*/
	}
	
	
	/**
	 * 包装邮件信息后的自定义邮件信息
	 * @author qinge
	 *
	 */
	public static class MailContentBean implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**   邮件内容  */
		public String content;
		/** 邮件附件  */
		public List<AttachBean> attachmentBeans;
	}

}
