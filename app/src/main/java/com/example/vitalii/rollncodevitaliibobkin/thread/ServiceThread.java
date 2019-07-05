package com.example.vitalii.rollncodevitaliibobkin.thread;

import android.annotation.SuppressLint;
import android.content.Intent;
import com.example.vitalii.rollncodevitaliibobkin.service.TimeService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.ACTION;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.CODE;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.CODE_COUNT_SHOW;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.CODE_TIME;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.COUNT;
import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.TIME;

public class ServiceThread implements Runnable {

    private boolean flag;
    private int count;
    private TimeService timeService;

    public ServiceThread(TimeService timeService, int count) {
        this.timeService = timeService;
        this.count = count;
        this.flag = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    public void run() {
        Intent intent = new Intent(ACTION);
        intent.putExtra(CODE, CODE_TIME);

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String time = df.format(Calendar.getInstance().getTime());

        intent.putExtra(TIME, time);
        timeService.sendBroadcast(intent);
        while(flag) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (flag) {
                count++;
                intent.putExtra(CODE, CODE_COUNT_SHOW);
                intent.putExtra(COUNT, count);
                timeService.sendBroadcast(intent);
            }
        }
    }

    public void stop() {
        this.flag = false;
    }
}