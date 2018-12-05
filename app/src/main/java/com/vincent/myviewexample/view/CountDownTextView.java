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
import android.view.View;

import com.vincent.myviewexample.DpUtil;
import com.vincent.myviewexample.R;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.view
 * @class describe
 * @date 2018/12/5 18:08
 */
public class CountDownTextView extends View {

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private Context mContext;
    private int mTextColor;
    private float mTextSize = 12;
    private float mViewWidth;
    private float mViewHeight;
    private String timeText;
    private Rect mTimeRect = new Rect();
    private Paint mTimePaint;

    public void setTimeText(String timeText) {
        this.timeText = timeText;
        mTimePaint.getTextBounds(timeText,0,timeText.length(),mTimeRect);
        mViewWidth = mTimeRect.width();
        mViewHeight = mTimeRect.height();
        invalidate();
    }

    private void init(Context context) {
        this.mContext = context;
        mTextColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mTextSize = DpUtil.dp2px(mContext,mTextSize);

        mTimePaint = new Paint();
        mTimePaint.setTextSize(mTextSize);
        mTimePaint.setAntiAlias(true);
        mTimePaint.setColor(mTextColor);
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/svgafix.fon");
        mTimePaint.setTypeface(tf);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!TextUtils.isEmpty(timeText)){
            canvas.drawText(timeText,0,0,mTimePaint);
        }
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
            result = (int) mViewHeight;
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
            result = (int) mViewWidth;//根据自己的需要更改
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }


}
