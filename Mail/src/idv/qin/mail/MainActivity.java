package idv.qin.mail;

import idv.qin.mail.R;
import idv.qin.mail.fragmet.HomeFragment;
import idv.qin.mail.fragmet.StartFragment;
import idv.qin.mail.fragmet.inbox.InboxFragment;
import idv.qin.mail.fragmet.sendbox.SendBoxFragment;
import idv.qin.utils.VersionManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

	private View view;
	public  Handler handler = new Handler();
	private static final String TAG = "MailActivity";
	public static final int MAIN_AREA = R.id.main_content_area;
	public static final int EXTRA_AREA = R.id.extra_content_area;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = getLayoutInflater().inflate(R.layout.main, null);
		getWindow().setContentView(view);
		
		StartFragment fragment = null;
		if(getFragmentManager().findFragmentByTag(TAG) == null){
			fragment = new StartFragment();
		}
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(MAIN_AREA, fragment, TAG);
		transaction.commit();
	}
	
	public Handler getHandler(){
		return handler;
	}

	
	/**
	 * this can resolve exception :  can not perform this action after onsaveinstancestate
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(VersionManager.getCurrentVersion() > 11){
			// do nothing 
		}else{
			super.onSaveInstanceState(outState);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU){
			
			if(getFragmentManager().findFragmentByTag(InboxFragment.INBOX_FRAGMENT_TAG) != null){
				return true;
			}
			
			if(getFragmentManager().findFragmentByTag(HomeFragment.HOME_FRAGMENT_TAG) != null){
				HomeFragment fragment = (HomeFragment) getFragmentManager().findFragmentByTag(
						HomeFragment.HOME_FRAGMENT_TAG);
				fragment.onpenMenu();
			}
			
			
		}
		
		
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//processInboxFragmentBackEvent();
			if(getFragmentManager().findFragmentByTag(InboxFragment.INBOX_FRAGMENT_TAG) != null){
				InboxFragment fragment = (InboxFragment) getFragmentManager().findFragmentByTag(
						InboxFragment.INBOX_FRAGMENT_TAG);
				fragment.backPrevPage();
				return true;
			}
			
			if(getFragmentManager().findFragmentByTag(SendBoxFragment.SENDBOX_FRAGMENT_TAG) != null){
				SendBoxFragment fragment = (SendBoxFragment) getFragmentManager().findFragmentByTag(
						SendBoxFragment.SENDBOX_FRAGMENT_TAG);
				fragment.backPrevPage();
				return true;
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}

	private void processInboxFragmentBackEvent() {
		
	}
}