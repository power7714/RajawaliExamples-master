package com.monyetmabuk.rajawali.tutorials.examples.materials.materials;

import com.monyetmabuk.rajawali.tutorials.R;

import org.rajawali3d.materials.shaders.FragmentShader;
import org.rajawali3d.util.RawShaderLoader;

public class CustomRawFragmentShader extends FragmentShader {

	private int muTextureInfluenceHandle;
	
	public CustomRawFragmentShader()
	{
		super();
		mNeedsBuild = false;
		initialize();
	}
	
	@Override
	public void initialize()
	{
		mShaderString = RawShaderLoader.fetch(R.raw.custom_frag_test);
	}
	

}
