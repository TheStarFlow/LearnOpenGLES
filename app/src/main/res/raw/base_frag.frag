precision mediump float;//指定精度
varying vec2 aCoord;

uniform sampler2D  vTexture;

void main(){
    vec4 rgba = texture2D(vTexture,aCoord);  //rgba
    gl_FragColor = rgba;
}