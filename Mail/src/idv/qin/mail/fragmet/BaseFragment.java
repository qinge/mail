package idv.qin.mail.fragmet;

import idv.qin.db.DBHelperManager;
import idv.qin.mail.MainActivity;
import idv.qin.mail.MyApplication;
import idv.qin.mail.R;
import idv.qin.utils.InputMethodUtil;
import idv.qin.utils.OutAnimationUtil;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 基本的 Fragment 如果需要刷新前一个 Fragment 中数据时候可以 调用并覆盖<br> 
 * {@link refreshPageData }
 */
public class BaseFragment extends Fragment {

	protected MainActivity mainActivity;
	protected View currentView;
	/** 用于操作数据 可直接调用其 crud 方法*/
	protected DBHelperManager dbHelperManager = MyApplication.getDbHelperManager();
	protected DisplayMetrics displayMetrics = new DisplayMetrics();
	
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	protected static DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
			.cacheOnDisc(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();

	/**
	 * 查找 fragment 中根 view 然后添加滑出动画 然后弹出堆栈
	 * use super method 
	 */
	public void backPrevPage(int mainAreaId) {

		View view = currentView.findViewById(mainAreaId);
		if (view != null) {
			InputMethodUtil.HideInputMethod(view);
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
	
	/**
	 *  如果需要刷新数据则覆盖该方法
	 */
	public void refreshPageData() {
	}




}
