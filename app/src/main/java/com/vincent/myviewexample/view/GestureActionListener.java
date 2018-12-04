package com.vincent.myviewexample.view;

import android.view.MotionEvent;
import android.widget.OverScroller;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name TestProject
 * @page com.example.administrator.testview
 * @class describe
 * @date 2018/8/10 10:59
 */
public interface GestureActionListener {

    void down(MotionEvent event);

    void move(MotionEvent event);

    void up(MotionEvent event);

    void pointerDown(MotionEvent event);

    void pointerMove(MotionEvent event);

    void pointerUp(MotionEvent event);

    void onScale(ScaleDetector detector);

    void onClick(MotionEvent event);

    void onDrag(MotionEvent event, float dx, float dy);

    void onFinishRoll();

    void onRoll(OverScroller scroller);

    void onRollCancel();

    public class ScaleDetector{
        public float scale, scaleX, scaleY;
        public float absScale, absScaleX, absScaleY;
        public float centerX, centerY;
    }
}
