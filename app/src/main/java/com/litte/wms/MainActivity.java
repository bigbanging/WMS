package com.litte.wms;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.litte.wms.adapter.MyFragmentPageViewAdapter;
import com.litte.wms.fragment.CheckFragment;
import com.litte.wms.fragment.IOFragment;
import com.litte.wms.fragment.MissionFragment;
import com.litte.wms.fragment.ScanFragment;
import com.litte.wms.fragment.UHFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    MyFragmentPageViewAdapter adapter = null;
    ScanFragment scanFragment ;
    UHFragment uhFragment ;
    IOFragment ioFragment;
    CheckFragment checkFragment;
    MissionFragment missionFragment;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        setListener();
    }

    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new MyFragmentPageViewAdapter(getSupportFragmentManager());
        scanFragment = new ScanFragment();
        uhFragment = new UHFragment();
        ioFragment = new IOFragment();
        checkFragment = new CheckFragment();
        missionFragment = new MissionFragment();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        viewPager.setAdapter(adapter);
        adapter.addFragment(scanFragment);
        adapter.addFragment(uhFragment);
        adapter.addFragment(ioFragment);
        adapter.addFragment(checkFragment);
        adapter.addFragment(missionFragment);
        viewPager.setCurrentItem(0,false);
    }

    private void setListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        radioGroup.check(R.id.radioButton_scanner);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton_uhf);
                        break;
                    case 2:
                        radioGroup.check(R.id.radioButton_io);
                        break;
                    case 3:
                        radioGroup.check(R.id.radioButton_check);
                        break;
                    case 4:
                        radioGroup.check(R.id.radioButton_mission);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_scanner:
                        viewPager.setCurrentItem(0,false);
                        break;
                    case R.id.radioButton_uhf:
                        viewPager.setCurrentItem(1,false);
                        break;
                    case R.id.radioButton_io:
                        viewPager.setCurrentItem(2,false);
                        break;
                    case R.id.radioButton_check:
                        viewPager.setCurrentItem(3,false);
                        break;
                    case R.id.radioButton_mission:
                        viewPager.setCurrentItem(4,false);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
