package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public class ShowLayerTask implements Task {
    private Context context = null;
    private String nextState = null;

    public ShowLayerTask(Context context) {
        this.context = context;
    }

    public void initialize(JSONObject object) throws JSONException {
        this.nextState = object.getString("next_state");
    }

    public String run(AbstractLayer layer) {
        layer.setEnable(true);
        layer.setDirty();
        return this.nextState;
    }

    public void terminate() {
        this.context = null;
    }
}