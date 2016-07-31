package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;
import android.os.Bundle;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;

import org.json.JSONObject;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.cameras.Camera2D;
import org.rajawali3d.renderer.Renderer;

import java.util.ArrayList;
import java.util.Timer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public class Scenario {
    private static final String TAG = Scenario.class.getName();
    private Camera camera = null;
    private Context context = null;
    private int frameRate = -1;
    private AbstractLayer[] layers = null;
    private String name = null;
    private String nextScenarioEpisode = null;
    private String nextScenarioFilename = null;
    private int nextScenarioVersionCode = -1;
    private int probeTimer = -1;
    private Schedule[] schedules = null;
    private Service[] services = null;
    private Timer timerProbe = null;
    private int versionCode = -1;
    private float xOffset = -1.0f;

    public Scenario(Context context, String name, int versionCode, int frameRate, int probeTimer) {
        this.context = context;
        this.name = name;
        setVersionCode(versionCode);
        this.frameRate = frameRate;
        this.probeTimer = probeTimer;
        if (this.probeTimer != -1) {
            this.probeTimer *= 1000;
        }
        this.layers = new AbstractLayer[0];
        this.xOffset = -1.0f;
    }

    public Context getContext() {
        return this.context;
    }

    public void setDirty() {
        for (AbstractLayer layer : this.layers) {
            layer.setDirty();
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getFrameRate() {
        return this.frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getProbeTimer() {
        return this.probeTimer;
    }

    public void setProbeTimer(int probeTimer) {
        this.probeTimer = probeTimer;
    }

    public Service[] getServices() {
        return this.services;
    }

    public Schedule[] getSchedules() {
        return this.schedules;
    }

    public Schedule[] getAllSchedules() {
        int i = 0;
        ArrayList<Schedule> layerSchedules = new ArrayList();
        for (Schedule schedule : this.schedules) {
            layerSchedules.add(schedule);
        }
        AbstractLayer[] abstractLayerArr = this.layers;
        int length = abstractLayerArr.length;
        while (i < length) {
            layerSchedules.addAll(abstractLayerArr[i].getAllSchedules());
            i++;
        }
        Schedule[] s = new Schedule[layerSchedules.size()];
        layerSchedules.toArray(s);
        return s;
    }

    public String getServiceType(String key) {
        String value = null;
        for (int i = 0; i < this.services.length && value == null; i++) {
            if (this.services[i].getName().equalsIgnoreCase(key)) {
                value = this.services[i].getType();
            }
        }
        return value;
    }

    public void setLayers(ArrayList<AbstractLayer> newLayers) {
        this.layers = new AbstractLayer[newLayers.size()];
        for (int i = 0; i < newLayers.size(); i++) {
            this.layers[i] = (AbstractLayer) newLayers.get(i);
        }
    }

    public void addLayer(AbstractLayer layer) {
        AbstractLayer[] newLayers = new AbstractLayer[(this.layers.length + 1)];
        System.arraycopy(this.layers, 0, newLayers, 0, this.layers.length);
        newLayers[newLayers.length - 1] = layer;
        this.layers = newLayers;
    }

    public void addSchedule(Schedule schedule) {
        Schedule[] newSchedules = new Schedule[(this.schedules.length + 1)];
        System.arraycopy(this.schedules, 0, newSchedules, 0, this.schedules.length);
        newSchedules[newSchedules.length - 1] = schedule;
        this.schedules = newSchedules;
    }

    public void removeLayer(AbstractLayer layer) {
        AbstractLayer[] newLayers = new AbstractLayer[(this.layers.length - 1)];
        int j = 0;
        for (int i = 0; i < this.layers.length; i++) {
            if (!this.layers[i].getName().equalsIgnoreCase(layer.getName())) {
                newLayers[j] = this.layers[i];
                j++;
            }
        }
        this.layers = newLayers;
    }

    public AbstractLayer[] getLayers() {
        return this.layers;
    }

    public AbstractLayer[] getAllLayers() {
        ArrayList<AbstractLayer> newLayers = new ArrayList();
        for (AbstractLayer layer : this.layers) {
            if (layer instanceof Layer) {
                newLayers.add(layer);
            } else {
                CompositeLayer cl = (CompositeLayer) layer;
                newLayers.add(cl);
                newLayers.addAll(cl.getAllLayers());
            }
        }
        AbstractLayer[] alayers = new AbstractLayer[newLayers.size()];
        newLayers.toArray(alayers);
        return alayers;
    }

    public AbstractLayer getLayer(String name) {
        AbstractLayer result = null;
        AbstractLayer[] alayers = getAllLayers();
        for (int i = 0; i < alayers.length && result == null; i++) {
            AbstractLayer layer = alayers[i];
            if (layer.getName().equalsIgnoreCase(name)) {
                result = layer;
            }
        }
        return result;
    }

    public void setCamera(ConfigurationCamera confCamera) {
        if (confCamera.getMode().equalsIgnoreCase("ortho")) {
            Camera2D camera2d = new Camera2D();
            camera2d.setWidth(confCamera.getWidth());
            camera2d.setHeight(confCamera.getHeight());
            this.camera = camera2d;
        } else if (confCamera.getMode().equalsIgnoreCase("offaxis_projection")) {
            this.camera = new OffAxisProjectionCamera();
            this.camera.setFieldOfView(confCamera.getFov());
            this.camera.setNearPlane(confCamera.getNearPlane());
            this.camera.setFarPlane(confCamera.getFarPlane());
            this.camera.setPosition(confCamera.getPosX(), confCamera.getPosY(), confCamera.getPosZ());
            this.camera.setLookAt(confCamera.getLookAtX(), confCamera.getLookAtY(), confCamera.getLookAtZ());
        } else {
            this.camera = new Camera();
            this.camera.setFieldOfView(confCamera.getFov());
            this.camera.setNearPlane(confCamera.getNearPlane());
            this.camera.setFarPlane(confCamera.getFarPlane());
            this.camera.setPosition(confCamera.getPosX(), confCamera.getPosY(), confCamera.getPosZ());
            this.camera.setLookAt(confCamera.getLookAtX(), confCamera.getLookAtY(), confCamera.getLookAtZ());
        }
    }

    public Camera getCamera() {
        return this.camera;
    }

    public String getNextScenario() {
        return this.nextScenarioFilename;
    }

    public String getNextScenarioEpisode() {
        return this.nextScenarioEpisode;
    }

    public void setNextScenarioEpisode(String nextScenarioEpisode) {
        this.nextScenarioEpisode = nextScenarioEpisode;
    }

    public void setNextScenario(String nextScenario) {
        this.nextScenarioFilename = nextScenario;
    }

    public void setNextScenario(String nextScenario, String id, int versionCode) {
        this.nextScenarioFilename = nextScenario.trim() + "_" + id.trim();
        this.nextScenarioVersionCode = versionCode;
    }

    public int getNextScenarioVersionCode() {
        return this.nextScenarioVersionCode;
    }

    public void initScene(Context context, Renderer renderer) {
        renderer.setFrameRate(getFrameRate());
        renderer.getCurrentScene().addAndSwitchCamera(this.camera);
        //renderer.getCurrentScene().setCamera(this.camera);
        for (AbstractLayer layer : this.layers) {
            try {
                layer.initScene(context, renderer);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void destroyScene(Context context, Renderer renderer) {
        for (AbstractLayer layer : this.layers) {
            try {
                layer.destroyScene(context, renderer);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void draw(Context context, Renderer renderer, int width, int height, float deltaTime) {
        for (AbstractLayer layer : this.layers) {
            try {
                layer.draw(context, renderer, width, height, deltaTime);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public boolean isAnyLayerEnable() {
        boolean anyLayerEnable = false;
        for (AbstractLayer layer : this.layers) {
            if (layer.isEnable()) {
                anyLayerEnable = true;
            }
        }
        return anyLayerEnable;
    }

    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        this.xOffset = xOffset;
    }

    public boolean onCommand(Context context, String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
        boolean handled = false;
        for (int i = this.layers.length - 1; i >= 0 && !handled; i--) {
            handled = this.layers[i].onCommand(context, action, x, y, z, extras, resultRequested);
        }
        return handled;
    }

    public boolean onSensorEvent(Context context, String sensorType, float x, float y, float z, long timestamp) {
        boolean handled = false;
        for (int i = this.layers.length - 1; i >= 0 && !handled; i--) {
            handled = this.layers[i].onSensorEvent(context, sensorType, x, y, z, timestamp);
        }
        return handled;
    }

    public boolean onNotificationEvent(Context context, Bundle bundle) {
        for (int i = this.layers.length - 1; i >= 0; i--) {
            this.layers[i].onNotificationEvent(context, bundle);
        }
        return true;
    }

    public void onVisibilityChanged(boolean visible) {
        if (this.layers.length > 0) {
            for (AbstractLayer layer : this.layers) {
                if (layer.isEnable()) {
                    layer.onVisibilityChanged(visible);
                }
            }
        }
    }

    public void onSurfaceChanged(float width, float height) {
        for (AbstractLayer layer : this.layers) {
            layer.onSurfaceChanged(width, height);
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (this.layers.length > 0) {
            for (AbstractLayer layer : this.layers) {
                if (layer.isEnable()) {
                    layer.onSurfaceCreated(gl, config);
                }
            }
        }
    }

    public void onInteraction(Context context, JSONObject parameters) {
        for (AbstractLayer layer : this.layers) {
            layer.onInteraction(context, parameters);
        }
    }

    public void recalculateLayerDimensions() {
        for (AbstractLayer recalculateLayerDimensions : getAllLayers()) {
            recalculateLayerDimensions.recalculateLayerDimensions();
        }
    }
}