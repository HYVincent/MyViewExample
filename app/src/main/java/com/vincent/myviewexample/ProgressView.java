package com.vincent.myviewexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.vincent.myviewexample.bean.ProgressBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample
 * @class describe 进度视图
 * @date 2018/11/21 17:11
 */
public class ProgressView extends View {

    public ProgressView(Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private static final String TAG = "进度条View";
    private Context mContext;
    private int mViewHeight;
    private int mViewWidth;
    private int mFinishColor;//已完成状态的颜色
    private int mFinishOuterCircleColor;//已完成 外层圆环的颜色
    private int mUnfinishColor;//未完成的颜色
    private int mNumberItemColor;//item数字的颜色
    private int mImaginaryColor;//虚线的颜色

    private float mStartMarginTop = 10;//顶部的间距
    private float mEndMarginBottom = 10;//底部的间距
    private float mItemMarginLong = 30;//两个iten之间的长度
    private float mCircleMarginLeft = 15;//圆距离View左边的间距

    private float mFirstCircleY;//第一个圆的圆心Y坐标
    private float mCircleRadius = 15;//圆的半径
    private float mStartX;//起点x,表示圆心，也表示虚线的x坐标
    private float mProgressLong;//不算上下边距剩下的试图高度
    private float mProgressHeight;//进度的高度
    private Rect rect;
    private float mNumberTextSize = 13;//数字item大小

    private List<ProgressBean> data = new ArrayList<>();//数据

    private Paint mPaint;
    private float mCurrentCircleRadius = 0;//变化中的圆半径
    private ObjectAnimator animator;
    private int mFinishDescTextColor;//描述文字颜色值
    private int mUnfinishDescTextColor;//描述文字颜色值
    private float mDescTextSize = 12;//描述文字大小
    private float mDescTextMarginRight = 5;//描述文字距离View右边的间距
    private float mDescTextMarginLeft = 15;//描述文字距离View左边的间距
    private float mDescTextMarginTop = 5;//描述文本与描述文本上下行之间的间距


    @Override
    protected void onDraw(Canvas canvas) {
        drawContent(canvas);
    }

    /**
     * 绘制虚线
     * @param mPaint 画笔
     * @param canvas 画布
     * @param interval 间隔
     * @param startX 七点x
     * @param startY 起点y
     * @param stopX 终点x
     * @param stopY 终点y
     */
    private void drawImaginary(Paint mPaint,Canvas canvas,int interval,float startX,float startY,float stopX,float stopY){
        mPaint.setPathEffect(new DashPathEffect(new float[]{interval, interval}, 0));//绘制虚线
        canvas.drawLine(startX,startY, stopX,stopY,mPaint);
        mPaint.setPathEffect(null);
    }

    private void drawContent(Canvas canvas) {
        //起点位置 = 左边距 + 圆的半径
        mStartX = mCircleMarginLeft + mCircleRadius;
        mProgressLong = mViewHeight - mStartMarginTop - mEndMarginBottom;
        //首先绘制头部的虚线
        if(data != null && data.size() > 0){
            mProgressHeight = mItemMarginLong *  (data.size() * 1.0f + 2/3.0f);
            if(mProgressHeight < mProgressLong){
//                Log.d(TAG, "不足填充整个屏幕: mProgressHeight -->"+ mProgressHeight + " mItemMarginLong-->"+ mItemMarginLong);
                mItemMarginLong = mProgressLong / ((data.size() -1) * 1.0f + 2/3f);
//                Log.d(TAG, "不足填充整个屏幕: mProgressHeight -->"+ mProgressHeight + " mItemMarginLong-->"+ mItemMarginLong);
            }
            //第一个圆圆心Y
            mFirstCircleY = mStartMarginTop + mItemMarginLong/3;
            mPaint.setColor(mImaginaryColor);
            //前后两端的半截取整个item长度的三分之一
            mPaint.setStrokeWidth(DpUtil.dp2px(mContext,1));
            drawImaginary(mPaint,canvas,5,mStartX,mStartMarginTop,mStartX,mFirstCircleY - mCircleRadius);
//            Log.d(TAG, "drawContent: 绘制完了头部虚线........"+data.size());
            for (int i = 0;i<data.size();i++){
                float mItemCircleY;
                if(i == 0){
                    mItemCircleY = mFirstCircleY;
                }else {
                    mItemCircleY = mFirstCircleY + mItemMarginLong * i;
                }
                ProgressBean itemBean = data.get(i);
                int status = itemBean.getStatus();
                if(status == 0){
                    //未作
//                    Log.d(TAG, "drawContent: 未作......");
                    mPaint.setStrokeWidth(DpUtil.dp2px(mContext,2));
                    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mPaint.setColor(mUnfinishColor);
                    canvas.drawCircle(mStartX,mItemCircleY,mCircleRadius,mPaint);
                }else if(status == 1){
                    //正在做
//                    Log.d(TAG, "drawContent: 正在做.........");
                    mPaint.setColor(mFinishColor);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setStrokeWidth(DpUtil.dp2px(mContext,2));
                    canvas.drawCircle(mStartX,mItemCircleY,mCurrentCircleRadius,mPaint);
                }else {
//                    Log.d(TAG, "drawContent: 绘制完成状态。。"+mItemCircleY);
                    //完成状态
                    //画圆边框
                    mPaint.setStrokeWidth(DpUtil.dp2px(mContext,2));
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setColor(mFinishOuterCircleColor);
                    canvas.drawCircle(mStartX,mItemCircleY,mCircleRadius,mPaint);
                    mPaint.setStrokeWidth(DpUtil.dp2px(mContext,1));
                    mPaint.setStyle(Paint.Style.FILL);
                    mPaint.setColor(mFinishColor);
                    canvas.drawCircle(mStartX,mItemCircleY,mCircleRadius,mPaint);
                }
                if(status != 1){
                    //绘制圆内部的文字
                    mPaint.reset();
                    String mNumberStr = String.valueOf(i+1);
                    mPaint.setTextSize(mNumberTextSize);
                    mPaint.setColor(mNumberItemColor);
                    mPaint.getTextBounds(mNumberStr,0,mNumberStr.length(),rect);
                    canvas.drawText(mNumberStr,mStartX - DpUtil.dp2px(mContext,3),mItemCircleY + rect.height()/2,mPaint);
                }
                if(i == data.size()-1){
//                    Log.d(TAG, "drawContent: 绘制末尾虚线...");
                    //绘制末尾虚线部分
                    mPaint.setStrokeWidth(DpUtil.dp2px(mContext,1));
                    mPaint.setColor(mImaginaryColor);
                    drawImaginary(mPaint,canvas,5,mStartX,mItemCircleY + mCircleRadius,
                            mStartX,mItemCircleY + mItemMarginLong /3);
                }
                //绘制虚线
                mPaint.setStrokeWidth(DpUtil.dp2px(mContext,1));
                mPaint.setColor(mImaginaryColor);
                if(i > 0){
//                    Log.d(TAG, "drawContent: 绘制虚线 "+i);
                    drawImaginary(mPaint,canvas,5,mStartX,mItemCircleY - mItemMarginLong + mCircleRadius,
                            mStartX,mItemCircleY - mCircleRadius);
                }
                drawDescText(itemBean,canvas,mPaint,mStartX +mCircleRadius + mDescTextMarginLeft,mItemCircleY);
            }
        }
    }

    /**
     * 最终需要绘制的文本
     */
    private List<String> needDrawTextList = new ArrayList<>();
    private Rect mItemDescRect = new Rect();

    /**
     * 绘制左边的文字
     * @param progressBean 对象
     * @param canvas 画布
     * @param mPaint 画笔
     * @param x 文本x坐标
     * @param y 文本y坐标
     */
    private void drawDescText(ProgressBean progressBean,Canvas canvas,Paint mPaint,float x,float y){
        mPaint.reset();
        if(progressBean.getStatus() != 2){
            //未完成
            mPaint.setColor(mUnfinishDescTextColor);
        }else {
            //已完成
            mPaint.setColor(mFinishDescTextColor);
        }
        mPaint.setTextSize(mDescTextSize);
        mPaint.setStrokeWidth(DpUtil.dp2px(mContext,1));
        String descStr = progressBean.getDesc();
        String[] itemStrArray = descStr.split("\\n");
        //文本最大宽度
        needDrawTextList.clear();
        float textMaxWidth = mViewWidth - mStartX - mCircleRadius - mDescTextMarginLeft - mDescTextMarginRight;
        for (int i = 0;i<itemStrArray.length;i++){
            //测量获取当前item文本宽度
            mPaint.getTextBounds(itemStrArray[i],0,itemStrArray[i].length(),mItemDescRect);
            float itemTextWidth = mItemDescRect.width();
            if(itemTextWidth > textMaxWidth){
                //超过View 拆分String
//                Log.d(TAG, "drawDescText: 超过屏幕...");
                needDrawTextList.addAll(split(textMaxWidth,mPaint,mItemDescRect,itemStrArray[i]));
            }else {
                needDrawTextList.add(itemStrArray[i]);
            }
        }
//        Log.d(TAG, "drawDescText: 绘制所有的文本->"+JSONArray.toJSONString(needDrawTextList));
        //所有文字+间隔的高度
        float allTextHeight = mItemDescRect.height() * needDrawTextList.size() + mDescTextMarginTop * (needDrawTextList.size() - 1);
        for (int i = 0;i<needDrawTextList.size();i++){
            canvas.drawText(needDrawTextList.get(i),x,y - allTextHeight / 2 + mItemDescRect.height() * (i+1) + mDescTextMarginTop * i,mPaint);
        }
    }

    /**
     * 拆分字符 适配屏幕
     * @param textMaxWidth
     * @param mPaint
     * @param text1Rect
     * @param text1
     * @return
     */
  private List<String> split(float textMaxWidth,Paint mPaint,Rect text1Rect,String text1) {
        List<String> itemString = new ArrayList<>();
        //文字最大宽度
        char[] textCharArray = text1.toCharArray();
        int lineCount = 0;
        float drawWidth = 0;//已经测量过了的文本宽度
        float charWidth;//单个字符宽度

        int mLineStartIndex = -1;//记录当前行的开始下标记
        int mLineEndIndex = -1;//记录当前结束的行i

        for (int i = 0;i<textCharArray.length;i++){
            charWidth = mPaint.measureText(textCharArray,i,1);
            //处理换行符
            if(textCharArray[i] == '\n') {
                lineCount++;
                drawWidth = 0;
                continue;
            }
            if(mLineStartIndex == -1){
                mLineStartIndex = i;
            }
            if(textMaxWidth - drawWidth < charWidth){//表示当前行已经放不下一个字符了
                lineCount ++ ;
                drawWidth = 0;
                mLineEndIndex = i -1;
                //注意 这里需要判断这是否是在完整的单词后面结束，完整的单词后面结束的话会有空白符 用空白来判断当前是否为一个完整的英文单词
                char itemChar = textCharArray[mLineEndIndex];
//                Log.d(TAG, "split: 当前行第一个字符-->"+String.valueOf(textCharArray[mLineStartIndex]+" "+mLineStartIndex)
//                        +"   当前行最后一个字符-->"+String.valueOf(textCharArray[mLineEndIndex])+" "+mLineEndIndex);
                if(TextUtils.isEmpty(String.valueOf(itemChar))){
                    itemString.add(text1.substring(mLineStartIndex,mLineEndIndex));
                    mLineStartIndex = -1;
                }else {
//                    Log.d(TAG, "split: 当前行最后一个字符不为空..."+String.valueOf(mLineEndIndex));
                    //不为空  向前一直寻找空白
                    for (int j = i-2;j > 0;j --){
//                        Log.d(TAG, "split: 正在寻找上一个为空的字符..当前index-->"+j+" 当前字符-->"+String.valueOf(textCharArray[j]));
                        if(TextUtils.equals(String.valueOf(textCharArray[j])," ")){
//                            Log.d(TAG, "split: 找到为空的字符！！！当前index-->"+j+" 当前字符-->"+String.valueOf(textCharArray[j]));
                            mLineEndIndex = j;
                            itemString.add(text1.substring(mLineStartIndex,mLineEndIndex));
                            mLineStartIndex = -1;
                            break;
                        }
                    }
                }
            }
            drawWidth += charWidth;
        }
        //判断itemString的最后一次添加是否为String的结束位置，如果不是就把末尾的加上
        if(mLineEndIndex != textCharArray.length -1){
            itemString.add(text1.substring(mLineEndIndex,textCharArray.length));
        }
      return itemString;
    }


    /**
     * 初始化属性
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        //初始化颜色值
        mFinishColor = ContextCompat.getColor(mContext,R.color.color_green_a8edc5);
        mUnfinishColor = ContextCompat.getColor(mContext,R.color.color_gray_cccbcb);
        mNumberItemColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mImaginaryColor =  ContextCompat.getColor(mContext,R.color.color_gray_cccbcb);
        mFinishOuterCircleColor = ContextCompat.getColor(mContext,R.color.color_gray_5d5f5e);
        mFinishDescTextColor = ContextCompat.getColor(mContext,R.color.color_black_545454);
        mUnfinishDescTextColor = ContextCompat.getColor(mContext,R.color.color_gray_cccbcb);

        //dp与px转换
        mStartMarginTop = DpUtil.dp2px(mContext,mStartMarginTop);
        mEndMarginBottom = DpUtil.dp2px(mContext, mEndMarginBottom);
        mCircleRadius = DpUtil.dp2px(mContext,mCircleRadius);
        mItemMarginLong = DpUtil.dp2px(mContext,mItemMarginLong);
        mCircleMarginLeft = DpUtil.dp2px(mContext, mCircleMarginLeft);
        mNumberTextSize = DpUtil.dp2px(mContext,mNumberTextSize);
        mDescTextSize = DpUtil.dp2px(mContext,mDescTextSize);
        mDescTextMarginRight = DpUtil.dp2px(mContext,mDescTextMarginRight);
        mDescTextMarginLeft = DpUtil.dp2px(mContext,mDescTextMarginLeft);
        mDescTextMarginTop = DpUtil.dp2px(mContext,mDescTextMarginTop);

        //初始化画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(DpUtil.dp2px(mContext,2));
        mPaint.setAntiAlias(true);

        rect = new Rect();
    }

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
//                Log.d(TAG, "onAnimationUpdate: "+mCurrentCircleRadius +"   "+mCircleRadius);
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


    /**
     * 刷新数据
     * @param data
     */
    public void setData(List<ProgressBean> data) {
        this.data.clear();
        this.data.addAll(data);
        startDrawable();
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
