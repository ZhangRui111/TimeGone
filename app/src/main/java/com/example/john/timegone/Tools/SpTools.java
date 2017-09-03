package com.example.john.timegone.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.john.timegone.Interface.MyConstants;

/**
 * Created by john on 2017/9/3.
 */

/**
 * 引导页面
 */
public class SpTools {

    public static void setBoolean(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.CONFIGFILE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();//提交保存键值对

    }


    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.CONFIGFILE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }
}