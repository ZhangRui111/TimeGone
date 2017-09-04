package com.example.john.timegone.Data;

import java.math.BigDecimal;

/**
 * Created by john on 2017/9/3.
 */

public class FactorForLifespan {

    public static int countryCode;
    public static int provinceCode;
    public static int occupationCode;
    public static int birthYear;
    public static int birthMonth;
    public static int birthDay;

    public static String[] countryCodeList = new String[]{"-", "中国"};
    /*        final String countryList[] = new String[]{"北京市", "天津市", "河北省", "山西省", "内蒙古自治区", "辽宁省",
                "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省",
                "湖南省", "广东省", "广西壮族自治区", "海南省", "重庆市", "四川省", "贵州省", "云南省", "西藏自治区",
                "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "台湾省", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区}";*/
    public static String[] provinceCodeList = new String[]{"-", "北京", "天津", "河北", "山西", "内蒙古", "辽宁",
            "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北",
            "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃",
            "青海", "宁夏", "台湾", "新疆", "香港", "澳门}"};
    public static String[] occupationCodeList = new String[]{"-", "程序员", "产品经理", "大佬", "学生"};

    public static BigDecimal hourLeft, minuteLeft, secondLeft;  //剩余时间计算结果

}
