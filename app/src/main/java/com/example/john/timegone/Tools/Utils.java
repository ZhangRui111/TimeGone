package com.example.john.timegone.Tools;

import android.content.Context;
import android.content.SharedPreferences;

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


    public static void funcReadFactorFromSharePre(Context context) {
        SharedPreferences pref = context.getSharedPreferences("user_lifespan_factor",MODE_PRIVATE);
        countryCode = pref.getInt("countryCode", -1);
        provinceCode = pref.getInt("provinceCode", -1);
        occupationCode = pref.getInt("occupationCode", -1);
        birthYear = pref.getInt("birthYear", -1);
        birthMonth = pref.getInt("birthMonth", -1);
        birthDay = pref.getInt("birthDay", -1);
    }
}
