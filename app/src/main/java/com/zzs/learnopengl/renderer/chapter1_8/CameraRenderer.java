package com.zzs.learnopengl.renderer.chapter1_8;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.Matrix;
import android.os.SystemClock;

import androidx.arch.core.executor.ArchTaskExecutor;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.Vec3;
import com.zzs.learnopengl.util.BaseBufferOpenGLES;
import com.zzs.learnopengl.util.OpenGLKit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzs
 * @Date 2022/1/5
 * @describe
 */
public class CameraRenderer extends BaseBufferOpenGLES {

    private int vPosition;
    private int vTexCoord;

    private int mTexture1;
    private int mTexture2;

    private final int[] mTextureId;
    private final int[] mTextureId2;

    private int model;
    private int view;
    private int projection;

    protected float mRotateDegree;
    protected float mZDis = 3f;

    private static float[] mModelMatrix = new float[16]; // 模型矩阵对物体进行操作
    private static float[] mViewMatrix = new float[16]; //观察矩阵移动距离
    private static float[] mProjectionMatrix = new float[16]; //有透视效果（顶点越远，变得越小）。

    private List<Vec3> coords ;
    private ValueAnimator mDegreeAnim;

   // private static float[] mCamera = new float[16];


    public CameraRenderer(Context context, int resId, int resId2) {
        super(context, R.raw.chapter_1_7_coords_vert, R.raw.chapter_1_7_coord_frag);
        mTextureId = new int[1];
        mTextureId2 = new int[1];
        //创建纹理
        mTextureId[0] = OpenGLKit.createTexture(context,resId);
        mTextureId2[0] = OpenGLKit.createTexture(context,resId2, GLES31.GL_NEAREST,GLES31.GL_LINEAR,GLES31.GL_CLAMP_TO_EDGE,GLES31.GL_CLAMP_TO_EDGE);
        //启用深度测试  后面的像素覆盖前面的像素
        GLES31.glEnable(GLES31.GL_DEPTH_TEST);
        createDegreeAnim();
    }

    private volatile int degree;

