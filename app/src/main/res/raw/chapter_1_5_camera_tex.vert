#version 300 es
layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec4 vCoord;
out vec2 sCoord;
uniform mat4 vMatrix;
void main(){
    gl_Position = vPosition;
    sCoord = ( vMatrix * vCoord).xy; //安卓端视频纹理采样想要正确  需要一个矩阵进行坐标变换
}

