package com.example.john.timegone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.john.timegone.Interface.MyConstants;
import com.example.john.timegone.MainActivity;
import com.example.john.timegone.R;
import com.example.john.timegone.Tools.SpTools;

/**
 * Created by john on 2017/9/3.
 */

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // 判断是否进入向导界面还是主界面
        if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, false)){
            //true，设置过，直接进入主界面
            Intent main = new Intent(InitActivity.this,MainActivity.class);
            startActivity(main);//主界面
            finish();//销毁初始化界面
        } else {
            //false，没设置过，进入设置向导界面
            SpTools.setBoolean(getApplicationContext(), MyConstants.ISSETUP, true);
            Intent intent = new Intent(InitActivity.this,GuideActivity.class);
            startActivity(intent);//向导界面
            finish();//销毁初始化界面
        }
    }
}
