package com.ben.android.gifvideo;

import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;

/**
 * AndroidUtilities The source code is from <a href="https://github.com/DrKLO/Telegram/blob/master/TMessagesProj/src/main/java/org/telegram/messenger/AndroidUtilities.java">https://github.com/DrKLO/Telegram/blob/master/TMessagesProj/src/main/java/org/telegram/messenger/AndroidUtilities.java</a>
 * @program: GifVideo
 * @description:
 * @author: ben622
 * @create: 2020-11-26 11:34
 **/
final class AndroidUtilities {
    private static float density = 1.0f;
    private static Handler applicationHandler;
    public static void initiate(Context context) {
        if (applicationHandler == null) {
            density = context.getResources().getDisplayMetrics().density;
            applicationHandler = new Handler(context.getMainLooper());
        }
    }
    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static int dp2(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.floor(density * value);
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            applicationHandler.post(runnable);
        } else {
            applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        applicationHandler.removeCallbacks(runnable);
    }

    public static void setRectToRect(android.graphics.Matrix matrix, RectF src, RectF dst, int rotation, boolean translate) {
        float tx, sx;
        float ty, sy;
        boolean xLarger = false;
        if (rotation == 90 || rotation == 270) {
            sx = dst.height() / src.width();
            sy = dst.width() / src.height();
        } else {
            sx = dst.width() / src.width();
            sy = dst.height() / src.height();
        }
        if (sx < sy) {
            sx = sy;
            xLarger = true;
        } else {
            sy = sx;
        }

        if (translate) {
            matrix.setTranslate(dst.left, dst.top);
        }
        if (rotation == 90) {
            matrix.preRotate(90);
            matrix.preTranslate(0, -dst.width());
        } else if (rotation == 180) {
            matrix.preRotate(180);
            matrix.preTranslate(-dst.width(), -dst.height());
        } else if (rotation == 270) {
            matrix.preRotate(270);
            matrix.preTranslate(-dst.height(), 0);
        }

        if (translate) {
            tx = -src.left * sx;
            ty = -src.top * sy;
        } else {
            tx = dst.left - src.left * sx;
            ty = dst.top - src.top * sy;
        }

        float diff;
        if (xLarger) {
            diff = dst.width() - src.width() * sy;
        } else {
            diff = dst.height() - src.height() * sy;
        }
        diff = diff / 2.0f;
        if (xLarger) {
            tx += diff;
        } else {
            ty += diff;
        }

        matrix.preScale(sx, sy);
        if (translate) {
            matrix.preTranslate(tx, ty);
        }
    }
}

