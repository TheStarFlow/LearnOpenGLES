#version 300 es
precision mediump float; //需要制定精度  否则一些低版本可能报错
out vec4 vColor;

in vec4 ourColor;
in vec2 TexCoord;

uniform sampler2D outTexture;
uniform sampler2D outTexture2;

void main(){
    vec2 reverseTexCoord = vec2(TexCoord.x,1.0 -TexCoord.y);
    vColor = mix(texture(outTexture,reverseTexCoord),texture(outTexture2,reverseTexCoord),0.2); //正常渲染纹理（会翻转）
}
