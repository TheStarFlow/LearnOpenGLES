#version 300 es
layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec2 vCoord;
out vec2 sCoord;
out vec2 outCoord;
void main(){
    gl_Position = vPosition;
    sCoord = vPosition.xy;
}