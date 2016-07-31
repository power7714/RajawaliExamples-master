package com.monyetmabuk.rajawali.tutorials.examples.general;

import android.content.Context;
import android.graphics.SurfaceTexture;

import com.monyetmabuk.rajawali.tutorials.R;
import com.monyetmabuk.rajawali.tutorials.examples.AExampleFragment;
import com.monyetmabuk.rajawali.tutorials.graphicengine.graphics.GLPlaneCustomAnimationGraphicEngine;
import com.monyetmabuk.rajawali.tutorials.graphicengine.utils.Scenario;

import org.rajawali3d.Object3D;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BasicFragment extends AExampleFragment {

	@Override
    public AExampleRenderer createRenderer() {
		return new BasicRenderer(getActivity());
	}

	private final class BasicRenderer extends AExampleRenderer {

        private Object3D mSphere;
		private ArrayList<GLPlaneCustomAnimationGraphicEngine> words;
		private float triggerWordsAnimationElapsedTime = 0.0f;
		protected boolean mbIsWordAutoPlayPref = true;
		private int mCurrentWordsUpdateFreq = 5;
		private boolean firstTime = true;
		Scenario scenario;
		private int mNumberOfWords;
		private Random mRandom;

		public Scenario getScenario() {
			return this.scenario;
		}

		public BasicRenderer(Context context) {
			super(context);
		}

        @Override
		protected void initScene() {
			try {
				Material material = new Material();
				material.addTexture(new Texture("earthColors", R.drawable.earthtruecolor_nasa_big));
				material.setColorInfluence(0);
				mSphere = new Sphere(1, 24, 24);
				mSphere.setMaterial(material);
				getCurrentScene().addChild(mSphere);
			} catch (ATexture.TextureException e) {
				e.printStackTrace();
			}

			this.words = new ArrayList();
			if (getScenario().getLayer("a7") != null) {
				this.words.add((GLPlaneCustomAnimationGraphicEngine) getScenario().getLayer("a7").getGraphicEngine());
			}
			this.mRandom = new Random();
			this.mNumberOfWords = this.words.size();

            getCurrentCamera().enableLookAt();
            getCurrentCamera().setLookAt(0, 0, 0);
            getCurrentCamera().setZ(6);
        }

        @Override
        public void onRender(final long elapsedTime, final double deltaTime) {
			super.onRender(elapsedTime, deltaTime);
			mSphere.rotate(Vector3.Axis.Y, 1.0);
		}

		public void replacePositioning() {
			int i;
			int numChildren = this.words.size();
			if (this.firstTime) {
				this.firstTime = false;
				for (i = 0; i < numChildren; i++) {
					((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).setCurrentTextureIndex(((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).getNumberOfFrames() - 1);
				}
			}
			i = 0;
			while (i < numChildren) {
				if (((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).getCurrentTextureIndex() == ((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).getNumberOfFrames() - 1 || ((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).getCurrentTextureIndex() == 0) {
					((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).stopAnimation();
				}
				i++;
			}
		}

		private void triggerWordsAnimation(float deltaTime) {
			if (this.mbIsWordAutoPlayPref) {
				this.triggerWordsAnimationElapsedTime += deltaTime;
				if (this.triggerWordsAnimationElapsedTime >= ((float) this.mCurrentWordsUpdateFreq)) {
					int i = this.mRandom.nextInt(this.mNumberOfWords);
					if (((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).getCurrentTextureIndex() > (((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).getNumberOfFrames() - 1) / 2) {
						((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).interruptAndRunForNumExecutions(1, true);
					} else {
						((GLPlaneCustomAnimationGraphicEngine) this.words.get(i)).interruptAndRunForNumExecutions(1, false);
					}
					this.triggerWordsAnimationElapsedTime = 0.0f;
				}
			}
		}

		@Override
		public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
			//replacePositioning();
			super.onRenderSurfaceCreated(config, gl, width, height);
		}

		@Override
		public void onRenderSurfaceDestroyed(SurfaceTexture surface) {
			super.onRenderSurfaceDestroyed(surface);
		}

		@Override
		public void onRenderSurfaceSizeChanged(GL10 gl, int width, int height) {
			//replacePositioning();
			super.onRenderSurfaceSizeChanged(gl, width, height);
		}
	}
}
