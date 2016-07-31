package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import org.rajawali3d.cameras.Camera;
import org.rajawali3d.math.MathUtil;

/**
 * Created by CurTro Studios on 7/28/2016.
 */
public class SceneDimensions {
    private int camHeight;
    private int camWidth;
    private float cameraMovement;
    private Context context;
    private float depth;
    private float fielOfView;
    private DisplayMetrics metrics;
    private float ratio = 1.0f;
    private float sceneHeight;
    private float sceneScreenRatio;
    private float sceneWidth;
    protected boolean useRatio = false;

    public SceneDimensions(Context context) {
        this.context = context;
        this.ratio = 1.0f;
        this.metrics = context.getResources().getDisplayMetrics();
        this.camWidth = this.metrics.widthPixels;
        this.camHeight = this.metrics.heightPixels;
        this.sceneHeight = (float) this.camHeight;
        this.sceneWidth = this.sceneHeight * this.ratio;
        this.sceneScreenRatio = this.sceneHeight / ((float) this.camHeight);
    }

    public SceneDimensions(Context context, float fov, float z, float ratio) {
        this.depth = z;
        this.fielOfView = fov;
        this.context = context;
        this.metrics = context.getResources().getDisplayMetrics();
        float screenRatio = ((float) this.metrics.heightPixels) / ((float) this.metrics.widthPixels);
        this.sceneHeight = (((float) Math.tan((double) (((this.fielOfView / 2.0f) * MathUtil.PI) / 180.0f))) * this.depth) * 2.0f;
        this.sceneWidth = this.sceneHeight * ratio;
        this.cameraMovement = this.sceneWidth - (((float) Math.tan((double) (((this.fielOfView / screenRatio) * MathUtil.PI) / 180.0f))) * this.depth);
        this.sceneScreenRatio = this.sceneHeight / ((float) this.metrics.heightPixels);
    }

    public float screenToSceneTouchX(Camera camera, float x) {
        float screenWidth = (float) this.metrics.widthPixels;
        float cameraX = (float)camera.getX();
        if (screenWidth / 2.0f >= x) {
            x = (screenWidth / 2.0f) - x;
            System.out.println("screenToSceneTouchX:" + String.valueOf(x));
            return cameraX - (this.sceneScreenRatio * x);
        }
        return (this.sceneScreenRatio * (x - (screenWidth / 2.0f))) + cameraX;
    }

    public float screenToSceneTouchY(Camera camera, float y) {
        float screenHeight = (float) this.metrics.heightPixels;
        float cameraY = (float)camera.getY();
        if (screenHeight / 2.0f >= y) {
            return cameraY - (this.sceneScreenRatio * (y - (screenHeight / 2.0f)));
        }
        return (this.sceneScreenRatio * ((screenHeight / 2.0f) - y)) + cameraY;
    }

    public void setDefaultRatio() {
        this.ratio = this.sceneHeight / this.sceneHeight;
    }

    public void setToScreenRatio() {
        float screenWidth;
        float screenHeight;
        DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
        if (metrics.widthPixels > metrics.heightPixels) {
            screenWidth = (float) metrics.heightPixels;
            screenHeight = (float) metrics.widthPixels;
        } else {
            screenWidth = (float) metrics.widthPixels;
            screenHeight = (float) metrics.heightPixels;
        }
        this.ratio = screenWidth / screenHeight;
        this.sceneHeight = (((float) Math.tan((double) (((this.fielOfView / 2.0f) * MathUtil.PI) / 180.0f))) * this.depth) * 2.0f;
        this.sceneWidth = this.sceneHeight * this.ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        this.sceneWidth = this.sceneHeight * ratio;
        this.sceneHeight = (((float) Math.tan((double) (((this.fielOfView / 2.0f) * MathUtil.PI) / 180.0f))) * this.depth) * 2.0f;
        this.sceneWidth = this.sceneHeight * ratio;
    }

    public float getRatio() {
        return this.ratio;
    }

    public float getSceneWidth() {
        return this.sceneWidth;
    }

    public float getSceneHeight() {
        return this.sceneHeight;
    }

    public int getCameraWidth() {
        return this.camWidth;
    }

    public int getCameraHeight() {
        return this.camHeight;
    }

    public float getCameraMovement() {
        return this.cameraMovement;
    }

    public float getWidthFromPercent(float widthPercent) {
        if (!this.useRatio || this.metrics.widthPixels <= this.metrics.heightPixels) {
            return (this.sceneWidth * widthPercent) / 100.0f;
        }
        return (((((float) this.metrics.widthPixels) * widthPercent) / ((float) this.metrics.heightPixels)) * this.sceneWidth) / 100.0f;
    }

    public float getHeightFromPercent(float heightPercent) {
        if (!this.useRatio || this.metrics.widthPixels <= this.metrics.heightPixels) {
            return (this.sceneHeight * heightPercent) / 100.0f;
        }
        return (((((float) this.metrics.widthPixels) * heightPercent) / ((float) this.metrics.heightPixels)) * this.sceneHeight) / 100.0f;
    }

    public float getXPositionFromPercent(float xPercent) {
        if (!this.useRatio || this.metrics.widthPixels <= this.metrics.heightPixels) {
            return (this.sceneWidth * xPercent) / 100.0f;
        }
        return (((((float) this.metrics.widthPixels) * xPercent) / ((float) this.metrics.heightPixels)) * this.sceneWidth) / 100.0f;
    }

    public float getYPositionFromPercent(float yPercent) {
        if (!this.useRatio || this.metrics.widthPixels <= this.metrics.heightPixels) {
            return (this.sceneHeight * yPercent) / 100.0f;
        }
        return (((((float) this.metrics.widthPixels) * yPercent) / ((float) this.metrics.heightPixels)) * this.sceneHeight) / 100.0f;
    }

    public float getWidthFromCameraPercent(float widthPercent) {
        if (!this.useRatio || this.metrics.widthPixels <= this.metrics.heightPixels) {
            return (((float) this.camWidth) * widthPercent) / 100.0f;
        }
        return (((((float) this.camHeight) * widthPercent) / ((float) this.camWidth)) * ((float) this.camWidth)) / 100.0f;
    }

    public float getHeightFromCameraPercent(float heightPercent) {
        if (!this.useRatio || this.metrics.widthPixels <= this.metrics.heightPixels) {
            return (((float) this.camHeight) * heightPercent) / 100.0f;
        }
        return (((((float) this.camHeight) * heightPercent) / ((float) this.camWidth)) * ((float) this.camHeight)) / 100.0f;
    }

    public float getXPositionCameraFromPercent(float xPercent) {
        if (!this.useRatio || this.metrics.widthPixels <= this.metrics.heightPixels) {
            return (((float) this.camWidth) * xPercent) / 100.0f;
        }
        return (((((float) this.camWidth) * xPercent) / ((float) this.camHeight)) * ((float) this.camWidth)) / 100.0f;
    }

    public float getYPositionFromCameraPercent(float yPercent) {
        if (!this.useRatio || this.metrics.widthPixels <= this.metrics.heightPixels) {
            return (((float) this.camHeight) * yPercent) / 100.0f;
        }
        return (((((float) this.camWidth) * yPercent) / ((float) this.camHeight)) * ((float) this.camHeight)) / 100.0f;
    }

    public boolean isUseScreenRatio() {
        return this.useRatio;
    }

    public void setUseScreenRatio(boolean useScreenRatio) {
        this.useRatio = useScreenRatio;
    }
}