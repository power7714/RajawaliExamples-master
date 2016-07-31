package com.monyetmabuk.rajawali.tutorials.graphicengine.utils;

import android.content.Context;

import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.AbstractLayer;
import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.GLPlaneCustomAnimationGraphicEngine;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

public class ReverseAnimation extends ShowLayerTask {
    protected Context context = null;
    protected boolean mbIsWordOnTapPlayPref = true;

    public ReverseAnimation(Context context) {
        super(context);
        this.context = context;
        //capturePreferenceSettings();
    }

    public void initialize(JSONObject object) throws JSONException {
        super.initialize(object);
        //capturePreferenceSettings();
    }

    public String run(AbstractLayer layer) {
        //capturePreferenceSettings();
        if (layer != null && this.mbIsWordOnTapPlayPref) {
            System.out.println("WE HAVE CLICKED!");
            int currentFrame = ((GLPlaneCustomAnimationGraphicEngine) layer.getGraphicEngine()).getCurrentTextureIndex();
            int amoutOfFrames = ((GLPlaneCustomAnimationGraphicEngine) layer.getGraphicEngine()).getNumberOfFrames() - 1;
            System.out.println(layer.getName() + "'s" + "currentFrame: " + currentFrame + " amoutOfFrames: " + amoutOfFrames);
            if (currentFrame > amoutOfFrames / 2) {
                ((GLPlaneCustomAnimationGraphicEngine) layer.getGraphicEngine()).interruptAndRunForNumExecutionsWithNoReverse(1, true);
                System.out.println("WE ARE ON THE TOP!");
            } else {
                System.out.println("WE ARE ON THE BOTTOM!");
                ((GLPlaneCustomAnimationGraphicEngine) layer.getGraphicEngine()).interruptAndRunForNumExecutions(1, false);
            }
        }
        return null;
    }

    public void terminate() {
        super.terminate();
    }


}