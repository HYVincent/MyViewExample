package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.view
 * @class describe
 * @date 2018/12/4 10:29
 */
public class MyTestView extends View {

    public MyTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    private static final String TAG = "三阶贝塞尔曲线进度条";
    private Paint mItemProgrfessPaint;
    private float mItemProgressWidth = 2;
    private Context mContext;
    private float mViewWidth;
    private float mViewHeight;
    private Path mPath;


    private int mStartColor;
    private int mEndColor;

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        mItemProgrfessPaint = new Paint();
        mItemProgressWidth = DpUtil.dp2px(mContext, mItemProgressWidth);
        mItemProgrfessPaint.setStrokeWidth(mItemProgressWidth);
        mItemProgrfessPaint.setAntiAlias(true);
        mItemProgrfessPaint.setDither(true);
        //笔头头部圆形
        mItemProgrfessPaint.setStrokeCap(Paint.Cap.ROUND);
        mItemProgrfessPaint.setColor(ContextCompat.getColor(mContext, R.color.color_purple_7a00ab));
        mItemProgrfessPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();

        mStartColor = ContextCompat.getColor(mContext, R.color.color_red_c8148d);
        mEndColor = ContextCompat.getColor(mContext, R.color.color_purple_17131a);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        drawTest3(canvas);
        drawTest(canvas);
    }

    private float mValue = 0.5f;
    private List<PointF> mCircle = new ArrayList<>();

    private void drawTest(Canvas canvas) {
        float startX = 200;
        float startY = 200;
        float control_1x = 200 + 200 * 0.35f;
        float control_1y = 200 +100 + 30;
        float control_2x = 200 + 200 * 0.75f;
        float control_2y = 200 - 50;
        float endX = 200 + 200;
        float endY = 200 + 30;
        mPath.moveTo(startX, startY);
        mPath.cubicTo(control_1x,control_1y,control_2x,control_2y,endX,endY);
        LinearGradient linearGradient = new LinearGradient(startX,startY,endX - 20,endY,mStartColor,mEndColor,Shader.TileMode.CLAMP);
        mItemProgrfessPaint.setShader(linearGradient);
        canvas.drawPath(mPath,mItemProgrfessPaint);
        PathMeasure pathMeasure = new PathMeasure(mPath,false);
        float length = pathMeasure.getLength();
        Log.d(TAG, "drawTest: length = "+length);
        for (int i = 0;i<length;i++){
            float[] pos = new float[2];
            float[] tan = new float[2];
            pathMeasure.getPosTan(i,pos,tan);
            Log.d(TAG, "drawTest: "+pos[0]+"  "+pos[1]+"  "+tan[0]+"  "+tan[1]);
            PointF pointF = new PointF(pos[0],pos[1]);
            mCircle.add(pointF);
        }
        canvas.drawCircle(mCircle.get(100).x,mCircle.get(100).y,5,mItemProgrfessPaint);
    }
}
