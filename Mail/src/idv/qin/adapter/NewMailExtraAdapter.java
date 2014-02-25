package idv.qin.adapter;

import idv.qin.doamin.AttachBean;
import idv.qin.doamin.ExtraType;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;

import java.io.File;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NewMailExtraAdapter extends CustomBaseAdapter {

	private List<AttachBean> beans;
	private LayoutInflater inflater;
	
	
	
	
	public NewMailExtraAdapter(List<AttachBean> beans,MainActivity mainActivity) {
		this.beans = beans;
		inflater = mainActivity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return beans != null ? beans.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return  beans != null ? beans.get(position) : null;
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
			convertView = initConvertView(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		AttachBean attachBean = beans.get(position);
		
		parseParams(holder, attachBean);
		
		return convertView;
	}


	private View initConvertView(ViewHolder holder) {
		View convertView;
		convertView = inflater.inflate(R.layout.write_mail_fragment_item, null);
		holder.imageView = (ImageView) convertView.findViewById(R.id.write_mail_item_image);
		holder.textView = (TextView) convertView.findViewById(R.id.write_mail_item_text);
		convertView.setTag(holder);
		return convertView;
	}
	
	private void parseParams(ViewHolder holder, AttachBean attachBean) {
		if(attachBean.extraType == ExtraType.IMAGE){
			//imageLoader.displayImage(Uri.fromFile(new File(attachBean.path)).toString(), holder.imageView, options);
			loadBitmap(attachBean.path, holder.imageView, 100, 100);
		}else if(attachBean.extraType == ExtraType.AUDIO){
			holder.imageView.setImageResource(R.drawable.icon_attach_audio_small);
		}else if(attachBean.extraType == ExtraType.ZIP){
			holder.imageView.setImageResource(R.drawable.icon_attach_compress_small);
		}else if(attachBean.extraType == ExtraType.TXT){
			holder.imageView.setImageResource(R.drawable.icon_attach_word_small);
		}else if(attachBean.extraType == ExtraType.NONE){
			holder.imageView.setImageResource(R.drawable.chat_balloon_break);
		}
		holder.textView.setText(attachBean.name);
	}

	
	private final class ViewHolder{
		public ImageView imageView;
		public TextView textView;
	}
}
