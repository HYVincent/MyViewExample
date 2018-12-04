package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;
import com.vincent.myviewexample.utils.ScreenUtils;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.example
 * @class describe
 * @date 2018/11/28 17:18
 */
public class NumberView extends View {

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        mZeroNumberColor = ContextCompat.getColor(mContext, R.color.color_green_b4f4dc);
        mNumberColor = ContextCompat.getColor(mContext, R.color.color_green_03E195);
        mUnitTextColor = ContextCompat.getColor(mContext, R.color.color_green_03E195);

        mNumberTextSize = DpUtil.dp2px(mContext, mNumberTextSize);
        mUnitTextSize = DpUtil.dp2px(mContext, mUnitTextSize);
        mNumberMarginUnit = DpUtil.dp2px(mContext, mNumberMarginUnit);

        mNumberPaint = new Paint();
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setTextSize(mNumberTextSize);
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/DINCond-Medium.otf");
        mNumberPaint.setTypeface(tf);


        mUnitPaint = new Paint();
        mUnitPaint.setAntiAlias(true);
        mUnitPaint.setTextSize(mUnitTextSize);
        mUnitPaint.setColor(mUnitTextColor);
        mUnitPaint.setTypeface(tf);
    }

    private Context mContext;
    private Paint mNumberPaint;
    private Paint mUnitPaint;
    private int mZeroNumberColor;
    private int mUnitTextColor;
    private int mNumberColor;
    private float mNumberTextSize = 72;
    private float mUnitTextSize = 19;
    private float mViewWidth;
    private float mViewHeight;
    private int mNumber = 889;
    private String mNumberStr;
    private String mZeroStr;
    private String mNumberUnit = "TDS";
    private float mNumberMarginUnit = 10;//数字和单位之间的间距
    private static final String TAG = "数字控件";


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(mContext, R.color.color_white_ffffff));
        canvas.drawLine(mViewWidth / 2, 0, mViewWidth / 2, mViewHeight, mNumberPaint);
        drawCenterNumber(canvas);
    }

    /**
     * 绘制中间的数字
     *
     * @param canvas
     */
    private void drawCenterNumber(Canvas canvas) {
        mNumberStr = String.valueOf(mNumber);
        if (TextUtils.isEmpty(mNumberUnit)) {
            drawNumber(canvas);
        } else {
           drawNumberAndUnit(canvas);
        }
    }

    /**
     * 有单位
     * @param canvas
     */
    private void drawNumberAndUnit(Canvas canvas) {
        Rect mUnitRect = new Rect();
        Rect mNumberRect = new Rect();
        Rect mZeroRect = new Rect();
        mNumberStr = String.valueOf(mNumber);
        mUnitPaint.getTextBounds(mNumberUnit,0,mNumberUnit.length(),mUnitRect);
        float totalWidth;
        if(mNumber != 0){
            if(mNumber > 99){
                mNumberPaint.setColor(mNumberColor);
                mNumberPaint.getTextBounds(mNumberStr,0,mNumberStr.length(),mNumberRect);
                totalWidth = mNumberPaint.measureText(mNumberStr);
                canvas.drawText(mNumberStr,mViewWidth/2 - totalWidth/2,mNumberRect.height(),mNumberPaint);
                canvas.drawText(mNumberUnit,mViewWidth/2 + totalWidth/2 + mNumberMarginUnit ,mNumberRect.height(),mUnitPaint);
            }else {
                if(mNumber < 10){
                    mZeroStr = "00";
                }else {
                    mZeroStr = "0";
                }
                mNumberPaint.getTextBounds(mZeroStr,0,mZeroStr.length(),mZeroRect);
                mNumberPaint.getTextBounds(mNumberStr,0,mNumberStr.length(),mNumberRect);
                totalWidth = mNumberPaint.measureText(mZeroStr) + mNumberPaint.measureText(mNumberStr);
                mNumberPaint.setColor(mZeroNumberColor);
                canvas.drawText(mZeroStr,mViewWidth/2 - totalWidth/2,mNumberRect.height(),mNumberPaint);
                mNumberPaint.setColor(mNumberColor);
                canvas.drawText(mNumberStr,mViewWidth/2 - totalWidth/2 + mNumberPaint.measureText(mZeroStr),mNumberRect.height(),mNumberPaint);
                canvas.drawText(mNumberUnit,mViewWidth/2 + totalWidth/2 + mNumberMarginUnit, mNumberRect.height(),mUnitPaint);
            }
        }else {
            // mNumber = 0
            mZeroStr = "000";
            mNumberPaint.setColor(mNumberColor);
            mNumberPaint.getTextBounds(mZeroStr,0,mZeroStr.length(),mNumberRect);
            canvas.drawText(mZeroStr,mViewWidth/2 - mNumberPaint.measureText(mZeroStr)/2,mNumberRect.height(),mNumberPaint);
            canvas.drawText(mNumberUnit,mViewWidth/2 + mNumberPaint.measureText(mZeroStr)/2 + mNumberMarginUnit ,mNumberRect.height(),mUnitPaint);
        }
    }

    private void drawNumber(Canvas canvas){
        Rect mNumberRect = new Rect();
        Rect mZeroNumberRect = new Rect();//数字0
        float totalWidth;
        float textWidth;
        //没有单位
        if (mNumber != 0) {
            if (mNumber < 10) {
                mZeroStr = "00";
                mNumberPaint.getTextBounds(mZeroStr, 0, mZeroStr.length(), mZeroNumberRect);
                mNumberPaint.getTextBounds(mNumberStr, 0, mNumberStr.length(), mNumberRect);
                textWidth = mNumberPaint.measureText(mZeroStr) + mNumberPaint.measureText(mNumberStr);
                mNumberPaint.setColor(mZeroNumberColor);
                canvas.drawText(mZeroStr, mViewWidth / 2 - textWidth / 2, mZeroNumberRect.height(), mNumberPaint);
                mNumberPaint.setColor(mNumberColor);
                canvas.drawText(mNumberStr, mViewWidth / 2 + mNumberPaint.measureText(mNumberStr) / 2, mZeroNumberRect.height(), mNumberPaint);
            } else if (mNumber < 100) {
                mZeroStr = "0";
                mNumberPaint.getTextBounds(mZeroStr, 0, mZeroStr.length(), mZeroNumberRect);
                mNumberPaint.getTextBounds(mNumberStr, 0, mNumberStr.length(), mNumberRect);
                textWidth = mNumberPaint.measureText(mZeroStr) + mNumberPaint.measureText(mNumberStr);
                mNumberPaint.setColor(mZeroNumberColor);
                canvas.drawText(mZeroStr, mViewWidth / 2 - textWidth / 2, mZeroNumberRect.height(), mNumberPaint);
                mNumberPaint.setColor(mNumberColor);
                canvas.drawText(mNumberStr, mViewWidth / 2 - textWidth / 2 + mNumberPaint.measureText(mZeroStr) ,
                        mZeroNumberRect.height(), mNumberPaint);
            } else {
                //大于100
                mNumberPaint.setColor(mNumberColor);
                mNumberPaint.getTextBounds(mNumberStr, 0, mNumberStr.length(), mNumberRect);
                canvas.drawText(mNumberStr,mViewWidth/2 - mNumberPaint.measureText(mNumberStr)/2,mNumberRect.height(),mNumberPaint);
            }
        } else {
            //number == 0
            mNumberStr = "000";
            mNumberPaint.setColor(mNumberColor);
            mNumberPaint.getTextBounds(mNumberStr, 0, mNumberStr.length(), mNumberRect);
            canvas.drawText(mNumberStr, mViewWidth / 2 - mNumberPaint.measureText(mNumberStr) / 2, mNumberRect.height(), mNumberPaint);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setmNumberUnit(String mNumberUnit) {
        this.mNumberUnit = mNumberUnit;
    }

    public void refreshView() {
        invalidate();
    }

    /**
     * view的大小控制
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    /**
     * 测量高度
     * @param measureSpec 测量模式
     * @return View height
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = DpUtil.dp2px(mContext,60);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    /**
     * 测量宽度
     * @param measureSpec 测量模式
     * @return View width
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result =  ScreenUtils.getScreenWidth(mContext);//根据自己的需要更改
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

}
