package com.aura.smartschool.utils;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BasePreferenceUtil {
	private SharedPreferences _sharedPreferences;
	 
	   protected BasePreferenceUtil(Context context)
	   {
	      super();
	      _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	   }
	 
	   /**
	    * key 수동 설정
	    * 
	    * @param key
	    *           키 값
	    * @param value
	    *           내용
	    */
	   protected void put(String key, String value)
	   {
	      SharedPreferences.Editor editor = _sharedPreferences.edit();
	      editor.putString(key, value);
	      editor.commit();
	   }
	 
	   /**
	    * String 값 가져오기
	    * 
	    * @param key
	    *           키 값
	    * @return String (기본값 null)
	    */
	   protected String get(String key)
	   {
	      return _sharedPreferences.getString(key, "");
	   }
	 
	   /**
	    * key 설정
	    * 
	    * @param key
	    *           키 값
	    * @param value
	    *           내용
	    */
	   protected void put(String key, boolean value)
	   {
	      SharedPreferences.Editor editor = _sharedPreferences.edit();
	      editor.putBoolean(key, value);
	      editor.commit();
	   }
	 
	   /**
	    * Boolean 값 가져오기
	    * 
	    * @param key
	    *           키 값
	    * @param defValue
	    *           기본값
	    * @return Boolean
	    */
	   protected boolean get(String key, boolean defValue)
	   {
	      return _sharedPreferences.getBoolean(key, defValue);
	   }
	 
	   /**
	    * key 설정
	    * 
	    * @param key
	    *           키 값
	    * @param value
	    *           내용
	    */
	   protected void put(String key, int value)
	   {
	      SharedPreferences.Editor editor = _sharedPreferences.edit();
	      editor.putInt(key, value);
	      editor.commit();
	   }
	 
	   /**
	    * int 값 가져오기
	    * 
	    * @param key
	    *           키 값
	    * @param defValue
	    *           기본값
	    * @return int
	    */
	   protected int get(String key, int defValue)
	   {
	      return _sharedPreferences.getInt(key, defValue);
	   }
	   
	   
	   protected void put(String key, Set<String> set) {
		  SharedPreferences.Editor editor = _sharedPreferences.edit();
	      editor.putStringSet(key, set);
	      editor.commit();
	   }
	   
	   protected Set<String> get(String key, Set<String> defValue) {
		   return _sharedPreferences.getStringSet(key, defValue);
	   }
}
