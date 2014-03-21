package idv.qin.mail.fragmet;

import idv.qin.doamin.ContactsBean;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.utils.CommonUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 联系人 编辑
 * @author qinge
 *
 */
public class ContactsEditFragment extends BaseFragment implements OnClickListener, OnFocusChangeListener{
	public static final String CONTACTS_EDIT_FRAGMENT_TAG = "ContactsEditFragment";
	private Button backButton;
	private Button editButton;
	private EditText nameText;
	private EditText mailText;
	private Button saveButton;
	private Button resetButton;
	
	/*** \\------ 以下字段用于联系人界面选中某个人后到编辑信息界面接收参数用-------\\ */
	private boolean isUpdate;
	private int id = 0;
	private String name = "";
	private String address = "";
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
		Bundle bundle = getArguments();
		if(bundle != null ){
			if(bundle.containsKey("id")){
				id = bundle.getInt("id");
			}
			if(bundle.containsKey("name")){
				name = bundle.getString("name");
			}
			if(bundle.containsKey("address")){
				address = bundle.getString("bundle");
			}
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.contacts_edit_fragment_layout, container, false);
		
		initComponent();
		
		processTouchEvent();
		
		return currentView;
	}
	
	private void initComponent() {
		backButton = (Button) currentView.findViewById(R.id.head_bar_back);
		backButton.setOnClickListener(this);
		editButton = (Button) currentView.findViewById(R.id.head_bar_ok);
		editButton.setVisibility(View.GONE);
		
		nameText = (EditText) currentView.findViewById(R.id.contacts_edit_name_text);
		nameText.setOnFocusChangeListener(this);
		if(!CommonUtil.isEmpty(name)){
			nameText.setText(name);
		}
		mailText = (EditText) currentView.findViewById(R.id.contacts_edit_address_text);
		mailText.setOnFocusChangeListener(this);
		if(!CommonUtil.isEmpty(address)){
			mailText.setText(address);
		}
		
		saveButton = (Button) currentView.findViewById(R.id.contacts_edit_save_button);
		saveButton.setOnClickListener(this);
		resetButton = (Button) currentView.findViewById(R.id.contacts_edit_reset_button);
		resetButton.setOnClickListener(this);
		
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	
	private void processTouchEvent() {
		currentView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_back:
			backPrevPage(R.id.contacts_edit_main_area);
			break;
		case R.id.contacts_edit_save_button:
			if(checkEmpty()){
				return ;
			}
			if(isUpdate){
				upDateContacts();
			}else{
				saveContacts();
			}
			backPrevPage(R.id.contacts_edit_main_area);
			break;
		case R.id.contacts_edit_reset_button:
			reSetEditText();
			break;

		default:
			break;
		}
		
	}
	
	
	private boolean checkEmpty() {
		if(CommonUtil.isEmpty(nameText.getText().toString())){
			nameText.setError("");
			return true;
		}
		if(CommonUtil.isEmpty(mailText.getText().toString()) ){
			mailText.setError("");
			return true;
		}
		return false;
	}

	private void upDateContacts() {
		ContactsBean contacts = new ContactsBean();
		contacts.id = id;
		contacts.name = nameText.getText().toString();
		contacts.mail_address = mailText.getText().toString();
		dbHelperManager.update(contacts);
	}

	
	private void saveContacts() {
		ContactsBean contacts = new ContactsBean();
		contacts.name = nameText.getText().toString();
		contacts.mail_address = mailText.getText().toString();
		dbHelperManager.insert(contacts);
	}

	private void reSetEditText(){
		nameText.setError(null);
		nameText.setText("");
		mailText.setError(null);
		mailText.setText("");
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus){
			nameText.setError(null);
			mailText.setText("");
		}
	}
	
}
