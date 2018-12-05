package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
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
 * @date 2018/12/5 14:08
 */
public class MyText extends View {
    public MyText(Context context,@Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private Context mContext;
    //文字
    private String mTextString;
    private int mStartColor;
    private int mEndColor;
    private Paint mPaint;
    private float mTextColorSize = 15;
    private Rect mTextRect = new Rect();
    private float mTextPaddingLeft = 10;
    private float mTextPaddingRight = 10;
    private float mTextPaddingTop = 5;
    private float mTextPaddingBottom = 5;
    private float mViewHeight;
    private float mViewWidth;
    private LinearGradient linearGradient;

    /**
     * 设置文字
     * @param mTextString 文字
     */
    public void setmTextString(String mTextString) {
        this.mTextString = mTextString;
        setmTextString(mTextString,0.25f,0.25f);
    }

    /**
     * 设置文字和渐变比例
     * @param mTextString 文字
     * @param startGradualChangeRatio 开始渐变的起始位置
     * @param endtGradualChangeRatio 结束渐变的位置比例
     */
    public void setmTextString(String mTextString,float startGradualChangeRatio,float endtGradualChangeRatio) {
        if(startGradualChangeRatio < 0){
            startGradualChangeRatio = 0.01f;
        }
        if(startGradualChangeRatio > 0.5){
            startGradualChangeRatio = 0.5f;
        }
        if(endtGradualChangeRatio < 0){
            startGradualChangeRatio = 0.01f;
        }
        if(startGradualChangeRatio > 0.5){
            startGradualChangeRatio = 0.5f;
        }
        this.mTextString = mTextString;
        mPaint.getTextBounds(mTextString,0,mTextString.length(),mTextRect);
        mTextPaddingLeft = DpUtil.dp2px(mContext,mTextPaddingLeft);
        mTextPaddingRight = DpUtil.dp2px(mContext,mTextPaddingRight);
        mTextPaddingTop = DpUtil.dp2px(mContext,mTextPaddingTop);
        mTextPaddingBottom = DpUtil.dp2px(mContext,mTextPaddingBottom);
        mViewHeight = mTextPaddingTop + mTextPaddingBottom + mTextRect.height();
        mViewWidth = mTextPaddingLeft +mTextPaddingRight + mTextRect.width();
        linearGradient = new LinearGradient(mViewWidth/2 - mTextRect.width() * startGradualChangeRatio,mViewHeight/2,
                mViewWidth/2 + mTextRect.width() * endtGradualChangeRatio,mViewHeight/2,mStartColor,mEndColor,Shader.TileMode.CLAMP);
        invalidate();
    }

    private void init(Context context) {
        this.mContext = context;
        mStartColor = ContextCompat.getColor(mContext,R.color.color_red_e21493);
        mEndColor = ContextCompat.getColor(mContext,R.color.color_purple_bf3ccf);
        mTextColorSize = DpUtil.dp2px(mContext,mTextColorSize);

        mPaint = new Paint();
        mPaint.setTextSize(mTextColorSize);
        mPaint.setAntiAlias(true);
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/NotoSansHans-Black.otf");
        mPaint.setTypeface(tf);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!TextUtils.isEmpty(mTextString)){
            mPaint.setShader(linearGradient);
            canvas.drawText(mTextString,mViewWidth/2 - mTextRect.width()/2,mViewHeight /2 +mTextRect.height()/2,mPaint);
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
