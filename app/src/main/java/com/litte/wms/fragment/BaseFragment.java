package com.litte.wms.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.litte.wms.R;

/**
 * Created by litte on 2017/12/2.
 */

public abstract class BaseFragment extends Fragment {
    protected LinearLayout actionBar;
    protected View convertView;
    private TextView textView_actionBar_title;

    public void initialActionBar(String title){
        textView_actionBar_title = actionBar.findViewById(R.id.actionBar_title);
        if (actionBar == null){
            return;
        }
        textView_actionBar_title.setText(title);
    }
    public abstract void initUI();
}
