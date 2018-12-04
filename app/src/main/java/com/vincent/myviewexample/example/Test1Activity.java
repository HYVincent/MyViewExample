package com.vincent.myviewexample.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.view.DynamicWave;
import com.vincent.myviewexample.view.TagTextView;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample
 * @class describe 水波纹效果
 * @date 2018/11/27 10:10
 */
public class Test1Activity extends AppCompatActivity {

    private TagTextView tagTextView;
    private DynamicWave dynamicWave;
    private EditText etInputNumber;
    private boolean runing = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        tagTextView = findViewById(R.id.tagTextView);
        tagTextView.setNumber(10);
        tagTextView.setTag("宠物");
        dynamicWave = findViewById(R.id.dynamic_wave);
        etInputNumber = findViewById(R.id.et_input_number);
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicWave.setmXOneOffset(Integer.valueOf(etInputNumber.getText().toString()));
            }
        });
        findViewById(R.id.runing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(runing){
                    runing = false;
                }else {
                    runing = true;
                }
                dynamicWave.setRunning(runing);
                Log.d("哎哎哎", "onClick: "+dynamicWave.getmXOneOffset() + " "+dynamicWave.getmXTwoOffset());
            }
        });
    }


}
