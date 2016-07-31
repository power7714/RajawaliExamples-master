package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

public class Interaction {
    private String clazz = null;
    private Context context = null;
    private String event = null;
    private String layer = null;
    private String nextState = null;
    private JSONObject parameters = null;
    private String state = null;
    private Task task = null;

    public Interaction(Context context, String layer, String state, String event, String nextState, String clazz, JSONObject parameters) throws JSONException {
        this.context = context;
        this.layer = layer;
        this.state = state;
        this.event = event;
        this.nextState = nextState;
        this.clazz = clazz;
        this.parameters = parameters;
        try {
            Object object = Class.forName(clazz).getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            if (object instanceof Task) {
                this.task = (Task) object;
                parameters.put("state", state);
                parameters.put("event", event);
                parameters.put("next_state", nextState);
                parameters.put("layer", layer);
                this.task.initialize(parameters);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            JSONException jSONException = new JSONException(t.getMessage());
        }
    }

    public String run(AbstractLayer layer) {
        return this.task.run(layer);
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getLayer() {
        return this.layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getNextState() {
        return this.nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
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