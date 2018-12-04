package com.vincent.myviewexample.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vincent.myviewexample.R;
import com.vincent.myviewexample.utils.DpUtil;


/**
 * @author Vincent Vincent
 * @version v1.0
 * @name EasyApp
 * @page com.vincent.easyapp.view
 * @class describe
 * @date 2018/9/10 9:45
 */
public class DynamicWaveMenu extends View {

    private static final String TAG = "波纹";
    // 波纹颜色
    private static int WAVE_PAINT_COLOR /*= 0x880000aa*/;
    // y = Asin(wx+b)+h 这里的值越大  波纹上下间隔越大
    private static final float STRETCH_FACTOR_A = 40;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 5;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 15;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset = 560;//可呈现移动效果;
    private int mXTwoOffset = 1260;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    private Context mContext;
    //距离View顶部的距离
    private float marginTop = 10;
    //动态设置波纹高度占View视图高度的比例
    private float ratio = 0.90f;

    private Paint mLinePaint;
    //背景比例线，true 绘制 false 不绘制
    private boolean drawLine = true;//绘制左边的虚线
    private Bitmap mBitmap;

    private Paint mPaint;

    // true 运行  false 暂停
    private boolean running = false;
    private float mBottomBitmapWidth = 22f;
    private float mBottomBitmapHeight = 18f;
    private OnImgMenuClickListener imgMenuClickListener;
    /**
     * 设置比例
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        this.ratio = ratio;
        if (ratio > 1) {
            ratio = 0.9f;
        }
        if (ratio < 0.25) {
            ratio = 0.25f;
        }
        postInvalidate();
    }

    /**
     * 呈现可移动效果
     */
    public void initXOneOffset() {
        this.mXOneOffset = 300;
        mXTwoOffset = 0;
        refreshView();
    }


    /**
     * 刷新视图参数
     */
    public void refreshView(){
        postInvalidate();
    }

    /**
     * 设置水波纹颜色
     * @param waveColor
     */
    public void setWavePaintColor(int waveColor) {
        this.WAVE_PAINT_COLOR = ContextCompat.getColor(mContext,waveColor);
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        refreshView();
    }

    public void setmXOneOffset(int mXOneOffset) {
        this.mXOneOffset = mXOneOffset;
        refreshView();
    }

    public DynamicWaveMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = DpUtil.dp2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = DpUtil.dp2px(context, TRANSLATE_X_SPEED_TWO);
        mBottomBitmapWidth = DpUtil.dp2px(mContext,mBottomBitmapWidth);
        mBottomBitmapHeight = DpUtil.dp2px(mContext,mBottomBitmapHeight);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.below);
        mBitmap = Bitmap.createScaledBitmap(mBitmap,(int)mBottomBitmapWidth, (int)mBottomBitmapHeight, true);//精确缩放到指定大小

        WAVE_PAINT_COLOR = ContextCompat.getColor(mContext,R.color.color_blue_72adf8);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mLinePaint = new Paint();
        mLinePaint.setTextSize(DpUtil.dp2px(mContext, 14));
        mLinePaint.setColor(ContextCompat.getColor(mContext, R.color.color_red_FF6347));
        mLinePaint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));//绘制长度为8的实线在绘制长度为8的空白 2位间隔
        //设置了反而不显示了 https://blog.csdn.net/csdn_yang123/article/details/76850854
//        mLinePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(DpUtil.dp2px(mContext, 14));

    }


    public void setRunning(boolean running) {
        this.running = running;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(ContextCompat.getColor(mContext,R.color.color_white_ffffff));
        marginTop = mTotalHeight * ratio;
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        resetPositonY();
        for (int i = 0; i < mTotalWidth; i++) {
            // 减marginTop只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] - marginTop, i,
                    mTotalHeight,
                    mWavePaint);

            // 绘制第二条水波纹
            canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] - marginTop, i,
                    mTotalHeight,
                    mWavePaint);
        }
        if(running){
            // 改变两条波纹的移动点
            mXOneOffset += mXOffsetSpeedOne;
            mXTwoOffset += mXOffsetSpeedTwo;
        }

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }
        drawMenuContent(canvas);
        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        if (running) {
            postInvalidate();
        }
    }

    public int getmXOneOffset() {
        return mXOneOffset;
    }

    public int getmXTwoOffset() {
        return mXTwoOffset;
    }

    /**
     * 绘制菜单内容
     * @param canvas
     */
    private void drawMenuContent(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(mTotalWidth / 2 - mBitmap.getWidth() / 2,
                 DpUtil.dp2px(mContext, 20) + mBitmap.getHeight());
        canvas.drawBitmap(mBitmap, matrix, mLinePaint);
    }


    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(x > mTotalWidth / 2 - mBottomBitmapWidth/2 && x < mTotalWidth/2 + mBottomBitmapWidth/2
                    && y < DpUtil.dp2px(mContext,20)+mBottomBitmapHeight * 2){
                    if(imgMenuClickListener != null){
                        imgMenuClickListener.onClick();
                    }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setImgMenuClickListener(OnImgMenuClickListener imgMenuClickListener) {
        this.imgMenuClickListener = imgMenuClickListener;
    }

    public interface OnImgMenuClickListener{
        void onClick();
    }

}
