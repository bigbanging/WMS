package com.litte.wms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litte on 2017/12/2.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    protected LayoutInflater layoutInflater;
    protected Context context;

    public MyBaseAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }
    private List<T> datas = new ArrayList<>();

    /**
     * 添加数据的方法
     * @param list
     * @param isClean
     */
    public void addDatas(List<T> list ,boolean isClean){
        if (isClean){
            datas.clear();
        }
        if (datas != null){
            datas.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 删除所有数据
     */
    public void removeDatas(){
        datas.clear();
    }

    /**
     * 删除指定数据
     * @param t
     */
    public void removeDatas(T t){
        datas.remove(t);
        notifyDataSetChanged();
    }

    /**
     * 查询数据
     * @return
     */
    public List<T> getDatas(){return datas;}
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent) ;
}
