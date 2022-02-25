#version 300 es

precision mediump float;

in vec2 sCoord;
out vec4 fragColor;
uniform float u_ResolutionWidth;
uniform float u_ResolutionHeight;
uniform vec4 u_time;

#define F length(.5-fract(fragColor.xyw*=mat3(-2,-1,2, 3,-2,1, 1,2,2)*


void main(){
    vec2 fragCoord = sCoord * vec2(u_ResolutionWidth,u_ResolutionHeight);
    fragColor.xy = fragCoord * (sin(fragColor = u_time*0.2).w+2.0)/2e2;
    fragColor = pow(min(min(F.5)),F.4))),F.3))), 7.)*25.+vec4(0,.35,.5,1);

}