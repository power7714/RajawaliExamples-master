package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;

/**
 * Created by CurTro Studios on 7/28/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;

public abstract class AbstractBitmap {
    public static Options options = null;
    private Bitmap bitmap;
    protected String bitmapName;

    public abstract Bitmap get(Context context);

    public abstract Bitmap get(Context context, Options options);

    protected AbstractBitmap() {
        this.bitmapName = "";
        this.bitmap = null;
        this.bitmap = null;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    protected Options getOptions() {
        if (options == null) {
            options = new Options();
            options.inScaled = false;
            options.inPreferredConfig = Config.ARGB_8888;
        }
        return options;
    }

    public void recycle() {
        if (!(this.bitmap == null || this.bitmap.isRecycled())) {
            this.bitmap.recycle();
        }
        this.bitmap = null;
    }

    public int getId(Context context) {
        return 0;
    }

    public String getBitmapName() {
        return this.bitmapName;
    }

    public void setBitmapName(String bitmapName) {
        this.bitmapName = bitmapName;
    }
}