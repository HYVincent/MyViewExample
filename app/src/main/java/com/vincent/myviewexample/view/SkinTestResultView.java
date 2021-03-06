package com.vincent.myviewexample.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.bean.SkinTestResultBean;
//import com.vincent.myviewexample.utils.DpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name MyViewExample
 * @page com.vincent.myviewexample.view
 * @class describe 注意 这里的所有的单位都为px，这个控件为定制化控件，不做其它屏幕的适配 这里适配屏幕为728*480
 * @date 2018/12/3 14:46
 */
public class SkinTestResultView extends View {

    public SkinTestResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private static final String TAG = "雷达比例图";
    private Context mContext;
    //圆心x
    private float mCircleX = 358;
    //圆心y
    private float mCircleY = 223;
    private float mViewWidth = 728;
    private float mViewHeight = 480;
    //半径
    private float mCircleRadius = 96;
    private int mBgColor;
    private int mStartColor;
    private int mEndColor;
    //圆内部两条垂直交叉的线的颜色
    private int mCrossLineColor;
    private Paint mCirclePaint;
    private SkinTestResultBean bean;
    private String mBottomText = "肌肤检测结果";
    private String mTopTag = "皮肤水分";
    private String mLeftTag = "油分";
    private String mBottomTag = "白皙度";
    private String mRightTag = "弹性";


    private void init(Context context) {
        this.mContext = context;
//        mViewWidth = DpUtil.dp2px(mContext,mViewWidth);
//        mViewHeight = DpUtil.dp2px(mContext,mViewHeight);

//        mCircleRadius = DpUtil.dp2px(mContext,mCircleRadius);
        mBgColor = ContextCompat.getColor(mContext, R.color.color_black_000000);
        mStartColor = ContextCompat.getColor(mContext, R.color.color_red_c8148d);
        mEndColor = ContextCompat.getColor(mContext, R.color.color_purple_7a00ab);
        mCrossLineColor = ContextCompat.getColor(mContext,R.color.color_red_d66fd9);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

//        mBootomTextMarginBottom = DpUtil.dp2px(mContext,mBootomTextMarginBottom);
//        mBottomTextSize = DpUtil.dp2px(mContext,mBottomTextSize);
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/NotoSansHans-Black.otf");
        mBottomPaint = new Paint();
        mBottomPaint.setAntiAlias(true);
        mBottomPaint.setTextSize(mBottomTextSize);
        mBottomPaint.setTypeface(tf);

        mTagTextColor = ContextCompat.getColor(mContext, R.color.color_white_ffffff);
//        mTagTextSize =  DpUtil.dp2px(mContext,mTagTextSize);
//        mTagTextMragin = DpUtil.dp2px(mContext,mTagTextMragin);
        mTagPaint = new Paint();
        mTagPaint.setColor(mTagTextColor);
        mTagPaint.setAntiAlias(true);
        mTagPaint.setTextSize(mTagTextSize);

        mValueColor = ContextCompat.getColor(mContext,R.color.color_red_e21493);
        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mValuePaint.setColor(mValueColor);
        mValuePaint.setAlpha(175);

//        mNumberTextSize = DpUtil.dp2px(mContext,mNumberTextSize);
        mNumberTextColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
        mNumberPaint = new Paint();
        mNumberPaint.setColor(mNumberTextColor);
        mNumberPaint.setTextSize(mNumberTextSize);
        mNumberPaint.setAntiAlias(true);

        mSuggestTextColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
//        mSuggestTextSize = DpUtil.dp2px(mContext,mSuggestTextSize);
//        mSuggestMarginTop = DpUtil.dp2px(mContext, mSuggestMarginTop);
        mSuggestPaint = new Paint();
        mSuggestPaint.setAntiAlias(true);
        mSuggestPaint.setColor(mSuggestTextColor);
        mSuggestPaint.setTextSize(mSuggestTextSize);

//        empUpTextSize = DpUtil.dp2px(mContext,empUpTextSize);
//        empUpMarginTop = DpUtil.dp2px(mContext,empUpMarginTop);

        mProgressBgStartColor = ContextCompat.getColor(mContext,R.color.color_purple_891247);
        mProgressBgEndColor = ContextCompat.getColor(mContext,R.color.color_purple_17131a);
        mItemProgrfessPaint = new Paint();
//        mItemProgressWidth = DpUtil.dp2px(mContext,mItemProgressWidth);
        mItemProgrfessPaint.setStrokeWidth(mItemProgressWidth);
        //笔头头部圆形
        mItemProgrfessPaint.setStrokeCap(Paint.Cap.ROUND);
        mItemProgrfessPaint.setAntiAlias(true);
        mItemProgrfessPaint.setStyle(Paint.Style.STROKE);

//        mProgressMarginRight = DpUtil.dp2px(mContext,mProgressMarginRight);
//        mDifferenceValue = DpUtil.dp2px(mContext,mDifferenceValue);
//        mTargetCircleRadius = DpUtil.dp2px(mContext,mTargetCircleRadius);
//        mExtendValue = DpUtil.dp2px(mContext,mExtendValue);
//        mProgressMargin = DpUtil.dp2px(mContext,mProgressMargin);


        mTargetCirclePaint = new Paint();
        mTargetCirclePaint.setAntiAlias(true);

        mProgressTextColor = ContextCompat.getColor(mContext,R.color.color_white_ffffff);
//        mProgressTextSize = DpUtil.dp2px(mContext,mProgressTextSize);
        mProgressTextPaint = new Paint();
        mProgressTextPaint.setTextSize(mProgressTextSize);
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setColor(mProgressTextColor);
    }

//    private List<PointF> waterPoints = new ArrayList<>();
//    private List<PointF> whitePoints = new ArrayList<>();
//    private List<PointF> oilPoints = new ArrayList<>();
//    private List<PointF> elasticityPoints = new ArrayList<>();

