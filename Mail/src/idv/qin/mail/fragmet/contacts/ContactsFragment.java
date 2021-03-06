package idv.qin.mail.fragmet.contacts;

import idv.qin.adapter.SortAdapter;
import idv.qin.domain.ContactsBean;
import idv.qin.domain.SortModel;
import idv.qin.mail.MainActivity;
import idv.qin.mail.R;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.mail.fragmet.write.WriteMailFragment;
import idv.qin.sortlist.CharacterParser;
import idv.qin.sortlist.PinyinComparator;
import idv.qin.utils.InputMethodUtil;
import idv.qin.view.ClearEditText;
import idv.qin.view.SideBar;
import idv.qin.view.SideBar.OnTouchingLetterChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsFragment extends BaseFragment implements OnClickListener{
	public static final String CONTACTS_FRAGMENT_TAG = "ContactsFragment";
	private Button backButton;
	private Button editButton;
	
	private ListView sortListView;
	private SideBar sideBar;
	/** 
     * 显示字母的TextView 
     */  
    private TextView dialog;  
    private SortAdapter adapter;  
    private ClearEditText mClearEditText; 
    
  
    /** 
     * 汉字转换成拼音的类 
     */  
    private CharacterParser characterParser;  
//    private List<SortModel> SourceDateList;  
    private List<ContactsBean> SourceDateList;  
    
    /** 
     * 根据拼音来排列ListView里面的数据类 
     */  
    private PinyinComparator pinyinComparator;  
    
    /**
     * 从写邮件的 fragment 过来到参数 默认为 0 
     */
    private int type = 0 ;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null && bundle.containsKey("textViewType")){
			type = bundle.getInt("textViewType");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.contacts_fragment_layout, null);
		
		initViews();  
		
		registerForContextMenu(sortListView);
		
		processTouchEvent();
		return rootView;
	}

	

	@Override
	public void onPause() {
		super.onPause();
	}
	
	
	
	

	private void initViews() {
		backButton = (Button) rootView.findViewById(R.id.head_bar_back);
		backButton.setOnClickListener(this);
		editButton = (Button) rootView.findViewById(R.id.head_bar_ok);
		if(type == 0){
			editButton.setText(mainActivity.getResources().
					getString(R.string.contacts_button_edit_text)
					);
			editButton.setOnClickListener(this);
		}else{
			editButton.setVisibility(View.GONE);
		}
		
		//实例化汉字转拼音类  
		characterParser = CharacterParser.getInstance();  
		pinyinComparator = new PinyinComparator();  
       
	    sideBar = (SideBar) rootView.findViewById(R.id.sidrbar);  
	    sideBar.setDisplayMetrics(displayMetrics);
	    dialog = (TextView) rootView.findViewById(R.id.dialog);  
	    sideBar.setTextView(dialog);  
       
       //设置右侧触摸监听  
       sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置  
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
			}
		});
       
       sortListView = (ListView) rootView.findViewById(R.id.country_lvcountry);  
       sortListView.setOnItemClickListener(new OnItemClickListener() {  
 
           @Override  
           public void onItemClick(AdapterView<?> parent, View view,  
                   int position, long id) {  
               //这里要利用adapter.getItem(position)来获取当前position所对应的对象 
        	   if(type != 0){
        		   gobackPrgPage((ContactsBean)adapter.getItem(position));
        	   }
               Toast.makeText(mainActivity, ((ContactsBean)adapter.getItem(position)).toString(), Toast.LENGTH_SHORT).show();  
           }
       });  
       
