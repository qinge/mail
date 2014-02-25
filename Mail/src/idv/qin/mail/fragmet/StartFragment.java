package idv.qin.mail.fragmet;

import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.utils.MyLog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;


public class StartFragment extends BaseFragment {
	private final String TAG = "StartFragment";
	private static ScheduledExecutorService service;
	private Runnable runnable;
	private Handler handler;
	private ImageView image_1;
	private ImageView image_2;
	private ImageView image_3;
	private ScaleAnimation animation;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
		handler = mainActivity.getHandler();
		initAnimation();
		service = Executors.newScheduledThreadPool(1);
		runnable = new ResolvePageRunnable();
		handler.postDelayed(runnable, 2500);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		currentView =  inflater.inflate(R.layout.start_welcome_fragment, container, false);
		initComponent();
		startSchedule();
		return currentView;
	}

	private void initComponent() {
		image_1 = (ImageView) currentView.findViewById(R.id.start_state_image_1);
		image_2 = (ImageView) currentView.findViewById(R.id.start_state_image_2);
		image_3 = (ImageView) currentView.findViewById(R.id.start_state_image_3);
	}

	private void startSchedule() {
		service.scheduleAtFixedRate(new ChangeStartStateImageTask(), 
				800,
				250, 
				TimeUnit.MILLISECONDS
			);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	private final class ResolvePageRunnable implements Runnable{

		@Override
		public void run() {
			service.shutdownNow();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			LoginFragment loginFragment = new LoginFragment();
			transaction.replace(MainActivity.MAIN_AREA, loginFragment);
			transaction.commit();
		}
	}

	private final class ChangeStartStateImageTask implements Runnable{

		@Override
		public void run() {
			MyLog.i("", "----currentThread----"+Thread.currentThread().getName());
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					upDateStateImage();
				}
			});
		}
		
	}
	
	private static int count = 1;
	private void upDateStateImage() {
		if(count == 1){
			count = 2;
			image_1.startAnimation(animation);
		}else if(count == 2){
			count = 3;
			image_2.startAnimation(animation);
		}else if(count == 3){
			count = 1;
			image_3.startAnimation(animation);
		}
	}
	
	private void initAnimation(){
		animation = new ScaleAnimation(1f,1.6f,1f,1.6f,
				Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		animation.setDuration(150);
	}
}
