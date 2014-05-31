package idv.qin.mail.fragmet.extras;

import idv.qin.core.ExtrasService;
import idv.qin.domain.AttachBean;
import idv.qin.domain.ExtraType;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.MyBuildConfig;
import idv.qin.utils.MyLog;
import idv.qin.view.SwipeDismissListView;
import idv.qin.view.SwipeDismissListView.OnDismissCallback;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExtrasFragment extends BaseFragment implements OnClickListener, OnDismissCallback,
		OnItemClickListener{

	public static final String EXTRAS_FRAGMENT_FLAG = "ExtrasFragment";
	private SwipeDismissListView dismissListView;
	private BaseAdapter adapter;
	private Button button_back;
	private Button button_ok;
	private ExtrasService service;
	private List<AttachBean> attachBeans;
	private LayoutInflater inflater;
	
	private Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == REMOTE_LOAD_SUCCESS){
				attachBeans = (List<AttachBean>) msg.obj;
				adapter = new MyAdapter();
				dismissListView.setAdapter(adapter);
			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = mainActivity.getLayoutInflater();
		service = new ExtrasService(mainActivity, handler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.extras_fragment_layout, container,false);
		initComponent();
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		service.loadExtras();
	}

	private void initComponent() {
		dismissListView = (SwipeDismissListView) rootView.findViewById(R.id.extras_box_swip_dismiss_list_view);
		dismissListView.setOnDismissCallback(this);
		dismissListView.setOnItemClickListener(this);
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
	public void onDismiss(int dismissPosition) {
		service.deleteExtra(attachBeans.get(dismissPosition));
		attachBeans.remove(dismissPosition);
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_bar_back:
			backPrevPage(R.id.extras_box_main_area);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AttachBean attachBean = attachBeans.get(position);
		if(MyBuildConfig.DEBUG){
			MyLog.e(TAG, attachBean.name);
		}
		if(attachBean.extraType == ExtraType.IMAGE){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(attachBean.path)), "image/*");
			startActivity(intent);
		}else if(attachBean.extraType == ExtraType.AUDIO){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(attachBean.path)), "audio/*");
			startActivity(intent);
		}else if(attachBean.extraType == ExtraType.VIDEO){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(attachBean.path)), "video/*");
			startActivity(intent);
			
		}else if(attachBean.extraType == ExtraType.ZIP){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(attachBean.path)), "application/zip");
			startActivity(intent);
			
		}else if(attachBean.extraType == ExtraType.TXT){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(attachBean.path)), "text/*");
			startActivity(intent);
		}else if(attachBean.extraType == ExtraType.EXE){
			Toast.makeText(mainActivity, getResources().getString(R.string.extras_format_error), 0).show();
		}else if(attachBean.extraType == ExtraType.NONE){
			Toast.makeText(mainActivity, getResources().getString(R.string.extras_format_error), 0).show();
		}
	}

	
	private final class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return attachBeans != null ? attachBeans.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return  attachBeans != null ? attachBeans.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AttachBean attachBean = attachBeans.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.extras_fragment_item, null);
				holder.imageView = (ImageView) convertView.findViewById(R.id.extras_item_image);
				holder.textView = (TextView) convertView.findViewById(R.id.extras_item_text);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			inflaterViewHolder(holder, attachBean);
			return convertView;
		}

	}
	
	private void inflaterViewHolder(ViewHolder holder, AttachBean attachBean) {
		holder.textView.setText(attachBean.name);
		
		if(attachBean.extraType == ExtraType.IMAGE){
			holder.imageView.setImageResource(R.drawable.icon_attach_image_small);
		}else if(attachBean.extraType == ExtraType.AUDIO){
			holder.imageView.setImageResource(R.drawable.icon_attach_audio_small);
		}else if(attachBean.extraType == ExtraType.VIDEO){
			holder.imageView.setImageResource(R.drawable.icon_attach_video_mini);
			
		}else if(attachBean.extraType == ExtraType.ZIP){
			holder.imageView.setImageResource(R.drawable.icon_attach_compress_small);
			
		}else if(attachBean.extraType == ExtraType.TXT){
			holder.imageView.setImageResource(R.drawable.icon_attach_txt_small);
			
		}else if(attachBean.extraType == ExtraType.EXE){
			holder.imageView.setImageResource(R.drawable.chat_balloon_break);
			
		}else if(attachBean.extraType == ExtraType.NONE){
			holder.imageView.setImageResource(R.drawable.chat_balloon_break);
		}
	}
	
	private final class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}
}
