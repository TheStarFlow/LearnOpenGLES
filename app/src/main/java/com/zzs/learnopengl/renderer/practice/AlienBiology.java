package com.zzs.learnopengl.renderer.practice;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseBufferOpenGLES;

/**
 * @author zzs
 * @Date 2022/2/24
 * @describe
 */
public class AlienBiology extends BaseBufferOpenGLES {

    private int vPosition;
    private int vCoord;
    private int uTime;
    private int uResolutionWidth;
    private int uResolutionHeight;
    private long static_u_time;
    private float sTime = 0f;

    public AlienBiology(Context context) {
        super(context, R.raw.practice_alien_biology_vert, R.raw.practice_alien_biology_frag);
        static_u_time = System.currentTimeMillis();
    }

    @Override
    protected void loadAttribute() {
        vPosition = GLES30.glGetAttribLocation(program, "vPosition");
        vCoord = GLES30.glGetAttribLocation(program, "vCoord");
        uTime = GLES30.glGetUniformLocation(program, "u_time");
        uResolutionWidth = GLES30.glGetUniformLocation(program, "u_ResolutionWidth");
        uResolutionHeight = GLES30.glGetUniformLocation(program, "u_ResolutionHeight");

    }

    @Override
    protected void bindAttribute() {
        GLES30.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 16, 0);
        GLES30.glEnableVertexAttribArray(vPosition);

        GLES30.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 16, 8);
        GLES30.glEnableVertexAttribArray(vCoord);

    }

    @Override
    protected float[] getVertexArray() {
        return new float[]{
                //顶点         //  纹理
//                -1.0f, -1.0f, 0.0f, 0.0f,
//                1.0f, -1.0f, 1.0f, 0.0f,
//                -1.0f, 1.0f, 0.0f, 1.0f,
//                1.0f, 1.0f, 1.0f, 1.0f
                //搬运 shader toy 效果  纹理方向跟一般安卓渲染纹理方向不太一样 注意这里
                -1.0f, -1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 0.0f, 1.0f,
                -1.0f, 1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,0.0f

        };
    }

    @Override
    public void onDraw() {
        float dTime = System.currentTimeMillis()-static_u_time;
        sTime = dTime / 1000f;
        float remain = dTime % 1000f;
        sTime+= remain/1000f;
        GLES30.glUseProgram(program);
        Log.d("setTime",""+sTime);
        GLES30.glUniform1f(uTime,sTime);
        GLES30.glUniform1f(uResolutionWidth, width);
        GLES30.glUniform1f(uResolutionHeight, height);
        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
