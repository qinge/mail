package idv.qin.mail.fragmet.login;

import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.InputMethodUtil;
import idv.qin.utils.RoundBitmapUtil;
import idv.qin.view.ValidateDialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class LoginFragment extends BaseFragment implements OnClickListener,OnFocusChangeListener {

	private ImageView headImage;
	private EditText userName;
	private Button userNameSpinner;
	private EditText userPassword;
	private Button userPasswordLock;
	private Button loginButton;
	private ListView userNameListView;
	private Button registerButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView =  inflater.inflate(R.layout.login_fragment, container, false);
		initComponent();
		return currentView;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	
	private void initComponent(){
		headImage = (ImageView) currentView.findViewById(R.id.login_user_head_image);
		/*Resources res = getResources();
		Bitmap bmp = RoundBitmapUtil.toRoundBitmap(BitmapFactory.decodeResource(res, R.drawable.aa));
		headImage.setImageBitmap(bmp);*/
		
		userName = (EditText) currentView.findViewById(R.id.login_username);
		userName.setOnClickListener(this);
		userName.setOnFocusChangeListener(this);
		userNameSpinner = (Button) currentView.findViewById(R.id.login_spinner_button);
		userNameSpinner.setOnClickListener(this);
		
		userPassword = (EditText) currentView.findViewById(R.id.login_password);
		userPassword.setOnFocusChangeListener(this);
		//userPassword.setOnClickListener(this);
		userPasswordLock = (Button) currentView.findViewById(R.id.login_lock_button);
		userPasswordLock.setOnClickListener(this);
		
		loginButton = (Button) currentView.findViewById(R.id.login_action);
		loginButton.setOnClickListener(this);
		
		userNameListView = (ListView) currentView.findViewById(R.id.login_accounts_listview);
		
		registerButton = (Button) currentView.findViewById(R.id.login_register);
		registerButton.setOnClickListener(this);
	}
	
	
private static int lockClickCount = 1;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_spinner_button:
			hideOrShowNameListview(v);
			break;
			
		case R.id.login_username:
			hideNameListView();
			break;
			
		case R.id.login_lock_button:
			processIsRememberUser();
			break;
			
		case R.id.login_action:
			resolveLoginRequest();
			break;
			
		case R.id.login_register:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent .setData(Uri.parse("http://zc.qq.com/chs/index.html"));
			mainActivity.startActivity(intent);
			break;
		}
	}
	
	
	

	private void hideOrShowNameListview(View v) {
		if(userNameListView.isShown()){
			userNameListView.setVisibility(View.GONE);
			v.setBackgroundResource(R.drawable.login_more);
		}else{
			InputMethodUtil.hideInputMethod(v);
			userNameListView.setVisibility(View.VISIBLE);
			v.setBackgroundResource(R.drawable.login_more_up);
		}
	}


	private void hideNameListView() {
		userNameSpinner.setBackgroundResource(R.drawable.login_more);
		if(userNameListView.isShown())
			userNameListView.setVisibility(View.GONE);
	}


	/**
	 * if lock is lock state remember user  
	 */
	private void processIsRememberUser() {
		if(++lockClickCount % 2 == 0 ){
			userPasswordLock.setBackgroundResource(R.drawable.login_password_nor);
			lockClickCount = 0;
		}else{
			userPasswordLock.setBackgroundResource(R.drawable.login_password_sel);
		}
	}
	
	/**
	 * validate user if true turn to home page
	 */
	private void resolveLoginRequest() {
		// popup dialog loging and validate user 
		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
		ValidateDialogFragment dialogFragment = new ValidateDialogFragment();
		transaction.replace(MainActivity.EXTRA_AREA, dialogFragment,ValidateDialogFragment.VALIDATE_DIALOG_FRAGMENT_TAG);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.login_username:
			if(v.isFocused()){
				//userName.setHint("");
				if(userPassword.getText().toString().trim().equals(""))
					userPassword.setHint(getResources().getString(R.string.user_password));
				hideNameListView();
			}
			break;
		case R.id.login_password:
			if(v.isFocused()){
				//userPassword.setHint("");
				if(userName.getText().toString().trim().equals(""))
					userName.setHint(getResources().getString(R.string.user_name));
				hideNameListView();
			}
			break;
		}
	}

}
