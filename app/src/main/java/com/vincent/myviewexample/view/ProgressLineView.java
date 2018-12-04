package com.vincent.myviewexample.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;

/**
 * @author Vincent 自定义进度条
 * @version v1.0 版本
 */
public class ProgressLineView extends View {

    private static final String TAG = "自定义进度条";
    private Context mContext;
    // mViewWidth View视图的宽度
    private int mViewWidth;
    // mViewWidth View视图的高度
    private int mViewHeight;
    //背景颜色值
    private int mBgColor;
    //进度颜色值
    private int mProgressColor;
    //进度条高度
    private float mProgressHeight = 3;
    //进度的宽度
    private float mProgressWidth;
    //默认进度最大为100
    private float mMaxValue = 100;
    //默认进度最小值为0
    private float mMinValue = 0;
    //默认当前进度
    private float mCurrentValue = 40;
    //进度条其开始绘制位置距离View最左边的间距
    private float mMarginLeft = 15;
    //进度条其结束绘制位置距离View最右边的间距
    private float mMarginRight = 15;
    //进度条距离View顶部的间距
    private float mMarginTop = 10;
    //进度条距离View底部的间距
    private float mMarginBottom = 10;
    //背景画笔
    private Paint mBgPaint;
    //背景矩形
    private RectF bgRectF;
    //值矩形
    private RectF valueRectF;
    //进度画笔
    private Paint mValuePaint;

    //动画值
    private float mAnimValue = 0;//
    //true 有动画  false 没有动画
    private boolean startAnim = true;
    //是否可以触摸调节进度 true 可以 false不可以
    private boolean startTouch = true;
    private float defaultRadius = 2;
    private float bigRadius = 5;
    //默认的圆半径，进度点
    private float mSmallCirclrRadius;
    private Paint mSmallCirclePaint;
    private boolean move = false;//true 滑动中 false 停止滑动
    private MoveValueChangeListener moveValueChangeListener;
    //进度小圆的颜色值
    private int mSmallCircleColor;

    /**
     * 初始化属性
     *
     * @param context context
     */
    private void init(Context context) {
        this.mContext = context;
        mBgColor = ContextCompat.getColor(mContext, R.color.color_gray_5d5f5e);
        mProgressColor = ContextCompat.getColor(mContext, R.color.color_green_a8edc5);
        mSmallCircleColor = ContextCompat.getColor(mContext, android.R.color.holo_orange_dark);
        //把dp值转为px值
        mProgressHeight = DpUtil.dp2px(mContext, mProgressHeight);
        mMarginLeft = DpUtil.dp2px(mContext, mMarginLeft);
        mMarginRight = DpUtil.dp2px(mContext, mMarginRight);
        mMarginTop = DpUtil.dp2px(mContext, mMarginTop);
        mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, defaultRadius);
        mMarginBottom = DpUtil.dp2px(mContext,mMarginBottom);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mValuePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        mSmallCirclePaint = new Paint();
        mSmallCirclePaint.setAntiAlias(true);
        mSmallCirclePaint.setColor(mSmallCircleColor);
//        mSmallCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        bgRectF = new RectF();
        valueRectF = new RectF();
    }

    public ProgressLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mProgressWidth = mViewWidth - mMarginLeft - mMarginRight;
        drawProgressBg(canvas);
        drawValue(canvas);
    }

    /**
     * 统一调用此方法刷新
     */
    private void refreshView() {
        invalidate();
    }

    private float lastValue;

    /**
     * 绘制值
     *
     * @param canvas 画布
     */
    private void drawValue(Canvas canvas) {
        if(mCurrentValue <= mMinValue){
            mCurrentValue = mMinValue;
        }
        if(mCurrentValue >= mMaxValue){
            mCurrentValue = mMaxValue;
        }
        valueRectF.top = mMarginTop;
        valueRectF.left = mMarginLeft;
        float mX = x;
        if(mX < mMarginLeft){
            mX = mMarginLeft;
        }
        if(mX > mMarginLeft + mProgressWidth){
            mX = mMarginLeft + mProgressWidth;
        }
        if (move && startTouch) {
            //移动中 自动取消动画
            startAnim = false;
        } else {
            //处于非移动触摸状态 此时不需要计算偏移量
            if (startAnim) {
                //有动画
                mX = mMarginLeft + valueToWidth(mAnimValue);
            } else {
                mX = mMarginLeft + valueToWidth(mCurrentValue);
            }
        }
        valueRectF.right = mX;
        valueRectF.bottom = mProgressHeight + mMarginTop;
        //设置值画笔颜色 动态修改之后生效
        mValuePaint.setColor(mProgressColor);
        canvas.drawRoundRect(valueRectF, mProgressHeight / 2, mProgressHeight / 2.0f, mValuePaint);
        canvas.drawCircle(mX, mMarginTop + mProgressHeight / 2.0f, mSmallCirclrRadius, mSmallCirclePaint);
        if (moveValueChangeListener != null) {
            moveValueChangeListener.onMoveChange(mCurrentValue);
        }
    }

    /**
     * 把值转换为对应的宽度 不包含开头间距
     * @param value value
     * @return 实际距离
     */
    private float valueToWidth(float value) {
        return mProgressWidth * (value * 1.0f / (mMaxValue - mMinValue));
    }
    //记录前一个点的x坐标，用来计算偏移量
