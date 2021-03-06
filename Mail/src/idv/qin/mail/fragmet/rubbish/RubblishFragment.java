package idv.qin.mail.fragmet.rubbish;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import idv.qin.core.RubblishBxoService;
import idv.qin.domain.MailMessageBean;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.CommonUtil;
import idv.qin.view.SwipeDismissListView;
import idv.qin.view.SwipeDismissListView.OnDismissCallback;

/**
 * 删除邮件暂存目录
 * @author qinge
 *
 */
public class RubblishFragment extends BaseFragment implements OnDismissCallback,OnClickListener {

	public static String RUBBLISH_FRAGMENT_FLAG = "RubblishFragment";
	private List<MailMessageBean> beans;
	private SwipeDismissListView dismissListView;
	private RubblishBxoService service ;
	private LayoutInflater inflater;
	private Button button_back;
	private Button button_ok;
	private ViewGroup emptyContainer;
	
	private boolean refreshLocal = false; // 编辑模式下局部刷新数据(编辑按钮点击时候改变状态)
	private int clickedPosition = -1; // 编辑模式下点击的条目位置
	
	private int start = 0;
	private int end = 20; // 默认先抓取20条数据
	
	private BaseAdapter adapter = null;
	
	private Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOCAL_LOAD_SUCCESS:
				beans = (List<MailMessageBean>) msg.obj;
				if(beans != null && beans.size() > 0){
					if(adapter == null){
						adapter = new RubblishAdapter();
						dismissListView.setAdapter(adapter);
					}
					
				}else{
					RubblishFragment.super.addEmptyView(emptyContainer);
				}
				
				break;

			default:
				break;
			}
		}
		
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		service = new RubblishBxoService(mainActivity, handler);
		inflater = mainActivity.getLayoutInflater();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.rubblish_fragment_layout, container, false);
		initComponent();
		service.getPagingData(start, end);
		return rootView;
	}

	private void initComponent() {
		emptyContainer = (ViewGroup) rootView.findViewById(R.id.rubblish_empty_container);
		dismissListView = (SwipeDismissListView) rootView.findViewById(R.id.rubblish_fragment_swip_dismiss_listview);
		dismissListView.setOnDismissCallback(this);
		button_ok = (Button) rootView.findViewById(R.id.head_bar_ok);
		button_ok.setVisibility(View.INVISIBLE);
		button_back = (Button) rootView.findViewById(R.id.head_bar_back);
		button_back.setOnClickListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	/**
	 * 滑动删除回调方法
	 */
	@Override
	public void onDismiss(int dismissPosition) {
		if(beans != null && beans.get(dismissPosition) != null){
			service.deleteMessage(beans.get(dismissPosition));
			beans.remove(dismissPosition);
		}
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	
	private final class RubblishAdapter extends BaseAdapter{

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
				convertView = inflater.inflate(R.layout.generic_item_layout, null);
				
				holder.subjectView = (TextView) convertView.findViewById(R.id.generic_item_subject_view);
				holder.sendAddressView = (TextView) convertView.findViewById(R.id.generic_item_send_address);
				holder.extraView = (LinearLayout) convertView.findViewById(R.id.generic_item_extra_view);
				holder.receiveTimeView = (TextView) convertView.findViewById(R.id.generic_item_receive_time_view);
				
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
			
		}
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_back:
			backPrevPage(R.id.rubblish_main_area);
			break;

		default:
			break;
		}
	}

}
