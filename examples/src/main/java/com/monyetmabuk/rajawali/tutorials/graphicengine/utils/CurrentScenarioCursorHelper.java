package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.database.Cursor;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public class CurrentScenarioCursorHelper {
    private Cursor cursor = null;

    public CurrentScenarioCursorHelper(Cursor cursor) {
        this.cursor = cursor;
    }

    public int getId() {
        return this.cursor.getInt(this.cursor.getColumnIndex("_id"));
    }

    public int getCurrent() {
        return this.cursor.getInt(this.cursor.getColumnIndex("current"));
    }

    public long getTimestamp() {
        return this.cursor.getLong(this.cursor.getColumnIndex("timestamp"));
    }
}