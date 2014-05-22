package idv.qin.mail.fragmet.inbox;

import idv.qin.core.ReceiveMailService;
import idv.qin.domain.MailMessageBean;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.refresh.PullToRefreshBase.OnRefreshListener;
import idv.qin.utils.CommonUtil;
import idv.qin.utils.MyBuildConfig;
import idv.qin.utils.MyLog;
import idv.qin.utils.PreferencesManager;
import idv.qin.view.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 该类的设计思路 打开后直接从指定的文件夹中读取数据显示。 下拉刷新时候跟新文件夹中数据 然后重新加载。程序首次进入该页面的时候需要自动去加载一次
 * @author qinge
 *
 */
public class InboxFragment extends BaseFragment implements View.OnClickListener{
	
	public static final int ONES_LADE_COUNT = 20;
	private int inbox_message_count = 0; // 由 数据提供类赋值 用于分页
	private ListView listView;
//	private SwipeDismissListView listView;
	private PullToRefreshListView mPullRefreshListView;
	private Button buttonOk;
	private Button buttonBack;
	private LayoutInflater inflater;
	
	private LinearLayout editContainer;
	private Button selectAllButton;
	private Button deleteButton;
	
	private List<MailMessageBean> beans;
	private BaseAdapter adapter;
	
	private ReceiveMailService service;
	/** 模式字段 代表是否编辑 */
	private boolean isEditMode = false; 
	private boolean isRefreshing = false;
	
