package com.vincent.myviewexample.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.vincent.myviewexample.DpUtil;
import com.vincent.myviewexample.R;
import com.vincent.myviewexample.bean.SkinTestResultBean;

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
public class SkinTestResultContrastView extends View {

    public SkinTestResultContrastView(Context context) {
        super(context);
        init(context);
    }

    public SkinTestResultContrastView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    //数据源 里面的数据有3个 上一次的测试数据 这一次的测试数据 一个月后的预测数据
    private List<SkinTestResultBean> datas = new ArrayList<>();

    public void setDatas(List<SkinTestResultBean> datas) {
        this.datas = datas;
        mMoveAnim = ObjectAnimator.ofFloat(this, "mTempMoveRatio", 0, 1.0f);
        mMoveAnim.setDuration(800);
        mMoveAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTempMoveRatio = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mMoveAnim.start();
    }

    public void setMTempMoveRatio(float mTempMoveLength) {
        this.mTempMoveRatio = mTempMoveLength;
    }

    private static final String TAG = "检测结果对比分析图";
    private Context mContext;
    //渐变色 开始和结束颜色
    private int mStartColor, mEndColor;
    //初始化的时候 圆心位置 ，也就是中间圆心O(x,y)坐标
    private float mRootCircleX = 363;
    private float mRootCircleY = 200;
    //mCircleRadius 圆的半径
    private float mCircleRadius = 60;
    //平移的时候目标值，这个值需要动态计算
//    private float mMoveTarget = 222;
    //平移的时候临时移动的比例
    private float mTempMoveRatio;
    //当前View的宽高
    private float mViewWidth, mViewHeight;
    //View的背景颜色
    private int bgColor;

    private void init(Context context) {
        this.mContext = context;
        mStartColor = ContextCompat.getColor(mContext, R.color.color_red_c8148d);
        mEndColor = ContextCompat.getColor(mContext, R.color.color_purple_7a00ab);
        bgColor = ContextCompat.getColor(mContext, R.color.color_black_000000);

        mBottomTextSize = DpUtil.dp2px(mContext, mBottomTextSize);
        mBottomTextPaint = new Paint();
        mBottomTextPaint.setAntiAlias(true);
        mBottomTextPaint.setTextSize(mBottomTextSize);
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/NotoSansHans-Black.otf");
        mBottomTextPaint.setTypeface(tf);

        mBgCirclePaint = new Paint();
        mBgCirclePaint.setAntiAlias(true);
        mBgCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mTagTextSize = DpUtil.dp2px(mContext, mTagTextSize);
        mTagTextMargin = DpUtil.dp2px(mContext, mTagTextMargin);
        mTagTextColor = ContextCompat.getColor(mContext, R.color.color_white_ffffff);
        mTagPaint = new Paint();
        mTagPaint.setTextSize(mTagTextSize);
        mTagPaint.setAntiAlias(true);
        mTagPaint.setColor(mTagTextColor);

        mUserTagTextSize = DpUtil.dp2px(mContext, mUserTagTextSize);
        mUseTagPaint = new Paint();
        mUseTagPaint.setTextSize(mUserTagTextSize);
        mUseTagPaint.setTypeface(tf);

        mValueColor = ContextCompat.getColor(mContext, R.color.color_red_e21493);
        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setColor(mValueColor);
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mValuePaint.setAlpha(175);

        mNumberTextColor = ContextCompat.getColor(mContext, R.color.color_white_ffffff);
        mNumberPaint = new Paint();
        mNumberPaint.setColor(mNumberTextColor);
        mNumberPaint.setTextSize(mNumberTextSize);
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setTypeface(tf);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(bgColor);
        drawBottomText(canvas);
        for (int i = 0; i < datas.size(); i++) {
            drawItemBeanView(canvas, i);
        }
    }


    //当前对象的比例
    private float mOilRatio;//油分的比例
    private float mWaterRatio;//水分的比例
    private float mElasticityRatio;//弹性比例
    private float mWhiteRatio;//白皙度比例
    private Paint mBgCirclePaint;
    private ObjectAnimator mMoveAnim;

    private String mTopTag = "皮肤水分";
    private String mLeftTag = "油分";
    private String mBottomTag = "白皙度";
    private String mRightTag = "弹性";
    private Paint mTagPaint;
    private int mTagTextColor;
    private float mTagTextSize = 10;
    private Rect mTagTextRect = new Rect();
    private float mTagTextMargin = 10;

    private Paint mUseTagPaint;
    private float mUserTagTextSize = 15;
    private Rect mUseTagRect = new Rect();
    //使用提示文字距离圆底部的距离 这里直接用px
    private float mUseTagMarginCircleY = 63;

    private Paint mValuePaint;
    private int mValueColor;
    private Path mPath = new Path();


    private Paint mNumberPaint;
    private int mNumberTextColor;
    private int mNumberTextSize = 30;
    private Rect mValueTextRect = new Rect();


