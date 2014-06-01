package idv.qin.mail.fragmet.blacklist;

import java.util.List;

import idv.qin.core.BlackListSevice;
import idv.qin.core.DraftSevice;
import idv.qin.domain.BlackUserBean;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.view.SwipeDismissListView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

public class BlackListFragment extends BaseFragment implements OnClickListener{

	public static final String BLACK_LIST_FRAGMENT_TAG = "BlackListFragment";
	private ListView listView;
	private BaseAdapter adapter;
	private Button button_back;
	private Button button_ok;
	private LayoutInflater inflater;
	private ViewGroup emptyContainer;
	private BlackListSevice blackListSevice;
	private List<BlackUserBean> beans;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == LOCAL_LOAD_SUCCESS){
				beans = (List<BlackUserBean>) msg.obj;
				if(beans != null && beans.size() > 0){
					
				}else{
					BlackListFragment.super.addEmptyView(emptyContainer);
				}
			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = mainActivity.getLayoutInflater();
		blackListSevice = new BlackListSevice(mainActivity, handler);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.black_list_fragment_layout, container, false);
		initComponent();
		return rootView;
	}

	private void initComponent() {
		emptyContainer = (ViewGroup) rootView.findViewById(R.id.black_empty_container);
		listView = (ListView) rootView.findViewById(R.id.black_box_list_view);
		button_ok = (Button) rootView.findViewById(R.id.head_bar_ok);
		button_ok.setText(R.string.black_list_ok_button_text);
		button_back = (Button) rootView.findViewById(R.id.head_bar_back);
		button_back.setOnClickListener(this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		blackListSevice.loadBlackUserInfo();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_back:
			backPrevPage(R.id.black_box_main_area);
			break;

		default:
			break;
		}
	}

	
}
