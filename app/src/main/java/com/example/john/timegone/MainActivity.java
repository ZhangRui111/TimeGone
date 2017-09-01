package com.example.john.timegone;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView timeHourShowTextView, timeMiniteShowTextView, timeSecondShowTextView, timeMileSecondShowTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        timeHourShowTextView = (TextView) findViewById(R.id.TimeHourShowTxt);
        timeMiniteShowTextView = (TextView) findViewById(R.id.TimeMiniteShowTxt);
        timeSecondShowTextView = (TextView) findViewById(R.id.TimeSecondShowTxt);
        timeMileSecondShowTextView = (TextView) findViewById(R.id.TimeMileSecondShowTxt);

        countHourTimer.start();
        countMiniteTimer.start();
        countSecondTimer.start();
        countMileSecondTimer.start();
    }

    private CountDownTimer countHourTimer = new CountDownTimer(7200000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeHourShowTextView.setText(((millisUntilFinished/3600/1000)%24) + ":");
        }

        @Override
        public void onFinish() {

        }
    };

    private CountDownTimer countMiniteTimer = new CountDownTimer(7200000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeMiniteShowTextView.setText(((millisUntilFinished/60/1000)%60) + ":");
        }

        @Override
        public void onFinish() {

        }
    };

    private CountDownTimer countSecondTimer = new CountDownTimer(7200000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeSecondShowTextView.setText(((millisUntilFinished/1000)%60) + ":");
        }

        @Override
        public void onFinish() {

        }
    };

    private CountDownTimer countMileSecondTimer = new CountDownTimer(7200000, 100) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeMileSecondShowTextView.setText("" + ((millisUntilFinished/100)%10));
        }

        @Override
        public void onFinish() {
            timeHourShowTextView.setText("");
            timeMiniteShowTextView.setText("");
            timeSecondShowTextView.setText(R.string.counttimer_clues_end);
            timeMileSecondShowTextView.setText("");
        }
    };
}
