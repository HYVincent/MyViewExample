package com.vincent.myviewexample.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.view.DynamicWave;
import com.vincent.myviewexample.view.DynamicWaveMenu;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample
 * @class describe 水波纹效果
 * @date 2018/11/27 10:10
 */
public class WaterActivity extends AppCompatActivity {

    private DynamicWave dynamicWave;
    private DynamicWaveMenu dynamicWaveMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        dynamicWave = findViewById(R.id.dynamic_wave);
        dynamicWaveMenu = findViewById(R.id.dynamic_wave_menu);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicWave.setWavePaintColor(R.color.color_gray_8493a8);
                dynamicWave.refreshView();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicWave.setWavePaintColor(R.color.color_blue_72adf8);
                dynamicWave.refreshView();
            }
        });
        dynamicWave.setTouchListener(new DynamicWave.DynamicWaveTouchListener() {
            @Override
            public void touchUp() {
                //向上滑动
                dynamicWaveTouchUp();
            }

            @Override
            public void touchDown() {
                //向下滑动
                touchDownTouchUp();
            }
        });
    }

    /**
     * 向上滑动时处理
     */
    private void dynamicWaveTouchUp(){
        dynamicWave.setRatio(0.95f);
        dynamicWave.setWavePaintColor(R.color.color_gray_76879e);
        dynamicWave.setRunning(false);
        dynamicWave.setTop("");
        dynamicWave.refreshView();
    }

    /**
     * 向下滑动时处理
     */
    private void touchDownTouchUp(){
        dynamicWave.setRatio(0.65f);
        dynamicWave.setWavePaintColor(R.color.color_blue_72adf8);
        dynamicWave.setRunning(true);
        dynamicWave.setTop("向上滑动");
        dynamicWave.refreshView();
    }

}
