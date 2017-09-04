package com.example.john.timegone;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;

import android.widget.Toast;

import com.example.john.timegone.Tools.Utils;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import static com.example.john.timegone.Tools.Utils.funcReadFactorFromSharePre;
import static com.example.john.timegone.Tools.Utils.funcReadNewCountdownFromSharePre;
import static java.lang.Long.parseLong;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timeHourShowTextView, timeMiniteShowTextView, timeSecondShowTextView, timeMileSecondShowTextView;
    private TextView saying;

    private long hourLeftTmp, minuteLeftTmp, secondLeftTmp;  //将计算结果保存下来以更新倒计时
    private long initTimeLeft;

    private FloatingActionButton newCountDownBtn;

    private LinearLayout newCountDownHolder;
    private TextView newCountHourShowTextView, newCountMinuteShowTextView, newCountSecondShowTextView, newCountMileSecondShowTextView;
    private String newCountDownName;
    private long newCountDownMileTime, newLeftHour;
    private long newHour, newMinute, newSecond, newMileSecond;
    private long newHourTmp, newMinuteTmp, newSecondTmp;
    private long initNewTimeLeft;
    private int flag;  //1 -- 新的倒计时；2 -- 删除倒计时
    private long ifExistNewCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setImmersiveMode();

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

        newCountDownBtn = (FloatingActionButton) findViewById(R.id.fa_btn);

        newCountDownHolder = (LinearLayout) findViewById(R.id.newCountDownHolder);
        newCountHourShowTextView = (TextView) findViewById(R.id.newCountDownHourShowTxt);
        newCountMinuteShowTextView = (TextView) findViewById(R.id.newCountDownMinuteShowTxt);
        newCountSecondShowTextView = (TextView) findViewById(R.id.newCountDownSecondShowTxt);
        newCountMileSecondShowTextView = (TextView) findViewById(R.id.newCountDownMileSecondShowTxt);

        funcIfExistNewCD();
    }

    private void initEvent() {
        initCountDownTimer();

        newCountDownBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fa_btn:
                funcNewOrDeleteDown();
                break;
            default:
                break;
        }
    }
