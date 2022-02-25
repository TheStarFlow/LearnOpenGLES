package com.zzs.learnopengl.renderer.practice;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import androidx.arch.core.executor.ArchTaskExecutor;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseBufferOpenGLES;

import java.util.Calendar;
import java.util.Date;

/**
 * @author zzs
 * @Date 2022/2/25
 * @describe
 */
public class Ripples extends BaseBufferOpenGLES {

    private int vPosition;
    private int vCoord;
    private int uTime;
    private int uResolutionWidth;
    private int uResolutionHeight;

    private long static_u_time;
    private volatile float sTime = 0f;
    private ValueAnimator animator;

    @SuppressLint("RestrictedApi")
    public Ripples(Context context) {
        super(context, R.raw.practice_alien_biology_vert, R.raw.practice_ripples_frag);
        static_u_time = System.currentTimeMillis();
        ArchTaskExecutor.getInstance().postToMainThread(new Runnable() {
            @Override
            public void run() {
                animator = ValueAnimator.ofFloat(11000f,12000f);
                animator.setDuration(150000L);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        sTime = (float) animation.getAnimatedValue();
                    }
                });
                animator.start();
            }
        });

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
                //搬运 shader toy 效果  纹理方向跟一般安卓渲染纹理方向不太一样 注意这里
                -1.0f, -1.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 0.0f, 1.0f,
                -1.0f, 1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f, 0.0f

        };
    }

    @Override
    public void onDraw() {
        GLES30.glUseProgram(program);
        GLES30.glUniform4f(uTime, Calendar.getInstance().get(Calendar.YEAR
        ), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                sTime);
        Log.i("sTime","sTime = "+sTime);
        GLES30.glUniform1f(uResolutionWidth, width);
        GLES30.glUniform1f(uResolutionHeight, height);
        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
