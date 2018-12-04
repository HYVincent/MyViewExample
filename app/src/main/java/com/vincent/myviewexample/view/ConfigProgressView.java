package com.vincent.myviewexample.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;


/**
 * @author Administrator Vincent
 * @version v1.0
 * @name EasyApp
 * @page com.vincent.easyapp.view
 * @class describe
 * @date 2018/10/30 22:35
 */
public class ConfigProgressView extends View {

    public ConfigProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    private final String TAG = "进度控件";
    private String contentStr1 /*= "步骤1:开始加网"*/;
    private String contentStr2_1 /*= "步骤2:加入路由器"*/;
    private String contentStr2_2 /*= "此步骤需要1至3分钟，过程中请避免"*/;
    private String contentStr2_3 /*= "手机屏幕关闭"*/;
    private String contentStr3 /*= "正在做最后配置"*/;

    private float mViewWidth;//View的宽度
    private float mViewHeight;//View高度
    private float mMarginTop = 15;//距离顶部的间距
    private float mLengthwaysImaginaryLineMarginLeft = 30;//纵向的虚线距离View左边的距离
    private float mContentHeight;//两个步骤之间的完整距离
    private float mCircleTextSize = 13;//圆里面的文字大小
    private int mFinishTextColor;//已经完成的步骤的文字颜色
    private int mUnfinishedTextColor;//未完成的步骤的文字颜色
    private int mLengthwaysImaginaryLineColor;//纵向的虚线的颜色
    private int mFinishCirclrColor;//已完成的圆圈的填充颜色
    private int mFinishCirclrRimColor;//已完成的圆圈的边框颜色
    private int mUnFinishCircleColor;//未完成的圆圈填充颜色
    private int mFinishRightTextColor;//已完成的右边文字颜色
    private int mUnfinishRightTextColor;//未完成的右边文字颜色
    private float mCircleRadius = 15;//圆圈半径
    private float mCurrentCircleRadius;//当前正在进行的圆的半径 这是一个变化的之，会一直变化 从0 - n循环..
    private float mTextMarginLeft = 15;//步骤文字距离左边的圆圈的间距
    private float mTextMarginTop = 10;//步骤文字之间的间隔距离

    private Paint mImaginaryLinePaint;
    private boolean mFistFinish = false;//第一步是否完成
    private boolean mTwoFinish = false;//第二步是否完成
    private boolean mThreeFinish = false;//第三步是否完成

    private Paint mProgressPaint;
    private Paint mStatusPaint;

    public void setmFistFinish(boolean mFistFinish) {
        this.mFistFinish = mFistFinish;
        invalidate();
    }

    public void setmTwoFinish(boolean mTwoFinish) {
        this.mTwoFinish = mTwoFinish;
        invalidate();
    }

    public void setmThreeFinish(boolean mThreeFinish) {
        this.mThreeFinish = mThreeFinish;
        invalidate();
    }

    private void init(Context context) {
        this.mContext = context;
        contentStr1 = mContext.getString(R.string.string_add_device_config_hint_msg_1);
        contentStr2_1 = mContext.getString(R.string.string_add_device_config_hint_msg_21);
        contentStr2_2 = mContext.getString(R.string.string_add_device_config_hint_msg_22);
        contentStr2_3 = mContext.getString(R.string.string_add_device_config_hint_msg_23);
        contentStr3 = mContext.getString(R.string.string_add_device_config_hint_msg_3);
        mCircleRadius = DpUtil.dp2px(mContext,mCircleRadius);
        mTextMarginLeft = DpUtil.dp2px(mContext,mTextMarginLeft);
        mMarginTop = DpUtil.dp2px(mContext,mMarginTop);
        mCircleTextSize = DpUtil.dp2px(mContext,mCircleTextSize);
        mLengthwaysImaginaryLineMarginLeft = DpUtil.dp2px(mContext,mLengthwaysImaginaryLineMarginLeft);
        mTextMarginTop = DpUtil.dp2px(mContext,mTextMarginTop);
        mFinishCirclrColor = ContextCompat.getColor(mContext, R.color.color_green_a8edc5);
        mFinishCirclrRimColor = ContextCompat.getColor(mContext,R.color.color_gray_5d5f5e);
        mFinishTextColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mUnfinishedTextColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mLengthwaysImaginaryLineColor = ContextCompat.getColor(mContext,R.color.color_gray_d6d5d5);
        mUnFinishCircleColor = ContextCompat.getColor(mContext,R.color.color_gray_cccbcb);
        mFinishRightTextColor = ContextCompat.getColor(mContext,R.color.color_gray_6d6d6d);
        mUnfinishRightTextColor = ContextCompat.getColor(mContext,R.color.color_gray_dcdcdc);

        mImaginaryLinePaint = new Paint();
        mImaginaryLinePaint.setAntiAlias(true);
        mImaginaryLinePaint.setColor(mLengthwaysImaginaryLineColor);
        mImaginaryLinePaint.setStrokeWidth(DpUtil.dp2px(mContext,1));

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(DpUtil.dp2px(mContext,1));
        mProgressPaint.setTextSize(mCircleTextSize);

        mStatusPaint = new Paint();
        mStatusPaint.setAntiAlias(true);
        mStatusPaint.setStrokeWidth(DpUtil.dp2px(mContext,2));
        mStatusPaint.setColor(mFinishCirclrColor);
        mStatusPaint.setStyle(Paint.Style.STROKE);
//        mStatusPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));//设置重叠模式
    }

