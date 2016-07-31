package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationCamera {
    private float farPlane;
    private float fov;
    private float height;
    private float lookAtX;
    private float lookAtY;
    private float lookAtZ;
    private String mode;
    private float nearPlane;
    private float posX;
    private float posY;
    private float posZ;
    private float width;

    public ConfigurationCamera(Context context, JSONObject object) throws JSONException {
        this.mode = object.getString("mode");
        if (this.mode.equalsIgnoreCase("ortho")) {
            this.width = (float) object.getDouble("width");
            this.height = (float) object.getDouble("height");
        } else if (this.mode.equalsIgnoreCase("perspective")) {
            this.fov = (float) object.getDouble("fov");
            this.nearPlane = (float) object.getDouble("near_plane");
            this.farPlane = (float) object.getDouble("far_plane");
            this.posZ = (float) object.getDouble("pos_z");
            this.posY = (float) object.optDouble("pos_y", 0.0d);
            this.posX = (float) object.optDouble("pos_x", 0.0d);
            this.lookAtZ = (float) object.optDouble("lookat_z", 0.0d);
            this.lookAtY = (float) object.optDouble("lookat_y", 0.0d);
            this.lookAtX = (float) object.optDouble("lookat_x", 0.0d);
        } else if (this.mode.equalsIgnoreCase("offaxis_projection")) {
            this.fov = (float) object.getDouble("fov");
            this.nearPlane = (float) object.getDouble("near_plane");
            this.farPlane = (float) object.getDouble("far_plane");
            this.posZ = (float) object.getDouble("pos_z");
            this.posY = (float) object.optDouble("pos_y", 0.0d);
            this.posX = (float) object.optDouble("pos_x", 0.0d);
            this.lookAtZ = (float) object.optDouble("lookat_z", 0.0d);
            this.lookAtY = (float) object.optDouble("lookat_y", 0.0d);
            this.lookAtX = (float) object.optDouble("lookat_x", 0.0d);
        } else {
            throw new JSONException("Camera mode is either 'ortho' or 'perspective' or 'offaxis_projection'");
        }
    }

    public float getFov() {
        return this.fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public float getNearPlane() {
        return this.nearPlane;
    }

    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
    }

    public float getFarPlane() {
        return this.farPlane;
    }

    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
    }

    public float getPosZ() {
        return this.posZ;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }

    public float getPosY() {
        return this.posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosX() {
        return this.posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getLookAtZ() {
        return this.lookAtZ;
    }

    public void setLookAtZ(float lookAtZ) {
        this.lookAtZ = lookAtZ;
    }

    public float getLookAtY() {
        return this.lookAtY;
    }

    public void setLookAtY(float lookAtY) {
        this.lookAtY = lookAtY;
    }

    public float getLookAtX() {
        return this.lookAtX;
    }

    public void setLookAtX(float lookAtX) {
        this.lookAtX = lookAtX;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}