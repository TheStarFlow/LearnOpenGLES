#version 300 es
precision mediump float; //需要制定精度  否则一些低版本可能报错
out vec4 vColor;

in vec4 ourColor;
in vec2 TexCoord;

uniform sampler2D outTexture;
uniform sampler2D outTexture2;

void main(){
   // vColor =  texture(outTexture, TexCoord) * ourColor; //单个纹理采样
    vColor = mix(texture(outTexture,TexCoord),texture(outTexture2,TexCoord),0.2);
}

//在使用OpenGL函数加载纹理到图形时，经常遇到纹理上下颠倒的问题。原因是因为OpenGL要求纹理坐标原点在图片最下面，
//而图片信息中的原点一般都在最上方，一行行记录下来的，就会导致整个图片上下颠倒了。
//从这个思路出发共有三种办法解决问题：
//1.翻转纹理坐标 因为纹理坐标的范围是0-1，所以翻转的话都统一用1去减
//2.翻转着色器的Y轴坐标
//3.直接翻转纹理图片Y轴顶点
//@link https://blog.csdn.net/xipiaoyouzi/article/details/53611585