    private float mTempWaterRatio = 0.0f;
    private float mTempWhiteRatio = 0.0f;
    private float mTempOilRatio = 0.0f;
    private float mTempElasticityRatio = 0.0f;

    //移动之后的圆心
    private float mMoveCircleX = 358;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mBgColor);
        drawBgCircle(canvas);
        drawBottomText(canvas);
        drawTagText(canvas);
        drawValue(canvas);
        drawNumber(canvas);
        if(moveFinish){
            //平移结束，开始绘制右边的文字
            drawRightTopSuggestText(canvas);
            float mStartX = mRightTagTextX - 4;
            float mProgressWidth = (mViewWidth - mStartX - mProgressMarginRight - mProgressMargin)/2;
            float mStartY = mCircleY + 32;
//            waterPoints.clear();
            drawItemProgressCurve(canvas,mTopTag,mTempWaterRatio,waterRatio,mProgressWidth,mStartX,mStartY);

            mStartX = mStartX + mProgressWidth + mProgressMargin/2;
//            whitePoints.clear();
            drawItemProgressCurve(canvas,mBottomTag,mTempWhiteRatio,whiteRatio,mProgressWidth,mStartX,mStartY);

            mStartX = mRightTagTextX - 4;
            mStartY = mStartY + 64;
            drawItemProgressCurve(canvas,mLeftTag,mTempOilRatio,oilRatio,mProgressWidth,mStartX,mStartY);

            mStartX = mStartX + mProgressWidth + mProgressMargin/2;
            drawItemProgressCurve(canvas,mRightTag,mTempElasticityRatio,elasticityRatio,mProgressWidth,mStartX,mStartY);
        }
    }



    /**
     * 绘制背景
     * @param canvas 画布
     */
    private void drawBgCircle(Canvas canvas) {
        if(mMoveLeft){
            mMoveCircleX = mCircleX - mTempMargin;
        }else {
            mMoveCircleX = mCircleX + mTempMargin;
        }
//        mCircleY = mViewHeight/2.0f;
        LinearGradient lg=new LinearGradient(mMoveCircleX,mCircleY - 30,
                mMoveCircleX,mCircleY + 30,mStartColor,mEndColor,Shader.TileMode.CLAMP);
        mCirclePaint.setShader(lg);
        canvas.drawCircle(mMoveCircleX,mCircleY,mCircleRadius,mCirclePaint);
    }

    private float mBottomTextSize = 14;
    //底部文字距离View底部的距离
    private float mBootomTextMarginBottom = 68;
    private Paint mBottomPaint;

    private void drawBottomText(Canvas canvas) {
        Rect mBottomTextRect = new Rect();
        mBottomPaint.getTextBounds(mBottomText,0,mBottomText.length(),mBottomTextRect);
        //水平渐变
        LinearGradient lg = new LinearGradient(mViewWidth/2 - mBottomTextRect.width()/2 + mBottomTextRect.width()/3,mViewHeight - mBootomTextMarginBottom - mBottomTextRect.height(),
                mViewWidth/2 - mBottomTextRect.width()/2 + mBottomTextRect.width()*2/3,mViewHeight - mBootomTextMarginBottom - mBottomTextRect.height(),mStartColor,mEndColor,Shader.TileMode.CLAMP);
        mBottomPaint.setShader(lg);
        canvas.drawText(mBottomText,mViewWidth/2 - mBottomTextRect.width()/2,mViewHeight - mBootomTextMarginBottom - mBottomTextRect.height(),mBottomPaint);
    }


    private float mTagTextSize = 12;
    private int mTagTextColor;
    private Paint mTagPaint;
    private float mTagTextMragin = 15;
    //右边的tag文字最右端的x坐标
    private float mRightTagTextX;
    private void drawTagText(Canvas canvas) {
        mTagPaint.setColor(mTagTextColor);
        Rect mTagTextRect = new Rect();
        mTagPaint.getTextBounds(mTopTag,0,mTopTag.length(),mTagTextRect);
        canvas.drawText(mTopTag,mMoveCircleX - mTagTextRect.width()/2,mCircleY - mCircleRadius - mTagTextMragin,mTagPaint);
        mTagPaint.getTextBounds(mBottomTag,0,mBottomTag.length(),mTagTextRect);
        canvas.drawText(mBottomTag,mMoveCircleX - mTagTextRect.width()/2,mCircleY + mCircleRadius + mTagTextRect.height() + mTagTextMragin,mTagPaint);
        mTagPaint.getTextBounds(mLeftTag,0,mLeftTag.length(),mTagTextRect);
        canvas.drawText(mLeftTag,mMoveCircleX - mCircleRadius -  mTagTextRect.width() - mTagTextMragin,mCircleY + mTagTextRect.height()/2,mTagPaint);
        mTagPaint.getTextBounds(mRightTag,0,mRightTag.length(),mTagTextRect);
        mRightTagTextX  = mMoveCircleX + mCircleRadius + mTagTextMragin + mTagPaint.measureText(mRightTag);
        canvas.drawText(mRightTag,mMoveCircleX + mCircleRadius + mTagTextMragin,mCircleY + mTagTextRect.height()/2,mTagPaint);
        //绘制两条垂直交叉的线
        mTagPaint.setColor(mCrossLineColor);
        canvas.drawLine(mMoveCircleX,mCircleY - mCircleRadius,mMoveCircleX,mCircleY +mCircleRadius,mTagPaint);
        canvas.drawLine(mMoveCircleX - mCircleRadius,mCircleY,mMoveCircleX + mCircleRadius,mCircleY,mTagPaint);
    }

    private Paint mValuePaint;
    private int mValueColor;
    private long animTime = 1000;//动画持续时间
    private float mTempRatio = 0.0f;//临时比例
    private int mValue = 75;

    private float topYValue = 0;
    private float leftXValue = 0;
    private float bottomYValue = 0;
    private float rightXValue = 0;
    private Path mValuePath = new Path();
    /**
     * 开始绘制值
     * @param canvas
     */
    private void drawValue(Canvas canvas) {
        if(bean != null){
            mValuePath.reset();
            topYValue = mCircleY - mCircleRadius * waterRatio * mTempRatio ;
            leftXValue = mMoveCircleX - mCircleRadius * oilRatio * mTempRatio;
            bottomYValue = mCircleY + mCircleRadius * whiteRatio * mTempRatio;
            rightXValue = mMoveCircleX + mCircleRadius * elasticityRatio * mTempRatio;
            if(topYValue < mCircleY - mCircleRadius){
                topYValue = mCircleY - mCircleRadius;
            }
            if(leftXValue < mMoveCircleX - mCircleRadius){
                topYValue = mMoveCircleX - mCircleRadius;
            }
            if(bottomYValue > mCircleY + mCircleRadius){
                topYValue = mCircleY + mCircleRadius;
            }
            if(rightXValue > mMoveCircleX + mCircleRadius){
                topYValue = mMoveCircleX + mCircleRadius;
            }
            mValuePath.moveTo(mMoveCircleX,topYValue);
            mValuePath.lineTo(mMoveCircleX,topYValue);
            mValuePath.lineTo(leftXValue,mCircleY);
            mValuePath.lineTo(mMoveCircleX,bottomYValue);
            mValuePath.lineTo(rightXValue,mCircleY);
            mValuePath.lineTo(mMoveCircleX,topYValue);
            mValuePath.close();
            canvas.drawPath(mValuePath,mValuePaint);
//            Log.d(TAG, "半径:"+mCircleRadius+" waterRatio:"+waterRatio + " oilRatio:"+oilRatio
//                    + " whiteRatio:"+whiteRatio+ " elasticityRatio:"+elasticityRatio+" mTempRatio:"
//                    +mTempRatio+" topYValue-->"+topYValue);
        }
    }

    private Paint mNumberPaint;
    private float mNumberTextSize = 38;
    private int mTempNumber = 0;
    private int mNumberTextColor;

    /**
     * 绘制值
     * @param canvas
     */
    private void drawNumber(Canvas canvas) {
        Rect mNumberRect = new Rect();
        String mValueStr = String.valueOf(mTempNumber);
        mNumberPaint.getTextBounds(mValueStr,0,mValueStr.length(),mNumberRect);
        canvas.drawText(mValueStr,mMoveCircleX - mNumberPaint.measureText(mValueStr)/2,mCircleY + mNumberRect.height()/2,mNumberPaint);
    }



    private ObjectAnimator addAnimator;
    private float oilRatio;//油分的比例
    private float waterRatio;//水分的比例
    private float elasticityRatio;//弹性比例
    private float whiteRatio;//白皙度比例
    public void setBean(SkinTestResultBean bean) {
        this.bean = bean;
        oilRatio = bean.getOil() / 100.0f;
        waterRatio = bean.getWater() / 100.0f;
        elasticityRatio = bean.getElasticity()/100.0f;
        whiteRatio = bean.getWhite() / 100.0f;
        // mCurrentCircleRadius 表示为插值器在使用的值
        addAnimator = ObjectAnimator.ofFloat(this, "mTempRatio", 0.0f, 1.0f);
        addAnimator.setDuration(animTime);
        addAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTempRatio = (float) animation.getAnimatedValue();
                mTempNumber = (int) (mValue * mTempRatio);
                invalidate();
            }
        });
        addAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startMove();
            }
        });
        mValue = 75;
        startAnimDraw();
        addAnimator.start();
    }

    public void setMTempRatio(float mTempRatio) {
        this.mTempRatio = mTempRatio;
    }
    // true 向左平移 false 向右平移
    private boolean mMoveLeft = true;
    //平移的目标距离
    private float moveTarget;
    //平移的临时距离
    private float mTempMargin = 0;
    private ObjectAnimator moveAnim;
    private boolean moveFinish = false;

    private void startMove(){
        moveTarget = 211;//固定值
        moveAnim = ObjectAnimator.ofFloat(this, "moveTarget", 0.0f, moveTarget);
        moveAnim.setDuration(animTime);
        moveAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTempMargin = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        moveAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                moveFinish = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moveFinish = true;
                //开始绘制
                progressAnim.start();
            }
        });
        moveAnim.start();
    }

    private String suggestText = "油分测试值高于正常值，建议使用Clean功能";
    private Paint mSuggestPaint;
    private int mSuggestTextColor;
    private float mSuggestTextSize = 16;
    private float mSuggestMarginTop = 115;
    private Rect mSuggestTextRect = new Rect();
    private String empUp = "EMP UP";
    private float empUpTextSize = 30;
    // emp up 文字距离顶部的建议的间距
    private float empUpMarginTop = 29;
    private float temSuggestTextHeight;
    private float temSuggestTextWidth;

    /**
     * 绘制有上部分的结果建议文字
     * @param canvas
     */
    private void drawRightTopSuggestText(Canvas canvas) {
        if(!TextUtils.isEmpty(suggestText)){
            mSuggestPaint.setTextSize(mSuggestTextSize);
            mSuggestPaint.getTextBounds(suggestText,0,suggestText.length(),mSuggestTextRect);
            temSuggestTextHeight = mSuggestTextRect.height();
            temSuggestTextWidth = mSuggestTextRect.width();
            canvas.drawText(suggestText,mViewWidth /2, mSuggestMarginTop + mSuggestTextRect.height(),mSuggestPaint);
        }
        mSuggestPaint.setTextSize(empUpTextSize);
        mSuggestPaint.getTextBounds(empUp,0,empUp.length(),mSuggestTextRect);
        canvas.drawText(empUp,mViewWidth /2 + temSuggestTextWidth/2 - mSuggestPaint.measureText(empUp)/2,
                mSuggestMarginTop + temSuggestTextHeight * 2 + empUpMarginTop,mSuggestPaint);
    }
    //绘制贝塞尔曲线进度条
    private Paint mItemProgrfessPaint;
    private float mItemProgressWidth = 2;
    //进度背景开始的颜色值
    private int mProgressBgStartColor;
    //进度背景结束颜色值
    private int mProgressBgEndColor;
    //目标圆画笔
    private Paint mTargetCirclePaint;
    private float mProgressTextSize = 10;
    private int mProgressTextColor;
    //目标圆的半径
    private float mTargetCircleRadius = 10;
    //贝塞尔曲线进度的最大值和最小值的差值
    private float mDifferenceValue = 30;
    //贝塞尔曲线的控制点的延伸值
    private float mExtendValue = 30;
    //进度距离最右边的距离
    private float mProgressMarginRight = 30;
    //左右两条之间的距离
    private float mProgressMargin = 10;
    private Path mBgProgressPath = new Path();
    //临时的进度小圆的圆心x坐标
