package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public interface Task {
    void initialize(JSONObject jSONObject) throws JSONException;

    String run(AbstractLayer abstractLayer);

    void terminate();
}