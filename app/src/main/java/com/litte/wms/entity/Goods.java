package com.litte.wms.entity;

/**
 * Created by litte on 2017/11/27.
 */

public class Goods {
    private int barcodeid;
    private String name;  //商品名
    private String barcode;  //条码
    private int count;   //数量

    public Goods() {
    }

    public Goods(String name, String barcode, int count) {
        this.name = name;
        this.barcode = barcode;
        this.count = count;
    }

    public int getBarcodeid() {
        return barcodeid;
    }

    public void setBarcodeid(int barcodeid) {
        this.barcodeid = barcodeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "商品名：" + this.name + ",条码： " + this.barcode;
    }
}
