package com.example.mleykin.handlerthread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by mleykin on 28.11.2017.
 */

public class CustomHandler extends Handler {

    Looper mLooper;

    public CustomHandler (Looper looper) {
        mLooper = looper;
    }

    public void handleMessage(Message msg) {
        Log.d("CustomHandler", "!!!!");
    }
}
