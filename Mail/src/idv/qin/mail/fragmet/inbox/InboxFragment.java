package idv.qin.mail.fragmet.inbox;

import idv.qin.core.ReceiveMailService;
import idv.qin.core.ReceiveMailService.SaveMessageHead2Disk;
import idv.qin.domain.MailMessageBean;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.refresh.PullToRefreshBase.OnRefreshListener;
import idv.qin.utils.PreferencesManager;
import idv.qin.view.PullToRefreshListView;
import idv.qin.view.PullToRefreshListView.OnDismissCallback;
import idv.qin.view.PullToRefreshListView.SwipeDismissListView;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 该类的设计思路 打开后直接从指定的文件夹中读取数据显示。 下拉刷新时候跟新文件夹中数据 然后重新加载。程序首次进入该页面的时候需要自动去加载一次
 * @author qinge
 *
 */
public class InboxFragment extends BaseFragment implements View.OnClickListener{
	
	public static final String INBOX_FRAGMENT_TAG = "InboxFragment";
//	private ListView listView;
	private SwipeDismissListView listView;
	private PullToRefreshListView mPullRefreshListView;
	private Button buttonOk;
	private Button buttonBack;
	private LayoutInflater inflater;
	
	private LinkedList<MailMessageBean> beans;
	private BaseAdapter adapter;
	
	private ReceiveMailService service;
	/** 模式字段 代表是否编辑 */
	private boolean isEditMode = false; 
	private boolean isRefreshing = false;
	
	public static final byte REMOTE_LOAD_SUCCESS = 1;
	public static final byte LOCAL_LOAD_SUCCESS = 2;
	
	private Handler handler = new ReceiverHandler();
	
	/**
	 * 数据处理回调 handler 类
	 * @author qinge
	 *
	 */
	@SuppressLint("HandlerLeak")
	private class ReceiverHandler extends Handler{

		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case REMOTE_LOAD_SUCCESS:
					if(isRefreshing){
						mPullRefreshListView.onRefreshComplete();
						isRefreshing = false;
					}
					@SuppressWarnings("unchecked")
					LinkedList<MailMessageBean> messageBeans = (LinkedList<MailMessageBean>) msg.obj;
					if(messageBeans == null || messageBeans.size() <= 0){
						return ;
					}else{
						if(beans == null){
							beans = messageBeans;
							progressDialog.cancel();
							bindListViewAdapter();
						}else{
							// 将数据写回到缓存文件夹中 并通知数据改变刷新界面数据
							new Thread(new  SaveMessageHead2Disk(beans)).start();
							beans.clear();
							new LocalMessageHeadLoader().execute();// 重新加载本地数据
						}
					}
					break;
				case LOCAL_LOAD_SUCCESS:
					progressDialog.cancel();
					bindListViewAdapter();
					break;
	
				default:
					break;
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = mainActivity.getLayoutInflater();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.inbox_fragment, container, false);
		
		initComponent();
		
		// Set a listener to be invoked when the list should be refreshed.
		processRefreshAction();
		
		return currentView;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		service = new ReceiveMailService(mainActivity, handler);
		try {
			progressDialog.show();
			if(PreferencesManager.getInstance(mainActivity).getValue("isFirst").equalsIgnoreCase("true")
					|| "".equals(PreferencesManager.getInstance(mainActivity).getValue("isFirst"))){
				service.getHeadMessage(0, 20);
				PreferencesManager.getInstance(mainActivity).saveValue("isFirst", "false");
			}else{
				// 从本地加载数据
				new LocalMessageHeadLoader().execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		 //listView.setAdapter(adapter);
	}
	
	private void initComponent() {
		mPullRefreshListView = (PullToRefreshListView)currentView.findViewById(R.id.pull_refresh_list);
		listView = (SwipeDismissListView) mPullRefreshListView.getRefreshableView();
		listView.setOnItemClickListener(new MyItemClickListener());
		buttonOk = (Button) currentView.findViewById(R.id.head_bar_ok);
		buttonOk.setText(getResources().getString(R.string.inbox_okbutton_text));
		buttonOk.setOnClickListener(this);
		buttonBack = (Button) currentView.findViewById(R.id.head_bar_back);
		buttonBack.setOnClickListener(this);
	}
	
	private void processRefreshAction() {
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
//				new GetDataTask().execute();
				if(service != null){
					try {
						isRefreshing = true;
						service.getHeadMessage(0, 20);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onLoading() {}
			
			
		});
	}


	

	
	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Call onRefreshComplete when the list has been refreshed.
			

			super.onPostExecute(result);
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.head_bar_ok:
				if(isEditMode){
					isEditMode = false;
					// hide check box
				}else{
					isEditMode = true;
					// show check box
				}
				break;
			case R.id.head_bar_back:
				backPrevPage(R.id.inbox_main_area);
				break;
			}
	}

	public void backPrevPage(){
		backPrevPage(R.id.inbox_main_area);
	}
	

	/**
	 * 加载数据为 {@link beans} 赋值 
	 * @author qinge
	 *
	 */
	private final class LocalMessageHeadLoader extends AsyncTask<Void, Integer, LinkedList<MailMessageBean>>{

		@Override
		protected LinkedList<MailMessageBean> doInBackground(Void... params) {
			return ReceiveMailService.loadLocalMessageHeads();
		}

		@Override
		protected void onPostExecute(LinkedList<MailMessageBean> result) {
			beans = result;
			handler.sendEmptyMessage(LOCAL_LOAD_SUCCESS);
		}
	}
	
	/**
	 * 为 listviw 绑定 adapter
	 */
	private void bindListViewAdapter(){
		adapter = new CustomAdapter();
		listView.setAdapter(adapter);
		listView.setOnDismissCallback(new OnDismissCallback() {
			
			@Override
			public void onDismiss(int dismissPosition) {
				beans.remove(adapter.getItem(dismissPosition));
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private final class CustomAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return beans != null ? beans.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return beans != null ? beans.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.inbox_item_layout, null);
				holder.subjectView = (TextView) convertView.findViewById(R.id.inbox_item_subject_view);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			inflateViewData(beans.get(position), holder);
			return convertView;
		}

	}
	
	
	private void inflateViewData(MailMessageBean mailMessageBean, ViewHolder holder) {
		holder.subjectView.setText(mailMessageBean.mailHead.subject);
	}
	
	private class ViewHolder{

		/**
		 * 发件人 view
		 */
		public TextView sendAddressView;
		
		/**
		 * 主题 view
		 */
		public TextView subjectView;
		public ImageView extraView;
		public TextView receiveTimeView;
		
		/**
		 * 是否阅读过的 rootView
		 */
		public Button isReadView;
		/**
		 * 编辑模式的 rootView
		 */
		public LinearLayout checkboxContiner;
		public Button inCheckedView; // 选中与否的 view 需要改变背景色
		
	}
	
	private final class MyItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(isEditMode){
				// 勾选条目并且
			}else{
				// 跳转到邮件详细界面
			}
		}
		
	}
}