    @SuppressLint("RestrictedApi")
    private void createDegreeAnim() {
        ArchTaskExecutor.getInstance().postToMainThread(new Runnable() {
            @Override
            public void run() {
                mDegreeAnim = ValueAnimator.ofInt(1,360);
                mDegreeAnim.setInterpolator(null);
                mDegreeAnim.setRepeatCount(ValueAnimator.INFINITE);
                mDegreeAnim.setDuration(20000);
                mDegreeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        degree = (int) animation.getAnimatedValue();
                    }
                });
                mDegreeAnim.start();
            }
        });

    }

    @Override
    protected void loadAttribute() {
        //获取句柄
        vPosition = GLES31.glGetAttribLocation(program,"vPosition");
        vTexCoord = GLES31.glGetAttribLocation(program,"aTexCoord");
        mTexture1 = GLES31.glGetUniformLocation(program,"outTexture");
        mTexture2 = GLES31.glGetUniformLocation(program,"outTexture2");
        model = GLES31.glGetUniformLocation(program,"model");
        view = GLES31.glGetUniformLocation(program,"view");
        projection = GLES31.glGetUniformLocation(program,"projection");
        coords  = new ArrayList<>();
        coords.add(new Vec3(0.0f,0.0f,0.0f));
        coords.add(new Vec3(2.0f,  5.0f, -15.0f));
        coords.add(new Vec3(-1.5f, -2.2f, -2.5f));
        coords.add(new Vec3(-3.8f, -2.0f, -12.3f));
        coords.add(new Vec3(2.4f, -0.4f, -3.5f));
        coords.add(new Vec3(-1.7f,  3.0f, -7.5f));
        coords.add(new Vec3(1.3f, -2.0f, -2.5f));
        coords.add(new Vec3(1.5f,  2.0f, -2.5f));
        coords.add(new Vec3(1.5f,  0.2f, -1.5f));
        coords.add(new Vec3(-1.3f,  1.0f, -1.5f));
    }

    @Override
    protected void bindAttribute() {
        //绑定VBO的数据 VBO绑定在VAO上
        GLES31.glVertexAttribPointer(vPosition,3, GLES31.GL_FLOAT,false,20,0);
        GLES31.glEnableVertexAttribArray(vPosition);

        GLES31.glVertexAttribPointer(vTexCoord,2, GLES31.GL_FLOAT,false,20,12);
        GLES31.glEnableVertexAttribArray(vTexCoord);
    }

    @Override
    protected float[] getVertexArray() {
        return new float[]{// 一个正方体的顶点数组
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };
    }

    @Override
    public void onDraw() {
        //置为单位矩阵
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.setIdentityM(mViewMatrix,0);
        Matrix.setIdentityM(mProjectionMatrix,0);

        float radius = 10f;
        float camX = (float) (Math.sin(Math.toRadians(degree))*radius);
        float camZ = (float) (Math.cos(Math.toRadians(degree))*radius);
        Matrix.setLookAtM(mViewMatrix,0,camX,0.0f,camZ,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f);
        //  Matrix.setIdentityM(mCamera,0);
        //变换矩阵
      //  Matrix.rotateM(mModelMatrix,0,mRotateDegree,1f,0.5f,0.2f);
        Matrix.perspectiveM(mProjectionMatrix,0,45f,width/height,0.1f,100f);//一般的固定设置

        GLES31.glUseProgram(program);
       // GLES31.glUniformMatrix4fv(model,1,false,mModelMatrix,0);
        GLES31.glUniformMatrix4fv(view,1,false,mViewMatrix,0);
        GLES31.glUniformMatrix4fv(projection,1,false,mProjectionMatrix,0);

        //因为我们使用了深度测试，我们也想要在每次渲染迭代之前清除深度缓冲（否则前一帧的深度信息仍然保存在缓冲中）。
        // 就像清除颜色缓冲一样，我们可以通过在glClear函数中指定DEPTH_BUFFER_BIT位来清除深度缓冲：
        GLES31.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        //激活纹理
        GLES31.glActiveTexture(GLES20.GL_TEXTURE0);
        //绑定纹理
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,mTextureId[0]);
        //指定 采样器 mTexture1 采样 index 为 0（既 GLES20.GL_TEXTURE0）所绑定的纹理 mTextureId[0]
        GLES31.glUniform1i(mTexture1,0);
        GLES31.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES31.glBindTexture(GLES20.GL_TEXTURE_2D,mTextureId2[0]);
        GLES31.glUniform1i(mTexture2,1);

        GLES31.glBindVertexArray(VAO[0]);

        for (int i = 0; i < coords.size(); i++) {
            Vec3 vec3 = coords.get(i);
            Matrix.setIdentityM(mModelMatrix,0);
            float degree = 20f*i;
            Matrix.translateM(mModelMatrix,0,vec3.getX(),vec3.getY(),vec3.getZ());
            Matrix.rotateM(mModelMatrix,0,degree,1.0f,0.3f,0.5f);
            GLES31.glUniformMatrix4fv(model,1,false,mModelMatrix,0);
            GLES31.glDrawArrays(GLES31.GL_TRIANGLES,0,36);
        }

        // GLES31.glDrawElements(GLES31.GL_TRIANGLES,6,GLES31.GL_UNSIGNED_INT,indexBuffer);
        //GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,0);
    }

    @Override
    public void release() {
        //不确定这些释放是否必要，因为已经将其绑定的VAO VBO 释放 2021/12/30
        GLES31.glDisableVertexAttribArray(vPosition);
        GLES31.glDisableVertexAttribArray(vTexCoord);
        super.release();
    }

    @Override
    public void setOnRotateChange(int progress) {
        super.setOnRotateChange(progress);
       // mRotateDegree = progress;
    }

    @Override
    public void setOnZChange(int progress) {
        super.setOnZChange(progress);
       // mZDis = progress;
    }
}
