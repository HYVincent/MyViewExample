package com.vincent.myviewexample.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample
 * @class describe
 * @date 2018/11/21 15:40
 */
public class MyCircleView extends View {

    public MyCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(mContext);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        mCircleX = w/2;
        mCircleY = h/2;
        if(mViewWidth > mViewHeight){
            mCircleRadius = mViewHeight / 2;
        }else {
            mCircleRadius = mViewWidth / 2;
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mCircleX,mCircleY,mCurrentCircleRadius,mPaint);
    }

    public void startDrawable(){
        animator = ObjectAnimator.ofFloat(mContext, "progress", 0, mCircleRadius);
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentCircleRadius = (float) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: "+mCurrentCircleRadius);
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animation.start();
            }
        });
        animator.start();
    }

    private void init(Context mContext) {
        mCircleRadius = DpUtil.dp2px(mContext,mCircleRadius);
        mCircleColor = ContextCompat.getColor(mContext,R.color.color_green_a8edc5);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mCircleColor);
        mPaint.setStrokeWidth(DpUtil.dp2px(mContext,2));
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private Context mContext;
    private Paint mPaint;
    private float mViewWidth;
    private float mViewHeight;
    private float mCircleRadius = 10;//半径
    private float mCurrentCircleRadius;//正在绘制的圆的半径
    private int mCircleColor;
    private float mCircleX;
    private float mCircleY;
    private ObjectAnimator animator;
    private static final String TAG = "绘制圆";

    /**
     * view的大小控制
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

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
