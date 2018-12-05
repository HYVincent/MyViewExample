package com.vincent.myviewexample.example;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.vincent.myviewexample.utils.ScreenUtils;
import com.vincent.myviewexample.view.MyCircleView;
import com.vincent.myviewexample.bean.ProgressBean;
import com.vincent.myviewexample.view.ProgressLineView;
import com.vincent.myviewexample.view.ProgressView;
import com.vincent.myviewexample.R;
import com.vincent.myviewexample.view.WaveViewBySinCos;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "首页";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCircleView = findViewById(R.id.myCircleView);
//        myCircleView.startDrawable();
//        configProgressView = findViewById(R.id.config_progress_view);
//        configProgressView.startDrawable();
        progressView = findViewById(R.id.progressView);
        initProgressData(progressView);
        waveViewBySinCos1 = findViewById(R.id.wave_view_1);
        waveViewBySinCos2 = findViewById(R.id.wave_view_2);
//        waveViewBySinCos1.setWaveColor(R.color.cob);
        waveViewBySinCos1.startAnimation();
        waveViewBySinCos2.startAnimation();
        progressLineView = findViewById(R.id.progress_line_view);
        progressLineView.setmCurrentValue(50);
        findViewById(R.id.btn_start_anim_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressLineView.setStartAnim(true);
                progressLineView.setmCurrentValue(50);
            }
        });
        findViewById(R.id.btn_start_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressLineView.setStartAnim(false);
                progressLineView.setmCurrentValue(80);
            }
        });
        progressLineView.setMoveValueChangeListener(new ProgressLineView.MoveValueChangeListener() {
            @Override
            public void onMoveChange(float value) {
//                Log.d("当前值---》", "onMoveChange: "+value);
            }
        });
        progressLineView.setStartTouch(true);
        findViewById(R.id.btn_start_anim_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                mShowAction.setDuration(500);
                progressView.startAnimation(mShowAction);*/
                AnimationSet animationSet=new AnimationSet(true);
                AlphaAnimation alphaAnimation=new AlphaAnimation(1,0);
                alphaAnimation.setDuration(2000);
                animationSet.addAnimation(alphaAnimation);
                progressView.startAnimation(animationSet);
                progressView.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.btn_start_anim_gone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f);
                mHiddenAction.setDuration(500);
                progressView.startAnimation(mHiddenAction);
                progressView.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.btn_water).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WaterActivity.class));
            }
        });
//        findViewById(R.id.btn_water).performClick();
//        startActivity(new Intent(MainActivity.this,Test1Activity.class));
//        startActivity(new Intent(MainActivity.this,WaterActivity.class));
//        startActivity(new Intent(MainActivity.this,Test3Activity.class));
        startActivity(new Intent(MainActivity.this,Test4Activity.class));
//        startActivity(new Intent(MainActivity.this,Test5Activity.class));
        Log.d(TAG, "onCreate: "+ScreenUtils.getScreenWidth(MainActivity.this)+ " " + ScreenUtils.getScreenHeight(MainActivity.this));
    }

    private void initProgressData(ProgressView progressView) {
        String tag1 = new String("Step 2: Join the router.\nThis step takes about 1 to 3 minutes, please avoid the phone.\nscreen off during the process.");
        String tag2 = new String("Step 2: Join the router.\nThis step takes about 1 to 3 minutes, please avoid the phone akes about 1 to 3 minutes, please av akes about 1 to 3 minutes, please av.\nscreen off during the process.");
        String tag3 = new String("Step 2: Join the router.\nThis step takes about 1 to 3 minutes, please avoid the phone akes about 1 to 3 minutes, please av akes about 1 to 3 minutes, please av.\nscreen off during the process.");
        List<ProgressBean> data =  new ArrayList<>();
        ProgressBean doFinish = new ProgressBean();
        doFinish.setDesc(tag1);
        doFinish.setStatus(2);

        ProgressBean doing = new ProgressBean();
        doing.setDesc(tag2);
        doing.setStatus(1);


        ProgressBean noDo = new ProgressBean();
        noDo.setDesc(tag3);
        noDo.setStatus(0);

        data.add(doFinish);
        data.add(doing);
        data.add(noDo);
        progressView.setData(data);
    }

    private MyCircleView myCircleView;
//    private ConfigProgressView configProgressView;
    private ProgressView progressView;
    private WaveViewBySinCos waveViewBySinCos1;
    private WaveViewBySinCos waveViewBySinCos2;
    private ProgressLineView progressLineView;
}
