package com.litte.wms.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litte on 2017/11/27.
 */

public class MyFragmentPageViewAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    public void addFragment(Fragment fragment){
        if (fragment != null){
            fragments.add(fragment);
            notifyDataSetChanged();
        }
    }
    public MyFragmentPageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
