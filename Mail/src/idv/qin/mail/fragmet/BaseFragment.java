package idv.qin.mail.fragmet;

import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.utils.OutAnimationUtil;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseFragment extends Fragment {

	protected MainActivity mainActivity;
	protected View currentView;
	protected DisplayMetrics displayMetrics = new DisplayMetrics();
	
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	protected static DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
			.cacheOnDisc(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();

	/**
	 * use super method 
	 */
	public void backPrevPage(int mainAreaId) {

		View view = currentView.findViewById(mainAreaId);
		if (view != null) {
			Animation animation = OutAnimationUtil.getOutAnimation();
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mainActivity.getFragmentManager().popBackStack();
				}
			});
			if(view != null && OutAnimationUtil.getOutAnimation() != null)
				view.startAnimation(OutAnimationUtil.getOutAnimation());
		}
	}

}
