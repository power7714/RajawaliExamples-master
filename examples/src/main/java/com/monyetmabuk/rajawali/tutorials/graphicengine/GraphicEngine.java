package com.monyetmabuk.rajawali.tutorials.graphicengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractObj;
import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.Texture;
import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.TextureInfo;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.State;

import org.json.JSONObject;
import org.rajawali3d.Object3D;
import org.rajawali3d.lights.ALight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import aurelienribon.tweenengine.TweenCallback;

/**
 * Created by CurTro Studios on 7/28/2016.
 */

public abstract class GraphicEngine {
    private Constructor aLightConstructor;
    private Constructor aMaterialConstructor;
    protected boolean bIsTimerEnabled = false;
    private Context context = null;
    protected int currentSubTextureIndex = 0;
    private Texture currentTexture = null;
    protected int currentTextureIndex = 0;
    protected int delay = 5;
    private boolean dirty = false;
    private boolean enable = false;
    private float heightWorld = -1.0f;
    protected int initialDelay = 5;
    protected boolean initialized = false;
    private ALight light;
    protected Vector3 mLastPosition = new Vector3(0.0f, 0.0f, 0.0f);
    protected Vector3 mLastRotation = new Vector3(0.0f, 0.0f, 0.0f);
    protected Vector3 mLastScale = new Vector3(1.0f, 1.0f, 1.0f);
    protected Renderer mRenderer = null;
    protected Handler mTimerHandler;
    protected float[] mTintColorARGB;
    private Material material;
    protected boolean mbHasSceneBeenFlushed = false;
    protected boolean mbIsAlphaTintEnabled = false;
    protected boolean mbIsFirstStart = false;
    protected boolean mbIsGLDataPurged = true;
    protected boolean mbIsHidden = false;
    protected boolean mbShouldPreservePreviousStatesDuringInit = false;
    private String name = null;
    private int obj = 0;
    private AbstractObj object;
    private Vector3 origPosition = new Vector3(0.0f, 0.0f, 0.0f);
    private Vector3 origRotation = new Vector3(0.0f, 0.0f, 0.0f);
    private Vector3 origScale = new Vector3(1.0f, 1.0f, 1.0f);
    private JSONObject parameters;
    private Vector3 position = null;
    private boolean removed = false;
    private Vector3 rotation = null;
    private Vector3 scale = null;
    private boolean selectable;
    private ArrayList<Texture> textures = null;
    protected Runnable timerRunnable = new Runnable() {
        public void run() {
            GraphicEngine.this.runTimedProcess();
            GraphicEngine.this.mTimerHandler.postDelayed(this, (long) (GraphicEngine.this.delay * 1000));
        }
    };
    private boolean visible = false;
    private float widthWorld = -1.0f;
    private float xOffset = -1.0f;

    public boolean getShouldPreservePreviousStatesDuringInit() {
        return this.mbShouldPreservePreviousStatesDuringInit;
    }

    public void setShouldPreservePreviousStatesDuringInit(boolean bShouldPreservePreviousStatesDuringInit) {
        this.mbShouldPreservePreviousStatesDuringInit = bShouldPreservePreviousStatesDuringInit;
    }

    public boolean isHidden() {
        return this.mbIsHidden;
    }

    public void setHidden(boolean isHidden) {
        if (!(this.mbIsHidden == isHidden || getBaseObject3D() == null)) {
            if (isHidden) {
                removeChild(this.mRenderer, getBaseObject3D());
            } else if (!this.mRenderer.getCurrentScene().getChildrenCopy().contains(getBaseObject3D())) {
                addChild(this.mRenderer, getBaseObject3D());
            }
        }
        this.mbIsHidden = isHidden;
    }

    protected GraphicEngine(Context context, String name) {
        setContext(context);
        setName(name);
        setParameters(null);
        setSelectable(false);
        this.position = new Vector3(0.0f, 0.0f, 0.0f);
        this.rotation = new Vector3(0.0f, 0.0f, 0.0f);
        this.scale = new Vector3(1.0f, 1.0f, 1.0f);
        this.textures = new ArrayList();
        this.delay = 5;
        this.initialDelay = 5;
        this.mTimerHandler = null;
        this.bIsTimerEnabled = false;
        this.mTintColorARGB = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    }

