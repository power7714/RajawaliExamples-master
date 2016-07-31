package com.monyetmabuk.rajawali.tutorials.examples.materials;

import android.content.Context;

import com.monyetmabuk.rajawali.tutorials.R;
import com.monyetmabuk.rajawali.tutorials.examples.AExampleFragment;
import com.monyetmabuk.rajawali.tutorials.examples.materials.materials.CustomRawFragmentShader;
import com.monyetmabuk.rajawali.tutorials.examples.materials.materials.CustomRawVertexShader;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.AlphaMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;

public class RawShaderFilesFragment extends AExampleFragment {

	@Override
    public AExampleRenderer createRenderer() {
		return new RawShaderFilesRenderer(getActivity());
	}

	public class RawShaderFilesRenderer extends AExampleRenderer {
		private DirectionalLight mLight;
		private Object3D mCube;
		private float mTime;
		private Material mMaterial;

		public RawShaderFilesRenderer(Context context) {
			super(context);
		}

        @Override
		protected void initScene() {
			mLight = new DirectionalLight(0, 1, 1);

			getCurrentScene().addLight(mLight);

			mMaterial = new Material(new CustomRawVertexShader(), new CustomRawFragmentShader());
			mMaterial.enableTime(true);
			try {
                mMaterial.addTexture(new Texture("diffuse", R.drawable.waterdiffuse));
                mMaterial.addTexture(new AlphaMapTexture("alpha", R.drawable.wateralpha));
			} catch (ATexture.TextureException e) {
				e.printStackTrace();
			}
			mMaterial.setColorInfluence(1.5f);
            Plane cube = new Plane(4,4,40,40);
            cube.setMaterial(mMaterial);
            cube.setTransparent(true);
            cube.setDoubleSided(true);
			cube.setRotZ(80f);
			getCurrentScene().addChild(cube);

			getCurrentCamera().setPosition(0, 0, 10);

			mTime = 0;
		}

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
			mTime += .07f;
			mMaterial.setTime(mTime);
		}
	}

}
