package com.example.john.timegone;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import java.util.Calendar;

import android.widget.Toast;

import com.example.john.timegone.Activity.BaseActivity;
import com.example.john.timegone.Data.DateTime;
import com.example.john.timegone.Tools.Utils;

import static com.example.john.timegone.Tools.Utils.funcFreshNewCountdownTimeInSharePre;
import static com.example.john.timegone.Tools.Utils.funcFreshTimeInSharePre;
import static com.example.john.timegone.Tools.Utils.funcGetDateTime;
import static com.example.john.timegone.Tools.Utils.funcReadFactorFromSharePre;
import static com.example.john.timegone.Tools.Utils.funcReadNewCountdownFromSharePre;
import static com.example.john.timegone.Tools.Utils.funcReadTimeFromSharePre;
import static com.example.john.timegone.Tools.Utils.funcRecordCurrentTime;
import static java.lang.Long.parseLong;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private FloatingActionButton newFunctionBtn;  //额外功能菜单
    private int functionOption;  //额外功能菜单选项：1 -- 新的倒计时；2 -- 删除倒计时

    /**
     * Life Countdown
     */
    //生命倒数的  时:分:秒:毫秒  TextView
    private TextView timeHourShowTextView, timeMiniteShowTextView, timeSecondShowTextView, timeMileSecondShowTextView;

    private long hourLeftTmp, minuteLeftTmp, secondLeftTmp;  //将计算结果保存下来以更新倒计时
    private long initTimeLeft;  //退出后再次进入时的初始化时间，即第一次循环的总时长，本次循环结束后转入常规循环；每次循环使得总时长减一

    /**
     * New Countdown
     */
    private LinearLayout newCountDownHolder;
    private TextView newCountHourShowTextView, newCountMinuteShowTextView, newCountSecondShowTextView, newCountMileSecondShowTextView;

    private String newCountDownName;
    private long ifExistNewCountdown;  //local variate
    //新设置新的倒计时的时分秒
    private long newHourTmp, newMinuteTmp, newSecondTmp;

    private long newCountDownMileTime, newLeftHour;
    private long newHour, newMinute, newSecond;
    private long initNewTimeLeft;


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
        funcReadFactorFromSharePre(this);  //每次重新登录后将各个因素从"user_lifespan_factor"写入Data.FactorForLifespan

        //hourLeftTmp = 1;   //Test for life's end.
        //从"user_left_time"读取上次退出时的显示的时:分:秒
        hourLeftTmp = funcReadTimeFromSharePre(this, "hourLeft");
        minuteLeftTmp = funcReadTimeFromSharePre(this, "minuteLeft");
        secondLeftTmp = funcReadTimeFromSharePre(this, "secondLeft");
        //从"last_exit_time"读取上次退出时的时间，进行新的时间计算（从总时长里减去APP没有运行这段时间）
        SharedPreferences timePref = getSharedPreferences("last_exit_time",MODE_PRIVATE);
        if (timePref != null) {  //避免第一次进入读取的"last_exit_time"为空产生错误
            long recordMileSecond = timePref.getLong("exitTime", -1L);
            if (recordMileSecond != -1L) {
                long passMileSecond = System.currentTimeMillis() - recordMileSecond;  //没有运行APP期间的时间段

                //校准时:分:秒
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
        //计算第一次循环的初始化时间(不足一小时)
        initTimeLeft = (minuteLeftTmp*60+secondLeftTmp)*1000;
    }

    private void initView() {
        //生命倒计时
        timeHourShowTextView = (TextView) findViewById(R.id.TimeHourShowTxt);
        timeMiniteShowTextView = (TextView) findViewById(R.id.TimeMiniteShowTxt);
        timeSecondShowTextView = (TextView) findViewById(R.id.TimeSecondShowTxt);
        timeMileSecondShowTextView = (TextView) findViewById(R.id.TimeMileSecondShowTxt);

        //初始赋值时:分:秒
        timeHourShowTextView.setText(hourLeftTmp + ":");
        timeMiniteShowTextView.setText(minuteLeftTmp + ":");
        timeSecondShowTextView.setText(secondLeftTmp + ":");

        //新的倒计时
        newFunctionBtn = (FloatingActionButton) findViewById(R.id.fa_btn);

        newCountDownHolder = (LinearLayout) findViewById(R.id.newCountDownHolder);
        newCountHourShowTextView = (TextView) findViewById(R.id.newCountDownHourShowTxt);
        newCountMinuteShowTextView = (TextView) findViewById(R.id.newCountDownMinuteShowTxt);
        newCountSecondShowTextView = (TextView) findViewById(R.id.newCountDownSecondShowTxt);
        newCountMileSecondShowTextView = (TextView) findViewById(R.id.newCountDownMileSecondShowTxt);

        //初始化新的倒计时的控件后才能进行计算等后续操作
        SharedPreferences ifExistNewCountdown = getSharedPreferences("new_countdown",MODE_PRIVATE);
        if (ifExistNewCountdown != null) {
            funcIfExistNewCD();
        }
    }

    private void initEvent()
    {
        //初始化生命倒数的第一次循环
        initCountDownTimer();
        newFunctionBtn.setOnClickListener(this);
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

    //检查之前是否有新的倒计时存在
    private void funcIfExistNewCD() {
        //ifExistNewCountdown == 1 ----> Exist
        //ifExistNewCountdown == 0 ----> Don't exist
        ifExistNewCountdown = funcReadNewCountdownFromSharePre("flag", this);
        if (ifExistNewCountdown == 1) {
            //设置计时器可见
            newCountDownHolder.setVisibility(View.VISIBLE);

            newHourTmp = funcReadNewCountdownFromSharePre("newCountdownHour", this);
            newMinuteTmp = funcReadNewCountdownFromSharePre("newCountdownMinute", this);
            newSecondTmp = funcReadNewCountdownFromSharePre("newCountdownSecond", this);
            //Log.d("TAGTAG", newHourTmp + "%" + newMinuteTmp + "&" + newSecondTmp);

            //校准时:分:秒
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
                    //Log.d("TAGTA", newHourTmp + "%" + newMinuteTmp + "&" + newSecondTmp);
                }
            }

            initNewTimeLeft = (newMinuteTmp*60+newSecondTmp)*1000;

            //初始化计时器的显示
            newCountHourShowTextView.setText(newHourTmp+":");
            newCountMinuteShowTextView.setText(newMinuteTmp+":");
            newCountSecondShowTextView.setText(newSecondTmp+":");
            //开始新倒计时的初始循环
            startInitNewCountDownTimer();
        }
        //之前不存在新的倒计时，不需要处理
        //...
    }

    //判断是否能创建新的倒计时（只能同时存在一个新的倒计时）
    private void funcNewOrDeleteDown() {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Menu");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (functionOption == 1) {
                    SharedPreferences ifExist = getSharedPreferences("new_countdown",MODE_PRIVATE);
                    int a = ifExist.getInt("flag", -1);
                    if(a == 1){  //a == 1说明已经存在新的倒计时
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
                functionOption = which;
            }
        });
        builder.show();
    }

    //设置新的倒计时的相关参数
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

    private void funcSetDeadline() {
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
     * 设置新的倒计时的时间参数，对应于生命倒计时的GuideActivity部分
     */
    private void funcInitNewCountDownData() {
        long duration = newCountDownMileTime - System.currentTimeMillis();

        newHour = duration/1000/60/60;
        duration = duration%(1000*60*60);
        newMinute = (duration)/1000/60;
        duration = duration%(1000*60);
        newSecond = duration/1000;
        Log.d("TAGB", newHour + "/" + newMinute + "/" + newSecond);

        DateTime dt = funcGetDateTime();
        newLeftHour = newHour - dt.getHour();

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
     * Start Life countdown's normal circle.
     */
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
     * Start new countdown's normal circle.
     */
    private void startNewCountDownTimer() {
        newLeftHour -= 1;
        newCountHourShowTextView.setText(newLeftHour+":");
        if (newLeftHour == -1) {
            newCountHourShowTextView.setText("-:");
            newCountMinuteShowTextView.setText("-:");
            newCountSecondShowTextView.setText("-:");
            newCountMileSecondShowTextView.setText("-");
            //新的倒计时结束后修改"new_countdown"的标志位"flag"，"删除"本次倒计时
            SharedPreferences.Editor editor = getSharedPreferences("new_countdown",MODE_PRIVATE).edit();
            editor.putInt("flag",0);  //若不设置新的倒计时，则下次启动时不再显示子倒计时模块
            editor.apply();
        } else {
            newCountMiniteTimer.start();
            newCountSecondTimer.start();
            newCountMileSecondTimer.start();
        }
    }

    /**
     * Life countTimer's initial circle
     */
    private void initCountDownTimer() {

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

    /**
     * Life countTimer's normal circle
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
     * New CountTimer's initial circle
     */
    private void startInitNewCountDownTimer() {

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

    /**
     * New countTimer's normal circle
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
        //退出时保存生命倒数的时:分:秒
        String str1 = timeHourShowTextView.getText().toString();
        long hourLeftNow = parseLong(str1.substring(0,str1.length()-1));  //去除分隔符:
        String str2 = timeMiniteShowTextView.getText().toString();
        long minuteLeftNow = parseLong(str2.substring(0,str2.length()-1));
        String str3 = timeSecondShowTextView.getText().toString();
        long secondLeftNow = parseLong(str3.substring(0,str3.length()-1));

        funcFreshTimeInSharePre(this, hourLeftNow, minuteLeftNow, secondLeftNow);

        //若有新的倒计时存在就保存新的倒计时当前显示的时:分:秒
        if(ifExistNewCountdown == 1) {
            String str4 = newCountHourShowTextView.getText().toString();
            long newCountDownHourNow = parseLong(str4.substring(0,str4.length()-1));  //去除分隔符:
            String str5 = newCountMinuteShowTextView.getText().toString();
            long newCountDownHourMinuteNow = parseLong(str5.substring(0,str5.length()-1));
            String str6 = newCountSecondShowTextView.getText().toString();
            long newCountDownHourSecondNow = parseLong(str6.substring(0,str6.length()-1));

            funcFreshNewCountdownTimeInSharePre(this, newCountDownHourNow, newCountDownHourMinuteNow, newCountDownHourSecondNow);
        }

        //保存退出时间
        funcRecordCurrentTime(this);
        //结束当前Activity/App
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
            Utils.setupStatusBarView(this, contentLayout, Color.parseColor(R.color.background_color_main + ""));
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
