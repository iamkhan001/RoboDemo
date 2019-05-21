package com.csjbot.blackgaga.util;

import android.content.Context;

import com.csjbot.blackgaga.BaseApplication;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by xiasuhuei321 on 2017/8/14.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class SharedPreUtil {
    public static boolean putString(String spName, String key, String data) {
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .edit()
                .putString(key, data)
                .commit();
    }

    public static boolean putString(String spName, String key,Object data) {
        String str = new Gson().toJson(data);
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .edit()
                .putString(key, str)
                .commit();
    }


    public static String getString(String spName, String key) {
        return getString(spName, key, null);
    }

    public static String getString(String spName, String key, String defValue) {
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .getString(key, defValue);
    }

    public static boolean putFloat(String spName, String key, float data) {
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .edit()
                .putFloat(key, data)
                .commit();
    }

    public static float getFloat(String spName, String key) {
        return getFloat(spName, key);
    }

    public static float getFloat(String spName, String key, float defValue) {
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .getFloat(key, defValue);
    }

    public static boolean removeString(String name, String key) {
        return BaseApplication.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit().remove(key).commit();
    }
    public static boolean removeString(String name) {
        return BaseApplication.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit().clear().commit();
    }



    public static boolean putInt(String spName, String key, int data) {
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, data)
                .commit();
    }

    public static boolean putBoolean(String spName, String key, boolean data){
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, data)
                .commit();
    }

    public static boolean getBoolean(String spName, String key){
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .getBoolean(key,true);
    }

    public static boolean getBoolean(String spName, String key, boolean defaultValue){
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .getBoolean(key,defaultValue);
    }

    public static int getInt(String spName, String key, int defValue) {
        return BaseApplication.getAppContext().getSharedPreferences(spName, Context.MODE_PRIVATE)
                .getInt(key, defValue);
    }

    public static Map<String, ?> getAllSp(String spname){
        return BaseApplication.getAppContext().getSharedPreferences(spname,Context.MODE_PRIVATE)
                .getAll();
    }
}
