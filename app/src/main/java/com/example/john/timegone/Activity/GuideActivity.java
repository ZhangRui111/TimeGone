package com.example.john.timegone.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

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
import static com.example.john.timegone.Tools.Utils.funcSaveFactorInSharePre;
import static com.example.john.timegone.Tools.Utils.funcSaveTimeInSharePre;

/**
 * Created by john on 2017/9/3.
 */

public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private Button startMainActBtn;  //Start main page.
    private Button selectCountryBtn, selectProvinceBtn, selectOccupBtn, selectBirthBtn;  //Selece country\province\job\birthday
    private String initCountryStr, initProvinceStr, initOccupStr, initBirthStr;  //个人选项结果String表示
    private int user_country, user_province, user_occupa, user_year, user_month, user_day;  //个人选项结果的索引表示
    private Boolean[] isAllSelect = new Boolean[]{false, false, false, false};  //控制是否跳转：四个选项全为True，才可以跳转

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setImmersiveMode();

        initData();
        initView();
        initEvent();
    }

    /**
     * Initialize data.
     */
    private void initData() {}

    /**
     * Initialize view.
     */
    private void initView() {
        //五个主要按钮
        startMainActBtn = (Button) findViewById(R.id.start_main_act_button);
        selectCountryBtn = (Button) findViewById(R.id.select_country_button);
        selectProvinceBtn = (Button) findViewById(R.id.select_province_button);
        selectOccupBtn = (Button) findViewById(R.id.select_occupation_button);
        selectBirthBtn = (Button) findViewById(R.id.select_birthday_button);
    }

    /**
     * Initialize event.
     */
    private void initEvent() {
        startMainActBtn.setOnClickListener(this);
        selectCountryBtn.setOnClickListener(this);
        selectProvinceBtn.setOnClickListener(this);
        selectOccupBtn.setOnClickListener(this);
        selectBirthBtn.setOnClickListener(this);
    }

    //Click event handler.
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_main_act_button:
                if(isAllSelect[0] && isAllSelect[1] && isAllSelect[2] && isAllSelect[3]){  //所有选项均选过了，才可以进行下一步判定
                    //funcSaveNewCountdownInSharePre();  // 保存新倒计时的SharePre文件
                    if(funcSaveFactorInSharePre(this,user_country,user_province,user_occupa,user_year,
                            user_month,user_day)) {  //各个因素保存成功后再进行下一步计算
                        funcReadFactorFromSharePre(this);  //首先将各个要素写入FactorForLifespan
                        hourLeft = calcuRestHour();  //计算剩余时间
                        minuteLeft = calcuRestMinute();  //计算剩余分钟数
                        secondLeft = calcuRestSecond();  //计算剩余秒钟数
                        //Toast.makeText(this, "" + hourLeft + ":" + minuteLeft + ":" +secondLeft, Toast.LENGTH_SHORT).show();
                        if(funcSaveTimeInSharePre(this,hourLeft,minuteLeft,secondLeft)) {  //将剩余时间计算结果保存到 user_left_time
                            //弹出确认信息的对话框
                            funcConfirmInfo(initCountryStr+"-"+initProvinceStr+"\n"+initOccupStr+"\n"+initBirthStr);
                        }
                    } else {
                        //各个因素保存不成功
                        funcAlertInfo("Couldn't save data in local!");
                    }
                } else {
                    //有遗漏信息没有填写/选择
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

    //选择国家
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

    //选择省份
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

    //选择职业
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

    //选择生日
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

    //弹出确认信息对话框
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

    //弹出提示对话框
    private void funcAlertInfo(String msg) {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    //对于不同版本的安卓系统实现沉浸式体验
    private void setImmersiveMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            Utils.setStateBarColor(this);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentLayout = (ViewGroup) this.findViewById(android.R.id.content);
            View contentChild = contentLayout.getChildAt(0);
            contentChild.setFitsSystemWindows(false);  // 这里设置成侵占状态栏，反而不会侵占状态栏，太坑
            Utils.setupStatusBarView(this, contentLayout, Color.parseColor("#4A90E2"));
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
