package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
import android.os.Build.VERSION;

import org.rajawali3d.cameras.Camera;
import org.rajawali3d.math.MathUtil;
import org.rajawali3d.math.Matrix;
import org.rajawali3d.math.vector.Vector3;

public class OffAxisProjectionCamera extends Camera {
    protected float fov;
    protected Vector3 mHeadPos;
    protected float ratio;

    public OffAxisProjectionCamera() {
        this(0.0f, 0.0f, 0.0f);
    }

    public OffAxisProjectionCamera(Vector3 headPosition) {
        this((float)headPosition.x, (float)headPosition.y, (float)headPosition.z);
    }

    public OffAxisProjectionCamera(float x, float y, float z) {
        this.ratio = 0.0f;
        this.fov = 0.0f;
        this.mHeadPos = new Vector3(x, y, z);
    }

    public Vector3 getHeadPosition() {
        return this.mHeadPos;
    }

    public void setHeadPosition(Vector3 headPosX) {
        this.mHeadPos = headPosX;
    }

    public float getHeadPositionX() {
        return (float)this.mHeadPos.x;
    }

    public void setHeadPositionX(float headPosX) {
        this.mHeadPos.x = headPosX;
    }

    public float getHeadPositionY() {
        return (float)this.mHeadPos.y;
    }

    public void setHeadPositionY(float headPosY) {
        this.mHeadPos.y = headPosY;
    }

    public float getHeadPositionZ() {
        return (float)this.mHeadPos.z;
    }

    public void setHeadPositionZ(float headPosZ) {
        this.mHeadPos.z = headPosZ;
    }
}