package com.monyetmabuk.rajawali.tutorials.graphicengine;

import android.content.Context;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.SimplePlane;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.SceneDimensions;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.State;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.ATexture.WrapType;
import org.rajawali3d.renderer.Renderer;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by CurTro Studios on 7/28/2016.
 */

public class GLPlaneGraphicEngine extends GraphicEngine {
    protected int mBitmapWidth = 0;
    protected float mHeight = 0.0f;
    protected SimplePlane mPlane = null;
    protected float mRatio = 0.0f;
    protected ArrayList<ATexture> mTextureInfo = null;
    protected int mTiling = 0;
    protected float mWidth = 0.0f;
    protected WrapType mWrapType = WrapType.CLAMP;
    protected boolean mbIs2D = false;

    public GLPlaneGraphicEngine(Context context, String name) {
        super(context, name);
    }

    public void initScene(Renderer renderer) {
        this.mTextureInfo = new ArrayList();
        if (this.mbIsFirstStart || !this.mbShouldPreservePreviousStatesDuringInit) {
            this.mWidth = (float) getParameters().optInt("width", 100);
            this.mHeight = (float) getParameters().optInt("height", 100);
            this.mTiling = getParameters().optInt("tiling", 0);
            this.mRatio = (float) getParameters().optInt("ratio", 2);
            this.mbIs2D = getParameters().optBoolean("is2d", false);
            this.mWrapType = getParameters().optString("wrapType", "clamp").equalsIgnoreCase("clamp") ? WrapType.CLAMP : WrapType.REPEAT;
            createPlaneWithSceneDimensions(this.mWidth, this.mHeight, this.mTiling, this.mRatio);
            this.mPlane.setName(getName());
            this.mPlane.setTransparent(true);
            this.mPlane.setRotation(getInitialRotX(), getInitialRotY(), getInitialRotZ());
            this.mPlane.setScale(getInitialScaleX(), getInitialScaleY(), getInitialScaleZ());
        } else {
            createPlaneWithSceneDimensions(this.mWidth, this.mHeight, this.mTiling, this.mRatio);
            this.mPlane.setName(getName());
            this.mPlane.setTransparent(true);
            this.mPlane.setRotation(this.mLastRotation);
            this.mPlane.setScale(this.mLastScale);
            this.mPlane.setPosition(this.mLastPosition);
        }
        super.initScene(renderer);
    }

    protected void createPlaneWithSceneDimensions(float width, float height, int tiling, float ratio) {
        SceneDimensions dimensions;
        setCurrentTexture(0);
        this.mBitmapWidth = getCurrentTexture().getBitmap(0).getBitmap().getWidth();
        if (this.mbIs2D) {
            dimensions = new SceneDimensions(getContext());
        } else {
            dimensions = new SceneDimensions(getContext(), 60.0f, (float)getInitialPosZ(), ratio);
        }
        float planeWidth = dimensions.getWidthFromPercent(width);
        float planeHeight = dimensions.getHeightFromPercent(height);
        int nbTiles = 1;
        if (tiling == 1) {
            nbTiles = (int) Math.ceil((double) (planeWidth / ((float) this.mBitmapWidth)));
        }
        this.mPlane = new SimplePlane((float) ((int) planeWidth), (float) ((int) planeHeight), 1, nbTiles, 1);
        this.mPlane.setX(dimensions.getXPositionFromPercent((float)getInitialPosX()));
        this.mPlane.setY(dimensions.getYPositionFromPercent((float)getInitialPosY()));
        this.mPlane.setZ(getInitialPosZ());
    }

    public void recalculateDimensions() {
        if (this.mPlane != null) {
            SceneDimensions dimensions;
            if (this.mbIs2D) {
                dimensions = new SceneDimensions(getContext());
            } else {
                dimensions = new SceneDimensions(getContext(), 60.0f, (float)getInitialPosZ(), this.mRatio);
            }
            float planeWidth = dimensions.getWidthFromPercent(this.mWidth);
            float planeHeight = dimensions.getHeightFromPercent(this.mHeight);
            int nbTiles = 1;
            if (this.mTiling == 1) {
                nbTiles = (int) Math.ceil((double) (planeWidth / ((float) this.mBitmapWidth)));
            }
            this.mPlane.recalculatePlane((float) ((int) planeWidth), (float) ((int) planeHeight), 1, nbTiles, 1);
            this.mPlane.setX(dimensions.getXPositionFromPercent((float)getInitialPosX()));
            this.mPlane.setY(dimensions.getYPositionFromPercent((float)getInitialPosY()));
            this.mPlane.setZ(getInitialPosZ());
            this.mPlane.setRotation(getInitialRotX(), getInitialRotY(), getInitialRotZ());
            this.mPlane.setScale(getInitialScaleX(), getInitialScaleY(), getInitialScaleZ());
            applyTintToObject();
        }
    }

    public Object3D getBaseObject3D() {
        return this.mPlane;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (isEnable() && this.initialized) {
            this.mbHasSceneBeenFlushed = true;
            updateTexture(getContext(), this.mRenderer);
            this.mPlane.getGeometry().reload();
            applyTintToObject();
            if (!(this.mbIsHidden || this.mRenderer.getCurrentScene().getChildrenCopy().contains(getBaseObject3D()))) {
                addChild(this.mRenderer, this.mPlane);
            }
            if (isSelectable()) {
                //this.mRenderer.get
            }
            this.mbHasSceneBeenFlushed = false;
        }
    }

    public void destroyScene(Renderer renderer) {
        terminate(getContext(), renderer, this.mPlane);
    }

    public void updateTexture(Context context, Renderer renderer) {
        setTexture(context, this.mPlane, renderer);
    }

    public State draw(Renderer renderer, float deltaTime) {
        if (isDirty()) {
            updateTexture(getContext(), renderer);
            setDirty(false);
        }
        return null;
    }

    public void flipTextureCoords(boolean shouldFlipX, boolean shouldFlipY) {
        if (this.mPlane == null) {
            return;
        }
        if (shouldFlipX || shouldFlipY) {
            FloatBuffer oldBuffer = this.mPlane.getGeometry().getTextureCoords();
            float[] buffer = new float[oldBuffer.capacity()];
            int size = buffer.length;
            int i = 0;
            while (i < size) {
                if (i % 2 == 0) {
                    buffer[i] = shouldFlipX ? oldBuffer.get(i) * -1.0f : oldBuffer.get(i);
                } else {
                    buffer[i] = shouldFlipY ? oldBuffer.get(i) * -1.0f : oldBuffer.get(i);
                }
                i++;
            }
            this.mPlane.getGeometry().setTextureCoords(buffer);
            this.mPlane.getGeometry().reload();
        }
    }

    public float getPlaneWidth() {
        return this.mPlane.getPlaneWidth();
    }

    public float getPlaneHeight() {
        return this.mPlane.getPlaneHeight();
    }
}