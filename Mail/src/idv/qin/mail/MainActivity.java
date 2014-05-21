package idv.qin.mail;

import idv.qin.gcm.CommonUtilities;
import idv.qin.gcm.ServerUtilities;
import idv.qin.mail.fragmet.HomeFragment;
import idv.qin.mail.fragmet.StartFragment;
import idv.qin.mail.fragmet.inbox.InboxFragment;
import idv.qin.mail.fragmet.sendbox.SendBoxFragment;
import idv.qin.utils.VersionManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	private View view;
	public  Handler handler = new Handler();
	private static final String TAG = "MailActivity";
	public static final int MAIN_AREA = R.id.main_content_area;
	public static final int EXTRA_AREA = R.id.extra_content_area;
	private AsyncTask<Void, Void, Void> mRegisterTask;
	
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
		regiestGCM();
	}
	
	private void regiestGCM() {
		// TODO Auto-generated method stub
		// Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            // Automatically registers application on startup.
            GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        boolean registered =
                                ServerUtilities.register(context, regId);
                        // At this point all attempts to register with the app
                        // server failed, so we need to unregister the device
                        // from GCM - the app will try to register again when
                        // it is restarted. Note that GCM will send an
                        // unregistered callback upon completion, but
                        // GCMIntentService.onUnregistered() will ignore it.
                        if (!registered) {
                            GCMRegistrar.unregister(context);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
		
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