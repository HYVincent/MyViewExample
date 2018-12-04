package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.bean.SkinTestResultBean;
import com.vincent.myviewexample.utils.DpUtil;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.view
 * @class describe 皮肤测试结果展示视图
 * @date 2018/12/3 10:47
 */
public class MySkinTestResultView extends View {

    public MySkinTestResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private Context mContext;
    private float mViewWidth;
    private float mViewHeight;
    //View的X轴方向中点x
    private float mViewWidthHalf;
    //View的Y轴方向中点Y
    private float mViewHeightHalf;
    //0 当前(此时View位于中间，这个时候只测量过一次，没有多余数据)  1 前一个(上一次的数据)  2 后一个(当前的数据)
    private int viewStatus = 0;
    private float mNumberTextSize = 18;
    private int mNumberTextColor;
    private Paint mNumberPaint;
    //矩形背景边线的颜色值
    private int mRectBackgroundMarginColor;
    private Paint mMarginPaint;
    //矩形背景交车斜线颜色
    private int mRectBackgroundCrossLineColor;
    //正方形背景颜色
    private int mRectBackgroundColor;
    private float mRectBackgroundCrossLineWidth = 1;
    //矩形背景边线的宽度
    private float mRectBackgroundMarginWidth = 2;
    private Paint mRectBackgroundPaint;
    //三角形的长度
    private float mTrilateralLength = 75f;
    private float mCenterX;
    private float mCenterY;
    private float mRectLeftX;
    private float mRectLeftY;
    private float mRectTopX;
    private float mRectTopY;
    private float mRectBottomX;
    private float mRectBottomY;
    private float mRectRightX;
    private float mRectRightY;
    private String mTopTag = "皮肤水分";
    private String mLeftTag = "油分";
    private String mBottomTag = "白皙度";
    private String mRightTag = "弹性";
    private Paint mTagPaint;
    private int mTagTextColor;
    private float mTagTextSize = 12;
    private float mTagMargin = 3;
    private SkinTestResultBean result;


    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        //设置中间文字属性
        mNumberTextColor = ContextCompat.getColor(mContext, R.color.color_white_ffffff);
        mNumberTextSize = DpUtil.dp2px(mContext,mNumberTextSize);
        mNumberPaint = new Paint();
        mNumberPaint.setTextSize(mNumberTextSize);
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setColor(mNumberTextColor);

        mTrilateralLength = DpUtil.dp2px(mContext,mTrilateralLength);
        mRectBackgroundMarginWidth = DpUtil.dp2px(mContext,mRectBackgroundMarginWidth);
        mRectBackgroundCrossLineWidth = DpUtil.dp2px(mContext, mRectBackgroundCrossLineWidth);
        mRectBackgroundMarginColor = ContextCompat.getColor(mContext,R.color.color_purple_bf3ccf);
        mRectBackgroundCrossLineColor = ContextCompat.getColor(mContext,R.color.color_purple_ce72e4);
        mRectBackgroundColor = ContextCompat.getColor(mContext,R.color.color_purple_7a00ab);
        mRectBackgroundPaint = new Paint();
        mRectBackgroundPaint.setAntiAlias(true);
        mRectBackgroundPaint.setColor(mRectBackgroundColor);
        mRectBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        mRectBackgroundPaint.setStyle(Paint.Style.FILL);

        mTagTextColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mTagTextSize = DpUtil.dp2px(mContext,mTagTextSize);
        mTagMargin = DpUtil.dp2px(mContext,mTagMargin);
        mTagPaint = new Paint();
        mTagPaint.setAntiAlias(true);
        mTagPaint.setTextSize(mTagTextSize);
        mTagPaint.setColor(mTagTextColor);

        mMarginPaint = new Paint();
        mMarginPaint.setAntiAlias(true);
        mMarginPaint.setStrokeWidth(mRectBackgroundMarginWidth);
        mMarginPaint.setStyle(Paint.Style.STROKE);
        mMarginPaint.setColor(mRectBackgroundMarginColor);
        mMarginPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔头头部变圆

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(mContext,R.color.color_black_000000));
        drawBgRect(canvas);
    }

    /**
     * 先绘制紫色的矩形背景
     * @param canvas
     */
    private void drawBgRect(Canvas canvas) {
        mCenterX = mViewWidthHalf;
        mCenterY = mViewHeightHalf;

        mRectTopX = mCenterX;
        mRectTopY = mCenterY - mTrilateralLength;

        mRectBottomX = mCenterX;
        mRectBottomY = mCenterY + mTrilateralLength;

        mRectLeftX = mCenterX - mTrilateralLength;
        mRectLeftY = mCenterY;

        mRectRightX = mCenterX + mTrilateralLength;
        mRectRightY = mCenterY;
        Path mPath = new Path();
        mPath.moveTo(mRectTopX,mRectTopY);
        mPath.lineTo(mRectTopX,mRectTopY);
        mPath.lineTo(mRectLeftX,mRectLeftY);
        mPath.lineTo(mRectBottomX,mRectBottomY);
        mPath.lineTo(mRectRightX,mRectRightY);
        mPath.close();
        canvas.drawPath(mPath,mRectBackgroundPaint);
        Path mLinePath = new Path();
        mLinePath.moveTo(mRectTopX,mRectTopY);
        mLinePath.lineTo(mRectTopX,mRectTopY);
        mLinePath.lineTo(mRectLeftX,mRectLeftY);
        mLinePath.lineTo(mRectBottomX,mRectBottomY);
        mLinePath.lineTo(mRectRightX,mRectRightY);
        mLinePath.lineTo(mRectTopX,mRectTopY);
        canvas.drawPath(mLinePath,mMarginPaint);
        //绘制上下左右的tag文字
        Rect tagRect = new Rect();
        mTagPaint.getTextBounds(mTopTag,0,mTopTag.length(),tagRect);
        canvas.drawText(mTopTag,mRectTopX - mTagPaint.measureText(mTopTag)/2,mRectTopY  - mTagMargin - mTagMargin,mTagPaint);
        mTagPaint.getTextBounds(mBottomTag,0,mBottomTag.length(),tagRect);
        canvas.drawText(mBottomTag,mRectBottomX - mTagPaint.measureText(mBottomTag)/2,mRectBottomY + tagRect.height() + mTagMargin,mTagPaint);
        mTagPaint.getTextBounds(mLeftTag,0,mLeftTag.length(),tagRect);
        canvas.drawText(mLeftTag,mRectLeftX - mTagPaint.measureText(mLeftTag) - mTagMargin,mRectLeftY + tagRect.height()/2,mTagPaint);
        mTagPaint.getTextBounds(mRightTag,0,mRightTag.length(),tagRect);
        canvas.drawText(mRightTag,mRectRightX + mTagMargin,mRectRightY + tagRect.height()/2,mTagPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mViewWidth = w;
        this.mViewHeight = h;
        mViewWidthHalf = mViewWidth/2.0f;
        mViewHeightHalf = mViewHeight/2.0f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 设置测试结果
     * @param result
     */
    public void setResult(SkinTestResultBean result) {
        this.result = result;
    }
}
