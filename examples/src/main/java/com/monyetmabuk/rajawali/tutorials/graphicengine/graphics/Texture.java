package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CurTro Studios on 7/28/2016.
 */

public class Texture {
    private ArrayList<AbstractBitmap> bitmaps = null;
    private Context context = null;
    private ArrayList<String> fshader = null;
    private String name = null;
    private int obj = -1;
    private ArrayList<String> vshader = null;

    public Texture(Context context, String name) {
        this.context = context;
        this.name = name;
        this.bitmaps = new ArrayList();
    }

    public Texture(Context context, String name, float x, float y, float z, float a) {
        this.context = context;
        this.name = name;
        this.bitmaps = new ArrayList();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<AbstractBitmap> getBitmaps() {
        return this.bitmaps;
    }

    public void setBitmaps(ArrayList<AbstractBitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public AbstractBitmap getBitmap(int index) {
        return index > size() ? null : (AbstractBitmap) this.bitmaps.get(index);
    }

    public int size() {
        return this.bitmaps.size();
    }

    public void initialize(Context context) {
    }

    public void terminate(Context context) {
        Iterator it = this.bitmaps.iterator();
        while (it.hasNext()) {
            ((AbstractBitmap) it.next()).recycle();
        }
    }

    public ArrayList<String> getVShader() {
        return this.vshader;
    }

    public void setVShader(ArrayList<String> vshader) {
        this.vshader = vshader;
    }

    public ArrayList<String> getFShader() {
        return this.fshader;
    }

    public void setFShader(ArrayList<String> fshader) {
        this.fshader = fshader;
    }
}