package com.waveviewdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/**
 * @author zijiao
 * @version 16/7/28
 *          适配发布进度跨度大的WaveView类
 */
public class MGWaveView extends WaveView {

    private OnCompleteListener mListener;
    private ValueAnimator mAnimator;
    private int mTarget;

    public MGWaveView(Context context) {
        this(context, null, 0);
    }

    public MGWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MGWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    public void reset() {
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            mAnimator = null;
        }
        super.setProgress(0);
    }

    @Override
    public void setProgress(float progress) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        float from = getProgress();
        mAnimator = ValueAnimator.ofFloat(from, progress);
        mAnimator.setDuration((long) (Math.abs(progress - from) * 100 * 1000 / 60));
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                MGWaveView.super.setProgress(progress);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
                if (getProgress() == 1 && mListener != null) {
                    mListener.onComplete();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    /**
     * 设置进度完成回调函数
     *
     * @param listener 完成回调函数
     */
    public void setOnCompleteListener(OnCompleteListener listener) {
        this.mListener = listener;
    }

    public interface OnCompleteListener {
        void onComplete();
    }

}
