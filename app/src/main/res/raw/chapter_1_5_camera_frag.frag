#version 300 es
#extension GL_OES_EGL_image_external : require
precision mediump float;
in vec2 sCoord;
out vec4 fragColor;
uniform samplerExternalOES vTexture;  //安卓下声明使用 samplerExternalOES 需要指定第二行
void main(){
    fragColor = texture(vTexture,sCoord);
}