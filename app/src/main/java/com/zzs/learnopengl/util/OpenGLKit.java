package com.zzs.learnopengl.util;

import android.content.Context;
import android.opengl.GLES31;
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
        if (state[0]!=GLES31.GL_TRUE)
            //加载片段着色器失败
            throw new IllegalStateException("init fragment Shader failed");

        int vShader = GLES31.glCreateShader(GLES31.GL_VERTEX_SHADER);
        GLES31.glShaderSource(vShader,vertexShader);
        GLES31.glCompileShader(vShader);
        GLES31.glGetShaderiv(vShader,GLES31.GL_COMPILE_STATUS,state,0);
        if (state[0]!=GLES31.GL_TRUE)
        //加载顶点着色器失败
            throw new IllegalStateException("init vertex Shader failed");

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
}
