package idv.qin.mail.fragmet.write;

import idv.qin.adapter.NewMailExtraAdapter;
import idv.qin.core.SendMailService;
import idv.qin.domain.AttachBean;
import idv.qin.domain.ContactsBean;
import idv.qin.domain.ExtraType;
import idv.qin.domain.SendMessageBean;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.mail.fragmet.contacts.ContactsFragment;
import idv.qin.utils.ExtraTypeUtil;
import idv.qin.utils.InputMethodUtil;
import idv.qin.utils.MyBuildConfig;
import idv.qin.view.AttachFragment;
import idv.qin.view.AttachFragment.ExtraedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class WriteMailFragment extends BaseFragment implements View.OnClickListener ,
		OnFocusChangeListener, ExtraedListener{
	public static final String WRITE_MAIL_FRAGMENT_FLAG = "WriteMailFragment";
	

	private Button sendButton;
	private Button goBackButton;
	private EditText receive_address;
	private ImageButton address_button;
	private EditText cc_receive_address;
	private ImageButton cc_address_button;
	private EditText bcc_receive_address;
	private ImageButton bcc_address_button;
	private EditText sendAddress;
	private EditText subject;
	private EditText mailContent;
	private ImageButton extra_button;
	private LinearLayout ccLayout;
	private LinearLayout bccLayout;
	private GridView gridView;
	private NewMailExtraAdapter adapter;
	
	private List<AttachBean> attachBeans;  //
	private List<AttachBean> innerAttachBeans; // 
	
	/*** \\------ 以下字段用于联系人界面选中某个人后到编辑信息界面接收参数用-------\\ */
	private String address = "";
	private String ccAddress = "";
	private String bccAddress = "";
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
		attachBeans = new ArrayList<AttachBean>();
		innerAttachBeans = new ArrayList<AttachBean>();
		AttachBean attachBean = new AttachBean();
		attachBean.extraType = ExtraType.IMAGE;
		attachBean.path = "/mnt/sdcard2/aa.jpg";
		attachBean.name = "aa.jpg";
		innerAttachBeans.add(attachBean);
		
		Bundle bundle = getArguments();
		if(bundle != null ){
			if(bundle.containsKey("address")){
				address = bundle.getString("address");
			}
			if(bundle.containsKey("ccAddress")){
				ccAddress = bundle.getString("ccAddress");
			}
			if(bundle.containsKey("bccAddress")){
				bccAddress = bundle.getString("bccAddress");
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.write_mail_fragment, container, false);
		initComponent(currentView);
		processTouchEvent(currentView);
		initAdapter();
		return currentView;
	}



	private void initComponent(View currentView) {
		if(currentView == null){
			return ;
		}
		
		sendButton = (Button) currentView.findViewById(R.id.head_bar_ok);
		if(sendButton != null)
			sendButton.setOnClickListener(this);
		
		goBackButton = (Button) currentView.findViewById(R.id.head_bar_back);
		if(goBackButton != null)
			goBackButton.setOnClickListener(this);
		
		receive_address = (EditText) currentView.findViewById(R.id.write_mail_receive_address);
		if(receive_address != null)
			receive_address.setOnFocusChangeListener(this);
		address_button = (ImageButton) currentView.findViewById(R.id.write_mail_receive_address_button);
		if(address_button != null)
			address_button.setOnClickListener(this);
		
		ccLayout = (LinearLayout) currentView.findViewById(R.id.cc_layout);
		bccLayout = (LinearLayout) currentView.findViewById(R.id.bcc_layout);
		
		cc_receive_address = (EditText) currentView.findViewById(R.id.write_mail_cc_receive_address);
		if(cc_receive_address != null)
			cc_receive_address.setOnFocusChangeListener(this);
		cc_address_button = (ImageButton) currentView.findViewById(R.id.write_cc_address_select);
		if(cc_address_button != null)
			cc_address_button.setOnClickListener(this);
		
		bcc_receive_address = (EditText) currentView.findViewById(R.id.write_mail_bcc_receive_address);
		if(bcc_receive_address != null)
			bcc_receive_address.setOnFocusChangeListener(this);
		bcc_address_button = (ImageButton) currentView.findViewById(R.id.write_bcc_address_select);
		if(bcc_address_button != null)
			bcc_address_button.setOnClickListener(this);
		
		sendAddress = (EditText) currentView.findViewById(R.id.write_mail_send_address);
		if(sendAddress != null)
			sendAddress.setOnFocusChangeListener(this);
		
		subject = (EditText) currentView.findViewById(R.id.write_mail_theme);
		if(subject != null)
			subject.setOnFocusChangeListener(this);
		
		mailContent = (EditText) currentView.findViewById(R.id.write_mail_content);
		if(mailContent != null)
			mailContent.setOnFocusChangeListener(this);
		
		extra_button = (ImageButton) currentView.findViewById(R.id.write_mail_extra_button);
		if(extra_button != null)
			extra_button.setOnClickListener(this);
		
		gridView = (GridView) currentView.findViewById(R.id.write_mail_extras_gridview);
		
	}
	
	private void processTouchEvent(View currentView) {
		if(currentView == null){
			return ;
		}
		currentView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	
	private void initAdapter() {
		adapter = new NewMailExtraAdapter(attachBeans, mainActivity);
		if(adapter != null){
			gridView.setAdapter(adapter);
		}
	}
	
	
	
	
	
	private void processRemoveEvent(int location) {
		attachBeans.remove(location);
		adapter.notifyDataSetChanged();
		/*if(adapter != null){
			adapter.refreshData(attachBeans);
		}*/
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_ok:
			sendMail();
			break;
		case R.id.head_bar_back:
			goBack();
			break;
		case R.id.write_mail_receive_address_button:
			jumpContactsFragment(1);
			break;
		case R.id.write_cc_address_select:
			jumpContactsFragment(2);
			break;
		case R.id.write_bcc_address_select:
			jumpContactsFragment(3);
			break;
		case R.id.write_mail_extra_button:
			InputMethodUtil.hideInputMethod(v);
			addAttachment();
			break;
			
		}
		
	}



	private void sendMail() {
		String receive_addr = receive_address.getText().toString();
		if(receive_addr == null || "".equals(receive_addr.trim())){
			return ;
		}
		String content = mailContent.getText().toString();
		if(content == null || "".equals(content)){
			if((attachBeans == null ||attachBeans.size() == 0)
					&& (innerAttachBeans == null || innerAttachBeans.size() == 0) ){
				return ;
			}
		}
		SendMessageBean messageBean = new SendMessageBean();
		messageBean.sendDate = new Date(System.currentTimeMillis());
		messageBean.receiveAddress = receive_addr;
		messageBean.sendAddress = sendAddress.getText().toString();
		messageBean.sendCCAddress = cc_receive_address.getText().toString();
		messageBean.sendBCCAddress = bcc_receive_address.getText().toString();
		messageBean.subject = subject.getText().toString();
		messageBean.content = content;
		messageBean.innerAttachBeans = innerAttachBeans;
		messageBean.attachBeans = attachBeans;
		
		/**--------- by sendMailService send mail ------------*/
		SendMailService mailService = new SendMailService();
		
		try {
			mailService.sendMessage(messageBean);
		} catch (Exception e) {
			Toast.makeText(mainActivity, "send fail", Toast.LENGTH_SHORT).show();
		}
		
		goBack();
	}

	
	private void goBack() {
		backPrevPage(R.id.write_mail_main_area);
	}
	
	private void addAttachment() {
		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
		AttachFragment fragment = new AttachFragment();
		fragment.setOnExtraedListener(this);
		transaction.add(MainActivity.MAIN_AREA,fragment, AttachFragment.ATTACH_FRAGMENT_FLAG);
		transaction.addToBackStack(AttachFragment.ATTACH_FRAGMENT_FLAG);
		transaction.commit();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		hideAllWidgetButton();
		
		switch (v.getId()) {
		case R.id.write_mail_receive_address:
			if(v.isFocused()){
				address_button.setVisibility(View.VISIBLE);
				hideCCAndBCCEdit();
			}
			break;
		case R.id.write_mail_cc_receive_address:
			if(v.isFocused()){
				cc_address_button.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.write_mail_bcc_receive_address:
			if(v.isFocused()){
				bcc_address_button.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.write_mail_send_address:
			if(v.isFocused()){
				showCCAndBCCEdit();
			}
			break;
		case R.id.write_mail_theme:
			if(v.isFocused()){
				 hideCCAndBCCEdit();
			}
			break;
		case R.id.write_mail_content:
			if(v.isFocused()){
				hideCCAndBCCEdit();
			}
			break;

		}
		
	}

	private void hideAllWidgetButton() {
		if(address_button != null)
			address_button.setVisibility(View.INVISIBLE);
		if(cc_address_button != null)
			cc_address_button.setVisibility(View.INVISIBLE);
		if(bcc_address_button != null)
			bcc_address_button.setVisibility(View.INVISIBLE);
	}
	
	
	private void hideCCAndBCCEdit(){
		if(ccLayout != null && bccLayout != null &&
				(cc_receive_address.getText().toString().equals("")) && bcc_receive_address.getText().toString().equals("") ){
			ccLayout.setVisibility(View.GONE);
			bccLayout.setVisibility(View.GONE);
		}
	}
	private void showCCAndBCCEdit(){
		if(ccLayout != null && bccLayout != null){
			ccLayout.setVisibility(View.VISIBLE);
			bccLayout.setVisibility(View.VISIBLE);
		}
	}

	int i = 1;
	@Override
	public void onExtraed(String path) {
		if(path != null && !"".equals(path)){
			AttachBean attachBean = new AttachBean();
			attachBean.path = path;
			attachBean.name = path.substring(path.lastIndexOf("/")+1);
			attachBean.extraType = ExtraTypeUtil.parseExtraType(path);
			attachBeans.add(attachBean);
			if(attachBeans.size() > 3 * i){
				MarginLayoutParams layoutParams =  (MarginLayoutParams) gridView.getLayoutParams();
				layoutParams.height +=  230 *( displayMetrics.density / 1.5) ;
				gridView.setLayoutParams(layoutParams);
				i++;
			}
			adapter.notifyDataSetChanged();
		}
		if(MyBuildConfig.DEBUG){
			System.out.println(attachBeans);
		}
	}
	
	
	/**
	 * 点击添加收件人等按钮时候跳到联系人界面 选择联系人，回填地址
	 * @param type  1 ： 收件人地址， 2 ： cc 地址，3 : bcc 地址
	 */
	private void jumpContactsFragment(int type){
		InputMethodUtil.hideInputMethod(currentView);
		ContactsFragment fragment = new ContactsFragment();
		fragment.setPrgFragment(WriteMailFragment.this);
		Bundle args = new Bundle();
		args.putInt("textViewType", type);
		fragment.setArguments(args);
		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.fade_in , R.anim.fade_out);
		transaction.add(MainActivity.MAIN_AREA, fragment,TAG);
		transaction.addToBackStack(TAG);
		transaction.commit();
	}
	
	/**
	 * 
	 * @param mailAddress
	 */
	public void setMailAddress(String mailAddress){
		address = mailAddress;
		receive_address.setText(address);
	}
	
	public void setCCMailAddress(String ccMailAddress){
		ccAddress = ccMailAddress;
		cc_receive_address.setText(ccAddress);
	}
	
	public void setBCCMailAddress(String bccMailAddress){
		bccAddress = bccMailAddress;
		bcc_receive_address.setText(bccAddress);
	}
	
}