    private Context mContext;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mContentHeight = mViewHeight - mMarginTop * 2;
        drawImaginaryLine(canvas);
        //绘制第一个圆
        drawCircleAndText(canvas,2,mLengthwaysImaginaryLineMarginLeft,mMarginTop + mContentHeight/8f,"1");
        drawCircleAndText(canvas,1,mLengthwaysImaginaryLineMarginLeft,mMarginTop + mContentHeight/2f,"2");
        drawCircleAndText(canvas,0,mLengthwaysImaginaryLineMarginLeft,mMarginTop + mContentHeight/8 * 7f,"3");
    }

    /**
     * 绘制圆和文字
     * @param canvas
     * @param doStatus 0 表示未做 1 表示正在做 2 表示已完成
     * @param x
     * @param y
     * @param tag
     */
    private void drawCircleAndText(Canvas canvas,int doStatus,float x,float y,String tag) {
        if(doStatus == 0){
            //未作
            mProgressPaint.setColor(mUnFinishCircleColor);
            mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(x,y,mCircleRadius,mProgressPaint);

        }else if(doStatus == 1){
            //正在做 使用插值器
            canvas.drawCircle(x,y,mCurrentCircleRadius,mStatusPaint);
        }else {
            //已完成
            mProgressPaint.setColor(mFinishCirclrRimColor);
            mProgressPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(x,y,mCircleRadius, mProgressPaint);
            mProgressPaint.setStyle(Paint.Style.FILL);
            mProgressPaint.setColor(mFinishCirclrColor);
            canvas.drawCircle(x,y,mCircleRadius,mProgressPaint);
        }
        if(doStatus != 1){
            Rect rect = new Rect();
            mProgressPaint.setColor(mFinishTextColor);
            mProgressPaint.getTextBounds(tag,0,tag.length(),rect);
            canvas.drawText(tag,x - rect.width()/2,y + DpUtil.dp2px(mContext,4), mProgressPaint);
        }
        if(TextUtils.equals(tag,"1")){
            Rect text1Rect = new Rect();
            if(doStatus == 2){
                mProgressPaint.setColor(mFinishRightTextColor);
            }else {
                mProgressPaint.setColor(mUnfinishRightTextColor);
            }
            mProgressPaint.getTextBounds(contentStr1,0,contentStr1.length(),text1Rect);
            canvas.drawText(contentStr1,x + mCircleRadius +  mTextMarginLeft,y + text1Rect.height()/2,mProgressPaint);
        }
        if(TextUtils.equals(tag,"2")){
            Rect text2Rect = new Rect();
            if(doStatus == 2){
                mProgressPaint.setColor(mFinishRightTextColor);
            }else {
                mProgressPaint.setColor(mUnfinishRightTextColor);
            }
            mProgressPaint.getTextBounds(contentStr2_1,0,contentStr2_1.length(),text2Rect);
            canvas.drawText(contentStr2_1,x + mCircleRadius + mTextMarginLeft,y - mTextMarginTop - text2Rect.height()/2  ,mProgressPaint);
            canvas.drawText(contentStr2_2,x + mCircleRadius + mTextMarginLeft, y + text2Rect.height()/2,mProgressPaint);
            canvas.drawText(contentStr2_3,x + mCircleRadius + mTextMarginLeft, y + text2Rect.height()/2*3 + mTextMarginTop ,mProgressPaint);
        }
        if(TextUtils.equals(tag,"3")){
            Rect text3Rect = new Rect();
            if(doStatus == 2){
                mProgressPaint.setColor(mFinishRightTextColor);
            }else {
                mProgressPaint.setColor(mUnfinishRightTextColor);
            }
            mProgressPaint.getTextBounds(contentStr3,0,contentStr3.length(),text3Rect);
            canvas.drawText(contentStr3,x + mCircleRadius +  mTextMarginLeft,y + text3Rect.height()/2,mProgressPaint);
        }
    }

    /**
     * 绘制纵向虚线
     * @param canvas
     */
    private void drawImaginaryLine(Canvas canvas) {
        mImaginaryLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));//绘制虚线
        canvas.drawLine(mLengthwaysImaginaryLineMarginLeft,mMarginTop,
                mLengthwaysImaginaryLineMarginLeft,mViewHeight - mMarginTop,mImaginaryLinePaint);
        mImaginaryLinePaint.setPathEffect(null);
    }

    private ObjectAnimator animator;

    /**
     * 开始赋值绘制
     */
    public void startDrawable(){
        animator = ObjectAnimator.ofFloat(mContext, "progress", 0, mCircleRadius);
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentCircleRadius = (float) animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: 动画开始-->"+mCurrentCircleRadius +"   "+mCircleRadius);
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCurrentCircleRadius = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd: 动画结束再次启动动画....");
                animation.start();
            }
        });
        animator.start();
    }




}