//    private float mLastX;
    //记录触摸的前一个点的y轴（垂直方向）偏移量
//    private int mLastY;
    //x轴（水平防线）上的偏移量
//    private float offsetX;
    //true 点击  false 非点击
//    private boolean isClick = false;
    //记录点击的时间
//    private long startDownTime;


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private float x;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        //当前的x坐标
        x =  event.getX();
        if(!startTouch){
            return false;
        }
        if(x > mViewWidth - mMarginRight){
            x = mViewWidth - mMarginRight;
        }
        if(x < mMarginLeft){
            x = mMarginLeft;
        }
        //计算当前进度值
        mCurrentValue = (x - mMarginLeft)/mProgressWidth * (mMaxValue - mMinValue);
        switch (event.getAction() ) {
            //按下
            case MotionEvent.ACTION_DOWN:
                lastValue = mCurrentValue;
                move = true;
                //当按下的时候重新设置圆的半径
                mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, bigRadius);
                break;
            case MotionEvent.ACTION_MOVE:
                refreshView();
                break;
            case MotionEvent.ACTION_UP:
                move = false;
                mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, defaultRadius);
                refreshView();
                break;
        }
        return true;
    }

    /**
     * 绘制进度条背景
     *
     * @param canvas  画布
     */
    private void drawProgressBg(Canvas canvas) {
        //判断mMarginTop这个值是否处于正常范围，不然会影响进度条的显示
        if (mMarginTop > (mViewHeight - mProgressHeight) / 2) {
            mMarginTop = (mViewHeight - mProgressHeight) / 2;
        }
        bgRectF.top = mMarginTop;
        bgRectF.left = mMarginLeft;
        bgRectF.right = mViewWidth - mMarginRight;
        bgRectF.bottom = mProgressHeight + mMarginTop;
        mBgPaint.setColor(mBgColor);
        canvas.drawRoundRect(bgRectF, mProgressHeight / 2, mProgressHeight / 2.0f, mBgPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 设置是否开启动画模式
     *
     * @param startAnim true 有动画  false没有动画
     */
    public void setStartAnim(boolean startAnim) {
        this.startAnim = startAnim;
    }

    /**
     * 设置当前进度
     *
     * @param mCurrentValue value
     */
    public void setmCurrentValue(float mCurrentValue) {
        this.mCurrentValue = mCurrentValue;
        if (startAnim) {
            startAnim();
        } else {
            refreshView();
        }
    }

    /**
     * 设置是否可以触摸调节进入值
     *
     * @param startTouch true 可触摸 false  禁止触摸
     */
    public void setStartTouch(boolean startTouch) {
        this.startTouch = startTouch;
    }

    /**
     * 设置背景颜色
     * @param mBgColor color
     */
    public void setmBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
    }


    private void setMAnimValue(float mAnimValue) {
        this.mAnimValue = mAnimValue;
    }

    /**
     * 开启动画
     */
    public void startAnim() {
        //动画插值器
        ObjectAnimator  animator = ObjectAnimator.ofFloat(this, "mAnimValue", 0, mCurrentValue);
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimValue = (float) animation.getAnimatedValue();
                refreshView();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //相当于无限循环
//                animation.start();
                mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, defaultRadius);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, bigRadius);
            }
        });
        animator.start();
    }

    /**
     * 设置滑动监听
     *
     * @param moveValueChangeListener listener
     */
    public void setMoveValueChangeListener(MoveValueChangeListener moveValueChangeListener) {
        this.moveValueChangeListener = moveValueChangeListener;
    }

    /**
     * 设置进度小圆的颜色值
     * @param mSmallCircleColor color
     */
    public void setmSmallCircleColor(int mSmallCircleColor) {
        this.mSmallCircleColor = mSmallCircleColor;
    }

    /**
     * 设置进度值颜色
     * @param mProgressColor
     */
    public void setmProgressColor(int mProgressColor){
        this.mProgressColor = mProgressColor;
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
     * 适配高度
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) (mMarginTop + mProgressHeight + mMarginBottom);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    /**
     * 适配宽度
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            //根据自己的需要更改
            result =  mViewWidth;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    public interface MoveValueChangeListener {

        void onMoveChange(float value);

    }

}
