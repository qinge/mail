package idv.qin.adapter;

import idv.qin.doamin.ContactsBean;
import idv.qin.mail.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter implements SectionIndexer {

	private List<ContactsBean> list = null;
	private Context context;
	private LayoutInflater inflater;
	
	
	public SortAdapter(List<ContactsBean> list, Context context) {
		this.list = list;
		this.context = context;
		if(this.context != null){
			inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
	}
	
	/** 
     * 当ListView数据发生变化时,调用此方法来更新ListView 
     * @param list 
     */  
    public void updateListView(List<ContactsBean> list){ 
    	if(this.list != null && list != null){
    		this.list.clear();
    		this.list.addAll(list);
    	}
        notifyDataSetChanged();  
    }  

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(inflater == null ){
			return null;
		}
		ViewHolder viewHolder = null;  
        final ContactsBean mContent = list.get(position);  
        if (convertView == null) {  
            viewHolder = new ViewHolder();  
            convertView = inflater.inflate(R.layout.sort_list_item, null);  
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);  
            viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);  
            convertView.setTag(viewHolder);  
        } else {  
            viewHolder = (ViewHolder) convertView.getTag();  
        }  
        
        //根据position获取分类的首字母的char ascii值  
        int section = getSectionForPosition(position);
        
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现  
        if(position == getPositionForSection(section)){
        	viewHolder.tvLetter.setVisibility(View.VISIBLE);  
            viewHolder.tvLetter.setText(mContent.sortLetters);  
        }else{  
            viewHolder.tvLetter.setVisibility(View.GONE);  
        }  
        
        viewHolder.tvTitle.setText(this.list.get(position).name); 
        
		return convertView;
	}
	
	final static class ViewHolder {  
        TextView tvLetter;  
        TextView tvTitle;  
    }  

	@Override
	public Object[] getSections() {
		return null;
	}

	 /** 
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置 
     */  
	@Override
	public int getPositionForSection(int section) {
		for(int i = 0 ; i < getCount() ; i++){
			String sortStr = list.get(i).sortLetters;
			char firstChar = sortStr.toUpperCase().charAt(0);
			if(firstChar == section){
				return i;
			}
		}
		return -1;
	}

	 /** 
     * 根据ListView的当前位置获取分类的首字母的char ascii值 
     */ 
	@Override
	public int getSectionForPosition(int position) {
		return list.get(position).sortLetters.charAt(0);
	}

}
