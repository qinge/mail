package idv.qin.mail.fragmet;

import idv.qin.core.ReceiveMailService;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.utils.FlickerAnimatorUtil;
import idv.qin.utils.RoundBitmapUtil;
import idv.qin.widget.SlideHolder;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

	private static SlideHolder mSlideHolder;
	public static final String HOME_FRAGMENT_TAG = "HomeFragment";
	private ImageView user_head_ican;
	private ImageButton openMenuButton;
	private ImageButton writeMailButton;
	private ImageView inbox_ican; // 收件箱
	private ImageView send_mail_ican; // 发件箱
	private ImageView draft_box_icon; // 草稿箱
	private ImageView contacts_icon; // 联系人
	private ImageView weather_icon; // 天气
	private ImageView rubbish_icon; // 垃圾箱
	private ImageView extras_icon; // 附件
	private ImageView black_list_icon; // 黑名单

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater
				.inflate(R.layout.home_fragment, container, false);
		mSlideHolder = (SlideHolder) currentView.findViewById(R.id.slideHolder);
		initComponent();
		return currentView;
	}

	private void initComponent() {
		user_head_ican = (ImageView) currentView
				.findViewById(R.id.user_head_ican);
		Bitmap bm = RoundBitmapUtil.toRoundBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.aa));
		user_head_ican.setImageBitmap(bm);
		openMenuButton = (ImageButton) currentView
				.findViewById(R.id.open_menu_ican);
		openMenuButton.setOnClickListener(this);
		writeMailButton = (ImageButton) currentView
				.findViewById(R.id.btn_write_mail);
		writeMailButton.setOnClickListener(this);
		inbox_ican = (ImageView) currentView.findViewById(R.id.inbox_icon);
		inbox_ican.setOnClickListener(this);
		send_mail_ican = (ImageView) currentView
				.findViewById(R.id.send_mail_ican);
		send_mail_ican.setOnClickListener(this);
		draft_box_icon = (ImageView) currentView
				.findViewById(R.id.draft_box_icon);
		draft_box_icon.setOnClickListener(this);
		contacts_icon = (ImageView) currentView
				.findViewById(R.id.contacts_icon);
		contacts_icon.setOnClickListener(this);
		weather_icon = (ImageView) currentView.findViewById(R.id.weather_icon);
		weather_icon.setOnClickListener(this);
		rubbish_icon = (ImageView) currentView.findViewById(R.id.rubbish_icon);
		rubbish_icon.setOnClickListener(this);
		extras_icon = (ImageView) currentView.findViewById(R.id.extras_icon);
		extras_icon.setOnClickListener(this);
		black_list_icon = (ImageView) currentView
				.findViewById(R.id.black_list_icon);
		black_list_icon.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * this method provider other fragment call 
	 */
	public static void closeMenu(){
		if(mSlideHolder != null){
			if(mSlideHolder.isOpened()){
				mSlideHolder.close();
			}
		}
	}
	@Override
	public void onPause() {
		super.onPause();
	}

	public void onpenMenu() {
		mSlideHolder.toggle();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.open_menu_ican:
			mSlideHolder.toggle();
			break;
		case R.id.btn_write_mail:
			startWriteMail();
			break;
		case R.id.inbox_icon:
			turn2InboxFragment();
			break;
		case R.id.send_mail_ican:
			turn2SendBoxFragment();
			break;
		case R.id.draft_box_icon: //草稿箱
			FlickerAnimatorUtil.flickerView(draft_box_icon, null);
			break;
		case R.id.contacts_icon:
			FlickerAnimatorUtil.flickerView(contacts_icon, null);
			startContactsFragment();
			break;
		case R.id.weather_icon:
			turn2WeatherFragment();
			break;
		case R.id.rubbish_icon: 

			break;
		case R.id.extras_icon:

			break;
		case R.id.black_list_icon:

			break;
		}
	}




	private void turn2WeatherFragment() {
		
		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
		WeatherFragment fragment = new WeatherFragment();
		transaction.setCustomAnimations(R.anim.fade_in , R.anim.fade_out);
		transaction.replace(MainActivity.MAIN_AREA, fragment,WeatherFragment.WEATHER_FRAGMENT_FLAG);
		transaction.addToBackStack(WeatherFragment.WEATHER_FRAGMENT_FLAG);
		transaction.commit();
	}

	private void startWriteMail() {
		FragmentTransaction transaction = mainActivity.getFragmentManager()
				.beginTransaction();
		WriteMailFragment fragment = new WriteMailFragment();
		transaction.setCustomAnimations(R.anim.fade_in , R.anim.fade_out);
		transaction.add(MainActivity.MAIN_AREA, fragment,
				WriteMailFragment.WRITE_MAIL_FRAGMENT_FLAG);
		transaction.addToBackStack(WriteMailFragment.WRITE_MAIL_FRAGMENT_FLAG);
		transaction.commit();
	}

	private void turn2InboxFragment() {
		
		
		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
		InboxFragment fragment = new InboxFragment();
		//transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
		transaction.setCustomAnimations(R.anim.fade_in , R.anim.fade_out);
		transaction.add(MainActivity.MAIN_AREA, fragment,InboxFragment.INBOX_FRAGMENT_TAG);
		transaction.addToBackStack(InboxFragment.INBOX_FRAGMENT_TAG);
		transaction.commit();
		//Animation  animation = AnimationUtils.loadAnimation(mainActivity, R.anim.left_anim_out);
		//currentView.findViewById(R.id.slideHolder).startAnimation(animation);
	}
	
	 
	private void turn2SendBoxFragment() {
		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
		Fragment fragment = new SendBoxFragment();
		transaction.setCustomAnimations(R.anim.fade_in , R.anim.fade_out);
		transaction.add(MainActivity.MAIN_AREA, fragment,SendBoxFragment.SENDBOX_FRAGMENT_TAG);
		transaction.addToBackStack(SendBoxFragment.SENDBOX_FRAGMENT_TAG);
		transaction.commit();
	}
	
	private void startContactsFragment() {
		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
		Fragment fragment = new ContactsFragment();
		transaction.setCustomAnimations(R.anim.fade_in , R.anim.fade_out);
		transaction.add(MainActivity.MAIN_AREA, fragment,ContactsFragment.CONTACTS_FRAGMENT_TAG);
		transaction.addToBackStack(ContactsFragment.CONTACTS_FRAGMENT_TAG);
		transaction.commit();
	}

}
