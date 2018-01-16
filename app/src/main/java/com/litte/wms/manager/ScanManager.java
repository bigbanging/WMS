package com.litte.wms.manager;

import com.litte.wms.entity.Goods;

import java.util.List;

/**
 * Created by litte on 2017/12/2.
 */

public class ScanManager {
    /**
     * @param list    原有数据
     * @param barcode 新扫描到的一维码
     * @return 返回的是将新扫描的数据添加到原有的数据并放在第一位
     */
    public static List<Goods> sortAndAdd(List<Goods> list, String barcode) {
        Goods goods = new Goods();
        goods.setBarcode(barcode);
        int temp = 1;
        if (list == null || list.size() == 0) {  //第一次添加数据
            goods.setCount(temp);
            list.add(goods);
            return list;
        }
        //原有数据中已经有条码
        for (int i = 0; i < list.size(); i++) {  //遍历原有list
            if (barcode.equals(list.get(i).getBarcode())) {
                temp = list.get(i).getCount() + temp;
                goods.setCount(temp);
                for (int j = i; j > 0; j--) {
                    list.set(j, list.get(j - 1));  //移动数据
                }
                list.set(0, goods);
                return list;
            }
        }
        //原有数据无条码
        Goods lastgoods = list.get(list.size() - 1);  //先把最后一位取出来
        for (int j = list.size() - 1; j >= 0; j--) {
            if (j == 0) {
                goods.setCount(temp);
                list.set(j, goods);   //新扫描的数据放第一行
            } else {
                list.set(j, list.get(j - 1));
            }

        }
        list.add(lastgoods);
        return list;
    }
}
