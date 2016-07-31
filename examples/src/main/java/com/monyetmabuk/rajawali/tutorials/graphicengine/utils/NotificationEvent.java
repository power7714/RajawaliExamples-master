package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
import android.content.Context;
import android.os.Bundle;

public abstract class NotificationEvent {
    private Layer parent;

    public abstract boolean execute(Context context, Bundle bundle);

    public NotificationEvent(Layer layer) {
        this.parent = layer;
    }

    public Layer getLayer() {
        return this.parent;
    }
}