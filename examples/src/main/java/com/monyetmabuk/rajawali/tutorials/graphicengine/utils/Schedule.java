package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

public class Schedule {
    public static final int AFTER = 0;
    public static final int AT = 1;
    private static final String TAG = Schedule.class.getName();
    private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private int action;
    private String clazz = null;
    private String layer;
    private String notification;
    private JSONObject parameters = null;
    private String state;
    private Task task = null;
    private long timestamp;
    private long when;

    public Schedule(Context context, int action, String state, String when, String layer, String notification, String clazz, JSONObject parameters) throws JSONException {
        setAction(action);
        setState(state);
        Date date = Calendar.getInstance().getTime();
        this.timestamp = date.getTime();
        setWhen(convert(action, when, date));
        setNotification(notification);
        this.layer = layer;
        this.clazz = clazz;
        this.parameters = parameters;
        try {
            Class[] parameterTypes = new Class[AT];
            parameterTypes[AFTER] = Context.class;
            Constructor ct = Class.forName(clazz).getConstructor(parameterTypes);
            Object[] objArr = new Object[AT];
            objArr[AFTER] = context;
            Object object = ct.newInstance(objArr);
            if (object instanceof Task) {
                this.task = (Task) object;
                this.task.initialize(parameters);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            JSONException jSONException = new JSONException(t.getMessage());
        }
    }

    public Schedule(Context context, int action, String state, long timestamp, long when, String layer, String notification, String clazz, JSONObject parameters) throws JSONException {
        this.action = action;
        this.state = state;
        this.timestamp = timestamp;
        this.when = when;
        this.notification = notification;
        this.layer = layer;
        this.clazz = clazz;
        this.parameters = parameters;
        try {
            Class[] parameterTypes = new Class[AT];
            parameterTypes[AFTER] = Context.class;
            Constructor ct = Class.forName(clazz).getConstructor(parameterTypes);
            Object[] objArr = new Object[AT];
            objArr[AFTER] = context;
            Object object = ct.newInstance(objArr);
            if (object instanceof Task) {
                this.task = (Task) object;
                this.task.initialize(parameters);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            JSONException jSONException = new JSONException(t.getMessage());
        }
    }

    public void run(AbstractLayer layer) {
        String overrideState = this.task.run(layer);
    }

    private long convert(int action, String time, Date now) {
        try {
            Date d = timeFormat.parse(time);
            if (action == 0) {
                return (long) (((((d.getHours() * 60) * 60) * 1000) + ((d.getMinutes() * 60) * 1000)) + (d.getSeconds() * 1000));
            }
            if (action != AT) {
                return 0;
            }
            return (long) ((((((d.getHours() - now.getHours()) * 60) * 60) * 1000) + (((d.getMinutes() - now.getMinutes()) * 60) * 1000)) + ((d.getSeconds() - now.getSeconds()) * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
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

    public long getWhen() {
        return this.when;
    }

    public void setWhen(long when) {
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

    public long getTimestamp() {
        return this.timestamp;
    }

    public Date getAbsoluteTime() {
        return new Date(this.timestamp + this.when);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.action).append(" ");
        sb.append(this.timestamp).append(" ");
        sb.append(new Date(this.timestamp).toString()).append(" ");
        sb.append(this.when).append(" ");
        sb.append(new Date(this.timestamp + this.when).toString()).append(" ");
        sb.append(this.notification);
        return sb.toString();
    }

    public String toJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("event", "schedule");
            object.put("action", this.action);
            object.put("timestamp", this.timestamp);
            object.put("when", this.when);
            object.put("layer", this.layer);
            object.put("notification", this.notification == null ? "" : this.notification);
            object.put("clazz", this.clazz);
            object.put("parameters", this.parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public boolean isDangling() {
        if (this.timestamp == -1 || this.when == -1) {
            return false;
        }
        if (this.timestamp + this.when < Calendar.getInstance().getTimeInMillis()) {
            return true;
        }
        return false;
    }

    public static Schedule fromJSON(Context context, JSONObject object) {
        try {
            return new Schedule(context, object.getInt("action"), object.getString("state"), object.getLong("timestamp"), object.getLong("when"), object.getString("layer"), object.optString("notification", ""), object.getString("clazz"), new JSONObject(object.getString("parameters")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getActionId(String action) {
        if (action.equalsIgnoreCase("after")) {
            return AFTER;
        }
        if (action.equalsIgnoreCase("at")) {
            return AT;
        }
        return -1;
    }
}