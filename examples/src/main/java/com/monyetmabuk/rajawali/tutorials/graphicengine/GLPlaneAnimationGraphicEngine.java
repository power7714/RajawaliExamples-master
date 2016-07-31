package com.monyetmabuk.rajawali.tutorials.graphicengine;

import android.content.Context;

import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.State;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.ATexture.FilterType;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.util.OnObjectPickedListener;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by CurTro Studios on 7/28/2016.
 */

public class GLPlaneAnimationGraphicEngine extends GLPlaneGraphicEngine {
    protected float mAnimationRate = 0.25f;
    protected CACHE_TYPE mCacheType = CACHE_TYPE.LAZY_LOAD;
    protected int mCurrentTextureIndex = 0;
    protected float mElapsedTime = 0.0f;
    protected int mLastTextureIndex = 0;
    protected int mNumExecutions = -1;
    protected State mState = null;
    protected boolean mbIsLoop = true;
    protected boolean mbIsPaused = false;
    protected boolean mbIsReverseEnabled = false;
    protected boolean mbIsReversing = false;

    protected enum CACHE_TYPE {
        LAZY_LOAD,
        PRE_CACHE,
        NO_CACHE
    }

    public GLPlaneAnimationGraphicEngine(Context context, String name) {
        super(context, name);
    }

    public void initScene(Renderer renderer) {
        if (this.mbIsFirstStart || !this.mbShouldPreservePreviousStatesDuringInit) {
            this.mbIsLoop = getParameters().optBoolean("isLoop", true);
            this.mAnimationRate = (float) getParameters().optDouble("animationRate", 0.25d);
            this.mNumExecutions = getParameters().optInt("nbExecutions", 1);
            this.mState = new State(getParameters().optString("state", ""), getParameters().optString("event", ""), getParameters().optString("next_state", ""), getParameters().optString("layer", ""));
            String cacheTypeCode = getParameters().optString("cacheType", "lazyLoad");
            if (cacheTypeCode.equalsIgnoreCase("lazyload")) {
                this.mCacheType = CACHE_TYPE.LAZY_LOAD;
            } else if (cacheTypeCode.equalsIgnoreCase("precache")) {
                this.mCacheType = CACHE_TYPE.PRE_CACHE;
            } else if (cacheTypeCode.equalsIgnoreCase("nocache")) {
                this.mCacheType = CACHE_TYPE.NO_CACHE;
            }
        }
        setDirty(true);
        super.initScene(renderer);
    }

