package com.example.john.timegone.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.john.timegone.MainActivity;
import com.example.john.timegone.R;

/**
 * Created by john on 2017/9/3.
 */

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // 判断是否进入向导界面还是主界面
        SharedPreferences pref = getSharedPreferences("if_main_page",MODE_PRIVATE);
        if (pref != null) {  //"if_main_page"存在
            int enter = pref.getInt("enter", -1);
            if (enter == 1) {
                //pref存在且标志位enter为1，直接进入主页面
                Intent main = new Intent(InitActivity.this,MainActivity.class);
                startActivity(main);//主界面
                finish();//销毁初始化界面
            } else {
                Intent guide = new Intent(InitActivity.this,GuideActivity.class);
                startActivity(guide);
                finish();//销毁初始化界面
            }
        } else {
            Intent guide = new Intent(InitActivity.this,GuideActivity.class);
            startActivity(guide);
            finish();//销毁初始化界面
        }
    }
}
