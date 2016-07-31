package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

import android.content.Context;
import android.os.Bundle;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONObject;
import org.rajawali3d.renderer.Renderer;

public class Layer extends AbstractLayer {
    private static final String TAG = Layer.class.getName();

    public Layer(String name, String type) {
        super(name, type);
    }

    public Layer(Context context, String name, String type, boolean enable) {
        super(context, name, type, enable);
    }

    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        if (isEnable()) {
            getGraphicEngine().onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
        }
    }

    public void initScene(Context context, Renderer renderer) {
        try {
            getGraphicEngine().initScene(context, renderer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void destroyScene(Context context, Renderer renderer) {
        try {
            getGraphicEngine().destroyScene(context, renderer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public State draw(Context context, Renderer renderer, int width, int height, float deltaTime) {
        State state = null;
        try {
            state = getGraphicEngine().draw(context, renderer, width, height, deltaTime);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return state;
    }

    public boolean onCommand(Context context, String commandAction, int x, int y, int z, Bundle extras, boolean resultRequested) {
        if (!isEnable()) {
            return false;
        }
        boolean handled = false;
        for (int i = 0; i < getActions().size() && !handled; i++) {
            Action action = (Action) getActions().get(i);
            if (action.contains(x, y)) {
                handled = action.execute(context, commandAction, x, y);
            }
        }
        return handled;
    }

    public boolean onSensorEvent(Context context, String sensorType, float x, float y, float z, long timestamp) {
        if (isEnable()) {
            return getGraphicEngine().onSensorEvent(context, sensorType, x, y, z, timestamp);
        }
        return false;
    }

    public void onVisibilityChanged(boolean visible) {
        getGraphicEngine().onVisibilityChanged(visible);
    }

    public void onSurfaceChanged(float width, float height) {
        getGraphicEngine().onSurfaceChanged(width, height);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        getGraphicEngine().onSurfaceCreated(gl, config);
    }

    public void setDirty() {
        getGraphicEngine().setDirty(true);
    }

    public boolean onNotificationEvent(Context context, Bundle bundle) {
        return getGraphicEngine().onNotificationEvent(context, bundle);
    }

    public State onInteraction(Context context, JSONObject parameters) {
        if (parameters.optString("layer", "").equalsIgnoreCase(getName())) {
            return new StateStrategy(this, parameters).run();
        }
        return null;
    }
}