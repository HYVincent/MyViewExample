package com.vincent.myviewexample.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.bean.SkinTestResultBean;
import com.vincent.myviewexample.view.SkinTestResultContrastView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.example
 * @class describe
 * @date 2018/12/5 15:00
 */
public class Test5Activity extends AppCompatActivity {

    private SkinTestResultContrastView skinTestResultContrastView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test5);
        skinTestResultContrastView = findViewById(R.id.skinTestResultContrastView);
        initData();

        skinTestResultContrastView.setmBottomText("肌肤检测结果对比");
//        skinTestResultContrastView.setBean(bean);
    }

    private void initData() {
        SkinTestResultBean bean1 = new SkinTestResultBean();
        bean1.setElasticity(30);
        bean1.setOil(89);
        bean1.setWater(58);
        bean1.setWhite(13);

        SkinTestResultBean bean2 = new SkinTestResultBean();
        bean2.setElasticity(30);
        bean2.setOil(89);
        bean2.setWater(58);
        bean2.setWhite(13);

        SkinTestResultBean bean3 = new SkinTestResultBean();
        bean3.setElasticity(30);
        bean3.setOil(89);
        bean3.setWater(58);
        bean3.setWhite(13);

        List<SkinTestResultBean> datas = new ArrayList<>();
        datas.add(bean1);
        datas.add(bean2);
        datas.add(bean3);

        skinTestResultContrastView.setDatas(datas);
    }
}
