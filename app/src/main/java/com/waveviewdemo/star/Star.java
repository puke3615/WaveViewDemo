package com.waveviewdemo.star;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.waveviewdemo.R;

import java.util.Random;

/**
 * @author zijiao
 * @version 16/7/27
 *          小星星
 */
public class Star {

    public Bitmap bitmap;
    public float alpha1;
    public float alpha2;
    public float scale1;
    public float scale2;
    public Matrix matrix;
    public int x;
    public int y;
    public float progressOffset;

    public void update(float progress, Canvas canvas, Paint paint) {

        progress += progressOffset;
        if (progress < 0) {
            progress = Math.abs(progress);
        } else if (progress > 1) {
            progress = 2 - progress;
        }

        float alpha = alpha1 + (alpha2 - alpha1) * progress;
        float scale = scale1 + (scale2 - scale1) * progress;

        paint.setAlpha((int) (alpha * 255));
        matrix.setScale(scale, scale);

        canvas.save();
        canvas.translate(x - scale * bitmap.getWidth() / 2, y - scale * bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, matrix, paint);
        canvas.restore();
    }

    public static Star randomInstance(Context context, float radius, int area) {
        Random r = new Random();
        Star star = new Star();
        star.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.star);
        int w = (int) (star.bitmap.getWidth() * Math.max(star.scale1, star.scale2));
        int h = (int) (star.bitmap.getHeight() * Math.max(star.scale1, star.scale2));
        star.alpha1 = r.nextFloat() * 0.3f + 0.6f;
        star.alpha2 = 1;
        star.scale1 = r.nextFloat() * 0.2f + 0.3f;
        star.scale2 = r.nextFloat() * 0.2f + 0.8f;
        star.progressOffset = r.nextFloat() - 0.5f;
        star.matrix = new Matrix();

        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        switch (area) {
            case 1:
                minX = (int) radius;
                maxX = (int) (radius * 1.5);
                minY = (int) (radius * 0.5);
                maxY = (int) radius;
                break;
            case 2:
                minX = (int) (radius * 0.5);
                maxX = (int) radius;
                minY = (int) (radius * 0.5);
                maxY = (int) radius;
                break;
            case 3:
                minX = (int) (radius * 0.5);
                maxX = (int) radius;
                minY = (int) radius;
                maxY = (int) (radius * 1.5);
                break;
            case 4:
                minX = (int) radius;
                maxX = (int) (radius * 1.5);
                minY = (int) radius;
                maxY = (int) (radius * 1.5);
                break;
        }

        star.x = minX + r.nextInt(maxX - minX);
        star.y = minY + r.nextInt(maxY - minY);
        return star;
    }

}
