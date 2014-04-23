package idv.qin.mail;

import idv.qin.db.DBHelperManager;
import idv.qin.utils.CacheManager;
import idv.qin.utils.InputMethodUtil;
import idv.qin.utils.OutAnimationUtil;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Application;

/**
 * custom application to configuration external jar file -- ImageLoaderConfiguration
 * @author qinge
 *
 */
public class MyApplication extends Application {

	private static DBHelperManager dbHelperManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoaderConfiguration();
		loadOutAnimation();
		acceptInputMethodManager();
		initCacheFilePath();
		initDataBase();
	}

	
	private void initDataBase() {
		// TODO Auto-generated method stub
		dbHelperManager = new DBHelperManager(getApplicationContext());
	}


	/**
	 * 获取数据库操作管理实例 可直接调用该对象存储或修改数据
	 * @return
	 */
	public static DBHelperManager getDbHelperManager() {
		return dbHelperManager;
	}


	private void initImageLoaderConfiguration() {
		// File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "idv_qin");
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 1) // default
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(13) // default
		        .discCache(new UnlimitedDiscCache(cacheDir)) // default
		        .discCacheSize(50 * 1024 * 1024)
		        .discCacheFileCount(100)
		        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
		        .imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		        .build();
		ImageLoader.getInstance().init(config);
	}
	
	private void loadOutAnimation(){
		OutAnimationUtil.loadAnimation(getApplicationContext());
	}
	
	private void acceptInputMethodManager() {
		InputMethodUtil.getInputMethodManager(getApplicationContext());
	}
	
	private void initCacheFilePath() {
		CacheManager cacheManager = CacheManager.getDefalutInstance();
		
		File receiver_mail_folder = new File(StorageUtils.getCacheDirectory(getApplicationContext()), "/receiver_folder");
		if(!receiver_mail_folder.exists()){
			receiver_mail_folder.mkdirs();
		}
		cacheManager.setReceiver_mail_folder(receiver_mail_folder);
		
		File send_mail_folder =  new File(StorageUtils.getCacheDirectory(getApplicationContext()), "/send_folder");
		if(!send_mail_folder.exists()){
			send_mail_folder.mkdirs();
		}
		cacheManager.setSend_mail_folder(send_mail_folder);
		
		
		File extras_folder = new File(StorageUtils.getCacheDirectory(getApplicationContext()), "/extras_folder");
		if(!extras_folder.exists()){
			extras_folder.mkdirs();
		}
		cacheManager.setExtras_folder(extras_folder);
		
		File user_info_folder = new File(StorageUtils.getCacheDirectory(getApplicationContext()), "/user_info");
		if(!user_info_folder.exists()){
			user_info_folder.mkdirs();
		}
		cacheManager.setUser_info_dir(user_info_folder);
		
		cacheManager.setCache_dir(StorageUtils.getCacheDirectory(getApplicationContext()));
	}
	
}
