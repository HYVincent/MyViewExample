package com.vincent.myviewexample;

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

/**
 * @author Vincent 自定义进度条
 * @version v1.0 版本
 */
public class ProgressLineView extends View {

//    private static final String TAG = "自定义进度条";
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
    private float mProgressHeight = 10;
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
    //默认的圆半径，进度点
    private float mSmallCirclrRadius;
//    private int mSmallCircleColor;//进度点圆的颜色值、
    private Paint mSmallCirclePaint;
    //    private float offset;//手指在水平方向上的滑动平移
    private float mMaxOffsetX;//水平方向最大偏移量
    private float mMinOffsetX;//水平方向最小偏移量
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
        mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, 3);

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
        float mX;
        if (move && startTouch) {
            //移动中 自动取消动画
            startAnim = false;
            if (offsetX < mMinOffsetX) {
                offsetX = mMinOffsetX;
            }
            if (offsetX > mMaxOffsetX) {
                offsetX = mMaxOffsetX;
            }
            //处于移动中.. 此时要计算偏移量 处于移动中不能启动动画效果
            mX = mMarginLeft + valueToWidth(mCurrentValue) + offsetX;
//            Log.d(TAG, "drawValue：" + move + "  " + startAnim + "mAnimValue-->" + mAnimValue + " mCurrentValue-->" + mCurrentValue + " " + offsetX);
        } else {
            //处于非移动触摸状态 此时不需要计算偏移量
            if (startAnim) {
                //有动画
                mX = mMarginLeft + valueToWidth(mAnimValue);
            } else {
                mX = mMarginLeft + valueToWidth(mCurrentValue);
            }
        }
        if (mX > mMarginLeft + mProgressWidth) {
            mX = mMarginLeft + mProgressWidth;
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
    private int mLastX;
    //记录触摸的前一个点的y轴（垂直方向）偏移量
//    private int mLastY;
    //x轴（水平防线）上的偏移量
    private float offsetX;
    //true 点击  false 非点击
//    private boolean isClick = false;
    //记录点击的时间
//    private long startDownTime;


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        //当前的x坐标
        int x = (int) event.getX();
        //当前手指触摸的y坐标
//        int y = (int) event.getY();
        //检查是否可以触摸
        if(!startTouch){
            return false;
        }
        switch (event.getAction() ) {
            //按下
            case MotionEvent.ACTION_DOWN:
//                startDownTime = System.currentTimeMillis();
//                isClick = false;
                mLastX = x;
//                mLastY = y;
                move = true;
                //当按下的时候重新设置圆的半径
                mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, 5);
                //当按下的时候重新计算x轴(水平方向，也就是左右滑动)的最小偏移量
                mMinOffsetX = valueToWidth(mCurrentValue) * -1;
                ////当按下的时候重新计算x轴(水平方向，也就是左右滑动)的最大偏移量
                mMaxOffsetX = mProgressWidth - valueToWidth(mCurrentValue);
                //判断按下的位置是否超过进度条末尾
                if(x > mViewWidth){
                    x =  mViewWidth;
                }
                //判断按下的位置是否超过进度条开始
                if(x < mMarginLeft){
                    x = (int) mMarginLeft;
                }
                //计算按下的位置和当前进度位置的偏移量，在右边为正，左边为负
                if (x > mMarginLeft + valueToWidth(mCurrentValue)) {
                    offsetX = x - mMarginLeft - valueToWidth(mCurrentValue);
                } else {
                    offsetX = -1 * (valueToWidth(mCurrentValue) - x);
                }
                //重新计算value
                mCurrentValue = (x - mMarginLeft)/mProgressWidth * (mMaxValue - mMinValue);
                refreshView();
                break;
            case MotionEvent.ACTION_MOVE:
//                if (y >= mMarginTop && y <= mMarginTop + mProgressHeight) {//可以控制触摸的范围
                //水平方向上的偏移量
                offsetX = x - mLastX;
                //垂直方向上的偏移量
//                    int offsetY = y - mLastY;
                refreshView();
//                }
                break;
            case MotionEvent.ACTION_UP:
                //对于点击事件的判断 其实可以不用判断了 按下的时候处理了
                /*if (System.currentTimeMillis() - startDownTime < 300) {
                    isClick = true;
                    //处理点击事件
                    if (x > mMarginLeft && x < mMarginLeft + mProgressWidth && y > mMarginTop / 2 && y < mMarginTop + mProgressHeight) {
                        mCurrentValue = (x - mMarginLeft) / mProgressWidth * (mMaxValue - mMinValue);
                        refreshView();
                    }
                } else {
                    isClick = false;
                }*/
                move = false;
                mCurrentValue = (x - mMarginLeft) / mProgressWidth * (mMaxValue - mMinValue);
                refreshView();
//                offset = 0;
                mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, 3);
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



    /**
     * 开启动画
     */
    public void startAnim() {
        //动画插值器
        ObjectAnimator  animator = ObjectAnimator.ofFloat(mContext, "progress", 0, mCurrentValue);
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
                mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, 3);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mSmallCirclrRadius = mProgressHeight / 2 + DpUtil.dp2px(mContext, 5);
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

    private int measureHeight(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result =  mViewHeight;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

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
