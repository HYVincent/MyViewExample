package com.vincent.myviewexample.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.view.TagTextView;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample
 * @class describe 水波纹效果
 * @date 2018/11/27 10:10
 */
public class Test2Activity extends AppCompatActivity {

    private TagTextView tagTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        tagTextView = findViewById(R.id.tagTextView);
        tagTextView.setNumber(10);
        tagTextView.setTag("宠物");
    }


}
