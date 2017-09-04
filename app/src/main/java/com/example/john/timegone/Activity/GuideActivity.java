package com.example.john.timegone.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.john.timegone.MainActivity;
import com.example.john.timegone.R;
import com.example.john.timegone.Tools.Utils;

import java.util.Calendar;

import static com.example.john.timegone.Data.FactorForLifespan.countryCodeList;
import static com.example.john.timegone.Data.FactorForLifespan.hourLeft;
import static com.example.john.timegone.Data.FactorForLifespan.minuteLeft;
import static com.example.john.timegone.Data.FactorForLifespan.occupationCodeList;
import static com.example.john.timegone.Data.FactorForLifespan.provinceCodeList;
import static com.example.john.timegone.Data.FactorForLifespan.secondLeft;
import static com.example.john.timegone.Tools.Calculation.calcuRestHour;
import static com.example.john.timegone.Tools.Calculation.calcuRestMinute;
import static com.example.john.timegone.Tools.Calculation.calcuRestSecond;
import static com.example.john.timegone.Tools.Utils.funcReadFactorFromSharePre;

/**
 * Created by john on 2017/9/3.
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startMainActBtn;
    private Button selectCountryBtn, selectProvinceBtn, selectOccupBtn, selectBirthBtn;
    private String initCountryStr, initProvinceStr, initOccupStr, initBirthStr;
    private int user_country, user_province, user_occupa, user_year, user_month, user_day;
    private Boolean[] isAllSelect = new Boolean[]{false, false, false, false};  //四个选项全为True，才可以跳转

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setImmersiveMode();

        initData();
        initView();
        initEvent();
    }

    private void initData() {

    }

    private void initView() {
        startMainActBtn = (Button) findViewById(R.id.start_main_act_button);
        selectCountryBtn = (Button) findViewById(R.id.select_country_button);
        selectProvinceBtn = (Button) findViewById(R.id.select_province_button);
        selectOccupBtn = (Button) findViewById(R.id.select_occupation_button);
        selectBirthBtn = (Button) findViewById(R.id.select_birthday_button);
    }


    private void initEvent() {
        startMainActBtn.setOnClickListener(this);
        selectCountryBtn.setOnClickListener(this);
        selectProvinceBtn.setOnClickListener(this);
        selectOccupBtn.setOnClickListener(this);
        selectBirthBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_main_act_button:
                if(isAllSelect[0] && isAllSelect[1] && isAllSelect[2] && isAllSelect[3]){
                    //全部选项均选择后，判断是否存储数据成功
                    funcSaveNewCountdownInSharePre();  // 保存新倒计时的SharePre文件
                    if(funcSaveFactorInSharePre()) {
                        //计算剩余时间并保存
                        funcReadFactorFromSharePre(this);  //首先将各个要素写入才能使用
                        hourLeft = calcuRestHour();
                        minuteLeft = calcuRestMinute();
                        secondLeft = calcuRestSecond();
                        //Toast.makeText(this, "" + hourLeft + ":" + minuteLeft + ":" +secondLeft, Toast.LENGTH_SHORT).show();
                        if(funcSaveTimeInSharePre()) {
                            funcConfirmInfo(initCountryStr+"-"+initProvinceStr+"\n"+initOccupStr+"\n"+initBirthStr);
                        }
                    } else {
                        funcAlertInfo("Couldn't save data in local!");
                    }
                } else {
                    funcAlertInfo("Some information is missing!");
                }
                break;
            case R.id.select_country_button:
                funcSelectCountryDialog();
                break;
            case R.id.select_province_button:
                funcSelectProvinceDialog();
                break;
            case R.id.select_occupation_button:
                funcSelectOccupDialog();
                break;
            case R.id.select_birthday_button:
                funcSelectBirthDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 保存 LifeSpan factor in local
     * @return
     */
    private boolean funcSaveFactorInSharePre() {
        Boolean result;
        try {
            SharedPreferences.Editor editor = getSharedPreferences("user_lifespan_factor",MODE_PRIVATE).edit();
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

    /**
     * 保存剩余时间
     * @return
     */
    private boolean funcSaveTimeInSharePre() {
        Boolean result;
        try {
            SharedPreferences.Editor editor = getSharedPreferences("user_left_time",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putLong("hourLeft",hourLeft.longValue());
            editor.putLong("minuteLeft",minuteLeft.longValue());
            editor.putLong("secondLeft",secondLeft.longValue());
            editor.apply();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * 保存剩余时间
     * @return
     */
    private boolean funcSaveNewCountdownInSharePre() {
        Boolean result;
        try {
            SharedPreferences.Editor editor = getSharedPreferences("new_countdown",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putInt("flag",0);
            editor.putString("name","");
            editor.putLong("newCountdownHour",-1L);
            editor.putLong("newCountdownMinute",-1L);
            editor.putLong("newCountdownSecond",-1L);
            editor.apply();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
    private void funcConfirmInfo(String msg) {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage(msg);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    private void funcAlertInfo(String msg) {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    /**
     * 选择相关信息以计算剩余时间
     */
    private void funcSelectCountryDialog() {
        final String countryList[] = countryCodeList;
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select your country");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.setSingleChoiceItems(countryList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != 0) {
                    user_country = which;
                    initCountryStr = countryList[which];
                    isAllSelect[0] = true;
                    selectCountryBtn.setText(countryList[which]);
                }
            }
        });
        builder.show();
    }

    private void funcSelectProvinceDialog() {
        final String provinceList[] = provinceCodeList;
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select your province");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.setSingleChoiceItems(provinceList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != 0){
                    user_province = which;
                    initProvinceStr = provinceList[which];
                    isAllSelect[1] = true;
                    selectProvinceBtn.setText(provinceList[which]);
                }
            }
        });
        builder.show();
    }

    private void funcSelectOccupDialog() {
        final String occupList[] = occupationCodeList;
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select your occupation");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.setSingleChoiceItems(occupList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != 0){
                    user_occupa = which;
                    initOccupStr = occupList[which];
                    isAllSelect[2] = true;
                    selectOccupBtn.setText(occupList[which]);
                }
            }
        });
        builder.show();
    }

    private void funcSelectBirthDialog() {
        final StringBuffer birthday = new StringBuffer();
        //获取Calendar对象，用于获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //实例化DatePickerDialog对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerDialogTheme,new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //因为monthOfYear会比实际月份少一月所以这边要加1
                user_year = year;
                user_month = monthOfYear+1;
                user_day = dayOfMonth;
                birthday.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                initBirthStr = birthday.toString();
                isAllSelect[3] = true;
                selectBirthBtn.setText(birthday);
            }
        }, year, month, day);
        //设置生日的上限是今天，下限是100年前
        calendar.add(Calendar.YEAR, -100);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        //弹出选择日期对话框
        datePickerDialog.show();
    }


    /**
     * 对于不同版本的安卓系统实现沉浸式体验
     */
    private void setImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 全透明实现
            //getWindow.setStatusBarColor(Color.TRANSPARENT)
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //调用静态方法设置沉浸式状态栏
            Utils.setStateBarColor(this);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //4.4 全透明状态栏
            //底部导航栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //顶部状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup contentLayout = (ViewGroup) this.findViewById(android.R.id.content);
            View contentChild = contentLayout.getChildAt(0);
            contentChild.setFitsSystemWindows(false);  // 这里设置成侵占状态栏，反而不会侵占状态栏，太坑
            Utils.setupStatusBarView(this, contentLayout, Color.parseColor("#4A90E2"));
            //隐藏底部导航栏
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;  此时将同时隐藏状态栏
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
