/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package com.ben.android.gifvideo;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * AnimatedDrawable The source code is from <a href="https://github.com/DrKLO/Telegram/blob/master/TMessagesProj/src/main/java/org/telegram/ui/Components/AnimatedFileDrawable.java">https://github.com/DrKLO/Telegram/blob/master/TMessagesProj/src/main/java/org/telegram/ui/Components/AnimatedFileDrawable.java</a>
 * @program: GifVideo
 * @description:
 * @author: ben622
 * @create: 2020-11-26 11:34
 **/
public class AnimatedDrawable extends BitmapDrawable implements Animatable {
    static {
        System.loadLibrary("gifvideo");
    }
    private static native long createDecoder(String src, int[] params, int account, long streamFileSize);
    private static native void destroyDecoder(long ptr);
    private static native void stopDecoder(long ptr);
    private static native int getVideoFrame(long ptr, Bitmap bitmap, int[] params, int stride);
    private static native void seekToMs(long ptr, long ms);
    private static native void prepareToSeek(long ptr);

    private VideoView.OnPreparedListener onPreparedListener;
    private long lastFrameTime;
    private int lastTimeStamp;
    private int invalidateAfter = 50;
    private final int[] metaData = new int[5];
    private Runnable loadFrameTask;
    private Bitmap renderingBitmap;
    private int renderingBitmapTime;
    private Bitmap nextRenderingBitmap;
    private int nextRenderingBitmapTime;
    private Bitmap backgroundBitmap;
    private int backgroundBitmapTime;
    private boolean destroyWhenDone;
    private boolean decoderCreated;
    private boolean decodeSingleFrame;
    private boolean singleFrameDecoded;
    private File path;
    private long streamFileSize;
    private boolean recycleWithSecond;
    private volatile long pendingSeekTo = -1;
    private volatile long pendingSeekToUI = -1;
    private boolean pendingRemoveLoading;
    private int pendingRemoveLoadingFramesReset;
    private final Object sync = new Object();

    private long lastFrameDecodeTime;

    private RectF actualDrawRect = new RectF();

    private BitmapShader renderingShader;
    private BitmapShader nextRenderingShader;
    private BitmapShader backgroundShader;

