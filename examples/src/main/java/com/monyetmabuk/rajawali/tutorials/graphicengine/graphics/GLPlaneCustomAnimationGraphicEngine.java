package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;

import android.content.Context;

import com.monyetmabuk.rajawali.tutorials.graphicengine.GLPlaneAnimationGraphicEngine;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.State;

import org.rajawali3d.renderer.Renderer;

/**
 * Created by CurTro Studios on 7/30/2016.
 */
public class GLPlaneCustomAnimationGraphicEngine extends GLPlaneAnimationGraphicEngine {
    private boolean backToStartInterpolation;

    public GLPlaneCustomAnimationGraphicEngine(Context context, String name) {
        super(context, name);
    }

    public State draw(Renderer renderer, float deltaTime) {
        boolean z = true;
        if (isDirty()) {
            this.mElapsedTime += deltaTime;
            if (this.mElapsedTime >= this.mAnimationRate) {
                this.mElapsedTime = 0.0f;
                this.mLastTextureIndex = this.mCurrentTextureIndex;
                if (this.mbIsReverseEnabled) {
                    this.mCurrentTextureIndex = (this.mbIsReversing ? -1 : 1) + this.mCurrentTextureIndex;
                } else {
                    this.mCurrentTextureIndex++;
                }
                if (this.mCurrentTextureIndex >= getCurrentTexture().getBitmaps().size()) {
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
                        this.mNumExecutions--;
                        if (this.mNumExecutions <= 0) {
                            setDirty(false);
                            this.mCurrentTextureIndex = getNumberOfFrames() - 1;
                            return new State(this.mState.state, this.mState.event, this.mState.nextState, this.mState.layer);
                        }
                    }
                } else if (this.mCurrentTextureIndex < 0 && this.backToStartInterpolation) {
                    interruptAndRunForNumExecutions(1, false);
                    this.mCurrentTextureIndex = 0;
                } else if (this.mCurrentTextureIndex < 0) {
                    setDirty(false);
                    this.mCurrentTextureIndex = 0;
                    return new State(this.mState.state, this.mState.event, this.mState.nextState, this.mState.layer);
                }
                updateTexture(getContext(), renderer);
            }
        }
        return null;
    }

    public int getNumberOfFrames() {
        return getCurrentTexture().getBitmaps().size();
    }

    public void interruptAndRunForNumExecutions(int nbExecutions, boolean isReverse) {
        this.backToStartInterpolation = true;
        this.mbIsLoop = false;
        this.mbIsReverseEnabled = true;
        this.mNumExecutions = nbExecutions;
        if (isReverse) {
            this.mCurrentTextureIndex = this.mTextureInfo.size() - 1;
            this.mbIsReversing = true;
        } else {
            this.mCurrentTextureIndex = 0;
            this.mbIsReversing = false;
        }
        this.mElapsedTime = 0.0f;
        setDirty(true);
    }

    public void interruptAndRunForNumExecutionsWithNoReverse(int nbExecutions, boolean isReverse) {
        this.backToStartInterpolation = false;
        this.mbIsLoop = false;
        this.mbIsReverseEnabled = true;
        this.mNumExecutions = nbExecutions;
        if (isReverse) {
            this.mCurrentTextureIndex = this.mTextureInfo.size() - 1;
            this.mbIsReversing = true;
        } else {
            this.mCurrentTextureIndex = 0;
            this.mbIsReversing = false;
        }
        this.mElapsedTime = 0.0f;
        setDirty(true);
    }

    public void stopAnimation() {
        setDirty(false);
    }

    public void setFrameTo(int index) {
        if (index < this.mTextureInfo.size() && index >= 0) {
            this.mCurrentTextureIndex = index;
        }
    }

    public int getCurrentTextureIndex() {
        return this.mCurrentTextureIndex;
    }

    public void setCurrentTextureIndex(int index) {
        this.mCurrentTextureIndex = index;
    }
}