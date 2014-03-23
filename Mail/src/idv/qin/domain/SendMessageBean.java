package idv.qin.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SendMessageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 发送时间 */
	public Date sendDate;
	
	/** 收件人地址 */
	public String receiveAddress;
	
	/** 发件人地址 */
	public String sendAddress;
	
	/** 抄送地址 */
	public String sendCCAddress;
	
	/** 暗送地址 */
	public String sendBCCAddress;
	
	/** 邮件主题  */
	public String subject;
	
	/** 邮件内容   */
	public String content;
	
	/** 附件资源  */
	public List<AttachBean> attachBeans; //附件资源
	
	/** 内嵌资源  */
	public List<AttachBean> innerAttachBeans; // 内嵌资源

}
