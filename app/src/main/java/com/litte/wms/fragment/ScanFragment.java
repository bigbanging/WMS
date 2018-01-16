package com.litte.wms.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.litte.wms.R;
import com.litte.wms.adapter.ScanAdapter;
import com.litte.wms.entity.Goods;
import com.litte.wms.manager.ScanManager;
import com.litte.wms.receiver.FunKeyReceiver;
import com.litte.wms.service.ScanService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends BaseFragment {

    private ListView listView_scan;
    private CheckBox checkBox_pre;
    private Button button_scan_start;
//    private ScanAdapter adapter = null;
    private List<Map<String, Object>> mList;
    private List<Goods> goodsList = new ArrayList<>();   //存放数据
    private String filepath;      //Excel表格的路径
    private String cmd = "scan";
    private FileOutputStream fos;        //用于保存扫描次
    private List<Goods> listFromExcel;        //存储从Excel表获取到的数据
    private MyBroadcast myBroad;  //广播接收者
    //    private String fragment = "com.hdhe.scanner.BarCodeFragment";
    private String activity = "com.hdhe.scanner.MainActivity";
    public String TAG = "MainActivity";   //Debug
    private MediaPlayer mPlayer;  //媒体播放者，用于播放提示音
    private FunKeyReceiver receive; //功能键广播接收者

    public ScanFragment() {
        // 需要空参构造器
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        convertView =  inflater.inflate(R.layout.fragment_scan, container, false);
        initUI();
        // 注册广播
        myBroad = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hdhe.scanner.MainActivity");
        Log.d("执行了：" ,"88888888888888888888888888888" );
        getActivity().registerReceiver(myBroad, filter);
        //启动服务
        Intent start = new Intent(getActivity(), ScanService.class);
        getActivity().startService(start);
        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        //		mPlayer.stop();
        getActivity().unregisterReceiver(myBroad);
        Intent stopService = new Intent();
        stopService.setAction("com.hdhe.service.ScanService");
        stopService.putExtra("stopflag", true);
        getActivity().sendBroadcast(stopService);  //给服务发送广播,令服务停止

        super.onDestroyView();
    }

    @Override
    public void initUI() {
        checkBox_pre = convertView.findViewById(R.id.checkbox_scanner_per);
        button_scan_start = convertView.findViewById(R.id.button_scanner_start);
        listView_scan = convertView.findViewById(R.id.listView_scanner);
        setListener();

//        adapter = new ScanAdapter(getActivity());
//        listView_scan.setAdapter(adapter);

    }

    private void setListener() {
        checkBox_pre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cmd = "toscan100ms";
                } else {
                    cmd = "scan";
                }
            }
        });
        button_scan_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCmd();
                Log.e(TAG, "onClick: button-scan..............." );
            }
        });
    }

    /**
     * 广播接收者,接收服务发送过来的数据，并更新UI
     */
    private class MyBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                fos = getActivity().openFileOutput("count.txt", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String receivedata = intent.getStringExtra("result");   // 服务返回的数据
            if (receivedata != null) {
                mList = new ArrayList<Map<String, Object>>();
                //对数据进行排序
                goodsList = ScanManager.sortAndAdd(goodsList, receivedata);
                String allcount = goodsList.get(0).getCount() + "";
                //写到固定的文件中
                try {
                    fos.write(allcount.getBytes());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                int count = 1;
                for (Goods goods : goodsList) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("barcodeID", count);
                    map.put("barcode", goods.getBarcode());
                    map.put("count", goods.getCount());
                    count++;
                    mList.add(map);
                }
                listView_scan.setAdapter(new SimpleAdapter(getActivity(), mList, R.layout.listview_item, new String[]{
                        "barcodeID", "barcode", "count"}, new int[]{
                        R.id.textView_id, R.id.textView_item, R.id.textView_count}));
                //媒体播放
                mPlayer = MediaPlayer.create(getActivity(), R.raw.msg);
                if (mPlayer.isPlaying()) {
                    return;
                }
                mPlayer.start();
//				Selection.setSelection(receive_data.getEditableText(), 0);  //让光标保持在最前面
            }
        }
    }

    /**
     * 发送指令
     */
    private void sendCmd() {
        // 给服务发送广播，内容为com.example.scandemo.MainActivity
        Intent ac = new Intent();
        ac.setAction("com.hdhe.service.ScanService");
        ac.putExtra("activity", activity);
        getActivity().sendBroadcast(ac);


        Intent sendToservice = new Intent(getActivity(), ScanService.class); // 用于发送指令
        sendToservice.putExtra("cmd", cmd);
        getActivity().startService(sendToservice); // 发送指令
    }
}
