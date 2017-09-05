package com.example.john.timegone.Tools;

import com.example.john.timegone.Data.DateTime;

import java.math.BigDecimal;
import java.util.Calendar;

import static com.example.john.timegone.Data.FactorForLifespan.birthDay;
import static com.example.john.timegone.Data.FactorForLifespan.birthMonth;
import static com.example.john.timegone.Data.FactorForLifespan.birthYear;
import static com.example.john.timegone.Data.FactorForLifespan.provinceCode;
import static com.example.john.timegone.Data.LifeExpectancy.LEList_China;
import static com.example.john.timegone.Tools.Utils.funcGetDateTime;

/**
 * Created by john on 2017/9/3.
 * Calculate time.
 */

public class Calculation {

    public static long calcuRestTime(){

        DateTime dt = funcGetDateTime();

        double LifeExpect = LEList_China[provinceCode];//预期寿命

        double yearSpan = LifeExpect-(dt.getYear()-birthYear);//还剩多少年

        double monthSpan = yearSpan*12-birthMonth;//还剩多少月

        double daySpan = monthSpan*30-birthDay;//还剩多少天

        long hourSpan = Double.valueOf(daySpan*24-dt.getHour()).longValue();//还剩多少小时

        long minuteSpan = hourSpan*60-dt.getMinute();//还剩多少分钟

        long secondSpan = minuteSpan*60-dt.getMinute();//还剩多少秒

        long MileSecond = secondSpan*1000;//毫秒

        return MileSecond;//返回毫秒
    }

    public static long calcuRestHour(){

        DateTime dt = funcGetDateTime();

        double LifeExpect = LEList_China[provinceCode];//预期寿命
        double yearSpan = LifeExpect-(dt.getYear()-birthYear);//还剩多少年
        double monthSpan = yearSpan*12-birthMonth;//还剩多少月
        double daySpan = monthSpan*30-birthDay;//还剩多少天
        long hourSpan = Double.valueOf(daySpan*24-dt.getHour()).longValue();//还剩多少小时

        return hourSpan;
    }

    public static long calcuRestMinute(){
        DateTime dt = funcGetDateTime();

        double LifeExpect = LEList_China[provinceCode];//预期寿命
        double yearSpan = LifeExpect-(dt.getYear()-birthYear);//还剩多少年
        double monthSpan = yearSpan*12-birthMonth;//还剩多少月
        double daySpan = monthSpan*30-birthDay;//还剩多少天
        long hourSpan = Double.valueOf(daySpan*24-dt.getHour()).longValue();//还剩多少小时
        long minuteSpan = hourSpan*60-dt.getMinute();//还剩多少分钟
        //返回分钟
        return minuteSpan;
    }

    public static long calcuRestSecond(){
        DateTime dt = funcGetDateTime();

        double LifeExpect = LEList_China[provinceCode];//预期寿命
        double yearSpan = LifeExpect-(dt.getYear()-birthYear);//还剩多少年
        double monthSpan = yearSpan*12-birthMonth;//还剩多少月
        double daySpan = monthSpan*30-birthDay;//还剩多少天
        long hourSpan = Double.valueOf(daySpan*24-dt.getHour()).longValue();//还剩多少小时
        long minuteSpan = hourSpan*60-dt.getMinute();//还剩多少分钟

        long secondSpan = minuteSpan*60-dt.getMinute();//还剩多少秒
        //返回秒
        return secondSpan;
    }
}
