package com.litte.wms.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.scandemo.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class ScanService extends Service {

    private SerialPort mSerialPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private MyReceiver myReceive;
    private ReadThread mReadThread;
    private Timer sendData;
    private Timer scan100ms;
    public String activity = null;
    public String data;
    public StringBuffer data_buffer = new StringBuffer();  //接收数据缓冲
    private boolean run = true;  //线程标识
    private boolean run_scan100ms = false;  //每100ms扫描标识
    private Timer timeout = null;

    public String TAG = "ScanService";  //Debug
    public ScanService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        init();
    }

    private void init() {
        try {
            mSerialPort = new SerialPort(0, 9600, 0);// scaner
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSerialPort.scaner_poweron();

        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();

        myReceive = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hdhe.service.ScanService");
        registerReceiver(myReceive, filter);
        // 注册Broadcast Receiver，用于关闭Service

        sendData = new Timer();
        scan100ms = new Timer();
		/* Create a receiving thread */
        mReadThread = new ReadThread();
        mReadThread.start(); // 开启读线程
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String cmd_arr = intent.getStringExtra("cmd");

        if (cmd_arr == null)
            return 0; // 没收到命令直接返回

        if("scan".equals(cmd_arr)){
            scan100ms.cancel();   //取消Timer任务
            run_scan100ms = false;
            if(mSerialPort.scaner_trig_stat() == true){
                mSerialPort.scaner_trigoff();
            }
            mSerialPort.scaner_trigon();  //触发扫描
            if(timeout != null){
                timeout.cancel();
                timeout = null;
                return 0;
            }
            timeout = new Timer();
            timeout.schedule(new TimerTask() {

                @Override
                public void run() {
                    mSerialPort.scaner_trigoff(); //设置5s超时
                    timeout = null;
                }
            }, 5000);
            Log.e(TAG, "start scan");
        }else if("toscan100ms".equals(cmd_arr)){
            if(run_scan100ms) return 0;
            run_scan100ms = true;
            scan100ms.cancel();
            scan100ms = new Timer();
            scan100ms.schedule(new TimerTask() {  //开始Timer任务，每100ms扫描一次
                @Override
                public void run() {
                    if(mSerialPort.scaner_trig_stat() == true){
                        mSerialPort.scaner_trigoff();
                    }
                    mSerialPort.scaner_trigon();  //触发扫描
                }
            }, 0, 100);

        }
        return 0;
    }
    @Override
    public void onDestroy() {
        if (mReadThread != null)
            run = false; 					// 关闭线程
        scan100ms.cancel();
        mSerialPort.scaner_poweroff(); 		// 关闭电源
        mSerialPort.close(14); 				// 关闭串口
        unregisterReceiver(myReceive); 		// 卸载注册
        super.onDestroy();
    }

    /**
     *  读线程 ,读取设备返回的信息，将其回传给发送请求的activity
     * @author Jimmy Pang
     *
     */
    private class ReadThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (run) {
                int size;
                try {
                    byte[] buffer = new byte[512];
                    if (mInputStream == null)
                        return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {

//						data = Tools.Bytes2HexString(buffer, size);
						/*中文字符编码*/
//						data = new String(buffer, 0, size, "GB2312");
//						data = new String(buffer, 0, size, "UTF-8");
                        data = new String(buffer, 0, size);
                        data_buffer.append(data);
                        Log.e(TAG, size +"********"+data);
                        data = null;
//						 Log.e("DeviceService data", data);
                        if(data_buffer != null && data_buffer.length() != 0 && activity != null){
                            Log.e("ScanService data", data_buffer.toString());

                            Intent serviceIntent = new Intent();
                            serviceIntent.setAction("com.hdhe.scanner.MainActivity");
                            serviceIntent.putExtra("result", data_buffer.toString());
                            data_buffer.setLength(0);  //清空缓存数据
                            Log.e(TAG, "result");
                            sendBroadcast(serviceIntent);
                        }
//						 sendData.schedule(new TimerTask() { //发送数据到activity
//							@Override
//							public void run() {
//
//								}
//							},  50);  //设置超时
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    /**
     *  广播接受者
     */
    private class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String ac = intent.getStringExtra("activity");
            if(ac!=null)

                activity = ac; // 获取activity
            if (intent.getBooleanExtra("stopflag", false))
                stopSelf(); // 收到停止服务信号

        }
    }

}
