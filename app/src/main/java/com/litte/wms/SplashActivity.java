package com.litte.wms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageView_splash;
    Animation animation_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
    }

    private void initUI() {
        //初始化控件
        imageView_splash = (ImageView) findViewById(R.id.imageView_splash);
        //获取动画对象
        animation_splash = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.splash);
        //应用动画对象
        imageView_splash.setAnimation(animation_splash);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                SplashActivity.this.finish();
            }
        };
        timer.schedule(timerTask,3000);
    }
}
