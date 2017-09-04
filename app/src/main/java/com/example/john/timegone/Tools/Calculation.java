package com.example.john.timegone.Tools;

import java.math.BigDecimal;
import java.util.Calendar;

import static com.example.john.timegone.Data.FactorForLifespan.birthDay;
import static com.example.john.timegone.Data.FactorForLifespan.birthMonth;
import static com.example.john.timegone.Data.FactorForLifespan.birthYear;
import static com.example.john.timegone.Data.FactorForLifespan.provinceCode;
import static com.example.john.timegone.Data.LifeExpectancy.LEList_China;

/**
 * Created by john on 2017/9/3.
 */

public class Calculation {

    /**
     * 计算剩余时间
     */
    public static BigDecimal calcuRestTime(){
        //获取系统当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //预期寿命
        BigDecimal LifeExpect = BigDecimal.valueOf(LEList_China[provinceCode]);
        //还剩多少年
        BigDecimal yearSpan = LifeExpect.subtract(BigDecimal.valueOf(year).subtract(BigDecimal.valueOf(birthYear)));
        //还剩多少月
        BigDecimal monthSpan = yearSpan.multiply(BigDecimal.valueOf(12)).subtract(BigDecimal.valueOf(birthMonth));
        //还剩多少天
        BigDecimal daySpan = monthSpan.multiply(BigDecimal.valueOf(30)).subtract(BigDecimal.valueOf(birthDay));
        //还剩多少小时
        BigDecimal hourSpan = daySpan.multiply(BigDecimal.valueOf(24)).subtract(BigDecimal.valueOf(hour));
        //还剩多少分钟
        BigDecimal minuteSpan = hourSpan.multiply(BigDecimal.valueOf(60)).subtract(BigDecimal.valueOf(minute));
        //还剩多少秒
        BigDecimal secondSpan = minuteSpan.multiply(BigDecimal.valueOf(60)).subtract(BigDecimal.valueOf(minute));
        //毫秒
        BigDecimal MileSecond = secondSpan.multiply(BigDecimal.valueOf(1000));

        //返回毫秒
        return MileSecond;
    }

    public static BigDecimal calcuRestHour(){
        //获取系统当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //预期寿命
        BigDecimal LifeExpect = BigDecimal.valueOf(LEList_China[provinceCode]);
        //还剩多少年
        BigDecimal yearSpan = LifeExpect.subtract(BigDecimal.valueOf(year).subtract(BigDecimal.valueOf(birthYear)));
        //还剩多少月
        BigDecimal monthSpan = yearSpan.multiply(BigDecimal.valueOf(12)).subtract(BigDecimal.valueOf(birthMonth));
        //还剩多少天
        BigDecimal daySpan = monthSpan.multiply(BigDecimal.valueOf(30)).subtract(BigDecimal.valueOf(birthDay));
        //还剩多少小时
        BigDecimal hourSpan = daySpan.multiply(BigDecimal.valueOf(24)).subtract(BigDecimal.valueOf(hour));

        /*//预期寿命
        double LifeExpect = LEList_China[provinceCode];
        //还剩多少年
        double yearSpan = LifeExpect-(year-birthYear);
        //还剩多少月
        double monthSpan = yearSpan*12-birthMonth;
        //还剩多少天
        double daySpan = monthSpan*30-birthDay;
        //还剩多少小时
        double hourSpan = daySpan*24-hour;*/

        //返回小时
        return hourSpan;
    }

    public static BigDecimal calcuRestMinute(){
        //获取系统当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //预期寿命
        BigDecimal LifeExpect = BigDecimal.valueOf(LEList_China[provinceCode]);
        //还剩多少年
        BigDecimal yearSpan = LifeExpect.subtract(BigDecimal.valueOf(year).subtract(BigDecimal.valueOf(birthYear)));
        //还剩多少月
        BigDecimal monthSpan = yearSpan.multiply(BigDecimal.valueOf(12)).subtract(BigDecimal.valueOf(birthMonth));
        //还剩多少天
        BigDecimal daySpan = monthSpan.multiply(BigDecimal.valueOf(30)).subtract(BigDecimal.valueOf(birthDay));
        //还剩多少小时
        BigDecimal hourSpan = daySpan.multiply(BigDecimal.valueOf(24)).subtract(BigDecimal.valueOf(hour));

        /*//预期寿命
        double LifeExpect = LEList_China[provinceCode];
        //还剩多少年
        double yearSpan = LifeExpect-(year-birthYear);
        //还剩多少月
        double monthSpan = yearSpan*12-birthMonth;
        //还剩多少天
        double daySpan = monthSpan*30-birthDay;
        //还剩多少小时
        double hourSpan = daySpan*24-hour;*/
        //还剩多少分钟
        BigDecimal minuteSpan = hourSpan.multiply(BigDecimal.valueOf(60)).subtract(BigDecimal.valueOf(minute));
        //返回分钟
        return minuteSpan;
    }

    public static BigDecimal calcuRestSecond(){
        //获取系统当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        //预期寿命
        BigDecimal LifeExpect = BigDecimal.valueOf(LEList_China[provinceCode]);
        //还剩多少年
        BigDecimal yearSpan = LifeExpect.subtract(BigDecimal.valueOf(year).subtract(BigDecimal.valueOf(birthYear)));
        //还剩多少月
        BigDecimal monthSpan = yearSpan.multiply(BigDecimal.valueOf(12)).subtract(BigDecimal.valueOf(birthMonth));
        //还剩多少天
        BigDecimal daySpan = monthSpan.multiply(BigDecimal.valueOf(30)).subtract(BigDecimal.valueOf(birthDay));
        //还剩多少小时
        BigDecimal hourSpan = daySpan.multiply(BigDecimal.valueOf(24)).subtract(BigDecimal.valueOf(hour));
        //还剩多少分钟
        BigDecimal minuteSpan = hourSpan.multiply(BigDecimal.valueOf(60)).subtract(BigDecimal.valueOf(minute));

        /*//预期寿命
        double LifeExpect = LEList_China[provinceCode];
        //还剩多少年
        double yearSpan = LifeExpect-(year-birthYear);
        //还剩多少月
        double monthSpan = yearSpan*12-birthMonth;
        //还剩多少天
        double daySpan = monthSpan*30-birthDay;
        //还剩多少小时
        double hourSpan = daySpan*24-hour;*/
        //还剩多少秒
        BigDecimal secondSpan = minuteSpan.multiply(BigDecimal.valueOf(60)).subtract(BigDecimal.valueOf(minute));
        //返回秒
        return secondSpan;
    }
}
