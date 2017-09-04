package com.example.john.timegone.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.MODE_PRIVATE;
import static com.example.john.timegone.Data.FactorForLifespan.birthDay;
import static com.example.john.timegone.Data.FactorForLifespan.birthMonth;
import static com.example.john.timegone.Data.FactorForLifespan.birthYear;
import static com.example.john.timegone.Data.FactorForLifespan.countryCode;
import static com.example.john.timegone.Data.FactorForLifespan.occupationCode;
import static com.example.john.timegone.Data.FactorForLifespan.provinceCode;

/**
 * Created by john on 2017/9/4.
 */

public class Utils {

    /**
     * 读取 lifeSpan Factor
     * @param context
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