    /**
     * 根据item来绘制对象
     *
     * @param canvas 画布
     * @param index  对象的下标
     */
    private void drawItemBeanView(Canvas canvas, int index) {
        SkinTestResultBean bean = datas.get(index);
        //TODO 注意 这个值需要计算一下 使其看起来更合理
        int mValue = 78;
        String mValueStr = String.valueOf(mValue);
        mOilRatio = bean.getOil() / 100.0f;
        mWaterRatio = bean.getWater() / 100.0f;
        mElasticityRatio = bean.getElasticity() / 100.0f;
        mWhiteRatio = bean.getWhite() / 100.0f;
        float mCircleX = mRootCircleX;//表示实时的
        String useTag = "";
        if (index == 0) {
            //当前数据  保持不动
            mCircleX = mRootCircleX;
            useTag = "使用后";
        }
        if (index == 1) {
            //上一次的数据 向左平移
            mCircleX = mCircleX - 222 * mTempMoveRatio;
            useTag = "使用前";
        }
        if (index == 2) {
            //一个月以后的 向右平移
            mCircleX = mCircleX + 217 * mTempMoveRatio;
            useTag = "一个月后";
        }
        LinearGradient linearGradient = new LinearGradient(mCircleX, mRootCircleY - mCircleRadius * 0.15f,
                mCircleX, mRootCircleY + mCircleRadius * 0.15f, mStartColor, mEndColor, Shader.TileMode.CLAMP);
        mBgCirclePaint.setShader(linearGradient);
        //先绘制背景圆
        canvas.drawCircle(mCircleX, mRootCircleY, mCircleRadius, mBgCirclePaint);
        mTagPaint.getTextBounds(mTopTag, 0, mTopTag.length(), mTagTextRect);
        //绘制四周的tag
        canvas.drawText(mTopTag, mCircleX - mTagTextRect.width() / 2, mRootCircleY - mCircleRadius - mTagTextMargin, mTagPaint);
        mTagPaint.getTextBounds(mBottomTag, 0, mBottomTag.length(), mTagTextRect);
        canvas.drawText(mBottomTag, mCircleX - mTagTextRect.width() / 2, mRootCircleY + mCircleRadius + mTagTextMargin + mTagTextRect.height(), mTagPaint);
        mTagPaint.getTextBounds(mLeftTag, 0, mLeftTag.length(), mTagTextRect);
        canvas.drawText(mLeftTag, mCircleX - mCircleRadius - mTagTextRect.width() - mTagTextMargin, mRootCircleY + mTagTextRect.height() / 2, mTagPaint);
        mTagPaint.getTextBounds(mRightTag, 0, mRightTag.length(), mTagTextRect);
        canvas.drawText(mRightTag, mCircleX + mCircleRadius + mTagTextMargin, mRootCircleY + mTagTextRect.height() / 2, mTagPaint);
        //绘制圆内部垂直的线
        canvas.drawLine(mCircleX, mRootCircleY - mCircleRadius, mCircleX, mRootCircleY + mCircleRadius, mTagPaint);
        canvas.drawLine(mCircleX - mCircleRadius, mRootCircleY, mCircleX + mCircleRadius, mRootCircleY, mTagPaint);
        //绘制数值不规则图形范围
        mPath.reset();
        mPath.moveTo(mCircleX, mRootCircleY - mCircleRadius * mWaterRatio);
        mPath.lineTo(mCircleX, mRootCircleY - mCircleRadius * mWaterRatio);
        mPath.lineTo(mCircleX - mCircleRadius * mOilRatio, mRootCircleY);
        mPath.lineTo(mCircleX, mRootCircleY + mCircleRadius * mWhiteRatio);
        mPath.lineTo(mCircleX + mCircleRadius * mElasticityRatio, mRootCircleY);
        mPath.lineTo(mCircleX, mRootCircleY - mCircleRadius * mWaterRatio);
        mPath.close();
        canvas.drawPath(mPath, mValuePaint);
        //绘制数值
        mNumberPaint.getTextBounds(mValueStr, 0, mValueStr.length(), mValueTextRect);
        canvas.drawText(mValueStr,mCircleX - mValueTextRect.width()/2,mRootCircleY + mValueTextRect.height()/2,mNumberPaint);
        mUseTagPaint.getTextBounds(useTag, 0, useTag.length(), mUseTagRect);
        LinearGradient mUseTagLinearGradient = new LinearGradient(mCircleX - mUseTagRect.width() / 2 * 0.05f, mRootCircleY + mCircleRadius + mUseTagMarginCircleY,
                mCircleX + mUseTagRect.width() / 2 * 0.05f, mRootCircleY + mCircleRadius + mUseTagMarginCircleY, mStartColor, mEndColor, Shader.TileMode.CLAMP);
        mUseTagPaint.setShader(mUseTagLinearGradient);
        canvas.drawText(useTag, mCircleX - mUseTagRect.width() / 2, mRootCircleY + mCircleRadius + mUseTagMarginCircleY + mUseTagRect.height(), mUseTagPaint);

    }

    private String mBottomText;
    private float mBottomTextSize = 15;
    private Paint mBottomTextPaint;
    private Rect mBottomTextRect = new Rect();
    private LinearGradient mBottomTextLinearGradient;
    private float mBottomTextMarginBottom = 34;

    /**
     * 设置底部的文字
     *
     * @param mBottomText
     */
    public void setmBottomText(String mBottomText) {
        this.mBottomText = mBottomText;
        mBottomTextPaint.getTextBounds(mBottomText, 0, mBottomText.length(), mBottomTextRect);
    }

    /**
     * 先绘制底部的文字
     *
     * @param canvas 画布
     */
    private void drawBottomText(Canvas canvas) {
        if (!TextUtils.isEmpty(mBottomText)) {
            mBottomTextLinearGradient = new LinearGradient(mViewWidth / 2 - mBottomTextRect.width() / 2 * 0.5f, mViewHeight - mBottomTextMarginBottom - mBottomTextRect.height() / 2,
                    mViewWidth / 2 + mBottomTextRect.width() / 2 * 0.5f, mViewHeight - mBottomTextMarginBottom - mBottomTextRect.height() / 2, mStartColor, mEndColor, Shader.TileMode.CLAMP);
            mBottomTextPaint.setShader(mBottomTextLinearGradient);
            canvas.drawText(mBottomText, mViewWidth / 2 - mBottomTextRect.width() / 2, mViewHeight - mBottomTextMarginBottom - mBottomTextRect.height() / 2, mBottomTextPaint);
        }
    }
}
