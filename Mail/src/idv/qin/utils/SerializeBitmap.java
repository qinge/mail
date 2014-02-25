package idv.qin.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 将图片保存到磁盘
 * @author qinge
 *
 */
public class SerializeBitmap {
	private SerializeBitmap (){}
	ImageLoader imageLoader = ImageLoader.getInstance();
	
	public static boolean saveBitmap2Disk(Context context, Bitmap bitmap) throws IOException{
		File file = createBitmapFile(context);
        writeBitmap2File(bitmap, file);
		return false;
	}

	

	private static File createBitmapFile(Context context) throws IOException {
		File fileDir = StorageUtils.getCacheDirectory(context);
		File imageCacheDir = new File(fileDir, "cache/image");
		if(!imageCacheDir.exists() ){
			imageCacheDir.mkdirs();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss"); // 年月日时分秒
		String fileName = format.format(new Date())+".jpg";
		File file = new File(imageCacheDir, fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		return file;
	}
	
	private static void writeBitmap2File(Bitmap bitmap, File file)
			throws FileNotFoundException, IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
	}
	
	
}
