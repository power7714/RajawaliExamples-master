package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

public class StateStrategy {
    private HomeAppDB db = null;
    private AbstractLayer layer = null;
    private JSONObject parameters = null;

    public StateStrategy(AbstractLayer layer, JSONObject parameters) {
        this.layer = layer;
        this.parameters = parameters;
    }

    public State run() {
        State returnValue;
        Exception e;
        Throwable th;
        State returnValue2 = null;
        try {
            this.db = new HomeAppDB(this.layer.getContext());
            this.db.open();
            ArrayList<Interaction> interactions = this.layer.getInteractions();
            Cursor cursor = this.db.getState(this.layer.getName());
            Iterator it;
            Interaction interaction;
            String overrideState;
            String state;
            if (cursor.getCount() == 0) {
                it = interactions.iterator();
                returnValue = null;
                while (it.hasNext()) {
                    try {
                        interaction = (Interaction) it.next();
                        if (interaction.getState().equalsIgnoreCase("initial") && this.parameters.optString("event", "").equalsIgnoreCase(interaction.getEvent())) {
                            overrideState = interaction.run(this.layer);
                            state = overrideState != null ? overrideState : interaction.getNextState();
                            this.db.updateState(this.layer.getName(), state);
                            returnValue = new State(interaction.getState(), interaction.getEvent(), state, this.layer.getName());
                        }
                    } catch (Exception e2) {
                        e = e2;
                        returnValue2 = returnValue;
                    } catch (Throwable th2) {
                        th = th2;
                        returnValue2 = returnValue;
                    }
                }
                returnValue2 = returnValue;
            } else {
                cursor.moveToFirst();
                StateCursorHelper helper = new StateCursorHelper(cursor);
                it = interactions.iterator();
                returnValue = null;
                while (it.hasNext()) {
                    interaction = (Interaction) it.next();
                    if (interaction.getState().equalsIgnoreCase(helper.getState()) && this.parameters.optString("event", "").equalsIgnoreCase(interaction.getEvent())) {
                        overrideState = interaction.run(this.layer);
                        state = overrideState != null ? overrideState : interaction.getNextState();
                        this.db.updateState(this.layer.getName(), state);
                        returnValue = new State(interaction.getState(), interaction.getEvent(), state, this.layer.getName());
                    }
                }
                returnValue2 = returnValue;
            }
            this.db.close();
        } catch (Exception e3) {
            e = e3;
            try {
                e.printStackTrace();
                this.db.close();
                return returnValue2;
            } catch (Throwable th3) {
                //th = th3;
                this.db.close();
                throw th3;
            }
        }
        return returnValue2;
    }

    private void notify(AbstractLayer later, String state) {
        if (state != null) {
            try {
                Intent intent = new Intent("event");
                JSONObject object = new JSONObject();
                object.put("event", "state");
                object.put("layer", this.layer.getName());
                object.put("state", state);
                intent.putExtra("parameters", object.toString());
                LocalBroadcastManager.getInstance(this.layer.getContext()).sendBroadcast(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyScheduler(Context context, String state) {
        if (state != null) {
            try {
                Intent intent = new Intent("event");
                JSONObject object = new JSONObject();
                object.put("event", "schedule");
                object.put("state", state);
                intent.putExtra("parameters", object.toString());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}