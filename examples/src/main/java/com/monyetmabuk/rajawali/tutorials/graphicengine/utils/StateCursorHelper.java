package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public class StateCursorHelper {
    private Cursor cursor = null;

    public StateCursorHelper(Cursor cursor) {
        this.cursor = cursor;
    }

    public int getId() {
        return this.cursor.getInt(this.cursor.getColumnIndex("_id"));
    }

    public String getLayer() {
        return this.cursor.getString(this.cursor.getColumnIndex("layer"));
    }

    public String getState() {
        return this.cursor.getString(this.cursor.getColumnIndex("state"));
    }

    public String getStateAsJSON() {
        try {
            return new JSONObject(getState()).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}