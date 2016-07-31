package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;
import android.os.Bundle;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;

import org.json.JSONObject;
import org.rajawali3d.renderer.Renderer;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public class CompositeLayer extends AbstractLayer {
    private static final String TAG = CompositeLayer.class.getName();
    private ArrayList<AbstractLayer> layers;

    public CompositeLayer(String name, String type) {
        super(name, type);
        this.layers = null;
        this.layers = new ArrayList();
    }

    public CompositeLayer(Context context, String name, String type, boolean enable) {
        super(context, name, type, enable);
        this.layers = null;
        this.layers = new ArrayList();
    }

    public void addLayer(AbstractLayer layer) {
        this.layers.add(layer);
    }

    public void removeLayer(AbstractLayer layer) {
        this.layers.remove(layer);
    }

    public ArrayList<AbstractLayer> getLayers() {
        return this.layers;
    }

    public ArrayList<AbstractLayer> getAllLayers() {
        ArrayList<AbstractLayer> newLayers = new ArrayList();
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            AbstractLayer layer = (AbstractLayer) it.next();
            if (layer instanceof Layer) {
                newLayers.add(layer);
            } else {
                CompositeLayer cl = (CompositeLayer) layer;
                newLayers.add(cl);
                newLayers.addAll(cl.getAllLayers());
            }
        }
        return newLayers;
    }

    public AbstractLayer getLayer(String name) {
        AbstractLayer result = null;
        ArrayList<AbstractLayer> alayers = getAllLayers();
        for (int i = 0; i < alayers.size() && result == null; i++) {
            AbstractLayer layer = (AbstractLayer) alayers.get(i);
            if (layer.getName().equalsIgnoreCase(name)) {
                result = layer;
            }
        }
        return result;
    }

    public ArrayList<Schedule> getAllSchedules() {
        ArrayList<Schedule> layerSchedules = super.getSchedules();
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            layerSchedules.addAll(((AbstractLayer) it.next()).getSchedules());
        }
        return layerSchedules;
    }

    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            ((AbstractLayer) it.next()).onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
        }
    }

    public void initScene(Context context, Renderer renderer) {
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            ((AbstractLayer) it.next()).initScene(context, renderer);
        }
    }

    public void destroyScene(Context context, Renderer renderer) {
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            ((AbstractLayer) it.next()).destroyScene(context, renderer);
        }
    }

    public State draw(Context context, Renderer renderer, int width, int height, float deltaTime) {
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            State state = ((AbstractLayer) it.next()).draw(context, renderer, width, height, deltaTime);
            if (state != null) {
                new CompositeChildStateStrategy(this, state, new JSONObject()).run();
            }
        }
        return null;
    }

    public boolean onCommand(Context context, String commandAction, int x, int y, int z, Bundle extras, boolean resultRequested) {
        boolean handled = false;
        for (int i = 0; i < this.layers.size() && !handled; i++) {
            handled = ((AbstractLayer) this.layers.get(i)).onCommand(context, commandAction, x, y, z, extras, resultRequested);
        }
        return handled;
    }

    public boolean onSensorEvent(Context context, String sensorType, float x, float y, float z, long timestamp) {
        boolean handled = false;
        for (int i = 0; i < this.layers.size() && !handled; i++) {
            handled = ((AbstractLayer) this.layers.get(i)).onSensorEvent(context, sensorType, x, y, z, timestamp);
        }
        return handled;
    }

    public boolean onNotificationEvent(Context context, Bundle bundle) {
        boolean handled = false;
        for (int i = 0; i < this.layers.size() && !handled; i++) {
            handled = ((AbstractLayer) this.layers.get(i)).onNotificationEvent(context, bundle);
        }
        return handled;
    }

    public void onVisibilityChanged(boolean visible) {
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            ((AbstractLayer) it.next()).onVisibilityChanged(visible);
        }
    }

    public void onSurfaceChanged(float width, float height) {
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            ((AbstractLayer) it.next()).onSurfaceChanged(width, height);
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            ((AbstractLayer) it.next()).onSurfaceCreated(gl, config);
        }
    }

    public State onInteraction(Context context, JSONObject parameters) {
        Iterator it = this.layers.iterator();
        while (it.hasNext()) {
            State state = ((AbstractLayer) it.next()).onInteraction(context, parameters);
            if (state != null) {
                new CompositeChildStateStrategy(this, state, parameters).run();
            }
        }
        return null;
    }

    public void setEnable(boolean enable) {
        super.setEnable(enable);
        int size = this.layers.size();
        for (int i = 0; i < size; i++) {
            ((AbstractLayer) this.layers.get(i)).setEnable(enable);
        }
    }
}