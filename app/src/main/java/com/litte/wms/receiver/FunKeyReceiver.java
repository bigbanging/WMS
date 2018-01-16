package com.litte.wms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FunKeyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");

        boolean defaultdown = false;
        int keycode = intent.getIntExtra("keycode", 0);
        boolean keydown = intent.getBooleanExtra("keydown", defaultdown);
        Log.i("ServiceDemo", "receiver:keycode=" + keycode + "keydown=" + keydown);
        //左侧下按键
        if (keycode == 133 && keydown) {
//            sendCmd();//---------------------------------------------------
        }
        //右侧按键
        if (keycode == 134 && keydown) {
//            sendCmd();//---------------------------------------------------
        }

        if (keycode == 131 && keydown) {
//	        	Toast.makeText(getApplicationContext(), "这是F1按键", 0).show();
        }

        if (keycode == 132 && keydown) {
//	        	Toast.makeText(getApplicationContext(), "这是F2按键", 0).show();
        }
    }
}
