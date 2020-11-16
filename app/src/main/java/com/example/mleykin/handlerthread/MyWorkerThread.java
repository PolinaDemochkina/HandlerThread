package com.example.mleykin.handlerthread;

import android.os.HandlerThread;
import android.os.Handler;
import android.os.Message;

/**
 * Created by mleykin on 28.11.2017.
 */

public class MyWorkerThread extends HandlerThread {

    private Handler mWorkerHandler1;
    private Handler mWorkerHandler2;

    public MyWorkerThread(String name) {
        super(name);
    }

    public void postTask(Runnable task){
        mWorkerHandler1.post(task);
        mWorkerHandler1.sendMessage(Message.obtain());
        mWorkerHandler2.post(task);
        mWorkerHandler2.sendMessage(Message.obtain());
    }

    public void prepareHandler(){
        mWorkerHandler1 = new Handler(getLooper());
        mWorkerHandler2 = new CustomHandler(getLooper());
    }
}
