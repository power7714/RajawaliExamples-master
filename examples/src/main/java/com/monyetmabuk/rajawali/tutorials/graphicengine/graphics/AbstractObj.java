package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;

import android.content.Context;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.textures.TextureManager;

/**
 * Created by CurTro Studios on 7/28/2016.
 */
public abstract class AbstractObj {
    private String name;
    private Object3D object;

    public abstract Object3D get(Context context, TextureManager textureManager);

    protected AbstractObj() {
        this.name = null;
        this.object = null;
        this.name = null;
    }

    protected AbstractObj(String name) {
        this.name = null;
        this.object = null;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object3D getObject() {
        return this.object;
    }

    public void setObject(Object3D object) {
        this.object = object;
    }
}