    private int roundRadius;
    private RectF roundRect = new RectF();
    private RectF bitmapRect = new RectF();
    private android.graphics.Matrix shaderMatrix = new android.graphics.Matrix();

    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private boolean applyTransformation;
    private final Rect dstRect = new Rect();
    private static final Handler uiHandler = new Handler(Looper.getMainLooper());
    private volatile boolean isRunning;
    private volatile boolean isRecycled;
    public volatile long nativePtr;
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2, new java.util.concurrent.ThreadPoolExecutor.DiscardPolicy());
    private DispatchQueue decodeQueue;


    private Runnable uiRunnableNoFrame = new Runnable() {
        @Override
        public void run() {
            if (destroyWhenDone && nativePtr != 0) {
                destroyDecoder(nativePtr);
                nativePtr = 0;
            }
            if (nativePtr == 0) {
                if (renderingBitmap != null) {
                    renderingBitmap.recycle();
                    renderingBitmap = null;
                }
                if (backgroundBitmap != null) {
                    backgroundBitmap.recycle();
                    backgroundBitmap = null;
                }
                if (decodeQueue != null) {
                    decodeQueue.recycle();
                    decodeQueue = null;
                }
                return;
            }
            loadFrameTask = null;
            scheduleNextGetFrame();
        }
    };

    private Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
            if (destroyWhenDone && nativePtr != 0) {
                destroyDecoder(nativePtr);
                nativePtr = 0;
            }
            if (nativePtr == 0) {
                if (renderingBitmap != null) {
                    renderingBitmap.recycle();
                    renderingBitmap = null;
                }
                if (backgroundBitmap != null) {
                    backgroundBitmap.recycle();
                    backgroundBitmap = null;
                }
                if (decodeQueue != null) {
                    decodeQueue.recycle();
                    decodeQueue = null;
                }
                return;
            }
            if (pendingRemoveLoadingFramesReset <= 0) {
                pendingRemoveLoading = true;
            } else {
                pendingRemoveLoadingFramesReset--;
            }
            singleFrameDecoded = true;
            loadFrameTask = null;
            nextRenderingBitmap = backgroundBitmap;
            nextRenderingBitmapTime = backgroundBitmapTime;
            nextRenderingShader = backgroundShader;
            if (metaData[3] < lastTimeStamp) {
                lastTimeStamp = 0;
            }
            if (metaData[3] - lastTimeStamp != 0) {
                invalidateAfter = metaData[3] - lastTimeStamp;
            }
            if (pendingSeekToUI >= 0 && pendingSeekTo == -1) {
                pendingSeekToUI = -1;
                invalidateAfter = 0;
            }
            lastTimeStamp = metaData[3];
            invalidateSelf();
            scheduleNextGetFrame();
        }
    };

    private Runnable loadFrameRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRecycled) {
                if (!decoderCreated && nativePtr == 0) {
                    nativePtr = createDecoder(path.getAbsolutePath(), metaData, 0, streamFileSize);
                    if (nativePtr != 0 && onPreparedListener!=null) {
                        AndroidUtilities.runOnUIThread(()->onPreparedListener.onPrepared(metaData[4]));
                    }
                    decoderCreated = true;
                }
                try {
                    if (nativePtr != 0 || metaData[0] == 0 || metaData[1] == 0) {
                        if (backgroundBitmap == null && metaData[0] > 0 && metaData[1] > 0) {
                            try {
                                backgroundBitmap = Bitmap.createBitmap(metaData[0], metaData[1], Bitmap.Config.ARGB_8888);
                            } catch (Throwable e) {
                                android.util.Log.e("AnimatedFileDrawable", e.getMessage());
                            }
                            if (backgroundShader == null && backgroundBitmap != null && roundRadius != 0) {
                                backgroundShader = new BitmapShader(backgroundBitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
                            }
                        }
                        boolean seekWas = false;
                        if (pendingSeekTo >= 0) {
                            metaData[3] = (int) pendingSeekTo;
                            long seekTo = pendingSeekTo;
                            synchronized(sync) {
                                pendingSeekTo = -1;
                            }
                            seekWas = true;
                            seekToMs(nativePtr, seekTo);
                        }
                        if (backgroundBitmap != null) {
                            lastFrameDecodeTime = System.currentTimeMillis();
                            if (getVideoFrame(nativePtr, backgroundBitmap, metaData, backgroundBitmap.getRowBytes()) == 0) {
                                AndroidUtilities.runOnUIThread(uiRunnableNoFrame);
                                return;
                            }
                            if (seekWas) {
                                lastTimeStamp = metaData[3];
                            }
                            backgroundBitmapTime = metaData[3];
                        }
                    } else {
                        AndroidUtilities.runOnUIThread(uiRunnableNoFrame);
                        return;
                    }
                } catch (Throwable e) {
                    android.util.Log.e("AnimatedFileDrawable", e.getMessage() );
                }
            }
            AndroidUtilities.runOnUIThread(uiRunnable);
        }
    };


    public AnimatedDrawable(File file) {
        path = file;
        streamFileSize = file.length();
    }


    public void setAllowDecodeSingleFrame(boolean value) {
        decodeSingleFrame = value;
        if (decodeSingleFrame) {
            scheduleNextGetFrame();
        }
    }

    public void seekTo(long ms, boolean removeLoading) {
        synchronized (sync) {
            pendingSeekTo = ms;
            pendingSeekToUI = ms;
            prepareToSeek(nativePtr);
        }
    }

    public void recycle() {
        isRunning = false;
        isRecycled = true;
        if (loadFrameTask == null) {
            if (nativePtr != 0) {
                destroyDecoder(nativePtr);
                nativePtr = 0;
            }
            if (renderingBitmap != null) {
                renderingBitmap.recycle();
                renderingBitmap = null;
            }
            if (nextRenderingBitmap != null) {
                nextRenderingBitmap.recycle();
                nextRenderingBitmap = null;
            }
            if (decodeQueue != null) {
                decodeQueue.recycle();
                decodeQueue = null;
            }
        } else {
            destroyWhenDone = true;
        }
    }

    protected static void runOnUiThread(Runnable task) {
        if (Looper.myLooper() == uiHandler.getLooper()) {
            task.run();
        } else {
            uiHandler.post(task);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public void start() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        scheduleNextGetFrame();
    }

    public float getCurrentProgress() {
        if (metaData[4] == 0) {
            return 0;
        }
        if (pendingSeekToUI >= 0) {
            return pendingSeekToUI / (float) metaData[4];
        }
        return metaData[3] / (float) metaData[4];
    }

    public int getCurrentProgressMs() {
        if (pendingSeekToUI >= 0) {
            return (int) pendingSeekToUI;
        }
        return nextRenderingBitmapTime != 0 ? nextRenderingBitmapTime : renderingBitmapTime;
    }

    public int getDurationMs() {
        return metaData[4];
    }

    private void scheduleNextGetFrame() {
        if (loadFrameTask != null || nativePtr == 0 && decoderCreated || destroyWhenDone || !isRunning && (!decodeSingleFrame || decodeSingleFrame && singleFrameDecoded)) {
            return;
        }
        long ms = 0;
        if (lastFrameDecodeTime != 0) {
            ms = Math.min(invalidateAfter, Math.max(0, invalidateAfter - (System.currentTimeMillis() - lastFrameDecodeTime)));
        }
        if (streamFileSize != 0) {
            if (decodeQueue == null) {
                decodeQueue = new DispatchQueue("decodeQueue" + this);
            }
            decodeQueue.postRunnable(loadFrameTask = loadFrameRunnable, ms);
        } else {
            executor.schedule(loadFrameTask = loadFrameRunnable, ms, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getIntrinsicHeight() {
        int height = decoderCreated ? (metaData[2] == 90 || metaData[2] == 270 ? metaData[0] : metaData[1]) : 0;
        if (height == 0) {
            return AndroidUtilities.dp(100);
        }
        return height;
    }

    @Override
    public int getIntrinsicWidth() {
        int width = decoderCreated ? (metaData[2] == 90 || metaData[2] == 270 ? metaData[1] : metaData[0]) : 0;
        if (width == 0) {
            return AndroidUtilities.dp(100);
        }
        return width;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        applyTransformation = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (nativePtr == 0 && decoderCreated || destroyWhenDone) {
            return;
        }
        long now = System.currentTimeMillis();
        if (isRunning) {
            if (renderingBitmap == null && nextRenderingBitmap == null) {
                scheduleNextGetFrame();
            } else if (nextRenderingBitmap != null && (renderingBitmap == null || Math.abs(now - lastFrameTime) >= invalidateAfter)) {
                renderingBitmap = nextRenderingBitmap;
                renderingBitmapTime = nextRenderingBitmapTime;
                renderingShader = nextRenderingShader;
                nextRenderingBitmap = null;
                nextRenderingBitmapTime = 0;
                nextRenderingShader = null;
                lastFrameTime = now;
            }
        } else if (!isRunning && decodeSingleFrame && Math.abs(now - lastFrameTime) >= invalidateAfter && nextRenderingBitmap != null) {
            renderingBitmap = nextRenderingBitmap;
            renderingBitmapTime = nextRenderingBitmapTime;
            renderingShader = nextRenderingShader;
            nextRenderingBitmap = null;
            nextRenderingBitmapTime = 0;
            nextRenderingShader = null;
            lastFrameTime = now;
        }

        if (renderingBitmap != null) {
            if (applyTransformation) {
                int bitmapW = renderingBitmap.getWidth();
                int bitmapH = renderingBitmap.getHeight();
                if (metaData[2] == 90 || metaData[2] == 270) {
                    int temp = bitmapW;
                    bitmapW = bitmapH;
                    bitmapH = temp;
                }
                dstRect.set(getBounds());
                scaleX = (float) dstRect.width() / bitmapW;
                scaleY = (float) dstRect.height() / bitmapH;
                applyTransformation = false;
            }
            if (roundRadius != 0) {
                float scale = Math.max(scaleX, scaleY);
                if (renderingShader == null) {
                    renderingShader = new BitmapShader(backgroundBitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
                }
                getPaint().setShader(renderingShader);
                roundRect.set(dstRect);
                shaderMatrix.reset();
                bitmapRect.set(0, 0, renderingBitmap.getWidth(), renderingBitmap.getHeight());
                AndroidUtilities.setRectToRect(shaderMatrix, bitmapRect, roundRect, metaData[2], true);
                renderingShader.setLocalMatrix(shaderMatrix);
                canvas.drawRoundRect(actualDrawRect, roundRadius, roundRadius, getPaint());
            } else {
                canvas.translate(dstRect.left, dstRect.top);
                if (metaData[2] == 90) {
                    canvas.rotate(90);
                    canvas.translate(0, -dstRect.width());
                } else if (metaData[2] == 180) {
                    canvas.rotate(180);
                    canvas.translate(-dstRect.width(), -dstRect.height());
                } else if (metaData[2] == 270) {
                    canvas.rotate(270);
                    canvas.translate(-dstRect.height(), 0);
                }
                canvas.scale(scaleX, scaleY);
                canvas.drawBitmap(renderingBitmap, 0, 0, getPaint());
            }
            if (isRunning) {
                long timeToNextFrame = Math.max(1, invalidateAfter - (now - lastFrameTime) - 17);
            }
        }
    }

    @Override
    public int getMinimumHeight() {
        int height = decoderCreated ? (metaData[2] == 90 || metaData[2] == 270 ? metaData[0] : metaData[1]) : 0;
        if (height == 0) {
            return AndroidUtilities.dp(100);
        }
        return height;
    }

    @Override
    public int getMinimumWidth() {
        int width = decoderCreated ? (metaData[2] == 90 || metaData[2] == 270 ? metaData[1] : metaData[0]) : 0;
        if (width == 0) {
            return AndroidUtilities.dp(100);
        }
        return width;
    }

    public Bitmap getRenderingBitmap() {
        return renderingBitmap;
    }

    public Bitmap getNextRenderingBitmap() {
        return nextRenderingBitmap;
    }

    public Bitmap getBackgroundBitmap() {
        return backgroundBitmap;
    }

    public Bitmap getAnimatedBitmap() {
        if (renderingBitmap != null) {
            return renderingBitmap;
        } else if (nextRenderingBitmap != null) {
            return nextRenderingBitmap;
        }
        return null;
    }

    public void setActualDrawRect(int x, int y, int width, int height) {
        actualDrawRect.set(x, y, x + width, y + height);
    }

    public void setRoundRadius(int value) {
        roundRadius = value;
        getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public boolean hasBitmap() {
        return nativePtr != 0 && (renderingBitmap != null || nextRenderingBitmap != null);
    }

    public int getOrientation() {
        return metaData[2];
    }

    public void setOnPreparedListener(VideoView.OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }
}
