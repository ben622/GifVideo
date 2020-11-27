package com.ben.android.gifvideo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.File;

/**
 * @program: GifVideo
 * @description:
 * @author: ben622
 * @create: 2020-11-27 15:34
 **/
public final class VideoView  extends ImageView {
    private AnimatedDrawable drawable;
    private boolean autoPlay = true;
    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Log.e("gifvideo", "The path was not found");
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
}
