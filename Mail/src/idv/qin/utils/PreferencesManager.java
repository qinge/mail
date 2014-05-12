/*
 * Copyright (C) 2013 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package idv.qin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * 用于安全的(添加类 Base64 加解密)保存临时数据 preference 文件 保存的数据<br>
 * 使用方法 PreferencesManager.getInstance().saveValue(key, value)  getValue(key);
 * @author 
 *
 */
public class PreferencesManager {

    public static final String SHARED_PREFERENCES_NAME = "SWIPE_LIST_VIEW_SAMPLE_PREFERENCES";

    /**
     * Instance
     */
    private static PreferencesManager preferencesManager = null;

    /**
     * Shared Preferences
     */
    private SharedPreferences sharedPreferences;

    /**
     * Preferences variables
     */
    private static final String SHARED_PREFERENCES_SHOW_ABOUT = "SHARED_PREFERENCES_SHOW_ABOUT";

    /**
     * Constructor
     *
     * @param context
     */
    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {
        if (preferencesManager == null) {
            preferencesManager = new PreferencesManager(context);
        }
        return preferencesManager;
    }

    public void setShowAbout(boolean showAbout) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREFERENCES_SHOW_ABOUT, showAbout);
        editor.commit();
    }

    public boolean getShowAbout() {
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_SHOW_ABOUT, true);
    }

    public void saveValue(String key, String value){
    	 SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putString(key, new String(Base64.encode(value.getBytes(),
        		 Base64.DEFAULT)));
         editor.commit();
    }
    
    public String getValue(String key){
    	return new String( Base64.decode(sharedPreferences.getString(key, "").getBytes(), 
    			Base64.DEFAULT));
   }
    

}
