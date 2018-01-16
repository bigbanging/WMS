package com.litte.wms.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.litte.wms.R;
import com.litte.wms.entity.Goods;

/**
 * Created by litte on 2017/12/2.
 */

public class ScanAdapter extends MyBaseAdapter<Goods> {

    public ScanAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            //如果View为空说明适配的是第一屏的数据
            //在内存缓存中还没有可用的listView
            //需要我们用布局解析出适用项
            convertView = layoutInflater.inflate(R.layout.listview_item,null);
            holder = new ViewHolder();
            holder.textView_id = convertView.findViewById(R.id.textView_id);
            holder.textView_item = convertView.findViewById(R.id.textView_item);
            holder.textView_count = convertView.findViewById(R.id.textView_count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取适配器中的一个适配项
        Goods goods = getItem(position);
        holder.textView_id.setText(goods.getBarcodeid());
        holder.textView_item.setText(goods.getBarcode());
        holder.textView_count.setText(goods.getCount());
        return convertView;
    }
    private class ViewHolder{
        TextView textView_id;
        TextView textView_item;
        TextView textView_count;
    }
}
