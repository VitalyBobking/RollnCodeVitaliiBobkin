package com.example.vitalii.rollncodevitaliibobkin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vitalii.rollncodevitaliibobkin.service.TimeService;

import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.ACTION;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.CODE;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.CODE_COUNT_SHOW;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.CODE_TIME;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.COUNT;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.TIME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTime, tvCount;

    private SharedPreferences sharedPreferences;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        initViews();
        initBroadcastReceiver();
    }

    private void initViews() {
        Button btnStart = findViewById(R.id.btnStart);
        Button btnStop = findViewById(R.id.btnStop);
        tvTime = findViewById(R.id.tvTime);
        tvCount = findViewById(R.id.tvCount);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        tvCount.setText(String.valueOf(getCountFromPreferences()));
        tvTime.setText(getTimeFromPreferences());
    }

    private void initBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int code = intent.getIntExtra(CODE, -1);
                switch (code) {
                    case CODE_TIME:
                        String date = intent.getStringExtra(TIME);
                        writeDataToPreferences(date, TIME);
                        tvTime.setText(date);
                        break;
                    case CODE_COUNT_SHOW:
                        tvCount.setText(String.valueOf(intent.getIntExtra(COUNT, -1)));
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private int getCountFromPreferences() {
        return sharedPreferences.getInt(COUNT, 0);
    }

    private String getTimeFromPreferences() {
        return sharedPreferences.getString(TIME, "0");
    }

    private void writeDataToPreferences(String time, String data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (data.equals(TIME)) {
            editor.putString(TIME, time);
        } else if (data.equals(COUNT)) {
            editor.putInt(COUNT, Integer.parseInt(time));
        }
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, TimeService.class);
        switch (view.getId()) {
            case R.id.btnStart:
                int count = getCountFromPreferences();
                startService(intent.putExtra(COUNT, count));
                break;
            case R.id.btnStop:
                stopService(intent);
                saveValuesBeforeDestroy();
                break;
        }
    }

    private void saveValuesBeforeDestroy() {
        String count = tvCount.getText().toString();
        writeDataToPreferences(count, COUNT);
    }
    @Override
    protected void onDestroy() {
        stopService(new Intent(this, TimeService.class));
        unregisterReceiver(broadcastReceiver);
        saveValuesBeforeDestroy();
        super.onDestroy();
    }
}