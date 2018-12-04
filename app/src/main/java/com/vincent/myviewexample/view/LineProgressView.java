package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.view
 * @class describe
 * @date 2018/11/28 16:49
 */
public class LineProgressView extends View {

    public LineProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(mContext);
    }
    private int position = 0;//当前选中为第一个
    private void init(Context mContext) {
        mDefaultColor = ContextCompat.getColor(mContext,R.color.color_gray_E7E4E4);
        mSelectColor = ContextCompat.getColor(mContext,R.color.color_yellow_F6AD42);
        mItemWidth = DpUtil.dp2px(mContext,mItemWidth);
        mMargin = DpUtil.dp2px(mContext,mMargin);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(DpUtil.dp2px(mContext,2));
    }

    public void setPosition(int position) {
        if(position >= 1){
            position = 1;
        }
        if(position <= 0){
            position = 0;
        }
        this.position = position;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(mContext,R.color.color_white_ffffff));
        float lineY = mViewHeight/2 - DpUtil.dp2px(mContext,2)/2;
        if(position == 1){
            mPaint.setColor(mDefaultColor);
            canvas.drawLine(mViewWidth / 2 - mMargin/2 - mItemWidth,lineY,mViewWidth / 2 - mMargin/2,lineY,mPaint);
            mPaint.setColor(mSelectColor);
            canvas.drawLine(mViewWidth / 2 + mMargin/2,lineY,mViewWidth / 2 + mMargin/2 + mItemWidth,lineY,mPaint);
        }
        if(position == 0){
            mPaint.setColor(mSelectColor);
            canvas.drawLine(mViewWidth / 2 - mMargin/2 - mItemWidth,lineY,mViewWidth / 2 - mMargin/2,lineY,mPaint);
            mPaint.setColor(mDefaultColor);
            canvas.drawLine(mViewWidth / 2 + mMargin/2,lineY,mViewWidth / 2 + mMargin/2 + mItemWidth,lineY,mPaint);
        }
    }

    private Context mContext;
    private Paint mPaint;
    private int mDefaultColor;
    private int mSelectColor;
    private float mItemWidth = 20;
    private int mViewWidth;
    private int mViewHeight;
    private float mMargin = 10;//间距

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
