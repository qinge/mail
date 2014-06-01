package idv.qin.mail.fragmet.draft;

import java.util.List;

import idv.qin.core.DraftSevice;
import idv.qin.domain.MailMessageBean;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.view.SwipeDismissListView;
import idv.qin.view.SwipeDismissListView.OnDismissCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * 草稿箱中的邮件信息删除的时候根据文件名。 文件名为 保存时候的 uid 这个uid 是手动设置的<br>
 * 再写邮件的时候没有发送直接返回弹出加入草稿箱的提示框 保存的时候生成一个uuid 
 * @author qinge
 *
 */
public class DraftFragment extends BaseFragment implements OnClickListener, OnDismissCallback,
		OnItemClickListener{


	public static final String DRAFT_FRAGMENT_FLAG = "DraftFragment";
	private SwipeDismissListView dismissListView;
	private BaseAdapter adapter;
	private Button button_back;
	private Button button_ok;
	private LayoutInflater inflater;
	private ViewGroup emptyContainer;
	private DraftSevice draftSevice;
	private List<MailMessageBean> mailMessageBeans;
	
	private Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == LOCAL_LOAD_SUCCESS){
				mailMessageBeans = (List<MailMessageBean>) msg.obj;
				if(mailMessageBeans != null && mailMessageBeans.size() > 0){
					
				}else{
					DraftFragment.super.addEmptyView(emptyContainer);
				}
			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = mainActivity.getLayoutInflater();
		draftSevice = new DraftSevice(mainActivity, handler);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.draft_fragment_layout, container, false);
		initComponent();
		return rootView;
	}
	
	private void initComponent() {
		emptyContainer = (ViewGroup) rootView.findViewById(R.id.draft_empty_container);
		dismissListView = (SwipeDismissListView) rootView.findViewById(R.id.draft_box_swip_dismiss_list_view);
		dismissListView.setOnDismissCallback(this);
		dismissListView.setOnItemClickListener(this);
		button_ok = (Button) rootView.findViewById(R.id.head_bar_ok);
		button_ok.setVisibility(View.INVISIBLE);
		button_back = (Button) rootView.findViewById(R.id.head_bar_back);
		button_back.setOnClickListener(this);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		draftSevice.loadLocalMailMessageBeans();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// 跳转到写邮件的界面
		
		
	}


	@Override
	public void onDismiss(int dismissPosition) {
		MailMessageBean messageBean = mailMessageBeans.get(dismissPosition);
		draftSevice.deleteTempMessage(messageBean.mailHead.uid);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_back:
			backPrevPage(R.id.draft_box_main_area);
			break;

		default:
			break;
		}
	}

	
}
