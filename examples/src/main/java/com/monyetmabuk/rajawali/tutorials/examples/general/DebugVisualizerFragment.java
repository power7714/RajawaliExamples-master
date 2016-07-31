package com.monyetmabuk.rajawali.tutorials.examples.general;

import android.content.Context;
import android.graphics.Color;

import com.monyetmabuk.rajawali.tutorials.R;
import com.monyetmabuk.rajawali.tutorials.examples.AExampleFragment;
import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateAroundAnimation3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.debug.DebugCamera;
import org.rajawali3d.debug.DebugLight;
import org.rajawali3d.debug.DebugVisualizer;
import org.rajawali3d.debug.GridFloor;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderAWD;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.SpecularMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;

public class DebugVisualizerFragment extends AExampleFragment {

    @Override
    public AExampleRenderer createRenderer() { return new DebugVisualizerRenderer(getActivity()); }

    public class DebugVisualizerRenderer extends AExampleRenderer {
        private DirectionalLight mDirectionalLight;
        private Camera mOtherCamera;
        private Object3D mSphere;

        public DebugVisualizerRenderer(Context context) {
            super(context);
        }

        @Override
        public void initScene() {
            mDirectionalLight = new DirectionalLight();
            mDirectionalLight.setLookAt(1, -1, -1);
            mDirectionalLight.enableLookAt();
            mDirectionalLight.setPosition(-4, 10, -4);
            mDirectionalLight.setPower(2);
            getCurrentScene().addLight(mDirectionalLight);
            getCurrentScene().setBackgroundColor(0x393939);

            animateCamera();
            try {
            final LoaderAWD parser = new LoaderAWD(mContext.getResources(), mTextureManager, R.raw.awd_label);
                try {
                    parser.parse();
                } catch (ParsingException e) {
                    e.printStackTrace();
                }

                final Object3D obj = parser.getParsedObject();

            obj.setScale(4f);
                obj.setColor(0xFFFFFF);


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
            getCurrentCamera().setZ(+11f);
            getCurrentCamera().setLookAt(0, +1, +2);
            getCurrentCamera().setFarPlane(5000);

            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }

            mOtherCamera = new Camera();
            mOtherCamera.setPosition(4, 2, -10);
            mOtherCamera.setFarPlane(10);
            mOtherCamera.enableLookAt();

            mSphere = createAnimatedSphere();

            DebugVisualizer debugViz = new DebugVisualizer(this);
            debugViz.addChild(new GridFloor(20, 0x555555, 1, 20));
            debugViz.addChild(new DebugLight(mDirectionalLight, 0x999900, 1));
            debugViz.addChild(new DebugCamera(mOtherCamera, 0x000000, 1));
            getCurrentScene().addChild(debugViz);
        }

        private void animateCamera() {
            getCurrentCamera().enableLookAt();
            getCurrentCamera().setLookAt(0, 0, 0);

            EllipticalOrbitAnimation3D a = new EllipticalOrbitAnimation3D(new Vector3(), new Vector3(20,
                    10, 20), Vector3.getAxisVector(Vector3.Axis.Y), 0, 360,
                    EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE);
            a.setDurationMilliseconds(20000);
            a.setRepeatMode(Animation.RepeatMode.INFINITE);
            a.setTransformable3D(getCurrentCamera());
            getCurrentScene().registerAnimation(a);
            a.play();
        }

        private Object3D createAnimatedSphere() {
            Object3D sphere = new Sphere(0.5f, 16, 12);
            Material sphereMaterial = new Material();
            sphereMaterial.enableLighting(true);
            sphereMaterial.setColor(Color.YELLOW);
            sphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
            sphere.setMaterial(sphereMaterial);
            getCurrentScene().addChild(sphere);

            RotateAroundAnimation3D a = new RotateAroundAnimation3D(
                    new Vector3(1, 0, 1),
                    Vector3.Axis.Y,
                    4
            );
            a.setDurationMilliseconds(6000);
            a.setRepeatMode(Animation.RepeatMode.INFINITE);
            a.setTransformable3D(sphere);
            getCurrentScene().registerAnimation(a);
            a.play();

            return sphere;
        }

        @Override
        public void onRender(final long elapsedTime, final double deltaTime) {
            mOtherCamera.setLookAt(mSphere.getPosition());
            mDirectionalLight.setLookAt(mSphere.getPosition());
            super.onRender(elapsedTime, deltaTime);
        }
    }
}
