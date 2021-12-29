attribute vec4 vPosition;//0
//接收纹理坐标，接收采样器采样图片的坐标
attribute vec4 vCoord;

uniform mat4 vMatrix;

varying vec2 aCoord;
void main(){
    gl_Position=vPosition;
    aCoord= (vMatrix * vCoord).xy;
}
