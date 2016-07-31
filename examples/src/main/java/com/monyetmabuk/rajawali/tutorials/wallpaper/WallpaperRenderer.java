package com.monyetmabuk.rajawali.tutorials.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.monyetmabuk.rajawali.tutorials.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.ALight;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.renderer.Renderer;

/**
 * @author Jared Woolston (jwoolston@idealcorp.com)
 */
public class WallpaperRenderer extends Renderer {

    public WallpaperRenderer(Context context) {
        super(context);
    }

    @Override
    public void onOffsetsChanged(float v, float v2, float v3, float v4, int i, int i2) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    protected void initScene() {
        ALight light = new DirectionalLight(-1, 0, -1);
        light.setPower(2);

        getCurrentScene().addLight(light);

        getCurrentCamera().setZ(3);

        try {
            getCurrentScene().setBackgroundColor(Color.BLUE);

            LoaderOBJ loader = new LoaderOBJ(this, R.raw.label_obj);
            loader.parse();
            Object3D obj = loader.getParsedObject();
            ATexture texture = obj.getMaterial().getTextureList().get(0);
            obj.getMaterial().removeTexture(texture);

            Bitmap bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0,0,128,128, paint);

            obj.getMaterial().addTexture(new Texture("canvas", bitmap));
            getCurrentScene().addChild(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
