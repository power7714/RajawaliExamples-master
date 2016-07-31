uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uCameraPosition;
uniform float uTime;
attribute vec4 aPosition;
attribute vec3 aNormal;
attribute vec2 aTextureCoord;
attribute vec4 aColor;
varying vec2 vTextureCoord;
varying vec4 vColor;


void main() {
    vec4 position = aPosition;
    vec3 normal = aNormal;
    float scale = 0.5;
    float s = sin( (uTime+2.0*position.y)*scale )+sin( (uTime+4.0*position.y)*scale )+sin( (uTime+6.0*position.y)*scale )+sin( (uTime+8.0*position.y)*scale );
    float c = cos( (uTime+4.0*position.x)*scale )+ cos( (uTime+3.0*position.x)*scale )+ cos( (uTime+6.0*position.x)*scale )+ cos( (uTime+2.0*position.x)*scale );
    float z = .5 * s * c;
    vec3 v = position.xyz + vec3(normal.xy * z, 0.0);
    gl_Position = uMVPMatrix * vec4( v, 1.0 );
    vTextureCoord = aTextureCoord;
    vColor = aColor;

}