//    private float mTempProgressCircleX = 0.0f;
    //临时的进度小圆的圆心y坐标
//    private float mTempProgressCircleY = 0.0f;
    private Rect mProgressTextRect = new Rect();
    private Paint mProgressTextPaint;

    /**
     * 绘制各项三阶贝塞尔进度曲线
     * @param canvas 画布
     * @param tagText 目标文字
     * @param ratio 临时比例值
     * @param targetRatio 实际比例值
     * @param mProgressWidth 进度在View上的实际宽度
     * @param mStartX 起点x坐标
     * @param mStartY 起点y坐标
     */
    private void drawItemProgressCurve(Canvas canvas,String tagText,float ratio,
                                       float targetRatio,float mProgressWidth,float mStartX,float mStartY) {
        float mTempProgressCircleX = 0.0f;
        float mTempProgressCircleY = 0.0f;
        float mEndX = mStartX + mProgressWidth;
        float mEndY = mStartY + 10;
        mBgProgressPath.reset();
        mBgProgressPath.moveTo(mStartX,mStartY);
        float mControl1X = mStartX + mProgressWidth * 0.25f;
        float mControl1Y = mStartY + mDifferenceValue + mExtendValue;
        float mConrtol2X = mStartX + mProgressWidth * 0.75f;
        float mConrtol2Y = mStartY - mExtendValue;
        mBgProgressPath.cubicTo(mControl1X,mControl1Y,mConrtol2X,mConrtol2Y,mEndX,mEndY);
        LinearGradient lg=new LinearGradient(mStartX + mProgressWidth * 0.2f,mStartY, mEndX - mProgressWidth * 0.2f,
                mEndY,mProgressBgStartColor,mProgressBgEndColor,Shader.TileMode.CLAMP);
        mItemProgrfessPaint.setShader(lg);
        canvas.drawPath(mBgProgressPath,mItemProgrfessPaint);
        //计算真实比例
        PathMeasure pathMeasure = new PathMeasure(mBgProgressPath,false);
        float length = pathMeasure.getLength();
        float realLength = length * targetRatio;
        float[] pos = new float[2];
        float[] tan = new float[2];
        pathMeasure.getPosTan(realLength * ratio,pos,tan);
        mTempProgressCircleX = pos[0];
        mTempProgressCircleY = pos[1];
        RadialGradient lgCircle = new RadialGradient(mTempProgressCircleX,mTempProgressCircleY,mTargetCircleRadius*3/4,mEndColor,mStartColor,Shader.TileMode.CLAMP);
        mTargetCirclePaint.setShader(lgCircle);
        mTargetCirclePaint.setStyle(Paint.Style.FILL);
        mTargetCirclePaint.setShadowLayer(5f, 5F, 1f, mStartColor);
        canvas.drawCircle(mTempProgressCircleX,mTempProgressCircleY,mTargetCircleRadius,mTargetCirclePaint);
        mTargetCirclePaint.setStyle(Paint.Style.STROKE);
        mTargetCirclePaint.setStrokeWidth(1);
        canvas.drawCircle(mTempProgressCircleX,mTempProgressCircleY,mTargetCircleRadius + 5,mTargetCirclePaint);
        String ratioStr = String.valueOf((int)(ratio * 100));
        mProgressTextPaint.getTextBounds(tagText,0,tagText.length(),mProgressTextRect);
        //这里的10 表示间距
        float textWidth = mProgressTextPaint.measureText(tagText) + mProgressTextPaint.measureText(ratioStr) + 20;
        canvas.drawText(ratioStr,(mConrtol2X + mStartX)/2 + textWidth/2 - mProgressTextPaint.measureText(ratioStr),mStartY - mProgressTextRect.height(),mProgressTextPaint);
        canvas.drawText(tagText,(mConrtol2X + mStartX)/2 - textWidth/2,mStartY - mProgressTextRect.height(),mProgressTextPaint);
    }

    //临时进度 动画值
    private float mTempProgress;
    private ObjectAnimator progressAnim;
    private void setMTempProgress(float mTempProgress){
        this.mTempProgress = mTempProgress;
    }

    private void startAnimDraw(){
// mCurrentCircleRadius 表示为插值器在使用的值
        progressAnim = ObjectAnimator.ofFloat(this, "mTempProgress", 0, 1.0f);
        progressAnim.setDuration(800);
        progressAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTempProgress = (float) animation.getAnimatedValue();
                mTempWaterRatio = waterRatio * mTempProgress;
                mTempWhiteRatio = whiteRatio * mTempProgress;
                mTempOilRatio = oilRatio * mTempProgress;
                mTempElasticityRatio = elasticityRatio * mTempProgress;
//                Log.e(TAG, "startAnimDraw -> onAnimationUpdate: " + mTempProgress );
                invalidate();
            }
        });
    }




    public void setMoveTarget(float mTagTextMragin) {
        this.moveTarget = mTagTextMragin;
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
