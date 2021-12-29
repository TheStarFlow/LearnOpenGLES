#version 300 es
layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec3 vColor;
out vec4 ourColor;
uniform float hor;
void main(){
    float dis = hor;
    dis = vPosition.x+hor;
    gl_Position = vec4(dis,-vPosition.y,vPosition.z,vPosition.w);
    ourColor = vec4(vColor.xyz,1.0);
}