	public static final String INBOX_FRAGMENT_TAG = "InboxFragment";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale(System.getProperty("user.language", "en")));
	private boolean [] ids = null; // 当适配器生成后初始化 new boolean [adapter.getCount]
	private boolean refreshLocal = false; // 编辑模式下局部刷新数据(编辑按钮点击时候改变状态)
	private int clickedPosition = -1; // 编辑模式下点击的条目位置
	private boolean isAllSelected = false; // 是否已经全选 如果是 再次点击全选取消选择
	
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
					List<MailMessageBean> messageBeans = (ArrayList<MailMessageBean>) msg.obj;
					if(messageBeans == null || messageBeans.size() <= 0){
						return ;
					}else{
						if(beans == null || beans.size() <= 0 && adapter== null){
							beans = messageBeans;
							progressDialog.cancel();
							bindListViewAdapter();
						}else{
							/** 这里应该使用缓冲队列 否则可能数据还没写入完成就要去读取 造成数据不全*/
							// 将数据写回到缓存文件夹中 并通知数据改变刷新界面数据
							service.saveMailMessageBeans(beans);
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
		service = new ReceiveMailService(mainActivity, handler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.inbox_fragment, container, false);
		
		initComponent();
		
		// Set a listener to be invoked when the list should be refreshed.
		processRefreshAction();
		
		return rootView;
	}
	
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try {
			progressDialog.show();
			if(PreferencesManager.getInstance(mainActivity).getValue("isFirst").equalsIgnoreCase("true")
					|| "".equals(PreferencesManager.getInstance(mainActivity).getValue("isFirst"))){
				service.getHeadMessage(0, ONES_LADE_COUNT);
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
		mPullRefreshListView = (PullToRefreshListView)rootView.findViewById(R.id.pull_refresh_list);
		listView = (ListView) mPullRefreshListView.getRefreshableView();
		listView.setOnItemClickListener(new MyItemClickListener());
		buttonOk = (Button) rootView.findViewById(R.id.head_bar_ok);
		buttonOk.setText(getResources().getString(R.string.inbox_okbutton_text));
		buttonOk.setOnClickListener(this);
		buttonBack = (Button) rootView.findViewById(R.id.head_bar_back);
		buttonBack.setOnClickListener(this);
		
		editContainer = (LinearLayout) rootView.findViewById(R.id.inbox_edit_container);
		selectAllButton = (Button) rootView.findViewById(R.id.inbox_edit_select_all_button);
		selectAllButton.setOnClickListener(this);
		deleteButton = (Button) rootView.findViewById(R.id.inbox_edit_delete_button);
		deleteButton.setOnClickListener(this);
	}
	
	private void processRefreshAction() {
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if(isEditMode){
					// 在编辑模式下下拉刷新直接返回
					mPullRefreshListView.onRefreshComplete();
					return ; 
				}
				// Do work to refresh the list here.
//				new GetDataTask().execute();
				if(service != null){
					try {
						isRefreshing = true;
						service.getHeadMessage(0, ONES_LADE_COUNT);
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
					// 退出编辑模式
					editContainer.setVisibility(View.GONE);
					selectAllButton.setText(getResources().getString(R.string.inbox_edit_selectall));
					buttonOk.setText(getResources().getString(R.string.inbox_okbutton_text));
					isEditMode = false;
					isAllSelected = false;
					if(adapter != null){
						adapter.notifyDataSetChanged();
					}
					//需要再界面改变后改变 refreshLocal
					refreshLocal = false;
					resetIds();
				}else{
					// 进入编辑模式
					editContainer.setVisibility(View.VISIBLE);
					buttonOk.setText(getResources().getString(R.string.inbox_okbutton_text_cancel));
					isEditMode = true;
					if(adapter != null){
						adapter.notifyDataSetChanged();
					}
				}
				break;
			case R.id.head_bar_back:
				backPrevPage(R.id.inbox_main_area);
				break;
			case R.id.inbox_edit_select_all_button:
				if(isAllSelected){
					isAllSelected = false;
					processAllSelectButtonClick(false);
					selectAllButton.setText(getResources().getString(R.string.inbox_edit_selectall));
				}else{
					isAllSelected = true;
					processAllSelectButtonClick(true);
					selectAllButton.setText(getResources().getString(R.string.inbox_edit_cancle_selectall));
				}
				break;
			case R.id.inbox_edit_delete_button:
				isAllSelected = false;
				deleteSelectedItem();
				break;
		}
	}



	private void deleteSelectedItem() {
		refreshLocal = false;
		clickedPosition = -1;
		List<MailMessageBean> tempList = new ArrayList<MailMessageBean>();
		// 1 将集合中的数据删除
		for(int i =0 ; i < ids.length ; i++){
			if(ids[i]){
				tempList.add(beans.get(i));
			}
		}
		
		// 2 重置 ids = new boolean [collection.size()];
		for(int i =0 ;i < tempList.size(); i++){
			beans.remove(tempList.get(i));
		}
		
		ids = null;
		ids = new boolean [beans.size()];
		if(MyBuildConfig.DEBUG){
			MyLog.e(TAG, "beans.size= "+beans.size());
		}
						
		
		// 3 刷新界面
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
		
		
		// 4 删除本地数据
		if(service != null && tempList.size() > 0){
			service.deleteLocalData(tempList);
		}
		// 5 删除服务器数据
	}

	
	private void processAllSelectButtonClick(boolean b ) {
		refreshLocal = false;
		clickedPosition = -1;
		for(int i =0 ; i < ids.length; i++){
			ids[i] = b;
		}
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	private void resetIds() {
		for(int i =0 ; i < ids.length ;i++){
			if(ids[i])
				ids[i] = false;
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
	private final class LocalMessageHeadLoader extends AsyncTask<Void, Integer, List<MailMessageBean>>{

		@Override
		protected List<MailMessageBean> doInBackground(Void... params) {
			return service.loadLocalMailMessageBeans();
		}

		@Override
		protected void onPostExecute(List<MailMessageBean> result) {
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
		/*listView.setOnDismissCallback(new OnDismissCallback() {
			
			@Override
			public void onDismiss(int dismissPosition) {
				beans.remove(adapter.getItem(dismissPosition));
				adapter.notifyDataSetChanged();
			}
		});
		*/
		ids = new boolean [adapter.getCount()];
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
			boolean refresh = true;// 初始值为 true 保证getview正确加载数据
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.inbox_item_layout, null);
				
				holder.checkboxContiner = (LinearLayout) convertView.findViewById(R.id.inbox_item_edit_view);
				holder.inCheckedView = (Button) convertView.findViewById(R.id.inbox_item_checked_view);
				holder.isReadView =  convertView.findViewById(R.id.inbox_item_isread_flag);
				holder.subjectView = (TextView) convertView.findViewById(R.id.inbox_item_subject_view);
				holder.sendAddressView = (TextView) convertView.findViewById(R.id.inbox_item_send_address);
				holder.extraView = (LinearLayout) convertView.findViewById(R.id.inbox_item_extra_view);
				holder.receiveTimeView = (TextView) convertView.findViewById(R.id.inbox_item_receive_time_view);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			if(refreshLocal){
				refresh = clickedPosition == position ? true : false; // 编辑模式点击条目 position 不匹配的不用改变状态直接返回convertView
			}
			if(refresh){
				inflateViewData(beans.get(position), holder,position);
			}
			return convertView;
		}

	}
	
	
	private void inflateViewData(MailMessageBean mailMessageBean, ViewHolder holder, int position) {
		if(CommonUtil.isEmpty(mailMessageBean.mailHead.subject)){
			holder.subjectView.setText(getResources().getString(R.string.inbox_nosubject_text));
		}else{
			holder.subjectView.setText(mailMessageBean.mailHead.subject);
		}
		
		holder.sendAddressView.setText(mailMessageBean.mailHead.from);
		
		if(mailMessageBean.mailHead.haveExtras){
			holder.extraView.setVisibility(View.VISIBLE);
		}else{
			holder.extraView.setVisibility(View.INVISIBLE);
		}
		holder.receiveTimeView.setText(dateFormat.format(mailMessageBean.mailHead.sendDate));
		if(isEditMode){
			holder.checkboxContiner.setVisibility(View.VISIBLE);
			if(ids[position]){
				holder.inCheckedView.setBackgroundResource(R.drawable.checkbox_checked_red);
			}else{
				holder.inCheckedView.setBackgroundResource(R.drawable.checkbox);
			}
		}else{
			holder.checkboxContiner.setVisibility(View.GONE);
		}
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
		public LinearLayout extraView;
		public TextView receiveTimeView;
		
		/**
		 * 是否阅读过的 rootView
		 */
		public View isReadView;
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
			if(MyBuildConfig.DEBUG)
				MyLog.e(TAG, "position= "+position);
			if(isEditMode){
				//需要再界面改变后改变 refreshLocal
				refreshLocal = true;
				clickedPosition = position;
				// 勾选条目并且
				if(ids[position]){
					ids[position] = false;
				}else{
					ids[position] = true;
				}
				if(adapter != null){
					adapter.notifyDataSetChanged();
				}
				refreshLocal = false; // 该次的点击事件界面刷新完成后一定要设置 refreshLocal = false; 否则滚动时候数据错乱
			}else{
				// 跳转到邮件详细界面
			}
		}
		
	}
	
	
}
