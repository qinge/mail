package idv.qin.mail.fragmet;

import idv.qin.doamin.ContactsBean;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 联系人 编辑
 * @author qinge
 *
 */
public class ContactsEditFragment extends BaseFragment implements OnClickListener{
	public static final String CONTACTS_EDIT_FRAGMENT_TAG = "ContactsEditFragment";
	private Button backButton;
	private Button editButton;
	private EditText nameText;
	private EditText mailText;
	private Button saveButton;
	private Button resetButton;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
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
		mailText = (EditText) currentView.findViewById(R.id.contacts_edit_address_text);
		
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
			saveContacts();
			backPrevPage(R.id.contacts_edit_main_area);
			break;
		case R.id.contacts_edit_reset_button:
			reSetEditText();
			break;

		default:
			break;
		}
		
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
	
}
