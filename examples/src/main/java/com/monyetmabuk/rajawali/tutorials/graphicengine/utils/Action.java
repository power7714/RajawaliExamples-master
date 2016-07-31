package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public abstract class Action {
    private static final int BASE = 100;
    private int bottom;
    private Context context;
    private int left;
    private Rect rect = new Rect(this.left, this.top, this.right, this.bottom);
    private int right;
    private int top;

    public abstract boolean execute(Context context, String str, int i, int i2);

    public Action(Context context, float left, float top, float right, float bottom) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        this.left = (int) ((left / 100.0f) * ((float) metrics.widthPixels));
        this.top = (int) ((top / 100.0f) * ((float) metrics.heightPixels));
        this.right = (int) ((right / 100.0f) * ((float) metrics.widthPixels));
        this.bottom = (int) ((bottom / 100.0f) * ((float) metrics.heightPixels));
    }

    public int getLeft() {
        return this.left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return this.top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return this.right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return this.bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public Rect getRect() {
        return this.rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean contains(int x, int y) {
        return this.rect.contains(x, y);
    }
}