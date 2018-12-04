package com.vincent.myviewexample.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.bean.SkinTestResultBean;
import com.vincent.myviewexample.view.SkinTestResultView;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.example
 * @class describe
 * @date 2018/12/3 10:21
 */
public class Test3Activity extends AppCompatActivity {

    private SkinTestResultView skinTestResultView;
    private SkinTestResultBean bean = new SkinTestResultBean();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        skinTestResultView = findViewById(R.id.skinTestResult);
        bean.setElasticity(30);
        bean.setOil(89);
        bean.setWater(58);
        bean.setWhite(13);
        skinTestResultView.setBean(bean);
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skinTestResultView.setBean(bean);
            }
        });
    }
}
