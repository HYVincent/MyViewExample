package com.vincent.myviewexample.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.view.MyErrorView;

/**
 * @author Administrator Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.example
 * @class describe
 * @date 2018/12/4 22:38
 */
public class Test4Activity extends AppCompatActivity {
    private MyErrorView myErrorView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test44);
        myErrorView = findViewById(R.id.my_error_view);
    }
}
