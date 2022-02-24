#version 300 es
#define iterations 3
#define modulo 3
#define c1 vec3(1.0, 0.5, 1.5)
precision mediump float;

in vec2 sCoord;
uniform float u_time;
out vec4 fragColor;
uniform float u_ResolutionWidth;
uniform float u_ResolutionHeight;


vec2 triangle_wave(vec2 a, float scale){
    return abs(fract((a+c1.xy)*scale)-0.5);
}

void main(){
    vec2 uv = sCoord;
    fragColor = vec4(uv.x,uv.y,0.0,1.0);
}