package idv.qin.mail.fragmet;

import idv.qin.adapter.NewMailExtraAdapter;
import idv.qin.core.SendMailService;
import idv.qin.domain.AttachBean;
import idv.qin.domain.ExtraType;
import idv.qin.domain.SendMessageBean;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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
	private EditText sendAddress;
	private EditText subject;
	private EditText mailContent;
	private ImageButton bcc_address_button;
	private ImageButton extra_button;
	private LinearLayout ccLayout;
	private LinearLayout bccLayout;
	private GridView gridView;
	private BaseAdapter adapter;
	
	private List<AttachBean> attachBeans;  //����
	private List<AttachBean> innerAttachBeans; // ��Ƕ��Դ
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
		mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		attachBeans = new ArrayList<AttachBean>();
		innerAttachBeans = new ArrayList<AttachBean>();
		AttachBean attachBean = new AttachBean();
		attachBean.extraType = ExtraType.IMAGE;
		attachBean.path = "/mnt/sdcard2/aa.jpg";
		attachBean.name = "aa.jpg";
		innerAttachBeans.add(attachBean);
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
		
		ccLayout = (LinearLayout) currentView.findViewById(R.id.cc_layout);
		bccLayout = (LinearLayout) currentView.findViewById(R.id.bcc_layout);
		
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
			Toast.makeText(mainActivity, "rr", 0).show();
			break;
		case R.id.write_cc_address_select:
			break;
		case R.id.write_bcc_address_select:
			break;
		case R.id.write_mail_extra_button:
			InputMethodUtil.HideInputMethod(v);
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
	
	
}
