package com.vincent.myviewexample.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;
import com.vincent.myviewexample.utils.ScreenUtils;

/**
 * @author win 10 Vincent
 * @version v1.0
 * @name SenMouPet
 * @page com.senmou.pet.view
 * @class describe
 * @date 2018/7/6 16:39
 */

@SuppressLint("AppCompatCustomView")
public class TagTextView extends View {

    private int number;
    private String tag = "xxx";
    private Paint mPaint;
    private Context mContext;
    private float viewWidth;
    private float viewHeight;
    private float mMarginTop = 10;
    private float mMarginBottom = 10;
    private float mMarginLeft = 10;
    private float mNumberMarginLeft = 20;

    public void setTag(String tag) {
        this.tag = tag;
        invalidate();
    }

    public void setNumber(int number) {
        this.number = number;
        invalidate();
    }

    public TagTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TagTextView, 0, 0);
        number = typeArray.getInteger(R.styleable.TagTextView_number,0);
        tag = typeArray.getString(R.styleable.TagTextView_tag);
        typeArray.recycle();
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_black_000000));
        mPaint.setTextSize(DpUtil.dp2px(mContext,14));
        mMarginTop = DpUtil.dp2px(mContext,mMarginTop);
        mMarginBottom = DpUtil.dp2px(mContext,mMarginBottom);
        mMarginLeft = DpUtil.dp2px(mContext,mMarginLeft);
        mNumberMarginLeft = DpUtil.dp2px(mContext,mNumberMarginLeft);
        mRect = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    private Rect mRect;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(ContextCompat.getColor(mContext,R.color.color_white_ffffff));
        mPaint.getTextBounds(tag,0,tag.length(),mRect);
        canvas.drawText(tag,mMarginLeft,mRect.height() + mMarginTop,mPaint);
        if(number != 0){
            String text = "("+String.valueOf(number)+")";
            canvas.drawText(text,mRect.width() + mNumberMarginLeft,mRect.height() + mMarginTop,mPaint);
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
            result = (int) Math.max(viewHeight,DpUtil.dp2px(mContext,40));
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
