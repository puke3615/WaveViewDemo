package com.waveviewdemo;

import android.app.Application;

import com.github.mmin18.layoutcast.LayoutCast;

/**
 * @author zijiao
 * @version 16/7/21
 */
public class WaveApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            LayoutCast.init(this);
        }
    }
}
