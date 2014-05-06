package idv.qin.mail.fragmet.inbox;

import idv.qin.core.ReceiveMailService;
import idv.qin.domain.MailMessageBean;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.refresh.PullToRefreshBase.OnRefreshListener;
import idv.qin.utils.CustomHandler;
import idv.qin.view.PullToRefreshListView;
import idv.qin.view.PullToRefreshListView.OnDismissCallback;
import idv.qin.view.PullToRefreshListView.SwipeDismissListView;

import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 每次打开应用时候都需要先去请求网络更新本地数据， 打开收件箱时候直接从本地缓存目录读取
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
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
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
		
	};
	
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
		
		new LocalMessageHeadLoader().execute();
		
		return currentView;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ReceiveMailService service = new ReceiveMailService(mainActivity, new ReceiverHandler());
		try {
			service.getHeadMessage(0, 10);
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
		buttonOk = (Button) currentView.findViewById(R.id.head_bar_ok);
		buttonOk.setOnClickListener(this);
		buttonBack = (Button) currentView.findViewById(R.id.head_bar_back);
		buttonBack.setOnClickListener(this);
	}
	
	private void processRefreshAction() {
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Do work to refresh the list here.
				new GetDataTask().execute();
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
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_ok:
			
			break;
		case R.id.head_bar_back:
			backPrevPage(R.id.inbox_main_area);
			break;
		}
		
	}

	public void backPrevPage(){
		backPrevPage(R.id.inbox_main_area);
	}
	

	private class ReceiverHandler extends CustomHandler{

		@Override
		public void success(Message msg) {
			if(msg == null){
				return ;
			}
			List<MailMessageBean> messageBeans = (List<MailMessageBean>) msg.obj;
			if(messageBeans == null || messageBeans.size() <= 0){
				return ;
			}
			Toast.makeText(mainActivity, messageBeans.get(0).mailHead.uid, Toast.LENGTH_SHORT).show();
		
			
		}

		@Override
		public void fail(Message msg) {
			
		}

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
			handler.sendEmptyMessage(200);
		}
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
	
	
}