/*    SharedPreferences.Editor editor = getSharedPreferences("new_countdown",MODE_PRIVATE).edit();
    editor.putInt("id",0);
    editor.putString("user_name","Tom");
    editor.putInt("flag", 1);
    editor.putLong("newCountdownHour",newCountDownHourNow);
    editor.putLong("newCountdownMinute",newCountDownHourMinuteNow);
    editor.putLong("newCountdownSecond",newCountDownHourSecondNow);
    Log.d("TAGG", newCountDownHourNow+"*"+newCountDownHourMinuteNow+"&"+newCountDownHourSecondNow+";");
    editor.apply();*/

    private void funcIfExistNewCD() {
        ifExistNewCountdown = funcReadNewCountdownFromSharePre("flag", this);
        if (ifExistNewCountdown == 1) {
            newCountDownHolder.setVisibility(View.VISIBLE);

            newHourTmp = funcReadNewCountdownFromSharePre("newCountdownHour", this);
            newMinuteTmp = funcReadNewCountdownFromSharePre("newCountdownMinute", this);
            newSecondTmp = funcReadNewCountdownFromSharePre("newCountdownSecond", this);
            Log.d("TAGTAG", newHourTmp + "%" + newMinuteTmp + "&" + newSecondTmp);

            SharedPreferences timePref = getSharedPreferences("last_exit_time",MODE_PRIVATE);
            if (timePref != null) {
                long recordMileSecond = timePref.getLong("exitTime", -1L);
                if (recordMileSecond != -1L) {
                    long passMileSecond = System.currentTimeMillis() - recordMileSecond;

                    newHourTmp -= passMileSecond/1000/60/60;
                    passMileSecond = passMileSecond%(1000*60*60);
                    newMinuteTmp -= (passMileSecond)/1000/60;
                    passMileSecond = passMileSecond%(1000*60);
                    newSecondTmp -= passMileSecond/1000;
                    Log.d("TAGTA", newHourTmp + "%" + newMinuteTmp + "&" + newSecondTmp);
                }
            }

            initNewTimeLeft = (newMinuteTmp*60+newSecondTmp)*1000;
            startInitNewCountDownTimer();
        }
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

    private void startInitNewCountDownTimer() {
        /**
         * Init New CountTimer
         */
        newCountHourShowTextView.setText(newHourTmp+":");
        newCountMinuteShowTextView.setText(newMinuteTmp+":");
        newCountSecondShowTextView.setText(newSecondTmp+":");
        //如果CountDownTimer的第一个参数是变量，那么定义声明new CountDownTimer()就不能放到外面
        CountDownTimer initNewCountMiniteTimer = new CountDownTimer(initNewTimeLeft, 1000) {

            @Override
            public void onTick(long initNewTimeLeft) {
                newCountMinuteShowTextView.setText(((initNewTimeLeft/60/1000)%60) + ":");
            }

            @Override
            public void onFinish() {
                startNewCountDownTimer();  //初始化循环结束后调用正常循环
            }
        };

        CountDownTimer initNewCountSecondTimer = new CountDownTimer(initNewTimeLeft, 1000) {

            @Override
            public void onTick(long initNewTimeLeft) {
                newCountSecondShowTextView.setText(((initNewTimeLeft/1000)%60) + ":");
            }

            @Override
            public void onFinish() {}
        };

        CountDownTimer initNewCountMileSecondTimer = new CountDownTimer(initNewTimeLeft, 100) {

            @Override
            public void onTick(long initNewTimeLeft) {
                newCountMileSecondShowTextView.setText(((initNewTimeLeft/100)%10) + "");
            }

            @Override
            public void onFinish() {}
        };

        initNewCountMiniteTimer.start();
        initNewCountSecondTimer.start();
        initNewCountMileSecondTimer.start();
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

    private void funcNewOrDeleteDown() {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("New CountDown");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (flag == 1) {
                    SharedPreferences pref = getSharedPreferences("new_countdown",MODE_PRIVATE);
                    int a = pref.getInt("flag", -1);
                    if(a == 1){
                        Toast.makeText(MainActivity.this, "请先删除现有倒计时", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = getSharedPreferences("new_countdown",MODE_PRIVATE).edit();
                        editor.putInt("flag", 1);
                        editor.apply();
                        ifExistNewCountdown = funcReadNewCountdownFromSharePre("flag", MainActivity.this);
                        funcNewCountDown();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "暂未开通此功能", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setSingleChoiceItems(new String[]{"-", "新的倒计时", "删除"}, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                flag = which;
            }
        });
        builder.show();
    }

    private void funcNewCountDown() {
        AlertDialog.Builder newbuilder = new android.support.v7.app.AlertDialog.Builder(this);
        newbuilder.setTitle("Note");
        final EditText input = new EditText(this);
        input.setHint("比如:考研");
        newbuilder.setView(input);
        newbuilder.setNegativeButton("取消", null);
        newbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newCountDownName = input.getText().toString();
                if (newCountDownName.equals("")){
                    newCountDownName = "未命名";
                }
                dialog.dismiss();
                funcSetDeadline();
            }
        });
        AlertDialog dialog = newbuilder.create();
        dialog.show();
    }

    /**
     * 设置新计划的Deadline
     */
    private void funcSetDeadline() {
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
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                newCountDownMileTime = calendar.getTimeInMillis();
                //birthday.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                newCountDownHolder.setVisibility(View.VISIBLE);
                funcInitNewCountDownData();
            }
        }, year, month, day);
        //设置生日的上限是今天，下限是100年前
        calendar.add(Calendar.YEAR, +10);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        //弹出选择日期对话框
        datePickerDialog.show();
    }

    /**
     * 新的倒计时
     */
    private void funcInitNewCountDownData() {
        long duration = newCountDownMileTime - System.currentTimeMillis();

        newHour = duration/1000/60/60;
        duration = duration%(1000*60*60);
        newMinute = (duration)/1000/60;
        duration = duration%(1000*60);
        newSecond = duration/1000;
        Log.d("TAGB", newHour + "/" + newMinute + "/" + newSecond);

        newLeftHour = newHour +1;

        newCountHourShowTextView.setText(newHour+":");
        newCountMinuteShowTextView.setText(newMinute+":");
        newCountSecondShowTextView.setText(newSecond+":");

        startNewCountDownTimer();

        SharedPreferences.Editor editor = getSharedPreferences("new_countdown",MODE_PRIVATE).edit();
        editor.putInt("id",0);
        editor.putString("user_name","Tom");
        editor.putInt("flag",1);
        editor.putString("name",newCountDownName);
        editor.putLong("newCountdownHour",newHour);
        editor.putLong("newCountdownMinute",newMinute);
        editor.putLong("newCountdownSecond",newSecond);
        editor.apply();
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

    private void startNewCountDownTimer() {
        newLeftHour -= 1;
        newCountHourShowTextView.setText(newLeftHour+":");
        if (newLeftHour == -1) {
            newCountHourShowTextView.setText("-:");
            newCountMinuteShowTextView.setText("-:");
            newCountSecondShowTextView.setText("-:");
            newCountMileSecondShowTextView.setText("-");
        } else {
            newCountMiniteTimer.start();
            newCountSecondTimer.start();
            newCountMileSecondTimer.start();
        }
    }

    /**
     * New countTimer
     */

    private CountDownTimer newCountMiniteTimer = new CountDownTimer(3600000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            newCountMinuteShowTextView.setText(((millisUntilFinished/60/1000)%60) + ":");
        }

        @Override
        public void onFinish() {
            startNewCountDownTimer();
        }
    };

    private CountDownTimer newCountSecondTimer = new CountDownTimer(3600000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            newCountSecondShowTextView.setText(((millisUntilFinished/1000)%60) + ":");
        }

        @Override
        public void onFinish() {}
    };

    private CountDownTimer newCountMileSecondTimer = new CountDownTimer(3600000, 100) {

        @Override
        public void onTick(long millisUntilFinished) {
            newCountMileSecondShowTextView.setText("" + ((millisUntilFinished/100)%10));
        }

        @Override
        public void onFinish() {}
    };


    /**
     * 监听退出APP
     */
    @Override
    public void onBackPressed() {
        String str1 = timeHourShowTextView.getText().toString();
        long hourLeftNow = parseLong(str1.substring(0,str1.length()-1));  //去除分隔符:
        String str2 = timeMiniteShowTextView.getText().toString();
        long minuteLeftNow = parseLong(str2.substring(0,str2.length()-1));
        String str3 = timeSecondShowTextView.getText().toString();
        long secondLeftNow = parseLong(str3.substring(0,str3.length()-1));

        funcFreshTimeInSharePre(hourLeftNow, minuteLeftNow, secondLeftNow);
        funcRecordCurrentTime();


        if(ifExistNewCountdown == 1) {
            String str4 = newCountHourShowTextView.getText().toString();
            long newCountDownHourNow = parseLong(str4.substring(0,str4.length()-1));  //去除分隔符:
            String str5 = newCountMinuteShowTextView.getText().toString();
            long newCountDownHourMinuteNow = parseLong(str5.substring(0,str5.length()-1));
            String str6 = newCountSecondShowTextView.getText().toString();
            long newCountDownHourSecondNow = parseLong(str6.substring(0,str6.length()-1));

            funcFreshNewCountdownTimeInSharePre(newCountDownHourNow, newCountDownHourMinuteNow, newCountDownHourSecondNow);
        }
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

    /**
     * 更新新的倒计时的剩余时间
     * @param newCountDownHourNow
     * @param newCountDownHourMinuteNow
     * @param newCountDownHourSecondNow
     */
    private Boolean funcFreshNewCountdownTimeInSharePre(long newCountDownHourNow, long newCountDownHourMinuteNow, long newCountDownHourSecondNow) {
        Boolean result;
        try {
            SharedPreferences.Editor editor = getSharedPreferences("new_countdown",MODE_PRIVATE).edit();
            editor.putInt("id",0);
            editor.putString("user_name","Tom");
            editor.putInt("flag", 1);
            editor.putLong("newCountdownHour",newCountDownHourNow);
            editor.putLong("newCountdownMinute",newCountDownHourMinuteNow);
            editor.putLong("newCountdownSecond",newCountDownHourSecondNow);
            Log.d("TAGG", newCountDownHourNow+"*"+newCountDownHourMinuteNow+"&"+newCountDownHourSecondNow+";");
            editor.apply();
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
            Utils.setupStatusBarView(this, contentLayout, Color.parseColor(R.color.background_color_main + ""));
            //隐藏底部导航栏
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;  此时将同时隐藏状态栏
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
