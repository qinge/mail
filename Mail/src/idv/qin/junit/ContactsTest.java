package idv.qin.junit;

import java.util.ArrayList;
import java.util.List;

import idv.qin.db.DBHelper;
import idv.qin.db.DBHelperManager;
import idv.qin.doamin.ContactsBean;
import android.test.AndroidTestCase;

public class ContactsTest extends AndroidTestCase {
	
	public void testContacts() throws Exception{
		DBHelper dbHelper = new DBHelper(getContext());
		dbHelper.getWritableDatabase();
	}
	
	public void testAdd() throws Exception{
		for(int i=0; i < 20 ; i++){
			ContactsBean contacts = new ContactsBean();
			contacts.name = "xiao名_"+i;
			contacts.mail_address = "fdfs@sina.com";
			DBHelperManager dbHelperManager = new DBHelperManager(getContext());
			dbHelperManager.insert(contacts);
		}
	}
	
	public void testFind() throws Exception{
		DBHelperManager dbHelperManager = new DBHelperManager(getContext());
		ContactsBean contacts = dbHelperManager.find(1);
		System.out.println(contacts);
	}
	
	public void testUpdate() throws Exception{
		DBHelperManager dbHelperManager = new DBHelperManager(getContext());
		ContactsBean contacts = dbHelperManager.find(1);
		contacts.name = "uuuu";
		dbHelperManager.update(contacts);
	}
	
	
	public void testDelete() throws Exception{
		DBHelperManager dbHelperManager = new DBHelperManager(getContext());
		dbHelperManager.delete(1);
	}
	
	public void testGetCount() throws Exception{
		DBHelperManager dbHelperManager = new DBHelperManager(getContext());
		System.out.println("---------------->>>>>> " + dbHelperManager.getCount());
	}
	
	
	public void testScrollData() throws Exception{
		DBHelperManager dbHelperManager = new DBHelperManager(getContext());
		List contacts = new ArrayList();
		dbHelperManager.getScrollDataAndInflateCollection(contacts, 6, 14);
		System.out.println("---------------->>>>>> " + contacts);
	}
	
	
}
