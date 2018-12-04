package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name OkHttpDemo
 * @page com.vincent.okhttpdemo
 * @class describe
 * @date 2018/11/19 17:56
 */
public class WaterInfoView extends View {

    public WaterInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(mContext);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mViewHeight = h;
        this.mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(mContext,R.color.color_white_ffffff));
        //首先绘制中间的数字
        drawCenterNumber(canvas);
//        drawButtomLine(canvas);
        drawStatusText(canvas);
        drawWaterQualityStatus(canvas);
    }

    private Paint mWaterQualityStatusPaint;
    private int mWaterQualityStatusTextColor;
    private int mWaterQualityStatusTextSize= 14;
    private String mWaterQualityStatus = "水质状态";
    private float mWaterQualityStatusTextMarginTop = 10;//状态文字距离View顶部的距离

    /**
     * 绘制水质状态文字
     * @param canvas
     */
    private void drawWaterQualityStatus(Canvas canvas) {
        Rect mWaterQualityStatusRect = new Rect();
        mWaterQualityStatusPaint.getTextBounds(mWaterQualityStatus,0,mWaterQualityStatus.length(),mWaterQualityStatusRect);
        canvas.drawText(mWaterQualityStatus,mViewWidth/2 - mWaterQualityStatusRect.width()/2,
                mWaterQualityStatusTextMarginTop + mWaterQualityStatusRect.height(),mWaterQualityStatusPaint);
    }

    /**
     * 绘制状态文字
     * @param status
     */
    public void setmStatusStr(String status){
        this.mStatusStr = status;
    }

    /**
     * 是否绘制状态文字背景
     * @param drawStatusBg
     */
    public void setDrawStatusBg(boolean drawStatusBg) {
        this.drawStatusBg = drawStatusBg;
    }

    private boolean drawStatusBg = false;//true 绘制状态背景 false 不绘制
    private Paint mStatusPaint;
    private int mStatusTextColor;
    private String mStatusStr = "设备已离线";
    private int mStatusTextSize = 14;
    private float mStatusTextMarginTop = 15;//状态文字距离数字的距离
    private float mNuberMarginY;//数字底部的y坐标
    private void drawStatusText(Canvas canvas) {
        if(!TextUtils.isEmpty(mStatusStr)){
            Rect mStatusRect = new Rect();
            mStatusPaint.getTextBounds(mStatusStr,0,mStatusStr.length(),mStatusRect);
            mStatusPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawText(mStatusStr,mViewWidth/2 - mStatusRect.width()/2,mNuberMarginY + mStatusTextMarginTop + mStatusRect.height(),mStatusPaint);
//            canvas.drawLine(0,mNuberMarginY,mViewWidth,mNuberMarginY,mStatusPaint);
            if(drawStatusBg){
                //绘制背景环形
                //绘制底部状态文字的背景
                RectF rectF = new RectF();
                rectF.set(mViewWidth/2 - mStatusRect.width()/2  - DpUtil.dp2px(mContext,10),
                        mNuberMarginY + mStatusTextMarginTop - DpUtil.dp2px(mContext,5) ,
                        mViewWidth/2 + mStatusRect.width()/2 + DpUtil.dp2px(mContext,10),
                        mNuberMarginY + mStatusTextMarginTop + mStatusRect.height()+ DpUtil.dp2px(mContext,8));
                mStatusPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRoundRect(rectF,mStatusRect.height() * 2,mStatusRect.height() * 2,mStatusPaint);
            }
        }
    }

    private Paint mButtomLinePaint;
    private float mBottomLineInterval = 10;//两个横线之间的间隔
    private int mBottomLineSelectColor;//选中的颜色
    private int mBottomLineColor;//线条默认颜色
    private boolean mFirstSelect = true;//默认选中第一个
    private float mBottomLineWidth = 20;//线条宽度
    private float mBottomLineMargin= 0;//底部的线条距离View底部最下面的距离
    private void drawButtomLine(Canvas canvas) {
        if(mFirstSelect){
            mButtomLinePaint.setColor(mBottomLineSelectColor);
            canvas.drawLine(mViewWidth/2 - mBottomLineInterval/2 - mBottomLineWidth,mViewHeight - mBottomLineMargin,
                    mViewWidth/2 - mBottomLineInterval/2,mViewHeight - mBottomLineMargin,mButtomLinePaint);
            mButtomLinePaint.setColor(mBottomLineColor);
            canvas.drawLine(mViewWidth/2 + mBottomLineInterval/2,mViewHeight - mBottomLineMargin,
                    mViewWidth/2 + mBottomLineInterval/2 + mBottomLineWidth,mViewHeight - mBottomLineMargin,mButtomLinePaint);
        }else {
            mButtomLinePaint.setColor(mBottomLineColor);
            canvas.drawLine(mViewWidth/2 - mBottomLineInterval/2 - mBottomLineWidth,mViewHeight - mBottomLineMargin,
                    mViewWidth/2 - mBottomLineInterval/2,mViewHeight - mBottomLineMargin,mButtomLinePaint);
            mButtomLinePaint.setColor(mBottomLineSelectColor);
            canvas.drawLine(mViewWidth/2 + mBottomLineInterval/2,mViewHeight - mBottomLineMargin,
                    mViewWidth/2 + mBottomLineInterval/2 + mBottomLineWidth,mViewHeight - mBottomLineMargin,mButtomLinePaint);
        }
    }

    private Context mContext;
    private float mViewWidth;
    private float mViewHeight;
    private static final String TAG = "绘图";

    private Paint mNumberPaint;
    private int mNumberColor;//数字的颜色
    private int mNumberZeroAlpha = 80;//数字0的透明度
    private int mNumberTextSize = 72;//数字的大小
    private int mNumber = 11;//值
    private String mNumberStr;
    private String mNumberUnit="ML";//数字旁边的额单位
    private Paint mNumberUnitPaint;
    private int mNuberUnitTextSize = 19;
    private int mNuberUnitTextColor;
    private float mNumberMarginUnit = 10;//数字和单位之间的间距


    private void init(Context mContext) {
        mNumberColor = ContextCompat.getColor(mContext,R.color.color_green_ff03e195);
        mNuberUnitTextColor = ContextCompat.getColor(mContext,R.color.color_green_ff03e195);
        mBottomLineSelectColor = ContextCompat.getColor(mContext,R.color.color_yellow_F6AD42);
        mBottomLineColor = ContextCompat.getColor(mContext,R.color.color_gray_E7E4E4);
        mStatusTextColor = ContextCompat.getColor(mContext,R.color.color_yellow_F1A604);
        mWaterQualityStatusTextColor = ContextCompat.getColor(mContext,R.color.color_black_545454);

        mNumberMarginUnit = DpUtil.dp2px(mContext,mNumberMarginUnit);
        mNumberTextSize = DpUtil.dp2px(mContext,mNumberTextSize);
        mNuberUnitTextSize = DpUtil.dp2px(mContext,mNuberUnitTextSize);
        mBottomLineMargin = DpUtil.dp2px(mContext,mBottomLineMargin);
        mBottomLineWidth = DpUtil.dp2px(mContext,mBottomLineWidth);
        mBottomLineInterval = DpUtil.dp2px(mContext,mBottomLineInterval);
        mStatusTextSize = DpUtil.dp2px(mContext,mStatusTextSize);
        mStatusTextMarginTop = DpUtil.dp2px(mContext,mStatusTextMarginTop);
        mWaterQualityStatusTextSize = DpUtil.dp2px(mContext,mWaterQualityStatusTextSize);
        mWaterQualityStatusTextMarginTop = DpUtil.dp2px(mContext,mWaterQualityStatusTextMarginTop);

        mNumberPaint = new Paint();
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setTextSize(mNumberTextSize);
        mNumberPaint.setColor(mNumberColor);// fonts/DINCond-Medium.ttf
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/DINCond-Medium.otf");
        mNumberPaint.setTypeface(tf);

        mNumberUnitPaint = new Paint();
        mNumberUnitPaint.setAntiAlias(true);
        mNumberUnitPaint.setTextSize(mNuberUnitTextSize);
        mNumberUnitPaint.setColor(mNuberUnitTextColor);

        mButtomLinePaint = new Paint();
        mButtomLinePaint.setAntiAlias(true);
        mButtomLinePaint.setStrokeWidth(DpUtil.dp2px(mContext,2));

        mStatusPaint = new Paint();
        mStatusPaint.setAntiAlias(true);
        mStatusPaint.setColor(mStatusTextColor);
        mStatusPaint.setTextSize(mStatusTextSize);

        mWaterQualityStatusPaint = new Paint();
        mWaterQualityStatusPaint.setAntiAlias(true);
        mWaterQualityStatusPaint.setColor(mWaterQualityStatusTextColor);
        mWaterQualityStatusPaint.setTextSize(mWaterQualityStatusTextSize);
    }

    /**
     * 绘制中间的数字
     * @param canvas
     */
    private void drawCenterNumber(Canvas canvas){
        Rect mNumberUnitRect = new Rect();//单位矩形
        if(mNumber != 0){
            mNumberStr = String.valueOf(mNumber);
            float mContentWidth;
            if(mNumber > 0 && mNumber < 10){
                String mNumberHead = "00";
                Rect mNumberHeadRect = new Rect();//头部矩形 小于100时才有用
                mNumberPaint.getTextBounds(mNumberHead,0,mNumberHead.length(),mNumberHeadRect);
                mNumberStr = String.valueOf(mNumber);
                mNumberUnitPaint.getTextBounds(mNumberUnit,0,mNumberUnit.length(),mNumberUnitRect);
                if(!TextUtils.isEmpty(mNumberUnit)){
                    //有单位
                    mContentWidth =  mNumberHeadRect.width()/2*3 + mNumberMarginUnit + mNumberUnitRect.width();
                    mNumberPaint.setAlpha(mNumberZeroAlpha);
                    canvas.drawText(mNumberHead,mViewWidth/2 - mContentWidth/2,mViewHeight/2 +mNumberHeadRect.height()/2,mNumberPaint);
                    mNumberPaint.setAlpha(255);
                    canvas.drawText(mNumberStr,mViewWidth/2 - mContentWidth/2 + mNumberHeadRect.width() + DpUtil.dp2px(mContext,8),
                            mViewHeight/2 +mNumberHeadRect.height()/2,mNumberPaint);
                    canvas.drawText(mNumberUnit,mViewWidth/2 + mContentWidth/2 - mNumberUnitRect.width(),mViewHeight/2 +mNumberHeadRect.height()/2,mNumberUnitPaint);
                }else {
                    mContentWidth = mNumberHeadRect.width() / 2 * 3 + DpUtil.dp2px(mContext,8);
                    //没有单位
                    canvas.drawText(mNumberHead,mViewWidth/2 - mContentWidth/2,mViewHeight/2 + mNumberHeadRect.height()/2,mNumberPaint);
                    canvas.drawText(mNumberStr,mViewWidth/2 + mContentWidth/2 - mNumberHeadRect.width()/2,
                            mViewHeight/2 + mNumberHeadRect.height()/2,mNumberPaint);
                }
                mNuberMarginY = mViewHeight/2 +mNumberHeadRect.height()/2;
            }else if(mNumber >= 10 && mNumber < 100) {
                Rect mNumberHeadRect = new Rect();//头部矩形 小于100时才有用
                String mNumberHead = "0";
                mNumberPaint.getTextBounds(mNumberHead,0,mNumberHead.length(),mNumberHeadRect);
                if(TextUtils.isEmpty(mNumberUnit)){
                    //无单位
                    mContentWidth = mNumberHeadRect.width() * 3 + DpUtil.dp2px(mContext,8);
                    mNumberPaint.setAlpha(mNumberZeroAlpha);
                    canvas.drawText(mNumberHead,mViewWidth/2 - mContentWidth/2,
                            mViewHeight/2 + mNumberHeadRect.height()/2,mNumberPaint);
                    mNumberPaint.setAlpha(255);
                    canvas.drawText(mNumberStr,mViewWidth/2 - mContentWidth/2 + mNumberHeadRect.width()+DpUtil.dp2px(mContext,8),
                            mViewHeight/2 +mNumberHeadRect.height()/2,mNumberPaint);
                }else {
                    //有单位 计算单位宽高矩形
                    mNumberUnitPaint.getTextBounds(mNumberUnit,0,mNumberUnit.length(),mNumberUnitRect);
                    mContentWidth = mNumberUnitRect.width() + mNumberHeadRect.width() * 3 + mNumberMarginUnit + DpUtil.dp2px(mContext,8);
                    mNumberPaint.setAlpha(mNumberZeroAlpha);
                    canvas.drawText(mNumberHead,mViewWidth/2 - mContentWidth/2,mViewHeight/2 +mNumberHeadRect.height()/2,mNumberPaint);
                    mNumberPaint.setAlpha(255);
                    canvas.drawText(mNumberStr,mViewWidth/2 - mContentWidth/2 + mNumberHeadRect.width() +DpUtil.dp2px(mContext,8),
                            mViewHeight/2 +mNumberHeadRect.height()/2,mNumberPaint);
                    canvas.drawText(mNumberUnit,mViewWidth/2 + mContentWidth/2 - mNumberUnitRect.width() + mNumberMarginUnit,
                            mViewHeight/2 +mNumberHeadRect.height()/2,mNumberUnitPaint);
                }
                mNuberMarginY = mViewHeight/2 +mNumberHeadRect.height()/2;
            }else if(mNumber >= 100){
                Rect mNumberRect = new Rect();//数字矩形
                mNumberPaint.getTextBounds(mNumberStr,0,mNumberStr.length(),mNumberRect);
                if(TextUtils.isEmpty(mNumberUnit)){
                    canvas.drawText(mNumberStr,mViewWidth/2 - mNumberRect.width()/2,mViewHeight/2 + mNumberRect.height()/2,mNumberPaint);
                }else {
                    //有单位
                    mNumberUnitPaint.getTextBounds(mNumberUnit,0,mNumberUnit.length(),mNumberUnitRect);
                    mContentWidth = mNumberRect.width() +mNumberUnitRect.width() + mNumberMarginUnit;
                    canvas.drawText(mNumberStr,mViewWidth/2 - mContentWidth/2,mViewHeight/2 + mNumberRect.height()/2,mNumberPaint);
                    canvas.drawText(mNumberUnit,mViewWidth/2 + mContentWidth/2 - mNumberUnitRect.width(),mViewHeight/2 + mNumberRect.height()/2,mNumberUnitPaint);
                }
                mNuberMarginY = mViewHeight/2 + mNumberRect.height()/2;
            }
        }else {
            Rect mNumberRect = new Rect();//数字矩形
            mNumberStr = "000";
            mNumberPaint.getTextBounds(mNumberStr,0,mNumberStr.length(),mNumberRect);
            if(!TextUtils.isEmpty(mNumberUnit)){
                mNumberUnitPaint.getTextBounds(mNumberUnit,0,mNumberUnit.length(),mNumberUnitRect);
                //把单位也考虑进去
                float mContentWidth = mNumberRect.width() + mNumberUnitRect.width() + mNumberMarginUnit;
                float mNumberX = mViewWidth/2 - mContentWidth/2;
                mNumberPaint.setAlpha(mNumberZeroAlpha);
                canvas.drawText(mNumberStr,mNumberX,mViewHeight/2 + mNumberRect.height()/2,mNumberPaint);
                mNumberPaint.setAlpha(255);
                canvas.drawText(mNumberUnit,mNumberX + mNumberRect.width() + mNumberMarginUnit,mViewHeight/2 + mNumberRect.height()/2,mNumberUnitPaint);
            }else {
                mNumberPaint.setAlpha(mNumberZeroAlpha);
                canvas.drawText(mNumberStr,mViewWidth/2 - mNumberRect.width()/2,mViewHeight/2 + mNumberRect.height()/2,mNumberPaint);
                mNumberPaint.setAlpha(255);
            }
            mNuberMarginY = mViewHeight/2 + mNumberRect.height()/2;
        }
//        canvas.drawLine(0,mViewHeight/2,mViewWidth,mViewHeight/2,mNumberPaint);
//        canvas.drawLine(mViewWidth/2,0,mViewWidth/2,mViewHeight,mNumberPaint);
    }

    /**
     * 直接传入dp就好
     * @param mNumberTextSize
     */
    public void setmNumberTextSize(int mNumberTextSize) {
        this.mNumberTextSize = DpUtil.px2sp(mContext,DpUtil.dp2px(mContext,mNumberTextSize));
    }

    /**
     * 是否为第一个选项
     * @param mFirstSelect
     */
    public void setmFirstSelect(boolean mFirstSelect) {
        this.mFirstSelect = mFirstSelect;
    }

    /**
     * 设置数字
     * @param mNumber 数值
     */
    public void setmNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    /**
     * 设置签名的数字0的透明度
     * @param mNumberZeroAlpha 透明度
     */
    public void setmNumberZeroAlpha(int mNumberZeroAlpha) {
        if(mNumberZeroAlpha < 0){
            mNumberZeroAlpha = 0;
        }
        if(mNumberZeroAlpha >255){
            mNumberZeroAlpha = 255;
        }
        this.mNumberZeroAlpha = mNumberZeroAlpha;
    }

    /**
     * 设置数值的单位
     * @param mNumberUnit
     */
    public void setmNumberUnit(String mNumberUnit) {
        this.mNumberUnit = mNumberUnit;
    }

    /**
     * 刷新视图
     */
    public void refreshView(){
        invalidate();
    }

    /**
     * 设置顶部的状态文字
     * @param mWaterQualityStatus
     */
    public void setmWaterQualityStatus(String mWaterQualityStatus) {
        this.mWaterQualityStatus = mWaterQualityStatus;
    }
}
