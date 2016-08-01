package com.waveviewdemo.star;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.waveviewdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zijiao
 * @version 16/7/27
 */
public class StarView extends View {

    private static final int START_COUNT = 4;
    private int size;
    private Paint paint;
    private Path path;
    private List<Star> stars;
    private float progress;
    private ValueAnimator animator;

    public StarView(Context context) {
        this(context, null, 0);
    }

    public StarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55f, getResources().getDisplayMetrics());
        paint = new Paint();
        path = new Path();
        float radius = size / 2;
        path.addCircle(radius, radius, radius, Path.Direction.CW);
        stars = new ArrayList<>();
        for (int i = 0; i < START_COUNT; i++) {
            stars.add(Star.randomInstance(context, radius, i % 4 + 1));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(path);
        canvas.drawColor(Color.RED);

        for (Star star : stars) {
            star.update(progress, canvas, paint);
        }
    }

    public void start() {
        if (animator == null) {
            animator = ValueAnimator.ofFloat(0, 1);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(300);
            animator.setRepeatCount(4);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    progress = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    public void stop() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    public boolean isRunning() {
        return animator != null && animator.isRunning();
    }

}
