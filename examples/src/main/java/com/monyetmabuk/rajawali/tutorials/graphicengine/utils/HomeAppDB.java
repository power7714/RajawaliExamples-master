package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public class HomeAppDB {
    private static final String CURRENT_SCENARIO_FIELD_ITEMS = " (current, timestamp) ";
    private static final String DB_CREATE_CURRENT_SCENARIO = "CREATE TABLE currentScenario (_id INTEGER PRIMARY KEY AUTOINCREMENT,current INTEGER, timestamp INTEGER);";
    private static final String DB_CREATE_SCENARIO = "CREATE TABLE scenario (_id INTEGER PRIMARY KEY AUTOINCREMENT,application INTEGER, type INTEGER, layer INTEGER, name TEXT, selected INTEGER);";
    private static final String DB_CREATE_SCHEDULE = "CREATE TABLE schedule (_id INTEGER PRIMARY KEY AUTOINCREMENT,action INTEGER, state TEXT, timestamp INTEGER, `when` INTEGER, layer TEXT, notification TEXT, class TEXT, parameters TEXT, executed INTEGER);";
    private static final String DB_CREATE_STATE = "CREATE TABLE state (_id INTEGER PRIMARY KEY AUTOINCREMENT,layer TEXT, state TEXT);";
    private static final String DB_CURRENT_SCENARIO_TABLE = "currentScenario";
    private static final String DB_NAME = "homeapp";
    private static final String DB_SCENARIO_TABLE = "scenario";
    private static final String DB_SCHEDULE_TABLE = "schedule";
    private static final String DB_STATE_TABLE = "state";
    private static final int DB_VERSION = 1;
    private static final String SCENARIO_FIELD_ITEMS = " (application, type, layer, name, selected) ";
    private static final String TAG = HomeAppDB.class.getName();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static long installationDateCached = -1;
    private Context context;
    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, HomeAppDB.DB_NAME, null, HomeAppDB.DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(HomeAppDB.DB_CREATE_SCENARIO);
                db.execSQL(HomeAppDB.DB_CREATE_CURRENT_SCENARIO);
                db.execSQL(HomeAppDB.DB_CREATE_SCHEDULE);
                db.execSQL(HomeAppDB.DB_CREATE_STATE);
                insertDefaultWallpaper(db);
                insertDefaultCurrentScenario(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        public void insertDefaultWallpaper(SQLiteDatabase db) {
            db.execSQL("INSERT INTO scenario (application, type, layer, name, selected)  VALUES (0, 0, 0, \"default\", 1);");
        }

        public void insertDefaultCurrentScenario(SQLiteDatabase db) {
            db.execSQL("INSERT INTO currentScenario (current, timestamp)  VALUES (0, \"" + HomeAppDB.dateFormat.format(Calendar.getInstance().getTime()) + "\");");
        }
    }

    public HomeAppDB(Context context) {
        this.context = context;
    }

    public HomeAppDB open() throws SQLException {
        this.dbHelper = new DatabaseHelper(this.context);
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public void addScenario(int application, int type, int layer, String name, boolean selected) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues addValues = new ContentValues();
        addValues.put("application", Integer.valueOf(application));
        addValues.put("type", Integer.valueOf(type));
        addValues.put("layer", Integer.valueOf(layer));
        addValues.put("title", name);
        addValues.put("selected", Integer.valueOf(bool2Int(selected)));
        db.insert(DB_SCENARIO_TABLE, null, addValues);
        db.close();
    }

    public Cursor getScenario() {
        return this.dbHelper.getWritableDatabase().query(DB_SCENARIO_TABLE, null, null, null, null, null, "type ASC, layer ASC");
    }

    public Cursor getScenario(int application) {
        String[] strArr = new String[DB_VERSION];
        strArr[0] = Integer.toString(application);
        return this.dbHelper.getWritableDatabase().query(DB_SCENARIO_TABLE, null, "application=?", strArr, null, null, "type ASC, layer ASC");
    }

    public Cursor getSelectedScenario(int application) {
        return this.dbHelper.getWritableDatabase().query(DB_SCENARIO_TABLE, null, "application=? AND type!=0 AND selected=?", new String[]{Integer.toString(application), "1"}, null, null, "type ASC, layer ASC");
    }

    public Cursor getSelectedScenario() {
        String[] strArr = new String[DB_VERSION];
        strArr[0] = "1";
        return this.dbHelper.getWritableDatabase().query(DB_SCENARIO_TABLE, null, "type!=0 AND selected=?", strArr, null, null, "type ASC, layer ASC");
    }

    public void deleteScenario(int id) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        String[] strArr = new String[DB_VERSION];
        strArr[0] = Integer.toString(id);
        db.delete(DB_SCENARIO_TABLE, "_id=?", strArr);
        db.close();
    }

    public Cursor getScenarioType(int type) {
        String[] strArr = new String[DB_VERSION];
        strArr[0] = Integer.toString(type);
        return this.dbHelper.getWritableDatabase().query(DB_SCENARIO_TABLE, null, "type=?", strArr, null, null, "layer ASC");
    }

    public int bool2Int(boolean bool) {
        return bool ? DB_VERSION : 0;
    }

    public void setSelected(int id, boolean selected) {
        ContentValues values = new ContentValues();
        values.put("selected", Integer.valueOf(bool2Int(selected)));
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        String[] strArr = new String[DB_VERSION];
        strArr[0] = Long.toString((long) id);
        db.update(DB_SCENARIO_TABLE, values, "_id=?", strArr);
        db.close();
    }

    public void uncheckedAllWallpapers(int application) {
        ContentValues values = new ContentValues();
        values.put("selected", Boolean.valueOf(false));
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        String[] strArr = new String[DB_VERSION];
        strArr[0] = Integer.toString(application);
        int rows = db.update(DB_SCENARIO_TABLE, values, "application=? AND (type=0 OR type=1)", strArr);
        db.close();
    }

    public void setDefaultWallpaper(int application) {
        ContentValues values = new ContentValues();
        values.put("selected", Boolean.valueOf(true));
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        String[] strArr = new String[DB_VERSION];
        strArr[0] = Long.toString((long) application);
        int rows = db.update(DB_SCENARIO_TABLE, values, "application=? AND type=0", strArr);
        db.close();
    }

    public Cursor getCurrentWallpaper(int application) {
        String[] strArr = new String[DB_VERSION];
        strArr[0] = Integer.toString(application);
        return this.dbHelper.getWritableDatabase().query(DB_SCENARIO_TABLE, null, "application=? AND (type=0 OR type=1) AND selected=1", strArr, null, null, null);
    }

    public Cursor getInstallation() {
        return this.dbHelper.getReadableDatabase().query(DB_CURRENT_SCENARIO_TABLE, null, "current=0", null, null, null, null);
    }

    public Cursor getLatestDownloaded() {
        return this.dbHelper.getReadableDatabase().query(DB_CURRENT_SCENARIO_TABLE, null, "current!=0", null, null, null, null);
    }

    public void insertLatestDownloaded(int current) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("current", Integer.valueOf(current));
        values.put("timestamp", Long.valueOf(Calendar.getInstance().getTimeInMillis()));
        Cursor cursor = getLatestDownloaded();
        if (cursor.moveToFirst()) {
            db.update(DB_CURRENT_SCENARIO_TABLE, values, "_id=" + cursor.getString(0), null);
        } else {
            db.insert(DB_CURRENT_SCENARIO_TABLE, null, values);
        }
        cursor.close();
        db.close();
    }

    public static long getInstallationDate(Context context) {
        if (installationDateCached == -1) {
            HomeAppDB db = new HomeAppDB(context);
            db.open();
            Cursor cursor = db.getInstallation();
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                installationDateCached = new CurrentScenarioCursorHelper(cursor).getTimestamp();
                cursor.close();
            }
            db.close();
        }
        return installationDateCached;
    }

    public static long getLatestDownloadedDate(Context context) {
        long downloadedDate = -1;
        HomeAppDB db = new HomeAppDB(context);
        db.open();
        Cursor cursor = db.getLatestDownloaded();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            downloadedDate = new CurrentScenarioCursorHelper(cursor).getTimestamp();
            cursor.close();
        }
        db.close();
        return downloadedDate;
    }

    public static int getLatestDownloadedScenario(Context context) {
        int current = -1;
        HomeAppDB db = new HomeAppDB(context);
        db.open();
        Cursor cursor = db.getLatestDownloaded();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            current = new CurrentScenarioCursorHelper(cursor).getCurrent();
            cursor.close();
        }
        db.close();
        return current;
    }

    public void deleteLatestDownloaded() {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.delete(DB_CURRENT_SCENARIO_TABLE, "current!=0", null);
        db.close();
    }

    public void addSchedule(int action, long timestamp, long when, String layer, String notification, String clazz, String parameters) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues addValues = new ContentValues();
        addValues.put("action", Integer.valueOf(action));
        addValues.put("timestamp", Long.valueOf(timestamp));
        addValues.put("`when`", Long.valueOf(when));
        addValues.put("layer", layer);
        addValues.put("class", clazz);
        addValues.put("parameters", parameters);
        addValues.put("notification", notification);
        addValues.put("executed", Integer.valueOf(bool2Int(false)));
        db.insert(DB_SCHEDULE_TABLE, null, addValues);
        db.close();
    }

    public Cursor getSchedules() {
        return this.dbHelper.getWritableDatabase().query(DB_SCHEDULE_TABLE, null, null, null, null, null, "timestamp ASC, `when` ASC");
    }

    public Cursor getSchedules(int action, long timestamp, long when, String layer) {
        return this.dbHelper.getWritableDatabase().query(DB_SCHEDULE_TABLE, null, "action=? AND timestamp=? AND `when`=? AND layer=?", new String[]{Integer.toString(action), Long.toString(timestamp), Long.toString(when), layer}, null, null, null);
    }

    public void deleteScheduleTable() {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.delete(DB_SCHEDULE_TABLE, null, null);
        db.close();
    }

    public void setScheduleExecuted(int action, long timestamp, long when, String layer) {
        ContentValues values = new ContentValues();
        values.put("executed", Integer.valueOf(bool2Int(true)));
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int rows = db.update(DB_SCHEDULE_TABLE, values, "action=? AND timestamp=? AND `when`=? AND layer=?", new String[]{Integer.toString(action), Long.toString(timestamp), Long.toString(when), layer});
        db.close();
    }

    public void addState(String layer, String state) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues addValues = new ContentValues();
        addValues.put("layer", layer);
        addValues.put(DB_STATE_TABLE, state);
        db.insert(DB_STATE_TABLE, null, addValues);
        db.close();
    }

    public void updateState(int id, String layer, String state) {
        ContentValues values = new ContentValues();
        values.put("layer", layer);
        values.put(DB_STATE_TABLE, state);
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        Cursor cursor = getState(layer);
        if (cursor.moveToFirst()) {
            String[] strArr = new String[DB_VERSION];
            strArr[0] = Integer.toString(id);
            db.update(DB_STATE_TABLE, values, "_id=?", strArr);
        } else {
            db.insert(DB_STATE_TABLE, null, values);
        }
        cursor.close();
        db.close();
    }

    public void updateState(String layer, String state) {
        ContentValues values = new ContentValues();
        values.put("layer", layer);
        values.put(DB_STATE_TABLE, state);
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        Cursor cursor = getState(layer);
        if (cursor.moveToFirst()) {
            String[] strArr = new String[DB_VERSION];
            strArr[0] = Integer.toString(new StateCursorHelper(cursor).getId());
            db.update(DB_STATE_TABLE, values, "_id=?", strArr);
        } else {
            db.insert(DB_STATE_TABLE, null, values);
        }
        cursor.close();
        db.close();
    }

    public static void updateState(Context context, String name, String state) {
        HomeAppDB db = new HomeAppDB(context);
        db.open();
        db.updateState(name, state);
        db.close();
    }

    public void deleteState(String name) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        String[] strArr = new String[DB_VERSION];
        strArr[0] = name;
        int rows = db.delete(DB_STATE_TABLE, "layer=?", strArr);
        db.close();
    }

    public static void deleteState(Context context, String name) {
        HomeAppDB db = new HomeAppDB(context);
        db.open();
        db.deleteState(name);
        db.close();
    }

    public Cursor getStates() {
        return this.dbHelper.getWritableDatabase().query(DB_STATE_TABLE, null, null, null, null, null, null);
    }

    public Cursor getState(String name) {
        String[] strArr = new String[DB_VERSION];
        strArr[0] = name;
        return this.dbHelper.getWritableDatabase().query(DB_STATE_TABLE, null, "layer=?", strArr, null, null, null);
    }

    public static String getState(Context context, String name) {
        String state = null;
        HomeAppDB db = new HomeAppDB(context);
        db.open();
        Cursor cursor = db.getState(name);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            state = new StateCursorHelper(cursor).getState();
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return state;
    }

    public static String statesToString(Context context) {
        StringBuffer sb = new StringBuffer();
        HomeAppDB db = new HomeAppDB(context);
        db.open();
        Cursor cursor = db.getStates();
        if (cursor.getCount() > 0) {
            sb.append("HomeAppDB ").append("----------------------------").append("\n");
        }
        for (boolean exists = cursor.moveToFirst(); exists; exists = cursor.moveToNext()) {
            StateCursorHelper helper = new StateCursorHelper(cursor);
            sb.append("HomeAppDB ").append("layer : ").append(helper.getLayer()).append(", ");
            sb.append("state : ").append(helper.getState()).append("\n");
        }
        cursor.close();
        db.close();
        return sb.toString();
    }
}