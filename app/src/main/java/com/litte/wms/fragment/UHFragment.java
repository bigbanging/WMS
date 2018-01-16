package com.litte.wms.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.hdhe.uhf.reader.Tools;
import com.android.hdhe.uhf.reader.UhfReader;
import com.litte.wms.R;
import com.litte.wms.entity.EPC;
import com.litte.wms.tools.SoundUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * A simple {@link Fragment} subclass.
 */
public class UHFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listView_uhf;
    private Button button_contact,button_start,button_clear;
    private TextView textVersion ;
    private ArrayList<EPC> listEPC;
    private ArrayList<Map<String, Object>> listMap;
    private boolean runFlag = true;
    private boolean startFlag = false;
    private boolean connectFlag = false;
    private UhfReader reader ; //超高频读写器

    private ScreenStateReceiver screenReceiver ;


    public UHFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_uh, container, false);
//        initUI(view);

        /*//获取读写器实例，若返回null，则串口初始化失败
        reader = UhfReader.getInstance();
        Log.e("TAG", "onCreateView: reader"+reader );
        if (reader == null){
            textVersion.setText("串口初始化失败");
            setButtonClickable(button_clear,false);
            setButtonClickable(button_contact,false);
            setButtonClickable(button_start,false);
            return null;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //获取用户设置功率，并设置
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("power",0);
        int value = sharedPreferences.getInt("value",26);
        Log.e("TAG", "initUI: 功率设置："+value );
        reader.setOutputPower(value);*/
        //添加广播，默认屏灭休眠，平亮时唤醒
//        screenReceiver = new ScreenStateReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        getActivity().registerReceiver(screenReceiver,filter);
        //初始化声音线程池
      /*  Thread thread = new InventoryThread();
        thread.start();*/
        SoundUtil.initSoundPool(getActivity());
        return view;
    }

    private void initUI(View view) {
        listView_uhf = view.findViewById(R.id.listView_uhf);
        button_contact = view.findViewById(R.id.button_uhf_connect);
        button_start = view.findViewById(R.id.button_uhf_start);
        button_clear = view.findViewById(R.id.button_uhf_clear);
        textVersion = view.findViewById(R.id.textView_uhf_version);
        button_start.setOnClickListener(this);
        button_contact.setOnClickListener(this);
        button_clear.setOnClickListener(this);
        setButtonClickable(button_start,false);
        listEPC = new ArrayList<>();
        listView_uhf.setOnItemClickListener(this);
    }

    @Override
    public void onPause() {
        startFlag = false;
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_uhf_connect:
                Log.e("TAG", "onClick: button_uhf_connect ");
                byte[] versionBytes = reader.getFirmware();
                if (versionBytes!=null){
                    reader.setWorkArea(3);//设置欧标
                    SoundUtil.play(1,0);
                    String version = new String(versionBytes);
                    setButtonClickable(button_contact,false);
                    setButtonClickable(button_start,true);
                }
                setButtonClickable(button_contact,false);
                setButtonClickable(button_start,true);
                break;
            case R.id.button_uhf_start:
                Log.e("TAG", "onClick: button_uhf_start " );
                if (!startFlag){
                    startFlag = true;
                    button_start.setText("停止盘存");
                }else {
                    startFlag = false;
                    button_start.setText("开始盘存");
                }
                break;
            case R.id.button_uhf_clear:
                clearData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    /**
     * 盘存线程
     */
   /* class InventoryThread extends Thread{

        private List<byte[]> epcList;

        @Override
        public void run() {
            super.run();
            while(runFlag){
                if (startFlag){
//                    reader.stopInventoryMulti();
                    epcList = reader.inventoryRealTime();//实时盘存
                    if (epcList!=null&&!epcList.isEmpty()){
                        //播放提示音
                        SoundUtil.play(1,0);
                        for (byte[] epc:epcList){
                            String epcStr = Tools.Bytes2HexString(epc,epc.length);
                            addToList(listEPC,epcStr);
                        }
                    }
                    epcList = null;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }*/

    private List<byte[]> epcList;

   Thread thread = new Thread(new Runnable() {

       @Override
       public void run() {
//           super.run();
           while(runFlag){
               if (startFlag){
//                    reader.stopInventoryMulti();
                   epcList = reader.inventoryRealTime();//实时盘存
                   if (epcList!=null&&!epcList.isEmpty()){
                       //播放提示音
                       SoundUtil.play(1,0);
                       for (byte[] epc:epcList){
                           String epcStr = Tools.Bytes2HexString(epc,epc.length);
                           addToList(listEPC,epcStr);
                       }
                   }
                   epcList = null;
                   try {
                       Thread.sleep(40);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }
       }
   });

    private void addToList(final ArrayList<EPC> list, final String epc) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //第一次读入数据
                if (list.isEmpty()){
                    EPC epcTag = new EPC();
                    epcTag.setEPC(epc);
                    epcTag.setCount(1);
                    list.add(epcTag);
                }else {
                    for (int i = 0;i<list.size();i++){
                        EPC mEPC = list.get(i);
                        //list中有此EPC
                        if (epc.equals(mEPC.getEPC())){
                            mEPC.setCount(mEPC.getCount()+1);
                            list.set(i,mEPC);
                            break;
                        }else if (i == (list.size()-1)){
                            //list中没有此epc
                            EPC newEPC = new EPC();
                            newEPC.setEPC(epc);
                            newEPC.setCount(1);
                            list.add(newEPC);
                        }
                    }
                }
                //将数据添加到listView
                listMap = new ArrayList<Map<String, Object>>();
                int idcount = 1;
                for (EPC epcdata:list){
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("ID",idcount);
                    map.put("EPC",epcdata.getEPC());
                    map.put("COUNT",epcdata.getCount());
                    idcount++;
                    listMap.add(map);
                }
                listView_uhf.setAdapter(new SimpleAdapter(getActivity(),
                        listMap,
                        R.layout.listview_item,
                        new String[]{"ID","EPC","COUNT"},
                        new int[]{R.id.textView_id,R.id.textView_item,R.id.textView_count}));
            }
        });
    }

    /**
     * 设置按钮是否可用
     * @param button 按钮
     * @param flag 是否可用
     */
    private void setButtonClickable(Button button, boolean flag) {
        button.setClickable(flag);
        if (flag){
            button.setTextColor(Color.BLACK);
        }else {
            button.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(screenReceiver);
        runFlag = false;
        if (reader!=null){
            reader.close();
        }
        super.onDestroy();
    }

    /**
     * 清空listview
     */
    private void clearData(){
        Log.e("TAG", "clearData: clearData方法被执行" );
        listEPC.removeAll(listEPC);
        listView_uhf.setAdapter(null);
    }
    private class ScreenStateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            reader = UhfReader.getInstance();
            //屏亮
//		if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
//			reader.powerOn();
//			Log.i("ScreenStateReceiver", "screen on");
//
//		}//屏灭
//		else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
//			reader.powerOff();
//			Log.i("ScreenStateReceiver", "screen off");
//		}
        }
    }
}
