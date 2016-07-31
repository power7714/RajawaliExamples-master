precision mediump float;

varying vec2 vTextureCoord;
varying vec4 vColor;
uniform sampler2D uDiffuseTexture;
uniform sampler2D uAlphaTexture;

void main() {
    gl_FragColor = texture2D(uDiffuseTexture, vTextureCoord);

    float alpha = texture2D(uAlphaTexture, vTextureCoord).r;
    gl_FragColor.a = alpha;
}