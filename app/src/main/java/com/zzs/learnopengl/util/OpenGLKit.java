package com.zzs.learnopengl.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES31;
import android.opengl.GLUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author zzs
 * @Date 2021/6/17
 * @describ  加载 opengl的工具包
 */
public class OpenGLKit {

    public static String readRawTextFile(Context context, int rawId) {
        InputStream is = context.getResources().openRawResource(rawId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static int loadProgram(String fragmentShader, String vertexShader) {

        int fShader = GLES31.glCreateShader(GLES31.GL_FRAGMENT_SHADER);
        GLES31.glShaderSource(fShader,fragmentShader);
        GLES31.glCompileShader(fShader);
        int[] state = new int[1];
        GLES31.glGetShaderiv(fShader,GLES31.GL_COMPILE_STATUS,state,0);
        if (state[0]!=GLES31.GL_TRUE){
            String log = GLES31.glGetShaderInfoLog(fShader);
            //加载片段着色器失败
            throw new IllegalStateException("init fragment Shader failed : " + log);
        }


        int vShader = GLES31.glCreateShader(GLES31.GL_VERTEX_SHADER);
        GLES31.glShaderSource(vShader,vertexShader);
        GLES31.glCompileShader(vShader);
        GLES31.glGetShaderiv(vShader,GLES31.GL_COMPILE_STATUS,state,0);
        if (state[0]!=GLES31.GL_TRUE){
            String log = GLES31.glGetShaderInfoLog(vShader);
            //加载顶点着色器失败
            throw new IllegalStateException("init vertex Shader failed :" + log);
        }


        int program = GLES31.glCreateProgram();
        GLES31.glAttachShader(program,vShader);
        GLES31.glAttachShader(program,fShader);
        GLES31.glLinkProgram(program);

        //获得状态
        GLES31.glGetProgramiv(program, GLES31.GL_LINK_STATUS, state, 0);
        if (state[0] != GLES31.GL_TRUE) {
            throw new IllegalStateException("link program:" + GLES31.glGetProgramInfoLog(program));
        }
        GLES31.glDeleteShader(vShader);
        GLES31.glDeleteShader(fShader);
        return program;
    }

    public static int createTexture(Context context, int resId,int MIN_FILTER,int MAG_FILTER,int WRAP_S_ROUND,
                                    int WRAP_T_ROUND) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        if (bitmap!=null){
            int[] texture = new int[1];
            //生成一个纹理
            GLES31.glGenTextures(1,texture,0);
            //绑定为一个2D纹理
            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER,MIN_FILTER);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,GLES31.GL_TEXTURE_MAG_FILTER,MAG_FILTER);
            //设置环绕方向S，
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S,WRAP_S_ROUND);
            //设置环绕方向T,
            GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T,WRAP_T_ROUND);
            //绑定纹理数据
            GLUtils.texImage2D(GLES31.GL_TEXTURE_2D,0,bitmap,0);
            bitmap.recycle();
            return texture[0];
        }
        return 0;
    }

    public static int createTexture(Context context, int resId){
        return createTexture(context, resId,GLES31.GL_NEAREST,GLES31.GL_LINEAR,GLES31.GL_REPEAT,GLES31.GL_REPEAT);

    }
}
