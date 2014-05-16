package idv.qin.mail.fragmet.login;

import idv.qin.domain.UserInfo;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.CacheManager;
import idv.qin.utils.InputMethodUtil;
import idv.qin.view.ValidateDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LoginFragment extends BaseFragment implements OnClickListener,OnFocusChangeListener, OnItemClickListener {

	private ImageView headImage;
	private EditText userName;
	private Button userNameSpinner;
	private EditText userPassword;
	private Button userPasswordLock;
	private Button loginButton;
	private ListView userNameListView;
	private Button registerButton;
	private List<UserInfo> userInfos = new ArrayList<UserInfo>();
	private UserInfoAdapter adapter;
	private LayoutInflater inflater;
	private final static int SUCCESS = 200;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == SUCCESS){
				
			}
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = mainActivity.getLayoutInflater();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView =  inflater.inflate(R.layout.login_fragment, container, false);
		currentView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideNameListView();
				return true;
			}
		});
		initComponent();
		inflateUserInfo();
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
		adapter = new UserInfoAdapter();
		userNameListView.setAdapter(adapter);
		userNameListView.setOnItemClickListener(new UserInfoItemClickListener());
		
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
			InputMethodUtil.hideInputMethod(v);
			UserInfo user = new UserInfo();
			user.name = userName.getText().toString();
			user.password = userPassword.getText().toString();
			saveUserInfo(user);
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

	/**
	 * 从缓存文件夹中读取用户信息到集合中
	 */
	private void inflateUserInfo() {
		new Thread(){

			@Override
			public void run() {
				File userInfoDir = CacheManager.getDefalutInstance().getUser_info_dir();
				for(File file : userInfoDir.listFiles()){
					UserInfo userInfo = new UserInfo();
					InputStream input = null;
					ObjectInputStream objectInputStream = null;
					try {
						input = new FileInputStream(file);
						objectInputStream = new ObjectInputStream(input);
						userInfo = (UserInfo) objectInputStream.readObject();
						userInfos.add(userInfo);
						handler.sendEmptyMessage(SUCCESS);
					} catch (Exception e) {
					}finally{
						try {
							if(objectInputStream != null){
								objectInputStream.close();
							}
							if(input != null){
								input.close();
							}
						} catch (Exception e2) {
						}
					}
				}
			}
			
		}.start();
	}
	
	/**
	 * 保存用户信息 当用户验证通过后
	 * @param userInfo
	 */
	private void saveUserInfo(UserInfo userInfo){
		File userInfoDir = CacheManager.getDefalutInstance().getUser_info_dir();
		OutputStream stream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			File file = new File(userInfoDir, userInfo.name);
			stream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(stream);
			objectOutputStream.writeObject(userInfo);
		} catch (Exception e) {
		}finally{
			try {
				if(objectOutputStream != null){
					objectOutputStream.close();
				}
				if(stream != null){
					stream.close();
				}
			} catch (Exception e2) {
			}
		}
	}
	
	/**
	 * 用户信息适配器 数据由 {@link userInfos} 提供
	 * @author monstar
	 *
	 */
	private final class UserInfoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return userInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return userInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.login_user_info_item_layout, null);
			TextView nameView = (TextView) convertView.findViewById(R.id.login_user_name_view);
			nameView.setText(userInfos.get(position).name);
			ImageButton button = (ImageButton) convertView.findViewById(R.id.login_user_delete_view);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// 弹出确认对话框
					
					//如果 编辑框中的名字和删除的一致 清空编辑框
					if(userInfos.get(position).name.equals(userName.getText().toString())){
						userName.setText(null);
						userPassword.setText(null);
					}
					
					// 删除指定条目 和 文件
					userInfos.remove(position);
					adapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		UserInfo info = userInfos.get(position);
		userName.setText(info.name);
		userPassword.setText(info.password);
		//隐藏  NameListView
		hideNameListView();
	}
	
	private class UserInfoItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			UserInfo info = userInfos.get(position);
			userName.setText(info.name);
			userPassword.setText(info.password);
			//隐藏  NameListView
			hideNameListView();
		}
		
	}
	
	
}
