package idv.qin.utils;

import java.io.File;

/**
 * 缓存的文件夹目录 在application 类中实例化并 设置各个文件夹缓存目录 其他类中直接得到该类实例后直接获取文件目录
 * @author qinge
 *
 */
public class CacheManager {
	/** 收件箱  */
	private File receiver_mail_folder = null;
	
	/** 发件箱  */
	private File send_mail_folder = null;
	
	/** 附件  */
	private File extras_folder = null;
	
	/** 缓存目录 */
	private File cache_dir = null;
	
	/** 用户信息缓存目录 */
	private File user_info_dir = null;
	
	private static CacheManager cacheManager;
	
	private CacheManager(){}
	
	public static CacheManager getDefalutInstance(){
		if(cacheManager == null){
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}
	

	public File getReceiver_mail_folder() {
		return receiver_mail_folder;
	}

	public void setReceiver_mail_folder(File receiver_mail_folder) {
		this.receiver_mail_folder = receiver_mail_folder;
	}

	public File getSend_mail_folder() {
		return send_mail_folder;
	}

	public void setSend_mail_folder(File send_mail_folder) {
		this.send_mail_folder = send_mail_folder;
	}

	public File getExtras_folder() {
		return extras_folder;
	}

	public void setExtras_folder(File extras_folder) {
		this.extras_folder = extras_folder;
	}

	public File getCache_dir() {
		return cache_dir;
	}

	public void setCache_dir(File cache_dir) {
		this.cache_dir = cache_dir;
	}

	public File getUser_info_dir() {
		return user_info_dir;
	}

	public void setUser_info_dir(File user_info_dir) {
		this.user_info_dir = user_info_dir;
	}

	
	
}
