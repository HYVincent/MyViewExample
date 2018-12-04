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
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
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
public class DynamicWave extends View {
    private static final String TAG = "波纹";
    // 波纹颜色
    private static int WAVE_PAINT_COLOR /*= 0x880000aa*/;
    // y = Asin(wx+b)+h 这里的值越大  波纹上下间隔越大
    private static final float STRETCH_FACTOR_A = 30;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 5;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 10;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    private Context mContext;
    //距离View顶部的距离
    private float marginTop = 20;
    //动态设置波纹高度占View视图高度的比例
    private float ratio = 0.45f;

    private Paint mLinePaint;
    private Path path;
    private Bitmap mBitmap;

    private Paint mPaint;
    //数据最大值距离view顶部的间距
    private float mDataTopMargin = 10;

    //1,定义GestureDetector类
    private GestureDetector m_gestureDetector;
    private DynamicWaveTouchListener touchListener;

    private float mBottomBitmapWidth = 22f;
    private float mBottomBitmapHeight = 18f;

    public void setTouchListener(DynamicWaveTouchListener touchListener) {
        this.touchListener = touchListener;
    }

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
    }

    /**
     * 呈现可移动效果
     */
    public void initXOneOffset() {
        this.mXOneOffset = 675;
        mXTwoOffset = 0;
        refreshView();
    }

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //2,初始化手势类，同时设置手势监听
        m_gestureDetector = new GestureDetector(context, onGestureListener);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = DpUtil.dp2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = DpUtil.dp2px(context, TRANSLATE_X_SPEED_TWO);
        mDataTopMargin = DpUtil.dp2px(mContext,mDataTopMargin);
        mBottomBitmapWidth = DpUtil.dp2px(mContext,mBottomBitmapWidth);
        mBottomBitmapHeight = DpUtil.dp2px(mContext,mBottomBitmapHeight);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.top);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)mBottomBitmapWidth, (int)mBottomBitmapHeight, true);//精确缩放到指定大小

        WAVE_PAINT_COLOR = ContextCompat.getColor(mContext, R.color.color_blue_72adf8);


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
        mLinePaint.setStrokeWidth(DpUtil.dp2px(mContext,1));
        mLinePaint.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));//绘制长度为8的实线在绘制长度为8的空白 2位间隔
        //设置了反而不显示了 https://blog.csdn.net/csdn_yang123/article/details/76850854
