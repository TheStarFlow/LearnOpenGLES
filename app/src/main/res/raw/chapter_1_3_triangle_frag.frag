#version 300 es
precision mediump float;
out vec4 vColor;
uniform vec4 sColor;
void main(){
    vColor = vec4(sColor.rgba);
}