//       SourceDateList = filledData(getResources().getStringArray(R.array.date));
       addTestData(getResources().getStringArray(R.array.date));
       SourceDateList = fetchData();
       // 根据a-z进行排序源数据  
       Collections.sort(SourceDateList, pinyinComparator);
       adapter = new SortAdapter(SourceDateList, mainActivity);
       sortListView.setAdapter(adapter);
       
       mClearEditText = (ClearEditText) rootView.findViewById(R.id.filter_edit); 
       //根据输入框输入值的改变来过滤搜索  
       mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表  
               filterData(s.toString());  
				
			}
			

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
       
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = mainActivity.getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}
	
	
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		ContactsBean model = (ContactsBean) adapter.getItem(info.position);
	    switch (item.getItemId()) {
	        case R.id.context_menu_contacts_remove:
	        	processRemoveEvent(model);
	            return true;
	        case R.id.context_menu_contacts_edit:
	        	processEditEvent(model);
	        	return true;
	        case R.id.context_menu_contacts_send_mail:
	        	processSendMailEvent(model);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}


	private void processRemoveEvent(ContactsBean bean) {
		dbHelperManager.delete(bean.id);
		refreshPageData();
	}
	

	private void processEditEvent(ContactsBean bean) {
		startAddContactsEditFragment(bean);
	}

	private void processSendMailEvent(ContactsBean bean) {
		FragmentTransaction transaction = mainActivity.getFragmentManager()
				.beginTransaction();
		WriteMailFragment fragment = new WriteMailFragment();
		if(bean != null){
			Bundle args = new Bundle();
			args.putInt("id", bean.id);
			args.putString("name", bean.name);
			args.putString("address", bean.mail_address);
			fragment.setArguments(args);
		}
		transaction.setCustomAnimations(R.anim.fade_in , R.anim.fade_out);
		transaction.add(MainActivity.MAIN_AREA, fragment,
				WriteMailFragment.WRITE_MAIL_FRAGMENT_FLAG);
		transaction.addToBackStack(WriteMailFragment.WRITE_MAIL_FRAGMENT_FLAG);
		transaction.commit();
		
	}
	
	private void processTouchEvent() {
		rootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
	}
	
	 /** 
    * 为ListView填充数据 
    * @param date 
    * @return 
    */  
	private List<SortModel> filledData(String[] data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();  
		for(int i=0; i< data.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(data[i]);
			// 汉字转换成拼音  
			String pinyin = characterParser.getSelling(data[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母  
           if(sortString.matches("[A-Z]")){  
               sortModel.setSortLetters(sortString.toUpperCase());  
           }else{  
               sortModel.setSortLetters("#");  
           }  
           mSortList.add(sortModel); 
		}
		return mSortList;
	}

	 /** 
    * 根据输入框中的值来过滤数据并更新ListView 
    * @param filterStr 
    */  
	private void filterData(String filterStr) {
		List<ContactsBean> filterDateList = new ArrayList<ContactsBean>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for( ContactsBean sortModel : SourceDateList){
				String name = sortModel.name;
				if(name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1
						|| characterParser.getSelling(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())){
					filterDateList.add(sortModel);
				}
			}
		}
		 // 根据a-z进行排序  
       Collections.sort(filterDateList, pinyinComparator);  
       adapter.updateListView(filterDateList); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.head_bar_back:
				if(type == 0){
					backPrevPage(R.id.contacts_main_area);
				}else{
					mainActivity.getFragmentManager().popBackStack();
				}
				break;
			case R.id.head_bar_ok:
				startAddContactsEditFragment(null);
				break;
	
			default:
				break;
		}
		
	}

	/**
	 * 跳转到添加联系人界面 并传递指定信息(如果是直接跳转页面可以 传 null )
	 */
	private void startAddContactsEditFragment(ContactsBean bean) {
		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
		ContactsEditFragment fragment = new ContactsEditFragment();
		if(bean != null){
			Bundle args = new Bundle();
			args.putInt("id", bean.id);
			args.putString("name", bean.name);
			args.putString("address", bean.mail_address);
			fragment.setArguments(args);
		}
		transaction.setCustomAnimations(R.anim.fade_in , R.anim.fade_out);
		transaction.add(MainActivity.MAIN_AREA, fragment,ContactsEditFragment.CONTACTS_EDIT_FRAGMENT_TAG);
		transaction.addToBackStack(ContactsEditFragment.CONTACTS_EDIT_FRAGMENT_TAG);
		transaction.commit();
	}
	
	
	/**
	 * 添加测试数据
	 */
	private void addTestData(String[] data){
		if(dbHelperManager.getCount() > 0){
			return ;
		}
		for(int i=0; i< data.length; i++){
			ContactsBean contacts = new ContactsBean();
			contacts.name = data[i];
			contacts.mail_address = "1241303415@qq.com";
			dbHelperManager.insert(contacts);
		}
	}
	
	
	private List<ContactsBean> fetchData(){
		List<ContactsBean> contactsBeans = new ArrayList<ContactsBean>();
		Cursor cursor = dbHelperManager.getScrollData(0, 300);
		while (cursor.moveToNext()) {
			ContactsBean contactsBean = new ContactsBean();
			contactsBean.id = cursor.getInt(cursor.getColumnIndex("_id"));
			contactsBean.name = cursor.getString(cursor.getColumnIndex("name"));
			contactsBean.mail_address = cursor.getString(cursor.getColumnIndex("mail_address"));
			// 汉字转换成拼音  
			String pinyin = characterParser.getSelling(contactsBean.name);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母  
	       if(sortString.matches("[A-Z]")){  
	    	   contactsBean.sortLetters = (sortString.toUpperCase());  
	       }else{  
	    	   contactsBean.sortLetters = "#";  
	       }  
	       
	       contactsBeans.add(contactsBean);
		}
		cursor.close();
		return contactsBeans;
	}

	@Override
	public void refreshPageData() {
		if(adapter != null){
			 SourceDateList = fetchData();
		       // 根据a-z进行排序源数据  
		       Collections.sort(SourceDateList, pinyinComparator);
			adapter.updateListView(SourceDateList);
		}
	}
	
	
	private void gobackPrgPage(ContactsBean item) {
		InputMethodUtil.hideInputMethod(rootView);
		if(preFragment != null && preFragment instanceof WriteMailFragment){
			WriteMailFragment writeMailFragment = (WriteMailFragment) preFragment;
			if(type == 1){
				writeMailFragment.setMailAddress(item.mail_address);
			}else if(type == 2){
				writeMailFragment.setCCMailAddress(item.mail_address);
			}else if(type == 3){
				writeMailFragment.setBCCMailAddress(item.mail_address);
			}
		}
//		FragmentTransaction transaction = mainActivity.getFragmentManager().beginTransaction();
//		transaction.remove(ContactsFragment.this);
//		transaction.commit();
		mainActivity.getFragmentManager().popBackStack();
	}

	
	
	
}
