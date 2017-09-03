package com.example.john.timegone.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.john.timegone.MainActivity;
import com.example.john.timegone.R;

import java.util.Calendar;

/**
 * Created by john on 2017/9/3.
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startMainActBtn;
    private Button selectCountryBtn, selectProvinceBtn, selectOccupBtn, selectBirthBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
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
     * 选择相关信息以计算剩余时间
     */
    private void funcSelectCountryDialog() {
        final String countryList[] = new String[]{"中国"};
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select your country");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.setSingleChoiceItems(countryList, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectCountryBtn.setText(countryList[0]);
            }
        });
        builder.show();
    }

    private void funcSelectProvinceDialog() {
/*        final String countryList[] = new String[]{"北京市", "天津市", "河北省", "山西省", "内蒙古自治区", "辽宁省",
                "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省",
                "湖南省", "广东省", "广西壮族自治区", "海南省", "重庆市", "四川省", "贵州省", "云南省", "西藏自治区",
                "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "台湾省", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区}";*/
        final String provinceList[] = new String[]{"北京", "天津", "河北", "山西", "内蒙古", "辽宁",
                "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北",
                "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃",
                "青海", "宁夏", "台湾", "新疆", "香港", "澳门}"};
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select your province");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.setSingleChoiceItems(provinceList, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectProvinceBtn.setText(provinceList[which]);
            }
        });
        builder.show();
    }

    private void funcSelectOccupDialog() {
        final String occupList[] = new String[]{"程序员", "产品经理", "大佬", "学生"};
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select your occupation");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.setSingleChoiceItems(occupList, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectOccupBtn.setText(occupList[which]);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //因为monthOfYear会比实际月份少一月所以这边要加1
                birthday.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                selectBirthBtn.setText(birthday);
            }
        }, year, month, day);
        //弹出选择日期对话框
        datePickerDialog.show();
    }

}
