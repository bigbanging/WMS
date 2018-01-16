package com.litte.wms.entity;

/**
 * Created by litte on 2017/11/27.
 */

public class EPC {
    private int id;
    private String EPC;
    private int count;

    public EPC() {
    }

    public EPC(int id, String EPC, int count) {
        this.id = id;
        this.EPC = EPC;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEPC() {
        return EPC;
    }

    public void setEPC(String EPC) {
        this.EPC = EPC;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "EPC{" +
                "id=" + id +
                ", EPC='" + EPC + '\'' +
                ", count=" + count +
                '}';
    }
}
