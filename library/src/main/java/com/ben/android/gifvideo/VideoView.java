package com.ben.android.gifvideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import java.io.File;

/**
 * @program: GifVideo
 * @description:
 * @author: ben622
 * @create: 2020-11-27 15:34
 **/
public final class VideoView  extends androidx.appcompat.widget.AppCompatImageView {
    private AnimatedDrawable drawable;
    private boolean autoPlay = true;
    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoView(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        if (drawable != null) {
            drawable.setOnPreparedListener(onPreparedListener);
        }
    }

    public void setPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            android.util.Log.e("gifvideo", "The path was not found");
            return;
        }
        if (drawable != null) {
            drawable.stop();
            drawable.recycle();
        }
        setImageDrawable(drawable = new AnimatedDrawable(file));
        drawable.start();
    }


    public void start() {
        if (drawable != null) {
            drawable.start();
        }
    }
    public void stop() {
        if (drawable != null) {
            drawable.stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (autoPlay) {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (autoPlay) {
            stop();
        }
    }

    public void setThumbnailBitmap(Bitmap bitmap) {
        setBackground(new BitmapDrawable(bitmap));
    }

    public interface OnPreparedListener {
        void onPrepared(int duration);
    }
}
