package com.vincent.myviewexample;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vincent.myviewexample.bean.ProgressBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCircleView = findViewById(R.id.myCircleView);
//        myCircleView.startDrawable();
        configProgressView = findViewById(R.id.config_progress_view);
//        configProgressView.startDrawable();
        progressView = findViewById(R.id.progressView);
        waveViewBySinCos1 = findViewById(R.id.wave_view_1);
        waveViewBySinCos2 = findViewById(R.id.wave_view_2);
//        waveViewBySinCos1.setWaveColor(R.color.cob);
        waveViewBySinCos1.startAnimation();
        waveViewBySinCos2.startAnimation();
        initProgressData(progressView);
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
    private ConfigProgressView configProgressView;
    private ProgressView progressView;
    private WaveViewBySinCos waveViewBySinCos1;
    private WaveViewBySinCos waveViewBySinCos2;
    private ProgressLineView progressLineView;
}
