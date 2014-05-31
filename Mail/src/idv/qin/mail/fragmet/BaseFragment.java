package idv.qin.mail.fragmet;

import java.text.SimpleDateFormat;
import java.util.Locale;

import idv.qin.db.DBHelperManager;
import idv.qin.mail.MainActivity;
import idv.qin.mail.MyApplication;
import idv.qin.mail.R;
import idv.qin.utils.InputMethodUtil;
import idv.qin.utils.MyBuildConfig;
import idv.qin.utils.MyLog;
import idv.qin.utils.OutAnimationUtil;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 基本的 Fragment 如果需要刷新前一个 Fragment 中数据时候可以 调用并覆盖<br> 
 * {@link refreshPageData }
 */
public class BaseFragment extends Fragment {

	public static final byte REMOTE_LOAD_SUCCESS = 1;
	public static final byte LOCAL_LOAD_SUCCESS = 2;
	protected MainActivity mainActivity;
	protected View rootView;
	protected String TAG;
	protected BaseFragment preFragment;
	/** 用于操作数据 可直接调用其 crud 方法*/
	protected DBHelperManager dbHelperManager = MyApplication.getDbHelperManager();
	protected DisplayMetrics displayMetrics = new DisplayMetrics();
	protected ProgressDialog progressDialog = null;
	
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	protected static DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
			.cacheOnDisc(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();

	
	protected final static MyTouchManager touchManager = new MyTouchManager();
	
	protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale(System.getProperty("user.language", "en")));
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
		mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		TAG = this.getClass().getSimpleName();
		progressDialog = new ProgressDialog(mainActivity);
		progressDialog.setMessage("please wait ...");
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(rootView != null){
			if (MyBuildConfig.DEBUG) {
				MyLog.e(TAG, "currentView.setOnTouchListener(touchManager)");
			}
			rootView.setOnTouchListener(touchManager);
		}
	}



	/**
	 * 查找 fragment 中根 view 然后添加滑出动画 然后弹出堆栈
	 * use super method 
	 */
	public void backPrevPage(int mainAreaId) {

		View view = rootView.findViewById(mainAreaId);
		if (view != null) {
			InputMethodUtil.hideInputMethod(view);
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

	/**
	 * 设置前一个页面的 Fragment 用于更新数据
	 * @param fragment
	 */
	public void setPrgFragment(BaseFragment fragment){
		preFragment = fragment;
	}

	
	public boolean customOnKeyDown(int keyCode, KeyEvent event){
		return false;
	}

	/**
	 * fragment 中 rootView touch 事件处理类 子类中既可以不用每个 rootview 都设置 ontouch 监听
	 * @author qinge
	 *
	 */
	private final static class MyTouchManager implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	
	/**
	 * 界面中没有数据时候显示一个默认的无数据的界面
	 */
	protected void addEmptyView(ViewGroup container){
		View view = mainActivity.getLayoutInflater().inflate(R.layout.empty_layout, null);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		container.addView(view, layoutParams);
	}
}
