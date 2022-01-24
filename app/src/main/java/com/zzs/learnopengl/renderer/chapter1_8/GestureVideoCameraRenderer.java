package com.zzs.learnopengl.renderer.chapter1_8;

import android.animation.ValueAnimator;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.Vec3;
import com.zzs.learnopengl.util.BaseBufferOpenGLES;
import com.zzs.learnopengl.util.OpenGLKit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzs
 * @Date 2022/1/18
 * @describe
 */
public class GestureVideoCameraRenderer extends BaseBufferOpenGLES {

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

    private List<Vec3> coords;
    private ValueAnimator mDegreeAnim;
    private Vec3 mCameraPos = new Vec3(0.0f, 0.0f, 3.0f);
    private Vec3 mCameraFront = new Vec3(0.0f, 0.0f, -1.0f);
    private Vec3 mCameraUp = new Vec3(0.0f, 1.0f, 0.0f);
    private GLSurfaceView mSurfaceView;

    int[] frameBuffer;
    int[] frameData;


    // private static float[] mCamera = new float[16];


    public GestureVideoCameraRenderer(Context context,int resId,int resId2) {
        super(context, R.raw.chapter_1_7_coords_vert, R.raw.chapter_1_8_coord_frag_video);
        mTextureId = new int[1];
        mTextureId2 = new int[1];
        //创建纹理
       // mTextureId[0] = OpenGLKit.createTexture(context, resId);
      //  mTextureId2[0] = OpenGLKit.createTexture(context, resId2, GLES31.GL_NEAREST, GLES31.GL_LINEAR, GLES31.GL_CLAMP_TO_EDGE, GLES31.GL_CLAMP_TO_EDGE);
        //启用深度测试  前面的像素覆盖后面的像素
        GLES31.glEnable(GLES31.GL_DEPTH_TEST);
    }

    @Override
    protected void loadAttribute() {
        //获取句柄
        vPosition = GLES31.glGetAttribLocation(program, "vPosition");
        vTexCoord = GLES31.glGetAttribLocation(program, "aTexCoord");
        mTexture1 = GLES31.glGetUniformLocation(program, "outTexture");
        mTexture2 = GLES31.glGetUniformLocation(program, "outTexture2");
        model = GLES31.glGetUniformLocation(program, "model");
        view = GLES31.glGetUniformLocation(program, "view");
        projection = GLES31.glGetUniformLocation(program, "projection");
        coords = new ArrayList<>();
        coords.add(new Vec3(0.0f, 0.0f, 0.0f));
        coords.add(new Vec3(2.0f, 3.0f, -5.0f));
        coords.add(new Vec3(-1.5f, -2.2f, -2.5f));
        coords.add(new Vec3(-3.8f, -2.0f, -3.3f));
        coords.add(new Vec3(2.4f, -0.4f, -3.5f));
        coords.add(new Vec3(-1.7f, 3.0f, -4.5f));
        coords.add(new Vec3(1.3f, -2.0f, -2.5f));
        coords.add(new Vec3(1.5f, 2.0f, -2.5f));
        coords.add(new Vec3(1.5f, 0.2f, -1.5f));
        coords.add(new Vec3(-1.3f, 1.0f, -1.5f));
    }

    @Override
    protected void bindAttribute() {
        //绑定VBO的数据 VBO绑定在VAO上
        GLES31.glVertexAttribPointer(vPosition, 3, GLES31.GL_FLOAT, false, 20, 0);
        GLES31.glEnableVertexAttribArray(vPosition);

        GLES31.glVertexAttribPointer(vTexCoord, 2, GLES31.GL_FLOAT, false, 20, 12);
        GLES31.glEnableVertexAttribArray(vTexCoord);
    }

    @Override
    protected float[] getVertexArray() {
        return new float[]{// 一个正方体的顶点数组
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
        };
    }

    @Override
    public void onDraw() {

    }

