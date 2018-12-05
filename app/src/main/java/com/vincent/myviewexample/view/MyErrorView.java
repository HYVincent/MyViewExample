package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vincent.myviewexample.DpUtil;
import com.vincent.myviewexample.R;

/**
 * @author Administrator Vincent
 * @version v1.0
 * @name TclApp
 * @page com.shenmou.tclapp.view
 * @class describe
 * @date 2018/12/4 22:12
 */
public class MyErrorView extends View {

    public MyErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private static final String TAG = "Error";
    private Context mContext;
    //渐变色开始颜色
    private int mStartColor;
    //渐变色结束颜色
    private int mEndColor;
    //错误文字画笔
    private Paint mErrorTextPaint;
    private int mErrorTextColor;
    private float mErrorTextSize = 15;
    private float mViewHeight;
    private float mViewWidth;

    private Paint mBgPaint;
    private Paint mOutBorderPaint;
    private int mOutBorderColor;
    private float mOutBorderWidth = 1;
    private String mErrorText = "连接中断，请重新连接";
    //错误文字矩形
    private Rect mErrorTextRect = new Rect();
    //view的最大宽度
    private float mErrorTextMaxWidth;
    private int defaultWidthSize = 200;
    private int defaultHeightSize = 100;
    private float mPadding = 10;
    private Paint mCirclePaint;
    private int mCircleColor;
    private float mCircularRingWidth = 4;
    private float mCircleMargin = 20;


    private void init(Context context) {
        this.mContext = context;

        mStartColor = ContextCompat.getColor(mContext,R.color.color_red_e21493);
        mEndColor = ContextCompat.getColor(mContext,R.color.color_purple_bf3ccf);
        mErrorTextColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mErrorTextSize = DpUtil.dp2px(mContext,mErrorTextSize);
        mErrorTextPaint = new Paint();
        mErrorTextPaint.setTextSize(mErrorTextSize);
        mErrorTextPaint.setAntiAlias(true);
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/NotoSansHans-Black.otf");
        mErrorTextPaint.setTypeface(tf);
        mErrorTextPaint.setColor(mErrorTextColor);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mOutBorderColor = ContextCompat.getColor(mContext,R.color.color_red_c8148d);
        mOutBorderPaint = new Paint();
        mOutBorderPaint.setAntiAlias(true);
        mOutBorderWidth = DpUtil.dp2px(mContext,mOutBorderWidth);
        mOutBorderPaint.setStrokeWidth(mOutBorderWidth);
        mOutBorderPaint.setColor(mOutBorderColor);
        mOutBorderPaint.setStyle(Paint.Style.STROKE);

        mPadding = DpUtil.dp2px(mContext, mPadding);

        mCircularRingWidth = DpUtil.dp2px(mContext,mCircularRingWidth);
        mCirclePaint = new Paint();
        mCircleColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setAntiAlias(true);
        mCircleMargin = DpUtil.dp2px(mContext,mCircleMargin);
        mCirclePaint.setStrokeWidth(mCircularRingWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);

    }

    public void setmErrorText(String mErrorText) {
        this.mErrorText = mErrorText;
//        defaultWidthSize = DpUtil.dp2px(mContext,defaultWidthSize);
//        defaultHeightSize = DpUtil.dp2px(mContext,defaultHeightSize);
        mErrorTextPaint.getTextBounds(mErrorText,0,mErrorText.length(),mErrorTextRect);
        defaultWidthSize = (int)(mErrorTextRect.width() + mCircleMargin * 4 + mPadding * 2 + DpUtil.dp2px(mContext,15) * 2);
        defaultHeightSize = mErrorTextRect.height();
        if(defaultHeightSize < DpUtil.dp2px(mContext,80)){
            defaultHeightSize = DpUtil.dp2px(mContext,80);
        }
        invalidate();
    }

    /**
     *  绘制 自定义View的 适配 padding
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mViewHeight = defaultHeightSize;
        mViewWidth = defaultWidthSize;
        //先绘制背景
        drawBg(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        float mTextX = mViewWidth/2 - mErrorTextRect.width()/2;
        float mTextY = mViewHeight/2 + mErrorTextRect.height()/2;
        canvas.drawText(mErrorText,mTextX,mTextY - DpUtil.dp2px(mContext,2),mErrorTextPaint);//向上挪动2个dp
        float mCircleRadiusY = mTextY - mErrorTextRect.height()/2;
        float mCircleRadius = mErrorTextRect.height()/2 + DpUtil.dp2px(mContext,1f);
        canvas.drawCircle(mTextX - mCircleMargin,mCircleRadiusY,mCircleRadius,mCirclePaint);
        canvas.drawCircle(mTextX + mErrorTextPaint.measureText(mErrorText) + mCircleMargin,mCircleRadiusY,mCircleRadius,mCirclePaint);
        Log.d(TAG, "drawText: "+ mCircleRadiusY);
        double width = Math.sqrt(mCircleRadius * mCircleRadius /2);
        Log.d(TAG, "drawText: "+ width);
        canvas.drawLine( mTextX - mCircleMargin - (float)width,mCircleRadiusY - (float)width,
                mTextX - mCircleMargin + (float)width,mCircleRadiusY + (float)width,mCirclePaint);
        canvas.drawLine( mTextX + mErrorTextPaint.measureText(mErrorText) + mCircleMargin - (float)width,mCircleRadiusY - (float)width,
                mTextX + mErrorTextPaint.measureText(mErrorText) + mCircleMargin + (float)width,mCircleRadiusY + (float)width,mCirclePaint);
    }

    private void drawBg(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.bottom = mViewHeight -mOutBorderWidth;
        rectF.left = mOutBorderWidth;
        rectF.right = mViewWidth -mOutBorderWidth;
        rectF.top = mOutBorderWidth;
        canvas.drawRoundRect(rectF,9,9,mOutBorderPaint);

        RectF rectF1 = new RectF();
        rectF1.bottom = rectF.bottom - mPadding;
        rectF1.left = mPadding;
        rectF1.right =  rectF.right - mPadding;
        rectF1.top = mPadding;
        LinearGradient linearGradient = new LinearGradient(mPadding,mViewHeight/2-mErrorTextRect.height(),mPadding,mViewHeight/2-mErrorTextRect.height()/5,
                mStartColor,mEndColor,Shader.TileMode.CLAMP);
        mBgPaint.setShader(linearGradient);
        canvas.drawRect(rectF1,mBgPaint);
    }

    /**
     * 绘制 自定义View的 wrap_content 可做模板
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(defaultWidthSize,defaultHeightSize);
        }else if (widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(defaultWidthSize,heightSize);
        }else if(heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize,defaultHeightSize);
        }
    }


}