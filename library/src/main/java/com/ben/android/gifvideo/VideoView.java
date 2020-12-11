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
public final class VideoView extends androidx.appcompat.widget.AppCompatImageView {
    private AnimatedDrawable drawable;
    private boolean autoPlay = true;

    public VideoView(Context context) {
        this(context, null);
    }

    public VideoView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AndroidUtilities.initiate(context.getApplicationContext());
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        if (drawable != null) {
            drawable.setOnPreparedListener(onPreparedListener);
        }
    }

    /**
     * If set to true, it will automatically play or pause according to
     * {@link VideoView#onAttachedToWindow()}{@link VideoView#onDetachedFromWindow()}
     * @param autoPlay
     */
    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    /**
     * Set an available file path and automatically play
     * @param path
     */
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

    public void recycle() {
        if (drawable != null) {
            drawable.stop();
            drawable.recycle();
            drawable = null;
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

    /**
     * There may be blank frames when sliding quickly in Recycler View.
     * You can use this function to set thumbnails to fill in blank frames
     * @param bitmap
     */
    public void setThumbnailBitmap(Bitmap bitmap) {
        setBackground(new BitmapDrawable(bitmap));
    }

    public interface OnPreparedListener {
        void onPrepared(int duration);
    }
}
