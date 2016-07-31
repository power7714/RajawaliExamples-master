package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import org.rajawali3d.materials.textures.ATexture.WrapType;
import org.rajawali3d.materials.textures.ATexture.TextureType;
import org.rajawali3d.materials.textures.ATexture.FilterType;

/**
 * Created by CurTro Studios on 7/28/2016.
 */
public class TextureInfo {
    protected boolean isCubeMap;
    protected Config mBitmapConfig;
    protected int mBitmapFormat;
    protected FilterType mFilterType;
    protected int mHeight;
    protected boolean mMipmap;
    protected boolean mShouldRecycle;
    protected Bitmap mTexture;
    protected int mTextureId;
    protected String mTextureName;
    protected TextureType mTextureType;
    protected Bitmap[] mTextures;
    protected int mUniformHandle;
    protected int mWidth;
    protected WrapType mWrapType;

    public TextureInfo(TextureInfo other) {
        this.mTextureName = "";
        this.mUniformHandle = -1;
        this.isCubeMap = false;
        setFrom(other);
    }

    public TextureInfo(int textureId) {
        this(textureId, TextureType.DIFFUSE);
    }

    public TextureInfo(int textureId, TextureType textureType) {
        this.mTextureName = "";
        this.mUniformHandle = -1;
        this.isCubeMap = false;
        this.mTextureId = textureId;
        this.mTextureType = textureType;
    }

    public void setFrom(TextureInfo other) {
        this.mTextureId = other.getTextureId();
        this.mTextureType = other.getTextureType();
        this.mUniformHandle = other.getUniformHandle();
        this.mWidth = other.getWidth();
        this.mHeight = other.getHeight();
        this.mTexture = other.getTexture();
        this.mTextures = other.getTextures();
        this.mMipmap = other.isMipmap();
        this.mBitmapConfig = other.getBitmapConfig();
        this.mTextureName = other.getTextureName();
    }

    public void setTextureId(int id) {
        this.mTextureId = id;
    }

    public int getTextureId() {
        return this.mTextureId;
    }

    public void setTextureName(String name) {
        this.mTextureName = name;
    }

    public String getTextureName() {
        return this.mTextureName;
    }

    public void setUniformHandle(int handle) {
        this.mUniformHandle = handle;
    }

    public int getUniformHandle() {
        return this.mUniformHandle;
    }

    public boolean isCubeMap() {
        return this.isCubeMap;
    }

    public void setIsCubeMap(boolean cube) {
        this.isCubeMap = cube;
    }

    public String toString() {
        return "id: " + this.mTextureId + " handle: " + this.mUniformHandle + " type: " + this.mTextureType + " name: " + this.mTextureName;
    }

    public TextureType getTextureType() {
        return this.mTextureType;
    }

    public void setTextureType(TextureType textureType) {
        this.mTextureType = textureType;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public Bitmap getTexture() {
        return this.mTexture;
    }

    public void setTexture(Bitmap texture) {
        this.mTexture = texture;
    }

    public Bitmap[] getTextures() {
        return this.mTextures;
    }

    public void setTextures(Bitmap[] textures) {
        this.mTextures = textures;
    }

    public boolean isMipmap() {
        return this.mMipmap;
    }

    public void setMipmap(boolean mipmap) {
        this.mMipmap = mipmap;
    }

    public Config getBitmapConfig() {
        return this.mBitmapConfig;
    }

    public void setBitmapConfig(Config bitmapConfig) {
        this.mBitmapConfig = bitmapConfig;
    }

    public void setFilterType(FilterType filterType) {
        this.mFilterType = filterType;
    }

    public FilterType getFilterType() {
        return this.mFilterType;
    }

    public void setWrapType(WrapType wrapType) {
        this.mWrapType = wrapType;
    }

    public WrapType getWrapType() {
        return this.mWrapType;
    }

    public boolean shouldRecycle() {
        return this.mShouldRecycle;
    }

    public void shouldRecycle(boolean should) {
        this.mShouldRecycle = should;
    }
}