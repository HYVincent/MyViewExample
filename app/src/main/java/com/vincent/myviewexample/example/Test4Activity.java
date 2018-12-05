package com.vincent.myviewexample.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.view.CountDownTextView;
import com.vincent.myviewexample.view.MyErrorView;
import com.vincent.myviewexample.view.MyText;

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
    private MyText myText;
    private CountDownTextView countDownTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test44);
        myErrorView = findViewById(R.id.my_error_view);
        myErrorView.setmErrorText("连接中断，请重新连接");
//        myErrorView.setmErrorText("ERROR");
        myText = findViewById(R.id.myTest);
        myText.setmTextString("使用步骤");
        countDownTextView = findViewById(R.id.count_down_text_view);
        countDownTextView.setTimeText("05:02");
    }
}
