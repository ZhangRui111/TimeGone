package com.example.john.timegone.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.john.timegone.Data.DateTime;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.example.john.timegone.Data.FactorForLifespan.birthDay;
import static com.example.john.timegone.Data.FactorForLifespan.birthMonth;
import static com.example.john.timegone.Data.FactorForLifespan.birthYear;
import static com.example.john.timegone.Data.FactorForLifespan.countryCode;
import static com.example.john.timegone.Data.FactorForLifespan.hourLeft;
import static com.example.john.timegone.Data.FactorForLifespan.minuteLeft;
import static com.example.john.timegone.Data.FactorForLifespan.occupationCode;
import static com.example.john.timegone.Data.FactorForLifespan.provinceCode;
import static com.example.john.timegone.Data.FactorForLifespan.secondLeft;

/**
 * Created by john on 2017/9/4.
 */

public class Utils {

    /**
     * 写入SharePreferences
     */
    //Save LifeSpan factor to "user_lifespan_factor"
    public static boolean funcSaveFactorInSharePre(Context context, int user_country, int user_province, int user_occupa,
                                             int user_year, int user_month, int user_day) {
        Boolean result;
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences("user_lifespan_factor",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putInt("countryCode",user_country);
            editor.putInt("provinceCode",user_province);
            editor.putInt("occupationCode",user_occupa);
            editor.putInt("birthYear",user_year);
            editor.putInt("birthMonth",user_month);
            editor.putInt("birthDay",user_day);
            editor.apply();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //Save left time to "user_left_time"
    public static boolean funcSaveTimeInSharePre(Context context, long hourLeft, long minuteLeft, long secondLeft) {
        Boolean result;
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences("user_left_time",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putLong("hourLeft",hourLeft);
            editor.putLong("minuteLeft",minuteLeft);
            editor.putLong("secondLeft",secondLeft);
            editor.apply();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //Save new Countdown to "new_countdown"
    public static boolean funcSaveNewCountdownInSharePre(Context context, int flag, long newCountdownHour,
                                                   long newCountdownMinute, long newCountdownSecond) {
        Boolean result;
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences("new_countdown",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putInt("flag",flag);
            editor.putString("name","");
            editor.putLong("newCountdownHour",newCountdownHour);
            editor.putLong("newCountdownMinute",newCountdownMinute);
            editor.putLong("newCountdownSecond",newCountdownSecond);
            editor.apply();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //退出时记录当前时间到"last_exit_time"
    public static void funcRecordCurrentTime(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("last_exit_time",MODE_PRIVATE).edit();
        editor.putInt("id",0);
        editor.putString("user_name","Tom");
        editor.putLong("exitTime",System.currentTimeMillis());
        editor.apply();
    }

    //Save 信号决定是否跳过向导而直接进入主界面
    public static void funcSaveEnterFlagInSharePre(Context context, int flag) {
        SharedPreferences.Editor editor = context.getSharedPreferences("if_main_page",MODE_PRIVATE).edit();
        editor.putInt("enter",flag);  //1 - 跳过；0 - 进入引导页
        editor.apply();
    }

    /**
     * 读取 SharePreferences
     */
    public static void funcReadFactorFromSharePre(Context context) {
        SharedPreferences pref = context.getSharedPreferences("user_lifespan_factor",MODE_PRIVATE);
        countryCode = pref.getInt("countryCode", -1);
        provinceCode = pref.getInt("provinceCode", -1);
        occupationCode = pref.getInt("occupationCode", -1);
        birthYear = pref.getInt("birthYear", -1);
        birthMonth = pref.getInt("birthMonth", -1);
        birthDay = pref.getInt("birthDay", -1);
    }

    public static long funcReadTimeFromSharePre(Context context, String which) {
        SharedPreferences pref = context.getSharedPreferences("user_left_time",MODE_PRIVATE);
        long result = -1;
        switch (which) {
            case "id":
                result = 0;
                break;
            case "user_name":
                result = 0;
                break;
            case "hourLeft":
                result = pref.getLong("hourLeft", -1L);
                break;
            case "minuteLeft":
                result = pref.getLong("minuteLeft", -1L);
                break;
            case "secondLeft":
                result = pref.getLong("secondLeft", -1L);
                break;
            default:
                break;
        }
        return result;
    }

    public static long funcReadNewCountdownFromSharePre(String flag, Context context) {
        long result = -1L;
        SharedPreferences pref = context.getSharedPreferences("new_countdown",MODE_PRIVATE);
        switch (flag) {
            case "flag":
                result = pref.getInt("flag", -1);
                break;
            case "newCountdownHour":
                result = pref.getLong("newCountdownHour", -1L);
                break;
            case "newCountdownMinute":
                result = pref.getLong("newCountdownMinute", -1L);
                break;
            case "newCountdownSecond":
                result = pref.getLong("newCountdownSecond", -1L);
                break;
            default:
                break;
        }
        return result;
    }

    //Save 信号决定是否跳过向导而直接进入主界面
    public static int funcReadEnterFlagInSharePre(Context context) {
        SharedPreferences pref = context.getSharedPreferences("if_main_page",MODE_PRIVATE);
        return pref.getInt("enter", -1);
    }
    /**
     * 更新
     */
    //更新剩余时间 "user_left_time"
    public static boolean funcFreshTimeInSharePre(Context context, long h, long m, long s) {
        Boolean result;
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences("user_left_time",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putLong("hourLeft",h);
            editor.putLong("minuteLeft",m);
            editor.putLong("secondLeft",s);
            editor.commit();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    //更新新的倒计时的剩余时间 "new_countdown"
    public static Boolean funcFreshNewCountdownTimeInSharePre(Context context, long newCountDownHourNow, long newCountDownHourMinuteNow, long newCountDownHourSecondNow) {
        Boolean result;
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences("new_countdown",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putInt("flag", 1);
            editor.putLong("newCountdownHour",newCountDownHourNow);
            editor.putLong("newCountdownMinute",newCountDownHourMinuteNow);
            editor.putLong("newCountdownSecond",newCountDownHourSecondNow);
            Log.d("TAGG", newCountDownHourNow+"*"+newCountDownHourMinuteNow+"&"+newCountDownHourSecondNow+";");
            editor.apply();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static DateTime funcGetDateTime(){
        DateTime dt = new DateTime();
        //获取系统当前时间
        Calendar calendar = Calendar.getInstance();
        dt.setYear(calendar.get(Calendar.YEAR));
        dt.setMonth(calendar.get(Calendar.MONTH));
        dt.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        //获取系统时间
        dt.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        dt.setMinute(calendar.get(Calendar.MINUTE));
        dt.setSecond(calendar.get(Calendar.SECOND));

        return dt;
    }

    /**
     * 状态栏沉浸
     * @param activity
     */
    //状态栏沉浸相关
    public static void setStateBarColor(Activity activity) {
        // 设置状态栏颜色
        ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
        setupStatusBarView(activity, contentLayout, Color.parseColor("#4A90E2"));
        // 设置Activity layout的fitsSystemWindows
        View contentChild = contentLayout.getChildAt(0);
        contentChild.setFitsSystemWindows(true);//等同于在根布局设置android:fitsSystemWindows="true"
    }

    public static void setupStatusBarView(Activity activity, ViewGroup contentLayout, int color) {
        View mStatusBarView = null;
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        contentLayout.addView(statusBarView, lp);
        mStatusBarView = statusBarView;
        mStatusBarView.setBackgroundColor(color);
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId =context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
