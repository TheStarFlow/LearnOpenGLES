#version 300 es
layout (location = 0) in  vec4 vPosition;
layout (location = 1) in vec4 sColor;
layout (location = 2) in vec2 aTexCoord;
out vec4 ourColor;
out vec2 TexCoord;
void main(){
    gl_Position = vPosition;
    ourColor = sColor;
    TexCoord = aTexCoord;
}