package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

import android.content.Context;
import android.os.Bundle;

import com.monyetmabuk.rajawali.tutorials.graphicengine.GraphicEngine;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.Action;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.ConfigurationSchedule;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.Interaction;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.NotificationEvent;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.Schedule;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.SensorEvent;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.State;

import java.util.ArrayList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONException;
import org.json.JSONObject;
import org.rajawali3d.renderer.Renderer;

public abstract class AbstractLayer {
    private static final String TAG = AbstractLayer.class.getName();
    private ArrayList<Action> actions;
    private Context context;
    private boolean enable;
    private String engine;
    private GraphicEngine graphicEngine;
    private ArrayList<Interaction> interactions;
    private String name;
    private ArrayList<NotificationEvent> notificationEvents;
    private ArrayList<Schedule> schedules;
    private ArrayList<SensorEvent> sensorEvents;

    public abstract void destroyScene(Context context, Renderer gLGenericRenderer);

    public abstract State draw(Context context, Renderer gLGenericRenderer, int i, int i2, float f);

    public abstract void initScene(Context context, Renderer gLGenericRenderer);

    public abstract boolean onCommand(Context context, String str, int i, int i2, int i3, Bundle bundle, boolean z);

    public abstract State onInteraction(Context context, JSONObject jSONObject);

    public abstract boolean onNotificationEvent(Context context, Bundle bundle);

    public abstract void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2);

    public abstract boolean onSensorEvent(Context context, String str, float f, float f2, float f3, long j);

    public abstract void onSurfaceChanged(float f, float f2);

    public abstract void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);

    public abstract void onVisibilityChanged(boolean z);

    public AbstractLayer(String name, String type) {
        this.name = name;
        this.engine = type;
        this.enable = true;
        this.graphicEngine = null;
        this.actions = new ArrayList();
        this.sensorEvents = new ArrayList();
        this.notificationEvents = new ArrayList();
        this.schedules = new ArrayList();
        this.interactions = new ArrayList();
    }

    public AbstractLayer(Context context, String name, String type, boolean enable) {
        setContext(context);
        this.name = name;
        this.engine = type;
        this.enable = enable;
        this.graphicEngine = null;
        this.actions = new ArrayList();
        this.sensorEvents = new ArrayList();
        this.notificationEvents = new ArrayList();
        this.schedules = new ArrayList();
        this.interactions = new ArrayList();
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngine() {
        return this.engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        if (this.graphicEngine != null) {
            this.graphicEngine.setEnable(enable);
        }
    }

    public ArrayList<Action> getActions() {
        return this.actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public GraphicEngine getGraphicEngine() {
        return this.graphicEngine;
    }

    public void setGraphicEngine(GraphicEngine engine) {
        this.graphicEngine = engine;
    }

    public ArrayList<SensorEvent> getSensorEvent() {
        return this.sensorEvents;
    }

    public void setSensorEvent(ArrayList<SensorEvent> sensorEvents) {
        this.sensorEvents = sensorEvents;
    }

    public void addSensorEvent(SensorEvent sensorEvent) {
        this.sensorEvents.add(sensorEvent);
    }

    public ArrayList<NotificationEvent> getNotificationEvent() {
        return this.notificationEvents;
    }

    public void setNotificationEvent(ArrayList<NotificationEvent> notificationEvents) {
        this.notificationEvents = notificationEvents;
    }

    public void addNotificationEvent(NotificationEvent notificationEvent) {
        this.notificationEvents.add(notificationEvent);
    }

    public ArrayList<Interaction> getInteractions() {
        return this.interactions;
    }

    public void setInteractions(ArrayList<Interaction> interactions) {
        this.interactions = interactions;
    }

    public void addInteraction(Interaction interaction) {
        this.interactions.add(interaction);
    }

    public void setDirty() {
        if (this.graphicEngine != null) {
            this.graphicEngine.setDirty(true);
        }
    }

    public void recalculateLayerDimensions() {
        if (this.graphicEngine != null) {
            this.graphicEngine.recalculateDimensions();
        }
    }

    public ArrayList<Schedule> getSchedules() {
        return this.schedules;
    }

    public ArrayList<Schedule> getAllSchedules() {
        return getSchedules();
    }

    public void setSchedules(ArrayList<ConfigurationSchedule> confSchedules) {
        this.schedules = new ArrayList();
        for (int i = 0; i < confSchedules.size(); i++) {
            ConfigurationSchedule s = (ConfigurationSchedule) confSchedules.get(i);
            try {
                this.schedules.add(new Schedule(this.context, s.getAction(), s.getState(), s.getWhen(), s.getLayer(), s.getNotification(), s.getClazz(), s.getParameters()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}