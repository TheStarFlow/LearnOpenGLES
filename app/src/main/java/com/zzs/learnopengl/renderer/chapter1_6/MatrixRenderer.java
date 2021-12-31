package com.zzs.learnopengl.renderer.chapter1_6;

import android.animation.ValueAnimator;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.LinearInterpolator;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseBufferOpenGLES;
import com.zzs.learnopengl.util.OpenGLKit;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author zzs
 * @Date 2021/12/30
 * @describe
 */
public class MatrixRenderer extends BaseBufferOpenGLES {

    private int vPosition;
    private int sColor;
    private int vTexCoord;
    private static int[] indices = new int[]{0, 1, 3, 1, 2, 3};

    float color[] = {0.0f, 1.0f, 0.5f, 1f};
    private IntBuffer indexBuffer;
    private int mTexture1;
    private int mTexture2;

    private int[] mTextureId;
    private int[] mTextureId2;

    private int mMatrix;
    private FloatBuffer floatBuffer;
    private static float[] matrix = new float[16];
    private ValueAnimator animator;
    private float mCurrDegree;
    private Handler handler = new Handler(Looper.getMainLooper());

    public MatrixRenderer(Context context, int resId, int resId2) {
        super(context, R.raw.chapter_1_6_maxtrix_vert, R.raw.chapter_1_6_matrix_frag);
        mTextureId = new int[1];
        mTextureId2 = new int[1];
        mTextureId[0] = OpenGLKit.createTexture(context, resId);
        mTextureId2[0] = OpenGLKit.createTexture(context, resId2, GLES31.GL_NEAREST, GLES31.GL_LINEAR, GLES31.GL_CLAMP_TO_EDGE, GLES31.GL_CLAMP_TO_EDGE);
        handler.post(new Runnable() {
            @Override
            public void run() {
                animator = ValueAnimator.ofFloat(0f,360f);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mCurrDegree = (float) animation.getAnimatedValue();
                            Matrix.setIdentityM(matrix,0);
                            Matrix.rotateM(matrix,0,mCurrDegree,0.0f,0.0f,1.0f);
                            floatBuffer.clear();
                            floatBuffer.put(matrix);
                            floatBuffer.position(0);
                    }
                });
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(20000);
               // animator.start();
            }
        });
    }

    @Override
    protected void loadAttribute() {
        vPosition = GLES31.glGetAttribLocation(program, "vPosition");
        sColor = GLES31.glGetAttribLocation(program, "sColor");
        vTexCoord = GLES31.glGetAttribLocation(program, "aTexCoord");
        mTexture1 = GLES31.glGetUniformLocation(program, "outTexture");
        mTexture2 = GLES31.glGetUniformLocation(program, "outTexture2");
        mMatrix = GLES31.glGetUniformLocation(program,"transform");
        indexBuffer = IntBuffer.allocate(indices.length);
        indexBuffer.clear();
        indexBuffer.put(indices);
        indexBuffer.position(0);
        floatBuffer = FloatBuffer.allocate(16);
        floatBuffer.clear();
        //生成单位矩阵
        Matrix.setIdentityM(matrix,0);
        //将矩阵 围绕（0,0,1）轴旋转 60 度
        Matrix.rotateM(matrix,0,-60f,0f,0f,1f);
        //将矩阵 按照 x,y,z 缩放
        Matrix.scaleM(matrix,0,1f,1f,1f);
        floatBuffer.put(matrix);
        floatBuffer.position(0);


    }

    @Override
    protected void bindAttribute() {
        //没有使用VAO  VBO
        GLES31.glVertexAttribPointer(vPosition, 3, GLES31.GL_FLOAT, false, 32, 0);
        GLES31.glEnableVertexAttribArray(vPosition);

        GLES31.glVertexAttribPointer(sColor, 3, GLES31.GL_FLOAT, false, 32, 12);
        GLES31.glEnableVertexAttribArray(sColor);

        GLES31.glVertexAttribPointer(vTexCoord, 2, GLES31.GL_FLOAT, false, 32, 24);
        GLES31.glEnableVertexAttribArray(vTexCoord);
    }

    @Override
    protected float[] getVertexArray() {
        return new float[]{
                0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,   // 右上
                0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,   // 右下
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,   // 左下
                -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f    // 左上
        };
    }

    @Override
    public void onDraw() {
        GLES31.glUseProgram(program);

        floatBuffer.position(0);
        GLES31.glUniformMatrix4fv(mMatrix,1,false,floatBuffer);//这里没有使用缓存 直接给着色器的矩阵赋值，后面可以尝试像顶点数组那样子使用缓冲对象 VBO给矩阵赋值
        //激活纹理
        GLES31.glActiveTexture(GLES20.GL_TEXTURE0);
        //绑定纹理
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,mTextureId[0]);
        GLES31.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES31.glUniform1i(mTexture1,0);
        GLES31.glBindTexture(GLES20.GL_TEXTURE_2D,mTextureId2[0]);
        GLES31.glUniform1i(mTexture2,1);

        GLES31.glBindVertexArray(VAO[0]);
        GLES31.glDrawElements(GLES31.GL_TRIANGLES,6,GLES31.GL_UNSIGNED_INT,indexBuffer);

        Matrix.setIdentityM(matrix,0);
        //将矩阵 围绕（0,0,1）轴旋转 60 度
        Matrix.rotateM(matrix,0,-120f,0f,0f,1f);
        //将矩阵 按照 x,y,z 缩放
        Matrix.scaleM(matrix,0,1f,1f,1f);
        Matrix.translateM(matrix,0,0.5f,-0.5f,0f);
        floatBuffer.clear();
        floatBuffer.put(matrix);
        floatBuffer.position(0);
        GLES31.glUniformMatrix4fv(mMatrix,1,false,floatBuffer);//这里没有使用缓存 直接给着色器的矩阵赋值，后面可以尝试像顶点数组那样子使用缓冲对象 VBO给矩阵赋值


        GLES31.glDrawElements(GLES31.GL_TRIANGLES,6,GLES31.GL_UNSIGNED_INT,indexBuffer);

        GLES31.glBindVertexArray(0);
    }

    @Override
    public void release() {
        //不确定这些释放是否必要，因为已经将其绑定的VAO VBO 释放 2021/12/30
        GLES31.glDeleteBuffers(1,indexBuffer);
        GLES31.glDisableVertexAttribArray(vPosition);
        GLES31.glDisableVertexAttribArray(sColor);
        GLES31.glDisableVertexAttribArray(vTexCoord);
        super.release();
    }
}
