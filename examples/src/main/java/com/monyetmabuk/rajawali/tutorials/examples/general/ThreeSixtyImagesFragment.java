package com.monyetmabuk.rajawali.tutorials.examples.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.monyetmabuk.rajawali.tutorials.examples.AExampleFragment;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.ScreenQuad;

public class ThreeSixtyImagesFragment extends AExampleFragment {

	@Override
    public AExampleRenderer createRenderer() {
		return new ThreeSixtyImagesRenderer(getActivity());
	}

	private final class ThreeSixtyImagesRenderer extends AExampleRenderer {
		private ATexture[] mTextures;
		private Plane mScreenQuad;
		private int mFrameCount;
		private Material mMaterial;
		private final static int NUM_TEXTURES = 21;

		public ThreeSixtyImagesRenderer(Context context) {
			super(context);
		}

        @Override
		protected void initScene() {
			if (mTextureManager != null)
				mTextureManager.reset();
			if (mMaterial != null)
				mMaterial.getTextureList().clear();

			getCurrentScene().setBackgroundColor(Color.GREEN);

			mMaterial = new Material();

			mScreenQuad = new Plane();
            mScreenQuad.setScale(0.5f);
            mScreenQuad.setTransparent(true);
			mScreenQuad.setMaterial(mMaterial);
			getCurrentScene().addChild(mScreenQuad);

			if (mTextures == null) {
				// -- create an array that will contain all TextureInfo objects
				mTextures = new ATexture[NUM_TEXTURES];
			}
			mFrameCount = 0;

			BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
			options.inInputShareable = true;

			for (int i = 1; i <= this.mTextures.length; i++) {
				// -- load all the textures from the drawable folder
				int resourceId = mContext.getResources().getIdentifier( i < 10 ? "fearless_0" + i : "fearless_" + i, "drawable", "com.monyetmabuk.rajawali.tutorials");
				//int resID = mContext.getResources().getIdentifier()

				Bitmap bitmap = BitmapFactory.decodeResource( mContext.getResources(), resourceId, options);

				ATexture texture = new Texture("bm" + i, bitmap);
                Log.i("ImageLoop", "Images: bm " + i);
                texture.setMipmap(false);
                texture.shouldRecycle(true);
				mTextures[i - 1] = mTextureManager.addTexture(texture);
			}
			try {
				mMaterial.addTexture(mTextures[0]);
				mMaterial.setColorInfluence(0);
			} catch (ATexture.TextureException e) {
				e.printStackTrace();
			}
		}

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
            mFrameCount = 10;
			// -- get the texture info list and remove the previous TextureInfo object
			mMaterial.getTextureList().remove(mTextures[mFrameCount++]);
			// -- add a new TextureInfo object
			mMaterial.getTextureList().add(mTextures[mFrameCount * (int)0.5]);
		}

	}

}
