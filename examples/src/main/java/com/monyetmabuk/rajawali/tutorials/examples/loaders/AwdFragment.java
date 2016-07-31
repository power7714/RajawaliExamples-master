package com.monyetmabuk.rajawali.tutorials.examples.loaders;

import android.content.Context;
import android.graphics.Color;

import com.monyetmabuk.rajawali.tutorials.R;
import com.monyetmabuk.rajawali.tutorials.examples.AExampleFragment;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderAWD;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.materials.textures.CubeMapTexture;
import org.rajawali3d.materials.textures.SpecularMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;

public class AwdFragment extends AExampleFragment {

	@Override
    public AExampleRenderer createRenderer() {
		return new AwdRenderer(getActivity());
	}

	private final class AwdRenderer extends AExampleRenderer {

		public AwdRenderer(Context context) {
			super(context);
		}

		@Override
		protected void initScene() {
            DirectionalLight mDirectionalLight = new DirectionalLight();
            mDirectionalLight.setLookAt(1, -1, -1);
            mDirectionalLight.enableLookAt();
            mDirectionalLight.setPosition(-4, 10, -4);
            mDirectionalLight.setPower(2);
            getCurrentScene().addLight(mDirectionalLight);
            getCurrentScene().setBackgroundColor(0x393939);

			try {
				final LoaderAWD parser = new LoaderAWD(mContext.getResources(), mTextureManager, R.raw.label_awd);
				parser.parse();

				final Object3D obj = parser.getParsedObject();

				obj.setScale(4f);


                Texture earthTexture = new Texture("earthDiffuseTex", R.drawable.earth_diffuse);

                Material material = new Material();
                material.enableLighting(true);
                material.setDiffuseMethod(new DiffuseMethod.Lambert());
                material.setSpecularMethod(new SpecularMethod.Phong(Color.WHITE, 40));
                material.addTexture(earthTexture);
                material.addTexture(new SpecularMapTexture("earthSpecularTex", R.drawable.earth_specular));
                material.setColorInfluence(0);
                obj.setMaterial(material);
                getCurrentScene().addChild(obj);
                getCurrentCamera().setZ(2);
                //getCurrentCamera().setLookAt(0, +1, +2);
                //getCurrentCamera().setFarPlane(5000);


				final Animation3D anim = new RotateOnAxisAnimation(Vector3.Axis.Y, -360);
				anim.setDurationDelta(4d);
				anim.setRepeatMode(Animation.RepeatMode.INFINITE);
				anim.setTransformable3D(obj);
				anim.play();
				getCurrentScene().registerAnimation(anim);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