//        mLinePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        path = new Path();


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(DpUtil.dp2px(mContext, 14));

    }


    // true 运行  false 暂停
    private boolean running = true;
    private Rect topRect = new Rect();
    private String top = "向上滑动";

    public void setTop(String topString){
        this.top = topString;
    }

    public void setRunning(boolean running) {
        this.running = running;
        refreshView();
    }

    public boolean isRunning() {
        return running;
    }

    /**
     * 设置水波纹颜色
     * @param wavePaintColor
     */
    public void setWavePaintColor(int wavePaintColor) {
        WAVE_PAINT_COLOR = ContextCompat.getColor(mContext,wavePaintColor);
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        postInvalidate();
    }

    /**
     * 刷新设置
     */
    public void refreshView(){
//        invalidate();
        postInvalidate();
    }

    public int getmXOneOffset() {
        return mXOneOffset;
    }

    public int getmXTwoOffset() {
        return mXTwoOffset;
    }

    public void setmXOneOffset(int mXOneOffset) {
        this.mXOneOffset = mXOneOffset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
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
        drawRadioLine(canvas);
        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        if (running) {
            /*try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            postInvalidate();
        }
    }

    /**
     * 绘制比例线 横向 4条线  25% 50% 75% 100%
     *
     * @param canvas
     */
    private void drawRadioLine(Canvas canvas) {
        path.reset();
        //画线
        for (int i = 0; i < 4; i++) {
            String radio = String.valueOf(100 - i * 25) + "%";
            Rect radioRect = new Rect();
            mLinePaint.getTextBounds(radio, 0, radio.length(), radioRect);
            float y = mDataTopMargin + (mTotalHeight - mDataTopMargin) * 0.25f * i;
            if (0.25f * (4 - i) > ratio) {
                mLinePaint.setColor(ContextCompat.getColor(mContext,R.color.color_gray_B5B4B4));
            } else {
                mLinePaint.setColor(ContextCompat.getColor(mContext,R.color.color_white_ffffff));
            }
            canvas.drawText(radio, mTotalWidth / 2 - radioRect.width() / 2, y, mLinePaint);
            canvas.drawLine(0, y - radioRect.height() / 2,
                    mTotalWidth / 2 - radioRect.width() / 2 - DpUtil.dp2px(mContext, 10), y - radioRect.height() / 2, mLinePaint);
            canvas.drawLine(mTotalWidth / 2 + radioRect.width() / 2 + DpUtil.dp2px(mContext, 10), y - radioRect.height() / 2,
                    mTotalWidth, y - radioRect.height() / 2, mLinePaint);
        }
        if(!TextUtils.isEmpty(top)){
            //绘制 向上滑动的文字
            mPaint.getTextBounds(top, 0, top.length(), topRect);
            mPaint.setTextSize(DpUtil.dp2px(mContext,12));
            canvas.drawText(top, mTotalWidth / 2 - topRect.width() / 2, mTotalHeight - topRect.height() - DpUtil.dp2px(mContext, 5), mPaint);

            Matrix matrix = new Matrix();
            matrix.postTranslate(mTotalWidth / 2 - mBitmap.getWidth() / 2,
                    mTotalHeight - topRect.height() - DpUtil.dp2px(mContext, 20) - mBitmap.getHeight());
            canvas.drawBitmap(mBitmap, matrix, mLinePaint);
        }
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
        //3,将touch事件交给gesture处理
        m_gestureDetector.onTouchEvent(event);
        return true;
    }

    protected static final float FLIP_DISTANCE = 50;
    // onScroll和onFling区别 https://blog.csdn.net/lamp_zy/article/details/52387732
    //初始化手势监听对象，使用GestureDetector.OnGestureListener的实现抽象类，因为实际开发中好多方法用不上
    private  GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG, "onDown: 点击。。。。。。。。。。");
            float x = e.getX();
            float y = e.getY();
            //判断点击范围是底部的向上按钮和文字的时候
            if(x > mTotalWidth /2 - mBottomBitmapWidth/2 && x < mTotalWidth/2 + mBottomBitmapWidth/2
                    && y > mTotalHeight - DpUtil.dp2px(mContext,5) - topRect.height() - mBottomBitmapHeight){
                if(touchListener != null){
                    touchListener.touchUp();
                }
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.d("GestureDemoView", "onFling() velocityX = " + velocityX);
            if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
//                Log.i("MYTAG", "向左滑...");
                return true;
            }
            if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
//                Log.i("MYTAG", "向右滑...");
                return true;
            }
            if (e1.getY() - e2.getY() > FLIP_DISTANCE) {
                //向上滑
                if(touchListener != null)touchListener.touchUp();
                return true;
            }
            if (e2.getY() - e1.getY() > FLIP_DISTANCE) {
//                Log.i("MYTAG", "向下滑...");
                if(touchListener != null)touchListener.touchDown();
                return true;
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("GestureDemoView", "onFling() velocityX = " + velocityX);
            if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
                Log.i("MYTAG", "向左滑...");
                return true;
            }
            if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
                Log.i("MYTAG", "向右滑...");
                return true;
            }
            if (e1.getY() - e2.getY() > FLIP_DISTANCE) {
                //向上滑
                if(touchListener != null)touchListener.touchUp();
                return true;
            }
            if (e2.getY() - e1.getY() > FLIP_DISTANCE) {
                Log.i("MYTAG", "向下滑...");
                if(touchListener != null)touchListener.touchDown();
                return true;
            }
            Log.d("TAG", e2.getX() + " " + e2.getY());
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    public interface DynamicWaveTouchListener{

        void touchUp();

        void touchDown();

    }

}