    @Override
    public int onDraw(int texName) {
        //置为单位矩阵
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setIdentityM(mProjectionMatrix, 0);

        Vec3 vec = mCameraPos.add(mCameraFront);
        Matrix.setLookAtM(mViewMatrix, 0, mCameraPos.getX(), mCameraPos.getY(), mCameraPos.getZ(), vec.getX(), vec.getY(), vec.getZ(), mCameraUp.getX(), mCameraUp.getY(), mCameraUp.getZ());
        //  Matrix.setIdentityM(mCamera,0);
        //变换矩阵
        //  Matrix.rotateM(mModelMatrix,0,mRotateDegree,1f,0.5f,0.2f);
        Matrix.perspectiveM(mProjectionMatrix, 0, 45f, width / height, 0.1f, 100f);//一般的固定设置

        GLES31.glUseProgram(program);
        // GLES31.glUniformMatrix4fv(model,1,false,mModelMatrix,0);
        GLES31.glUniformMatrix4fv(view, 1, false, mViewMatrix, 0);
        GLES31.glUniformMatrix4fv(projection, 1, false, mProjectionMatrix, 0);

        //因为我们使用了深度测试，我们也想要在每次渲染迭代之前清除深度缓冲（否则前一帧的深度信息仍然保存在缓冲中）。
        // 就像清除颜色缓冲一样，我们可以通过在glClear函数中指定DEPTH_BUFFER_BIT位来清除深度缓冲：
        GLES31.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//        //激活纹理
//        GLES31.glActiveTexture(GLES20.GL_TEXTURE0);
//        //绑定纹理
//        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mTextureId[0]);
//        //指定 采样器 mTexture1 采样 index 为 0（既 GLES20.GL_TEXTURE0）所绑定的纹理 mTextureId[0]
//        GLES31.glUniform1i(mTexture1, 0);
//
        GLES31.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES31.glBindTexture(GLES20.GL_TEXTURE_2D, texName);
        GLES31.glUniform1i(mTexture2, 1);

        GLES31.glBindVertexArray(VAO[0]);
        for (int i = 0; i < coords.size(); i++) {
            Vec3 vec3 = coords.get(i);
            Matrix.setIdentityM(mModelMatrix, 0);
            float degree = 20f * i;
            Matrix.translateM(mModelMatrix, 0, vec3.getX(), vec3.getY(), vec3.getZ());
            Matrix.rotateM(mModelMatrix, 0, degree, 1.0f, 0.3f, 0.5f);
            GLES31.glUniformMatrix4fv(model, 1, false, mModelMatrix, 0);
            GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 36);
        }
        return texName;
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
    /**
     * cameraPos  x,y,z   x 变大  观察视角向右移动（衬托物体向左）  x 减小 观察视角向左移动（衬托物体向右）
     *                    y 变大  观察视角向上移动（衬托物体向下）  y 减小 观察视角向下移动（衬托物体向上）
     *                    z 变大  垂直屏幕向外拉远（衬托物体变小）  z 减小 垂直屏幕向里拉近（衬托物体变大）
     *
     *
     * */
    public void moveCamera(float dx, float dy, float speed) {
       mCameraPos.setX(mCameraPos.getX()+dx*speed);
       mCameraPos.setY(mCameraPos.getY()+dy*speed);
        if (mSurfaceView!=null){
            mSurfaceView.requestRender();
        }
    }

    public void setGlSurface(@NotNull GLSurfaceView myGLSurfaceView) {
            mSurfaceView = myGLSurfaceView;
    }

    public void initFBO(int width, int height) {
        frameBuffer = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffer, 0);

        frameData = new int[1];
        GLES20.glGenTextures(1, frameData, 0);

        //do bind
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameData[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        //do operation
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameData[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        //do bind frame buffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, frameData[0], 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public void doBindBuffer() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
    }

    public void unBindBuffer() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public int getFboTex() {
        return frameData[0];
    }


}
