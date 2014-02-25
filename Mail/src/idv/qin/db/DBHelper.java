package idv.qin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 这里并没有对数据库加密 数据有可能会泄露 。如果需要加密请参考 
 * 	{@linkplain http://blog.csdn.net/guolin_blog/article/details/11952409}
 * @author qin
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	
	private final static int VERSION = 1;  
    private final static String DB_NAME = "user.db";  
    private final static String CREATE_TBL = "create table user(_id integer primary key autoincrement, name text, mail_address text)";  
    private DBVersionUpgradeListener upgradeListener;
    
    public DBHelper(Context context, String name, CursorFactory factory,int version) {  
        //必须通过super 调用父类的构造函数  
        super(context, name, factory, version);  
    }  
      
    //数据库的构造函数，传递三个参数的  
    public DBHelper(Context context, String name, int version){  
        this(context, name, null, version);  
    }  
      
    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都写死了  
    public DBHelper(Context context){  
        this(context, DB_NAME, null, VERSION);  
    }  

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL);  
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(upgradeListener != null){
			upgradeListener.onDBVersionUpgrade(db);
		}
	}

	
	
	
	public void setUpgradeListener(DBVersionUpgradeListener upgradeListener) {
		this.upgradeListener = upgradeListener;
	}




	/** 数据库版本变更监听器  */
	public static interface DBVersionUpgradeListener{
		/** 提供当数据库版本变更时候可能需要的修改操作  
		 * @param db a {@link DBHelper} instance .
		 * */
		public void onDBVersionUpgrade(SQLiteDatabase db);
	}
	
}
