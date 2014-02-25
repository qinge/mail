package idv.qin.adapter;

import idv.qin.mail.R;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class CustomBaseAdapter extends BaseAdapter {
	
protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	protected DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.s_icon_loading) // resource or bitmap
        .showImageForEmptyUri(R.drawable.chat_balloon_break) // resource or bitmap
        .showImageOnFail(R.drawable.chat_balloon_break) // resource or bitmap
        .resetViewBeforeLoading(false)  // default
        .delayBeforeLoading(1000)
        .cacheInMemory(true) // default
        .cacheOnDisc(true) // default
        .build();


	
	public void loadBitmap(String  pathName, ImageView imageView, int reqWidth, int reqHeight ) {
	    if (cancelPotentialWork(pathName, imageView)) {
	        final BitmapWorkerTask task = new BitmapWorkerTask(imageView, reqWidth, reqHeight);
	        final AsyncDrawable asyncDrawable =
	                new AsyncDrawable(pathName, task);
	        imageView.setImageDrawable(asyncDrawable);
	        task.execute(pathName);
	    }
	}

	
	
	public static boolean cancelPotentialWork(String  pathName, ImageView imageView) {
	    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

	    if (bitmapWorkerTask != null) {
	        final String bitmapData = bitmapWorkerTask.data;
	        if (bitmapData != pathName) {
	            // Cancel previous task
	            bitmapWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		   if (imageView != null) {
		       final Drawable drawable = imageView.getDrawable();
		       if (drawable instanceof AsyncDrawable) {
		           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		           return asyncDrawable.getBitmapWorkerTask();
		       }
		    }
		    return null;
		}
	
	
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;
	    private String data = null;
	    private int reqWidth;
	    private int reqHeight;

	    public BitmapWorkerTask(ImageView imageView, int reqWidth, int reqHeight ) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	        this.reqWidth = reqWidth;
	        this. reqHeight = reqHeight;
	    }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(String... params) {
	        data = params[0];
	        return decodeSampledBitmapFromResource(data, reqWidth, reqHeight);
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	    	
	    	if (isCancelled()) {
	            bitmap = null;
	        }

	    	
	        if (imageViewReference != null && bitmap != null) {
	        	 final ImageView imageView = imageViewReference.get();
	             final BitmapWorkerTask bitmapWorkerTask =
	                     getBitmapWorkerTask(imageView);
	             if (this == bitmapWorkerTask && imageView != null) {
	                 imageView.setImageBitmap(bitmap);
	             }

	        }
	    }
	}

	
	static class AsyncDrawable extends BitmapDrawable {
	    private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

	    public AsyncDrawable(String pathName, BitmapWorkerTask bitmapWorkerTask) {
	        bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
	    }

	    public BitmapWorkerTask getBitmapWorkerTask() {
	        return bitmapWorkerTaskReference.get();
	    }
	}

	
	
	
	public static Bitmap decodeSampledBitmapFromResource(String pathName,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(pathName , options);
	}
	

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

}
