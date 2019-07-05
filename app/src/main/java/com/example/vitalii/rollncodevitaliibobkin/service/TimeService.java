package com.example.vitalii.rollncodevitaliibobkin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.vitalii.rollncodevitaliibobkin.MainActivity;
import com.example.vitalii.rollncodevitaliibobkin.thread.ServiceThread;

import static com.example.vitalii.rollncodevitaliibobkin.constants.Constant.COUNT;


public class TimeService extends Service {

    private ServiceThread serviceThread;

    @Override
    public void onCreate() {
        Log.d("TAG", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "onStartCommand");
        if(startId != 1) {
            serviceThread.stop();
        }
        int count = intent.getIntExtra(COUNT, 0);
        serviceThread = new ServiceThread(this, count);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TAG", "onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        serviceThread.stop();
        super.onDestroy();
    }
}