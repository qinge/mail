package idv.qin.mail.fragmet.sendbox;

import idv.qin.core.SendBoxService;
import idv.qin.domain.MailMessageBean;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.CommonUtil;
import idv.qin.view.SwipeDismissListView;
import idv.qin.view.SwipeDismissListView.OnDismissCallback;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SendBoxFragment extends BaseFragment implements View.OnClickListener, OnDismissCallback {

	public static final String SENDBOX_FRAGMENT_TAG = "SendBoxFragment";
	private static final int REQUEST_CODE_SETTINGS = 0;
    private SwipeDismissListView dismissListView;
    private Button button_ok;
	private Button button_back;
	private SendBoxService service;
	
	private List<MailMessageBean> beans;
	private LayoutInflater inflater;
	private BaseAdapter adapter = null;
	private int start = 0;
	private int end = 20; // 默认先抓取20条数据
	
	private Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOCAL_LOAD_SUCCESS:
				beans = (List<MailMessageBean>) msg.obj;
				if(adapter == null){
					adapter = new SendBoxAdapter();
					dismissListView.setAdapter(adapter);
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
		service = new SendBoxService(mainActivity, handler);
		inflater = mainActivity.getLayoutInflater();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.send_box_fragment, container, false);
		dismissListView = (SwipeDismissListView) rootView.findViewById(R.id.send_box_swip_dismiss_list_view);
		dismissListView.setOnDismissCallback(this);
		button_ok = (Button) rootView.findViewById(R.id.head_bar_ok);
		button_ok.setOnClickListener(this);
		button_back = (Button) rootView.findViewById(R.id.head_bar_back);
		button_back.setOnClickListener(this);
		service.getPagingData(start, end);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	

	    }

	  
	/**
	 * 滑动删除回调方法
	 */
	@Override
	public void onDismiss(int dismissPosition) {
		// TODO Auto-generated method stub
		if(beans != null && beans.get(dismissPosition) != null){
			service.deleteMessage(beans.get(dismissPosition));
			beans.remove(dismissPosition);
		}
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}

	    public int convertDpToPixel(float dp) {
	        DisplayMetrics metrics = getResources().getDisplayMetrics();
	        float px = dp * (metrics.densityDpi / 160f);
	        return (int) px;
	    }


	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        switch (requestCode) {
	            case REQUEST_CODE_SETTINGS:
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
			
			break;
		case R.id.head_bar_back:
			backPrevPage(R.id.send_box_main_area);
			break;
		}
		
	}

	public void backPrevPage(){
		backPrevPage(R.id.send_box_main_area);
	}
	
	private final class SendBoxAdapter extends BaseAdapter{

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
				convertView = inflater.inflate(R.layout.generic_item_layout, null);
				
				holder.subjectView = (TextView) convertView.findViewById(R.id.generic_item_subject_view);
				holder.sendAddressView = (TextView) convertView.findViewById(R.id.generic_item_send_address);
				holder.extraView = (LinearLayout) convertView.findViewById(R.id.generic_item_extra_view);
				holder.receiveTimeView = (TextView) convertView.findViewById(R.id.generic_item_receive_time_view);
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			inflateViewData(beans.get(position), holder,position);
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
