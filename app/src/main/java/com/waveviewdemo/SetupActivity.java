package com.waveviewdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.puke.debug.DebugActivity;
import com.waveviewdemo.star.StarView;

public class SetupActivity extends DebugActivity {

    private MGWaveView mWaveView;
    private StarView starView;

    @Override
    protected void onLayoutCreate(LinearLayout layout) {
//        layout.addView(new MyView(this));
//        View frameLayout = LayoutInflater.from(this).inflate(R.layout.activity_main, layout);
//        mWaveView = (WaveView) frameLayout.findViewById(R.id.waveView);
//        layout.addView(frameLayout);
        mWaveView = (MGWaveView) LayoutInflater.from(this).inflate(R.layout.activity_main, layout).findViewById(R.id.waveView);
        mWaveView.setOnCompleteListener(new MGWaveView.OnCompleteListener() {
            @Override
            public void onComplete() {
                T("完成了..");
                mWaveView.reset();
            }
        });
//        layout.addView(mWaveView = new WaveView(this));

        layout.addView(starView = new StarView(this));

        SeekBar bar = new SeekBar(this);
        bar.setMax(100);
        bar.setProgress((int) (mWaveView.getProgress() * 100));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && progress % 10 == 0) {
                    mWaveView.setProgress(progress / 100f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        layout.addView(bar);
    }

    @Debug
    public void 开始动画() {
        mWaveView.start();
    }

    @Debug
    public void 结束动画() {
        mWaveView.stop();
    }

    @Debug
    public void 闪闪闪() {
        if (starView.isRunning()) {
            starView.stop();
        } else {
            starView.start();
        }
    }
}
