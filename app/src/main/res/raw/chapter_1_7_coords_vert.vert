#version 300 es
layout (location = 0) in  vec4 vPosition;
layout (location = 2) in vec2 aTexCoord;
out vec2 TexCoord;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
void main(){
    gl_Position = projection * view * model * vPosition;
    TexCoord = aTexCoord;
}