    public void updateTexture(Context context, Renderer renderer) {
        if (this.mCacheType == CACHE_TYPE.LAZY_LOAD) {
            if (((ATexture) this.mTextureInfo.get(this.mCurrentTextureIndex)).getTextureId() < 0) {
                //this.mTextureInfo.set(this.mCurrentTextureIndex, renderer.getTextureManager().addTexture(getCurrentTexture().getBitmap(this.mCurrentTextureIndex).get(getContext()), TextureType.DIFFUSE, false, true, this.mWrapType, FilterType.LINEAR));
                ((ATexture) this.mTextureInfo.get(this.mCurrentTextureIndex)).setFilterType(FilterType.LINEAR);
                ((ATexture) this.mTextureInfo.get(this.mCurrentTextureIndex)).setWrapType(this.mWrapType);
            }
            this.mPlane.getMaterial().getTextureList().clear();
            this.mPlane.getMaterial().getTextureList().add((ATexture) this.mTextureInfo.get(this.mCurrentTextureIndex));
        } else if (this.mCacheType == CACHE_TYPE.PRE_CACHE) {
            this.mPlane.getMaterial().getTextureList().clear();
            this.mPlane.getMaterial().getTextureList().add((ATexture) this.mTextureInfo.get(this.mCurrentTextureIndex));
        } else if (this.mCacheType == CACHE_TYPE.NO_CACHE) {
            setTexture(context, this.mPlane, renderer);
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (isEnable() && this.initialized) {
            this.mbHasSceneBeenFlushed = true;
            loadTextures();
            if (this.mPlane.getMaterial() == null) {
                this.mPlane.setMaterial(getMaterial());
            }
            //this.mPlane.getMaterial().setShaders();
            if (this.mbIsAlphaTintEnabled) {
                this.mPlane.getMaterial().useVertexColors(true);
            }
            updateTexture(getContext(), this.mRenderer);
            this.mPlane.getGeometry().reload();
            applyTintToObject();
            if (!(this.mbIsHidden || this.mRenderer.getCurrentScene().getChildrenCopy().contains(getBaseObject3D()))) {
                addChild(this.mRenderer, this.mPlane);
            }
            this.mbHasSceneBeenFlushed = false;
        }
    }

    protected void loadTextures() {
        int numTextures;
        int i;
        if (this.mCacheType == CACHE_TYPE.LAZY_LOAD) {
            this.mTextureInfo.clear();
            numTextures = getCurrentTexture().size();
            for (i = 0; i < numTextures; i++) {
                //this.mTextureInfo.add(new ATexture());
            }
        } else if (this.mCacheType == CACHE_TYPE.PRE_CACHE) {
            this.mTextureInfo.clear();
            numTextures = getCurrentTexture().size();
            for (i = 0; i < numTextures; i++) {
                //this.mTextureInfo.add(this.mRenderer.getTextureManager().addTexture(getCurrentTexture().getBitmap(i).get(getContext()), TextureType.DIFFUSE, false, true, this.mWrapType, FilterType.LINEAR));
                ((Texture) this.mTextureInfo.get(i)).setFilterType(FilterType.LINEAR);
                ((Texture) this.mTextureInfo.get(i)).setWrapType(this.mWrapType);
            }
        } else {
            CACHE_TYPE cache_type = CACHE_TYPE.NO_CACHE;
        }
    }

    public void setTexture(Context context, Object3D object3D, Renderer renderer) {
        if (!(this.mbHasSceneBeenFlushed || this.mTextureInfo.isEmpty() || ((Texture) this.mTextureInfo.get(0)).getTextureId() < 0)) {
            renderer.getTextureManager().removeTexture((Texture) this.mTextureInfo.get(0));
        }
        this.mTextureInfo.clear();
        //this.mTextureInfo.add(renderer.getTextureManager().addTexture(getCurrentTexture().getBitmap(this.mCurrentTextureIndex).get(getContext()), TextureType.DIFFUSE, false, true, this.mWrapType, FilterType.LINEAR));
        ((Texture) this.mTextureInfo.get(0)).setFilterType(FilterType.LINEAR);
        ((Texture) this.mTextureInfo.get(0)).setWrapType(this.mWrapType);
        this.mPlane.getMaterial().getTextureList().clear();
        //this.mPlane.getMaterial().addTexture((ATexture) this.mTextureInfo.get(0));
    }

    public State draw(Renderer renderer, float deltaTime) {
        boolean z = true;
        if (isDirty() && !this.mbIsPaused) {
            this.mElapsedTime += deltaTime;
            if (this.mElapsedTime >= this.mAnimationRate) {
                this.mElapsedTime = 0.0f;
                this.mLastTextureIndex = this.mCurrentTextureIndex;
                if (this.mbIsReverseEnabled) {
                    this.mCurrentTextureIndex = (this.mbIsReversing ? -1 : 1) + this.mCurrentTextureIndex;
                } else {
                    this.mCurrentTextureIndex++;
                }
                if (this.mCurrentTextureIndex >= getCurrentTexture().getBitmaps().size() || this.mCurrentTextureIndex < 0) {
                    if (this.mbIsReverseEnabled) {
                        if (this.mbIsReversing) {
                            z = false;
                        }
                        this.mbIsReversing = z;
                        this.mCurrentTextureIndex = this.mbIsReversing ? getCurrentTexture().getBitmaps().size() - 1 : 0;
                    } else {
                        this.mCurrentTextureIndex = 0;
                    }
                    if (!this.mbIsLoop) {
                        if (this.mCurrentTextureIndex == 0) {
                            this.mNumExecutions--;
                        }
                        if (this.mNumExecutions <= 0) {
                            setDirty(false);
                            return new State(this.mState.state, this.mState.event, this.mState.nextState, this.mState.layer);
                        }
                    }
                }
                updateTexture(getContext(), renderer);
            }
        }
        return null;
    }

    public void registerPicker(OnObjectPickedListener picker) {
        picker.onObjectPicked(getBaseObject3D());
    }

    public void enableLooping(boolean isReverse, int startFrameIndex) {
        if (startFrameIndex >= getCurrentTexture().getBitmaps().size() || startFrameIndex < 0) {
            startFrameIndex = 0;
        }
        this.mCurrentTextureIndex = startFrameIndex;
        this.mbIsLoop = true;
        this.mbIsReverseEnabled = isReverse;
        this.mbIsReversing = false;
        this.mElapsedTime = 0.0f;
        setDirty(true);
    }

    public void enableLooping() {
        enableLooping(false, 0);
    }

    public void interruptAndRunForNumExecutions(int nbExecutions, boolean isReverse, int startFrameIndex) {
        if (startFrameIndex >= getCurrentTexture().getBitmaps().size() || startFrameIndex < 0) {
            startFrameIndex = 0;
        }
        this.mCurrentTextureIndex = startFrameIndex;
        this.mbIsLoop = false;
        this.mbIsReverseEnabled = isReverse;
        this.mbIsReversing = false;
        this.mNumExecutions = nbExecutions;
        this.mElapsedTime = 0.0f;
        setDirty(true);
    }

    public void interruptAndRunForNumExecutions(int nbExecutions) {
        interruptAndRunForNumExecutions(nbExecutions, false, 0);
    }

    public void setFrameTo(int frameIndex) {
        if (frameIndex < getCurrentTexture().getBitmaps().size() && frameIndex >= 0) {
            this.mCurrentTextureIndex = frameIndex;
            updateTexture(getContext(), this.mRenderer);
        }
    }

    public void stopAnimation() {
        setDirty(false);
    }

    public void startAnimation(boolean bFromStartOfAnim) {
        setDirty(true);
        if (bFromStartOfAnim) {
            this.mCurrentTextureIndex = 0;
        }
    }

    public float getAnimationRate() {
        return this.mAnimationRate;
    }

    public void setThrottling(float animationRate) {
        this.mAnimationRate = animationRate;
    }

    public int getNbExecutions() {
        return this.mNumExecutions;
    }

    public void setNbExecutions(int nbExecutions) {
        this.mNumExecutions = nbExecutions;
    }

    public boolean IsLooping() {
        return this.mbIsLoop;
    }

    public void setReverse(boolean isReverse) {
        this.mbIsReverseEnabled = isReverse;
    }

    public boolean isReverse() {
        return this.mbIsReverseEnabled;
    }

    public int getAnimationFrameCount() {
        return this.mTextureInfo.size() - 1;
    }

    public int getCurrentFrameIndex() {
        return this.mCurrentTextureIndex;
    }

    public boolean IsPaused() {
        return this.mbIsPaused;
    }

    public void SetPaused(boolean isPaused) {
        this.mbIsPaused = isPaused;
    }
}