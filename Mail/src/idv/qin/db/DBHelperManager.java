package idv.qin.db;

import java.util.List;

import idv.qin.doamin.ContactsBean;
import idv.qin.mail.MyApplication;
import idv.qin.mail.fragmet.BaseFragment;
import idv.qin.utils.MyLog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作实例类 创建该对象时候会创建一个 {@link DBHelper } <br>
 * 该对象到创建由 {@link MyApplication} 创建 由 {@link BaseFragment} 中获取 并由其子类调用
 * @author qinge
 *
 */
public class DBHelperManager {

	private DBHelper dbHelper;
	private SQLiteDatabase database = null;

	public DBHelperManager(Context context) {
		dbHelper = new DBHelper(context);
	}
	
	
	/**
	 * 
	 * @param contacts
	 * @return
	 */
	public boolean insert(ContactsBean contacts){
		if(contacts == null ){
			return false;
		}
		
		
		try {
			ContentValues contentValues = new ContentValues();
			
			/**---------        填充数据           ----------------*/
			inflateContentValue(contacts, contentValues);
			
			database = dbHelper.getWritableDatabase();
			database.insert("user", null, contentValues);
			return true;
		} catch (Exception e) {
			MyLog.e("DBHelperManager-->insert(~)",e.getMessage());
			return false;
		}finally{
			if(database != null && database.isOpen()){
				database.close();
				database = null;
			}
		}
	}
	
	/**
	 * 填充 数据到 contentValues
	 * @param contacts 联系人 .
	 * @param contentValues 
	 */
	private void inflateContentValue(ContactsBean contacts,
			ContentValues contentValues) {
		contentValues.put("name", contacts.name);
		contentValues.put("mail_address", contacts.mail_address);
	}

	/**
	 * 如果是要批量删除 需要重新提供 api 此方法适合单一删除 
	 * @param id
	 * @return
	 */
	public boolean delete(int id){
		try {
			database = dbHelper.getWritableDatabase();
			database.execSQL("delete from user where _id = ? ", new String [] {id+""});
			return true;
		} catch (Exception e) {
			MyLog.e("DBHelperManager-->delete(~)",e.getMessage());
			return false;
		}finally{
			if(database != null && database.isOpen()){
				database.close();
				database = null;
			}
		}
	}
	
	/**
	 * 
	 * @param contacts
	 * @return
	 */
	public boolean update(ContactsBean contacts){
		try {
			database = dbHelper.getWritableDatabase();
			database.execSQL("update user set name = ? , mail_address = ? where _id = ? ",
					new String [] {contacts.name, contacts.mail_address, contacts.id+""}
			);
			return true;
		} catch (Exception e) {
			MyLog.e("DBHelperManager-->update(~)",e.getMessage());
			return false;
		}finally{
			if(database != null && database.isOpen()){
				database.close();
				database = null;
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return T
	 */
	public ContactsBean find(int id){
		try {
			database = dbHelper.getReadableDatabase();
			Cursor cursor = database.rawQuery("select * from user u where u._id=?", new String [] {id+""});
			if(cursor.moveToFirst()){
				ContactsBean contacts = new ContactsBean();
				contacts.id = cursor.getInt(cursor.getColumnIndex("_id"));
				contacts.name = cursor.getString(cursor.getColumnIndex("name"));
				contacts.mail_address = cursor.getString(cursor.getColumnIndex("mail_address"));
				cursor.close();
				return contacts;
			}
		} catch (Exception e) {
			MyLog.e("DBHelperManager-->find(~)",e.getMessage());
			return null;
		}finally{
			if(database != null && database.isOpen()){
				database.close();
				database = null;
			}
		}
		return null;
	}
	
	/**
	 * 使用完后 请关闭 cursor
	 * @param offset
	 * @param maxResult
	 * @return
	 */
	public Cursor getScrollData(int offset,int maxResult){
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select _id , name, mail_address from user u order by u._id asc limit ?,?", 
				new String [] {offset+"", maxResult+""});
		return cursor;
	}
	
	/**
	 * 获取分页数据 并填充在集合中
	 * @param ts
	 * @param offset
	 * @param maxResult
	 */
	public void getScrollDataAndInflateCollection(List<ContactsBean> ts , int offset,int maxResult){
		Cursor cursor = this.getScrollData(offset, maxResult);
		while(cursor.moveToNext()){
			ContactsBean contacts = new ContactsBean();
			contacts.id = cursor.getInt(cursor.getColumnIndex("_id"));
			contacts.name = cursor.getString(cursor.getColumnIndex("name"));
			contacts.mail_address = cursor.getString(cursor.getColumnIndex("mail_address"));
			ts.add(contacts);
		}
		cursor.close();
		cursor = null;
	}
	
	/**
	 * 获取记录总数
	 * @return
	 */
	public Long getCount(){
		try {
			database = dbHelper.getReadableDatabase();
			Cursor cursor = database.rawQuery("select count(*) from user", null);
			if(cursor.moveToFirst()){
				Long count = cursor.getLong(0);
				cursor.close();
				return count;
			}
		} catch (Exception e) {
			MyLog.e("DBHelperManager-->find(~)",e.getMessage());
			return 0L;
		}finally{
			if(database != null && database.isOpen()){
				database.close();
				database = null;
			}
		}
		return 0L;
	}
	
	
	
}
