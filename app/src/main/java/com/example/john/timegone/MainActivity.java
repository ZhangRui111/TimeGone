package com.example.john.timegone;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.Calendar;

import android.widget.Toast;

import static com.example.john.timegone.Tools.Utils.funcReadFactorFromSharePre;
import static java.lang.Long.parseLong;

public class MainActivity extends AppCompatActivity {

    private TextView timeHourShowTextView, timeMiniteShowTextView, timeSecondShowTextView, timeMileSecondShowTextView;
    private TextView saying;

    private long hourLeftTmp, minuteLeftTmp, secondLeftTmp;  //将计算结果保存下来以更新倒计时
    private long initTimeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        initEvent();
    }

    private void initData() {
        funcReadFactorFromSharePre(this);

        //Toast.makeText(this, "" + secondLeft, Toast.LENGTH_SHORT).show();

        hourLeftTmp = funcReadTimeFromSharePre("hourLeft");
        //hourLeftTmp = 2;   //Test for life's end.
        minuteLeftTmp = funcReadTimeFromSharePre("minuteLeft");
        secondLeftTmp = funcReadTimeFromSharePre("secondLeft");
        SharedPreferences timePref = getSharedPreferences("last_exit_time",MODE_PRIVATE);
        if (timePref != null) {
            long recordMileSecond = timePref.getLong("exitTime", -1L);
            if (recordMileSecond != -1L) {
                //Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
                long passMileSecond = System.currentTimeMillis() - recordMileSecond;
                //Log.d("TAGG", passMileSecond+"");

                hourLeftTmp -= passMileSecond/1000/60/60;
                //Log.d("TAGG", passMileSecond/1000/60/60+"");
                passMileSecond = passMileSecond%(1000*60*60);
                minuteLeftTmp -= (passMileSecond)/1000/60;
                //Log.d("TAGG", (passMileSecond)/1000/60 +"");
                passMileSecond = passMileSecond%(1000*60);
                secondLeftTmp -= passMileSecond/1000;
                //Log.d("TAGG", ""+passMileSecond);
            }
        }

        initTimeLeft = (minuteLeftTmp*60+secondLeftTmp)*1000;
    }

    private void initView() {
        timeHourShowTextView = (TextView) findViewById(R.id.TimeHourShowTxt);
        timeMiniteShowTextView = (TextView) findViewById(R.id.TimeMiniteShowTxt);
        timeSecondShowTextView = (TextView) findViewById(R.id.TimeSecondShowTxt);
        timeMileSecondShowTextView = (TextView) findViewById(R.id.TimeMileSecondShowTxt);

        saying = (TextView) findViewById(R.id.SayingShowTxt);

        timeHourShowTextView.setText(hourLeftTmp + ":");
        timeMiniteShowTextView.setText(minuteLeftTmp + ":");
        timeSecondShowTextView.setText(secondLeftTmp + ":");
    }

    private void initEvent() {
        initCountDownTimer();
    }

    private long funcReadTimeFromSharePre(String which) {
        SharedPreferences pref = getSharedPreferences("user_left_time",MODE_PRIVATE);
        long result = -1;
        switch (which) {
            case "id":
                result = 0;
                break;
            case "user_name":
                result = 0;
                break;
            case "hourLeft":
                result = pref.getLong("hourLeft", -1L);
                break;
            case "minuteLeft":
                result = pref.getLong("minuteLeft", -1L);
                break;
            case "secondLeft":
                result = pref.getLong("secondLeft", -1L);
                break;
            default:
                break;
        }
        return result;
    }

    private void initCountDownTimer() {
        /**
         * Init countTimer
         */

        //如果CountDownTimer的第一个参数是变量，那么定义声明new CountDownTimer()就不能放到外面
        CountDownTimer initCountMiniteTimer = new CountDownTimer(initTimeLeft, 1000) {

            @Override
            public void onTick(long initTimeLeft) {
                timeMiniteShowTextView.setText(((initTimeLeft/60/1000)%60) + ":");
            }

            @Override
            public void onFinish() {
                startCountDownTimer();  //初始化循环结束后调用正常循环
            }
        };

        CountDownTimer initCountSecondTimer = new CountDownTimer(initTimeLeft, 1000) {

            @Override
            public void onTick(long initTimeLeft) {
                timeSecondShowTextView.setText(((initTimeLeft/1000)%60) + ":");
            }

            @Override
            public void onFinish() {}
        };

        CountDownTimer initCountMileSecondTimer = new CountDownTimer(initTimeLeft, 100) {

            @Override
            public void onTick(long initTimeLeft) {
                timeMileSecondShowTextView.setText(((initTimeLeft/100)%10) + "");
            }

            @Override
            public void onFinish() {}
        };

        initCountMiniteTimer.start();
        initCountSecondTimer.start();
        initCountMileSecondTimer.start();
    }

    private void startCountDownTimer() {
        //每过一个小时重新启动倒计时以解决CountDownTimer的参数类型为long，不够大的问题
        hourLeftTmp = hourLeftTmp -1;
        timeHourShowTextView.setText(hourLeftTmp + ":");
        if(hourLeftTmp == -1) {
            timeHourShowTextView.setText("-:");
            timeMiniteShowTextView.setText("-:");
            timeSecondShowTextView.setText("-:");
            timeMileSecondShowTextView.setText("-");
        } else {
            countMiniteTimer.start();
            countSecondTimer.start();
            countMileSecondTimer.start();
        }
    }

    /**
     * Setup countTimer
     */

    private CountDownTimer countMiniteTimer = new CountDownTimer(3600000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeMiniteShowTextView.setText(((millisUntilFinished/60/1000)%60) + ":");
        }

        @Override
        public void onFinish() {
            startCountDownTimer();
        }
    };

    private CountDownTimer countSecondTimer = new CountDownTimer(3600000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeSecondShowTextView.setText(((millisUntilFinished/1000)%60) + ":");
        }

        @Override
        public void onFinish() {}
    };

    private CountDownTimer countMileSecondTimer = new CountDownTimer(3600000, 100) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeMileSecondShowTextView.setText("" + ((millisUntilFinished/100)%10));
        }

        @Override
        public void onFinish() {}
    };


    /**
     * 监听退出APP
     */
    @Override
    public void onBackPressed() {
        Log.i("TAG", "Exit");
        String str1 = timeHourShowTextView.getText().toString();
        long hourLeftNow = parseLong(str1.substring(0,str1.length()-1));  //去除分隔符:
        String str2 = timeMiniteShowTextView.getText().toString();
        long minuteLeftNow = parseLong(str2.substring(0,str2.length()-1));
        String str3 = timeSecondShowTextView.getText().toString();
        long secondLeftNow = parseLong(str3.substring(0,str3.length()-1));

        funcFreshTimeInSharePre(hourLeftNow, minuteLeftNow, secondLeftNow);
        funcRecordCurrentTime();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else { // 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 更新剩余时间
     * @return
     */
    private boolean funcFreshTimeInSharePre(long h, long m, long s) {
        Boolean result;
        try {
            SharedPreferences.Editor editor = getSharedPreferences("user_left_time",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putLong("hourLeft",h);
            editor.putLong("minuteLeft",m);
            editor.putLong("secondLeft",s);
            editor.commit();
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    private void funcRecordCurrentTime() {

        SharedPreferences.Editor editor = getSharedPreferences("last_exit_time",MODE_PRIVATE).edit();
        editor.putInt("id",0);
        editor.putString("user_name","Tom");
        editor.putLong("exitTime",System.currentTimeMillis());
        editor.apply();
    }
}
