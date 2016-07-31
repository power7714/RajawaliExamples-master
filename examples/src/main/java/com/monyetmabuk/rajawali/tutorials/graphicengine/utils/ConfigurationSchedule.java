package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

public class ConfigurationSchedule {
    public static final int AFTER = 0;
    public static final int AT = 1;
    private int action;
    private String clazz = null;
    private String layer;
    private String notification;
    private JSONObject parameters = null;
    private String state;
    private String when;

    public ConfigurationSchedule(Context context, JSONObject object) throws JSONException {
        String time = object.optString("after", "");
        if (time.trim().length() == 0) {
            time = object.optString("at", "");
            if (time.trim().length() == 0) {
                throw new JSONException("Scheduler needs 'after' or 'at' attribute.");
            }
            setAction(AT);
            setWhen(time);
        } else {
            setAction(AFTER);
            setWhen(time);
        }
        setState(object.optString("state", ""));
        setLayer(object.getString("layer"));
        setNotification(object.optString("notification", ""));
        setClazz(object.getString("class"));
        JSONObject o = object.optJSONObject("parameters");
        if (o != null) {
            setParameters(o);
        } else {
            setParameters(new JSONObject());
        }
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWhen() {
        return this.when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getLayer() {
        return this.layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getNotification() {
        return this.notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public JSONObject getParameters() {
        return this.parameters;
    }

    public void setParameters(JSONObject parameters) {
        this.parameters = parameters;
    }
}