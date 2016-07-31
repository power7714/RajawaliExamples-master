package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;

import android.opengl.GLES20;

import org.rajawali3d.materials.Material;

/**
 * Created by CurTro Studios on 7/30/2016.
 */

public class BasicMaterial extends Material {
    protected static final String mFShader = "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D uDiffuseTexture;\nvarying vec4 vColor;\nfloat alphaRecip;\nvoid main() {\n\tvec4 finalColor = texture2D(uDiffuseTexture, vTextureCoord);\n\talphaRecip = 1.0 / finalColor.a;\n\tfinalColor.r = finalColor.r * alphaRecip;\n\tfinalColor.g = finalColor.g * alphaRecip;\n\tfinalColor.b = finalColor.b * alphaRecip;\n#ifdef TEXTURED\n\tgl_FragColor = finalColor;\n#else\n\tgl_FragColor = finalColor * vColor;\n#endif\n}\n";
    protected static final String mVShader = "uniform mat4 uMVPMatrix;\nattribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nattribute vec4 aColor;\nuniform float textureOffsetX;\nuniform float textureOffsetY;\nvarying vec2 vTextureCoord;\nvarying vec4 vColor;\nvoid main() {\n\tgl_Position = uMVPMatrix * aPosition;\n\tvTextureCoord = aTextureCoord;\n\tvColor = aColor;\n}\n";
    protected float mTextureOffsetX;
    protected int mTextureOffsetXHandle;
    protected float mTextureOffsetY;
    protected int mTextureOffsetYHandle;

    public BasicMaterial() {
        //super(mVShader, mFShader, false);
        setShaders(mVShader,mFShader);
    }

    public BasicMaterial(String vertexShader, String fragmentShader) {
        //super(vertexShader, fragmentShader, false);
        setShaders(mVShader, mFShader);
        //this.getU
    }

    public void setShaders(String vertexShader, String fragmentShader) {
        setShaders(vertexShader, fragmentShader);
        //this.mTextureOffsetXHandle = this.getUniformLocation("textureOffsetX");
        //this.mTextureOffsetYHandle = getUniformLocation("textureOffsetY");
    }

    public void setTextureOffsetX(float textureOffsetX) {
        this.mTextureOffsetX = textureOffsetX;
        GLES20.glUniform1f(this.mTextureOffsetXHandle, this.mTextureOffsetX);
    }

    public void setTextureOffsetY(float textureOffsetY) {
        this.mTextureOffsetY = textureOffsetY;
        GLES20.glUniform1f(this.mTextureOffsetYHandle, this.mTextureOffsetY);
    }
}