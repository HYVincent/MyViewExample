package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vincent.myviewexample.utils.DpUtil;
import com.vincent.myviewexample.R;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name OkHttpDemo
 * @page com.vincent.okhttpdemo
 * @class describe
 * @date 2018/11/20 16:59
 */
public class MySwitchView extends View {


    public MySwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        init(context);
    }


    private Context mContext;
    private void init(Context mContext){
        this.mContext = mContext;

        mViewWidth = DpUtil.dp2px(mContext,mViewWidth);
        mViewHeight = DpUtil.dp2px(mContext,mViewHeight);
        mCirclrRadius = mViewHeight/2 - DpUtil.dp2px(mContext,1);

        mOpenBgColor = ContextCompat.getColor(mContext,R.color.color_green_03df1d);
        mOpenCircleColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mDefaultBgColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mDefaultCircleColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);

        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //先绘制背景
        drawBg(canvas);
        //绘制圆
        drawCircle(canvas);
    }

    /**
     * 绘制圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        if(mSwitch){
            mCirclePaint.clearShadowLayer();
            mCirclePaint.setColor(mOpenCircleColor);
            canvas.drawCircle(mViewWidth - mCirclrRadius - DpUtil.dp2px(mContext,1),mViewHeight/2,mCirclrRadius,mCirclePaint);
        }else {
            mCirclePaint.setColor(mDefaultCircleColor);
            //绘制阴影，param1：模糊半径；param2：x轴大小：param3：y轴大小；param4：阴影颜色
            mCirclePaint.setShadowLayer(5, 6, 6, ContextCompat.getColor(mContext,R.color.color_gray_cccbcb));
            canvas.drawCircle(mCirclrRadius + DpUtil.dp2px(mContext,1),mViewHeight/2,mCirclrRadius,mCirclePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mSwitch){
                    mSwitch = false;
                }else {
                    mSwitch = true;
                }
                setmSwitch(mSwitch);
                break;
        }
        return true;
    }

    /**
     * 绘制背景
     * @param canvas
     */
    private void drawBg(Canvas canvas) {
        RectF rect = new RectF();
        rect.left = 0;
        rect.right = mViewWidth;
        rect.top = 0;
        rect.bottom = mViewHeight;
        if(mSwitch){
            //打开
            mRectPaint.setColor(mOpenBgColor);
        }else {
            //关闭
            mRectPaint.setColor(mDefaultBgColor);
        }
        canvas.drawRoundRect(rect,mViewHeight/2,mViewHeight/2,mRectPaint);
    }

    private float mViewWidth = 33;//开关按钮的宽度
    private float mViewHeight = 20;//开关按钮的高度
    private float mCirclrRadius;//圆的半径
    private int mOpenBgColor;//打开状态的背景颜色
    private int mOpenCircleColor;//打开状态下的圆的颜色
    private int mDefaultBgColor;//默认背景颜色
    private int mDefaultCircleColor;//默认圆的颜色
    private boolean mSwitch = false; //true 开  false 关
    private Paint mRectPaint;//圆环背景画笔
    private Paint mCirclePaint;//圆画笔

    /**
     * true 开  false 关
     * @param mSwitch
     */
    public void setmSwitch(boolean mSwitch) {
        this.mSwitch = mSwitch;
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
