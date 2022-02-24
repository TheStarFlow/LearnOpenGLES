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
    fragColor = vec4(0.0);
    vec3 col = vec3(0.0);
    float t1 = 4.0;
    vec2 uv = (sCoord*vec2(u_ResolutionWidth,u_ResolutionHeight))/u_ResolutionHeight/t1/2.0;
    uv += vec2(u_time/2.0,u_time/3.0)/t1/8.0;
    float scale = c1.z;
    vec2 t2 = vec2(.0);
    vec2 t3 = vec2(.0);
    for(int k = 0;k<6*iterations;k++){
        uv -= (t2.yx)/(scale)+float(k%modulo);
        t2 = triangle_wave(uv.yx-.5,scale);
        t3 = -triangle_wave(uv,scale);
        t3 /= float(1+k%3);
        uv.yx = -(t2+t3)/scale;
        col.x = 1.-abs(-uv.y+uv.x+col.x);
        col = col.yzx;
        uv /= scale*scale;
    }
    fragColor = vec4(col,1.0);
}