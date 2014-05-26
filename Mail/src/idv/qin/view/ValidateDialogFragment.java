package idv.qin.view;

import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.mail.fragmet.HomeFragment;
import idv.qin.utils.PreferencesManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ValidateDialogFragment extends BaseFragment {

	public static final String VALIDATE_DIALOG_FRAGMENT_TAG = "ValidateDialogFragment";
	private TextView textView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.validate_user_dialog_fragment, container, false);
		textView = (TextView) rootView.findViewById(R.id.login_validate_dialog_text);
		textView.setText(R.string.validateing_user);
		mainActivity.getHandler().postDelayed(new startMenuRunnable(), 2000);
		return rootView;
		
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	
	private final class startMenuRunnable implements Runnable{

		@Override
		public void run() {
			if(ValidateDialogFragment.this.getFragmentManager() != null){
				PreferencesManager.getInstance(mainActivity).saveValue(PreferencesManager.IS_LOGIN, "true");
				FragmentTransaction transaction = ValidateDialogFragment.this.getFragmentManager().beginTransaction();
				HomeFragment fragment = new HomeFragment();
				ValidateDialogFragment.this.getFragmentManager().popBackStack();
				transaction.replace(MainActivity.MAIN_AREA, fragment,HomeFragment.HOME_FRAGMENT_TAG);
				transaction.commit();
			}
		}
		
	}
}
