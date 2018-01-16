package com.litte.wms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.litte.wms.entity.Goods;
import com.litte.wms.manager.ScanManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广播接收者,接收服务发送过来的数据，并更新UI
 */
public class ScanReceiver extends BroadcastReceiver {
    private List<Map<String, Object>> mList;
    private List<Goods> goodsList;
    private FileOutputStream fileOutputStream;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");

        try {
            fileOutputStream = context.openFileOutput("barcode.txt",Context.MODE_PRIVATE);

            String receiveData = intent.getStringExtra("result");//服务返回数据
            if (receiveData != null){
                mList = new ArrayList<>();
                //对数据进行排序
                goodsList = new ArrayList<>();
                goodsList = ScanManager.sortAndAdd(goodsList,receiveData);
                String allCount = goodsList.get(0).getCount()+"";
                //写入到固定的文件中
                fileOutputStream.write(allCount.getBytes());
                fileOutputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
    }
}