    public int getTimerDelay() {
        return this.delay;
    }

    public void setTimerDelay(int delay) {
        this.delay = delay;
    }

    public int getInitialTimerDelay() {
        return this.initialDelay;
    }

    public void setInitialTimerDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public boolean isTimerEnabled() {
        return this.bIsTimerEnabled;
    }

    public void setTimerEnabled(boolean bIsEnabled) {
        this.bIsTimerEnabled = bIsEnabled;
        if (this.bIsTimerEnabled) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    protected void applyTintToObject() {
        if (this.mbIsAlphaTintEnabled && getBaseObject3D() != null && getBaseObject3D().getMaterial() != null && this.enable) {
            getBaseObject3D().setColor((((0 | ((((int) (this.mTintColorARGB[0] * 255.0f)) & TweenCallback.ANY) << 24)) | ((((int) (this.mTintColorARGB[1] * 255.0f)) & TweenCallback.ANY) << 16)) | ((((int) (this.mTintColorARGB[2] * 255.0f)) & TweenCallback.ANY) << 8)) | (((int) (this.mTintColorARGB[3] * 255.0f)) & TweenCallback.ANY));
            getBaseObject3D().getMaterial().useVertexColors(true);
        }
    }

    public void setTint(double r, double g, double b) {
        this.mTintColorARGB[1] = (float)r;
        this.mTintColorARGB[2] = (float)g;
        this.mTintColorARGB[3] = (float)b;
        applyTintToObject();
    }

    public void setTint(Vector3 tint) {
        setTint(tint.x, tint.y, tint.z);
    }

    public void setTintR(float r) {
        this.mTintColorARGB[1] = r;
        applyTintToObject();
    }

    public void setTintG(float g) {
        this.mTintColorARGB[2] = g;
        applyTintToObject();
    }

    public void setTintB(float b) {
        this.mTintColorARGB[3] = b;
        applyTintToObject();
    }

    public Vector3 getTint() {
        return new Vector3(this.mTintColorARGB[1], this.mTintColorARGB[2], this.mTintColorARGB[3]);
    }

    public void setAlpha(float alpha) {
        if (alpha < 0.0f) {
            alpha = 0.0f;
        }
        this.mTintColorARGB[0] = alpha;
        applyTintToObject();
    }

    public float getAlpha() {
        return this.mTintColorARGB[0];
    }

    public void enableAlphaTint(boolean bIsAlphaTintEnabled) {
        this.mbIsAlphaTintEnabled = bIsAlphaTintEnabled;
        if (!this.mbIsAlphaTintEnabled || getBaseObject3D() == null) {
            getBaseObject3D().getMaterial().useVertexColors(false);
        } else {
            applyTintToObject();
        }
    }

    public boolean isAlphaTintEnabled() {
        return this.mbIsAlphaTintEnabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getParameters() {
        return this.parameters;
    }

    public void setParameters(JSONObject parameters) {
        this.parameters = parameters;
    }

    public boolean isSelectable() {
        return this.selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public Vector3 getInitialPosition() {
        return this.position;
    }

    public void setInitialPosition(Vector3 position) {
        this.position = position;
    }

    public void setInitialPosition(float x, float y, float z) {
        this.position.setAll(x, y, z);
    }

    public Vector3 getInitialRotation() {
        return this.rotation;
    }

    public void setInitialRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public void setInitialRotation(float x, float y, float z) {
        this.rotation.setAll(x, y, z);
    }

    public Vector3 getInitialScale() {
        return this.scale;
    }

    public void setInitialScale(Vector3 scale) {
        this.scale = scale;
    }

    public void setInitialScale(float x, float y, float z) {
        this.scale.setAll(x, y, z);
    }

    public double getInitialPosX() {
        return this.position.x;
    }

    public void setInitialPosX(float x) {
        this.position.x = x;
    }

    public double getInitialPosY() {
        return this.position.y;
    }

    public void setInitialPosY(float y) {
        this.position.y = y;
    }

    public double getInitialPosZ() {
        return this.position.z;
    }

    public void setInitialPosZ(float z) {
        this.position.z = z;
    }

    public double getInitialRotX() {
        return this.rotation.x;
    }

    public void setInitialRotX(float x) {
        this.rotation.x = x;
    }

    public double getInitialRotY() {
        return this.rotation.y;
    }

    public void setInitialRotY(float y) {
        this.rotation.y = y;
    }

    public double getInitialRotZ() {
        return this.rotation.z;
    }

    public void setInitialRotZ(float z) {
        this.rotation.z = z;
    }

    public double getInitialScaleX() {
        return this.scale.x;
    }

    public void setInitialScaleX(float x) {
        this.scale.x = x;
    }

    public double getInitialScaleY() {
        return this.scale.y;
    }

    public void setInitialScaleY(float y) {
        this.scale.y = y;
    }

    public double getInitialScaleZ() {
        return this.scale.z;
    }

    public void setInitialScaleZ(float z) {
        this.scale.z = z;
    }

    public void setLight(Constructor constructor) {
        this.aLightConstructor = constructor;
    }

    public ALight getLight() {
        this.light = null;
        try {
            this.light = (ALight) this.aLightConstructor.newInstance(new Object[0]);
        } catch (Throwable ignore) {
            ignore.printStackTrace();
            this.light = null;
        }
        return this.light;
    }

    public void setMaterial(Constructor constructor) {
        this.aMaterialConstructor = constructor;
    }

    public Material getMaterial() {
        this.material = null;
        try {
            this.material = (Material) this.aMaterialConstructor.newInstance(new Object[0]);
        } catch (Throwable ignore) {
            ignore.printStackTrace();
            this.material = null;
        }
        return this.material;
    }

    public int getObj() {
        return this.obj;
    }

    public void setObj(int obj) {
        this.obj = obj;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Texture> getTextures() {
        return this.textures;
    }

    public void setTextures(ArrayList<Texture> textures) {
        this.textures = textures;
    }

    public void addTexture(Texture texture) {
        this.textures.add(texture);
    }

    public Texture getTexture(String name) {
        for (int i = 0; i < this.textures.size(); i++) {
            if (((Texture) this.textures.get(i)).getName().equalsIgnoreCase(name)) {
                return (Texture) this.textures.get(i);
            }
        }
        return null;
    }

    public int getTextureIndexOf(String name) {
        for (int i = 0; i < this.textures.size(); i++) {
            if (((Texture) this.textures.get(i)).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public int getSubTextureIndexOf(String name) {
        for (int i = 0; i < ((Texture) this.textures.get(this.currentTextureIndex)).getBitmaps().size(); i++) {
            if (((Texture) this.textures.get(this.currentTextureIndex)).getBitmap(i).getBitmapName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public Texture getTexture(int index) {
        if (index < 0 || index >= this.textures.size()) {
            return null;
        }
        return (Texture) this.textures.get(index);
    }

    public void setCurrentTexture(int parentIndex) {
        setCurrentTexture(parentIndex, 0);
    }

    public void setCurrentTexture(int parentIndex, int subIndex) {
        if (parentIndex >= 0 && parentIndex < this.textures.size()) {
            Texture texture = (Texture) this.textures.get(parentIndex);
            if (texture != null && subIndex >= 0 && subIndex < texture.getBitmaps().size()) {
                this.currentSubTextureIndex = subIndex;
                this.currentTexture = texture;
                this.currentTextureIndex = parentIndex;
            }
        }
    }

    public void setCurrentTexture(String parentName) {
        if (parentName != null) {
            setCurrentTexture(getTextureIndexOf(parentName), 0);
        }
    }

    public void setCurrentSubTexture(String subTextureName) {
        if (subTextureName != null) {
            setCurrentSubTexture(getTextureIndexOf(subTextureName));
        }
    }

    public void setCurrentSubTexture(int subTextureIndex) {
        if (subTextureIndex >= 0 && subTextureIndex < ((Texture) this.textures.get(this.currentTextureIndex)).getBitmaps().size()) {
            this.currentSubTextureIndex = subTextureIndex;
        }
    }

    public void setCurrentTexture(String parentName, String subTextureName) {
        if (parentName != null && subTextureName != null) {
            setCurrentTexture(getTextureIndexOf(parentName), getTextureIndexOf(subTextureName));
        }
    }

    public Texture getCurrentTexture() {
        return this.currentTexture;
    }

    public int getCurrentTextureIndex() {
        return this.currentTextureIndex;
    }

    public int getCurrentSubTextureIndex() {
        return this.currentSubTextureIndex;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        if (this.enable != enable) {
            this.enable = enable;
            if (!enable) {
                if (getBaseObject3D() != null) {
                    this.mLastPosition = getBaseObject3D().getPosition();
                    //this.mLastRotation = getBaseObject3D().getLookAt().getRotationTo()
                    this.mLastScale = getBaseObject3D().getScale();
                }
                this.mbIsGLDataPurged = false;
                stopTimer();
            } else if (this.bIsTimerEnabled) {
                startTimer();
            }
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (this.enable) {
            this.mbIsGLDataPurged = false;
        }
    }

    public void onVisibilityChanged(boolean visible) {
        this.visible = visible;
        if (visible) {
            setDirty(true);
            if (this.bIsTimerEnabled) {
                startTimer();
                return;
            }
            return;
        }
        stopTimer();
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        this.xOffset = xOffset;
    }

    public float getXOffset() {
        return this.xOffset;
    }

    public void onSurfaceChanged(float width, float height) {
        this.widthWorld = width;
        this.heightWorld = height;
    }

    public float getWorldWidth() {
        return this.widthWorld;
    }

    public float getWorldHeight() {
        return this.heightWorld;
    }

    public boolean onNotificationEvent(Context context, Bundle bundle) {
        return false;
    }

    public void initScene(Context context, Renderer renderer) {
        if (isEnable()) {
            initScene(renderer);
            this.initialized = true;
            return;
        }
        this.initialized = false;
    }

    public void initScene(Renderer renderer) {
        this.mRenderer = renderer;
        if (this.mbIsFirstStart || !this.mbShouldPreservePreviousStatesDuringInit) {
            this.mbIsFirstStart = false;
            this.bIsTimerEnabled = getParameters().optBoolean("isTimerEnabled", false);
            this.delay = getParameters().optInt("delay", 5);
            this.initialDelay = getParameters().optInt("initialDelay", 5);
            this.mbIsAlphaTintEnabled = getParameters().optBoolean("isAlphaTint", false);
            this.mTintColorARGB[0] = (float) getParameters().optDouble("alpha", 1.0d);
            this.mTintColorARGB[1] = (float) getParameters().optDouble("red", 1.0d);
            this.mTintColorARGB[2] = (float) getParameters().optDouble("green", 1.0d);
            this.mTintColorARGB[3] = (float) getParameters().optDouble("blue", 1.0d);
            this.enable = getParameters().optBoolean("isEnabled", true);
            this.mbIsHidden = getParameters().optBoolean("isHidden", false);
            this.mbShouldPreservePreviousStatesDuringInit = getParameters().optBoolean("shouldPreservePreviousStatesDuringInit", false);
        }
        if (this.currentTexture == null) {
            setCurrentTexture(0);
            this.currentTextureIndex = 0;
        }
        if (this.mTimerHandler != null) {
            stopTimer();
        }
        this.mTimerHandler = new Handler(Looper.getMainLooper());
    }

    public void recalculateDimensions() {
    }

    public void destroyScene(Context context, Renderer renderer) {
        destroyScene(renderer);
    }

    public void destroyScene(Renderer renderer) {
        stopTimer();
    }

    public State draw(Context context, Renderer renderer, float deltaTime) {
        if (isEnable()) {
            if (!this.initialized) {
                initScene(context, renderer);
                if (this.mbIsGLDataPurged) {
                    onSurfaceCreated(null, null);
                    this.mbIsGLDataPurged = false;
                }
            }
            if (this.mbIsHidden) {
                return null;
            }
            return draw(renderer, deltaTime);
        } else if (this.mbIsGLDataPurged) {
            return null;
        } else {
            destroyScene(context, renderer);
            this.mbIsGLDataPurged = true;
            return null;
        }
    }

    public State draw(Context context, Renderer renderer, int width, int height, float deltaTime) {
        return draw(context, renderer, deltaTime);
    }

    public State draw(Renderer renderer, float deltaTime) {
        return null;
    }

    public void setTexture(Context context, Object3D object3D, Renderer renderer) {
        if (this.mbIsAlphaTintEnabled) {
            this.material.usingVertexColors();
        }
    }

    public Object3D getBaseObject3D() {
        return null;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public AbstractObj getObject() {
        return this.object;
    }

    public void setObject(AbstractObj object) {
        this.object = object;
    }

    public boolean onCommand(String command, int x, int y) {
        return false;
    }

    public void terminate(Context context, Renderer renderer, Object3D object3D) {
        if (this.initialized) {
            if (object3D != null) {
                renderer.getCurrentScene().removeChild(object3D);
                Material material = object3D.getMaterial();
                if (material != null) {
                    ArrayList<ATexture> tInfos = material.getTextureList();
                    renderer.getTextureManager().removeTextures(tInfos);
                    Iterator it = tInfos.iterator();
                    while (it.hasNext()) {
                        Bitmap[] bitmaps = ((TextureInfo) it.next()).getTextures();
                        if (bitmaps != null) {
                            for (Bitmap bitmap : bitmaps) {
                                if (bitmap != null) {
                                    bitmap.recycle();
                                }
                            }
                        }
                    }
                    if (getCurrentTexture() == null) {
                        setCurrentTexture(0);
                    }
                    getCurrentTexture().terminate(context);
                    object3D.destroy();
                }
            }
            Iterator it2 = getTextures().iterator();
            while (it2.hasNext()) {
                ((Texture) it2.next()).terminate(context);
            }
            this.initialized = false;
        }
    }

    protected void addChild(Renderer renderer, Object3D object3D) {
        renderer.getCurrentScene().addChild(object3D);
        this.removed = false;
    }

    protected boolean removeChild(Renderer renderer, Object3D object3D) {
        this.removed = renderer.getCurrentScene().removeChild(object3D);
        return this.removed;
    }

    protected void startTimer() {
        if (this.mTimerHandler != null) {
            this.mTimerHandler.removeCallbacks(this.timerRunnable);
            this.mTimerHandler.postDelayed(this.timerRunnable, (long) (this.initialDelay * 1000));
        }
    }

    protected void stopTimer() {
        if (this.mTimerHandler != null) {
            this.mTimerHandler.removeCallbacks(this.timerRunnable);
        }
    }

    public void runTimedProcess() {
    }

    public boolean onSensorEvent(Context context, String sensorType, float x, float y, float z, long timestamp) {
        return false;
    }

    public void resetToJSONInitialPosition() {
        this.position.setAll(this.origPosition);
    }

    public void resetToJSONInitialScale() {
        this.scale.setAll(this.origScale);
    }

    public void resetToJSONInitialRot() {
        this.rotation.setAll(this.origRotation);
    }

    public void setJSONInitialPosition(Vector3 pos) {
        this.origPosition.setAll(pos);
    }

    public Vector3 getJSONInitialPosition() {
        return this.origPosition.clone();
    }

    public void setJSONInitialScale(Vector3 scale) {
        this.origScale.setAll(scale);
    }

    public Vector3 getJSONInitialScale() {
        return this.origScale.clone();
    }

    public void setJSONInitialRotation(Vector3 rot) {
        this.origRotation.setAll(rot);
    }

    public Vector3 getJSONInitialRotation() {
        return this.origRotation.clone();
    }
}
