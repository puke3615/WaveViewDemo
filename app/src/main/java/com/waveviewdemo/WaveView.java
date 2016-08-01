package com.waveviewdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * @author zijiao
 * @version 16/7/21
 */
public class WaveView extends View {

    //百分比显示
    private Paint mTextPaint;
    private int mTextSize;
    private int mTextColor;
    private int mSecondTextColor;
    private Rect mTextBound;
    private String mProgressText;

    private int mStrokeWidth;
    private int mMainColor;
    private Paint mPaint;
    private Path mPath;
    private int mRadius;
    private int yRate;
    private float mCircleOffset;
    private float mCircleCount;
    private float mProgress;
    private ValueAnimator mAnimator;

    private RectF mRectF;

    public WaveView(Context context) {
        this(context, null, 0);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //设置设置,否则部分手机由于硬件加速导致clipPath无效
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //图形画笔
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextBound = new Rect();
        //文字区域
        mRectF = new RectF();
        mPath = new Path();

        //自定义属性
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
            mMainColor = array.getColor(R.styleable.WaveView_mainColor, 0);
            mTextColor = array.getColor(R.styleable.WaveView_textColor, 0);
            mSecondTextColor = array.getColor(R.styleable.WaveView_secondTextColor, 0);
            mTextSize = (int) array.getDimension(R.styleable.WaveView_textSize, 0);
            mStrokeWidth = (int) array.getDimension(R.styleable.WaveView_strokeWidth, 0);
            mProgress = array.getFloat(R.styleable.WaveView_progress, 0);
            yRate = (int) array.getDimension(R.styleable.WaveView_yRate, 0);
            array.recycle();
        }

        //check values
        if (mMainColor == 0) {
            mMainColor = Color.RED;
        }
        if (mTextColor == 0) {
            mTextColor = Color.RED;
        }
        if (mSecondTextColor == 0) {
            mSecondTextColor = Color.WHITE;
        }
        if (mTextSize == 0) {
            mTextSize = sp(50);
        }
        if (mStrokeWidth == 0) {
            mStrokeWidth = dp(5);
        }
        if (yRate == 0) {
            yRate = dp(5);
        }
        mPaint.setColor(mMainColor);
        mTextPaint.setTextSize(mTextSize);
        mProgressText = getProgressText(mProgress);
        mCircleCount = 1.5f;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        //切割切割
        mPath.reset();
        mPath.addCircle(mRadius, mRadius, mRadius, Path.Direction.CW);
        canvas.clipPath(mPath);

        int strokeWidth = mStrokeWidth;
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mRadius, mRadius, mRadius - strokeWidth / 2, mPaint);

        //下层文字
        String progressText = mProgressText;
        mTextPaint.getTextBounds(progressText, 0, progressText.length(), mTextBound);
        float textX = (width - mTextBound.width()) / 2;
        float textY = (height + mTextBound.height()) / 2;
        mTextPaint.setColor(mTextColor);
        canvas.drawText(progressText, textX, textY, mTextPaint);

        //正弦正弦
        if (mProgress != 1) {
            mPath.reset();
            float yOffset = height * (1 - mProgress);
            mPath.moveTo(0, yOffset);
            for (int i = 0; i < width; i++) {
                float y = yRate * (float) Math.sin((mCircleCount * i / width + mCircleOffset) * 2 * Math.PI) + yOffset;
                mPath.lineTo(i, y);
            }
            //画弧画弧
            float rotateOffset = 180 * mProgress;
            mPath.arcTo(mRectF, 90 - rotateOffset, 90 + rotateOffset);
        }
        mPaint.setStyle(Paint.Style.FILL);
//        canvas.drawPath(mPath, int);
        canvas.clipPath(mPath);
        canvas.drawColor(mMainColor);

        //上层文字
        mTextPaint.setColor(mSecondTextColor);
        canvas.drawText(progressText, textX, textY, mTextPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int suggestSize = calculateValue(width, height);
        mRadius = suggestSize / 2;
        mRectF.set(0, 0, mRadius * 2, mRadius * 2);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(suggestSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(suggestSize, MeasureSpec.EXACTLY)
        );
    }

    private int calculateValue(int width, int height) {
        if (width == 0 && height == 0) {
            throw new RuntimeException("the size set error");
        }
        if (width == 0 || height == 0) {
            return Math.max(width, height);
        } else {
            return Math.min(width, height);
        }
    }


    public boolean isRunning() {
        return mAnimator != null && mAnimator.isRunning();
    }

    /**
     * 开始动画
     */
    public void start() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0, 1);
            mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimator.setDuration(500);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCircleOffset = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    /**
     * 停止动画
     */
    public void stop() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }


    private int dp(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * @param yRate Y轴的极点
     */
    public void setYRate(int yRate) {
        this.yRate = yRate;
        invalidate();
    }

    /**
     * @param circleCount 一屏下的周期数量
     */
    public void setCircleCount(float circleCount) {
        this.mCircleCount = circleCount;
        invalidate();
    }

    /**
     * @param progress 设置当前进度(0 ~ 1)
     */
    public void setProgress(float progress) {
        this.mProgress = progress;
        mProgressText = getProgressText(progress);
        invalidate();
    }

    /**
     * @param textColor 设置底层文字颜色
     */
    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
        invalidate();
    }

    /**
     * @param secondColor 设置二层文字颜色
     */
    public void setSecondTextColor(int secondColor) {
        this.mSecondTextColor = secondColor;
        invalidate();
    }

    /**
     * 设置文字大小
     *
     * @param textSize size
     * @param mode     如:{@link TypedValue#COMPLEX_UNIT_SP}
     */
    public void setTextSize(float textSize, int mode) {
        float size = TypedValue.applyDimension(mode, textSize, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(size);
        invalidate();
    }

    /**
     * 设置文字大小
     *
     * @param textSize size
     */
    public void setTextSize(int textSize) {
        mTextPaint.setTextSize(textSize);
        invalidate();
    }

    /**
     * @param progress 当前进度(0 ~ 1)
     * @return 显示当前进度对应的文字内容
     */
    protected String getProgressText(float progress) {
        return (int) (progress * 100) + "%";
    }

    /**
     * 返回当前进度
     *
     * @return 当前进度(0 ~ 1)
     */
    public float getProgress() {
        return mProgress;
    }

}
