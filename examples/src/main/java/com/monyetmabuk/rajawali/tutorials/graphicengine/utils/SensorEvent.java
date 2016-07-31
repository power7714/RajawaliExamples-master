package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

import android.content.Context;

public abstract class SensorEvent {
    private Layer parent;

    public abstract boolean execute(Context context, String str, float f, float f2, float f3, long j);

    public SensorEvent(Layer layer) {
        this.parent = layer;
    }
}