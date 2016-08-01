package com.waveviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author zijiao
 * @version 16/7/26
 */
public class BallView extends FrameLayout {

    public BallView(Context context) {
        this(context, null, 0);
    }

    public BallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        removeAllViews();

    }
}
