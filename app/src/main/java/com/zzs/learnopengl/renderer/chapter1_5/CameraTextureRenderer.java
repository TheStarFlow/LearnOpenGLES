package com.zzs.learnopengl.renderer.chapter1_5;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLES30;
import android.opengl.GLES31;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseBufferOpenGLES;

/**
 * @author zzs
 * @Date 2022/1/24
 * @describe 视频纹理采样渲染  使用了 FBO 缓冲
 */
public class CameraTextureRenderer extends BaseBufferOpenGLES {

    private int vPosition;
    private int vTexCoord;
    private int[] mTexture;
    private int vMatrix;


    private float[] mMatrix;


    public CameraTextureRenderer(Context context) {
        super(context, R.raw.chapter_1_5_camera_tex, R.raw.chapter_1_5_camera_frag);
    }

    @Override
    protected void loadAttribute() {
        mTexture = new int[1];
        vPosition = GLES30.glGetAttribLocation(program, "vPosition");
        vTexCoord = GLES30.glGetAttribLocation(program, "vCoord");
        mTexture[0] = GLES30.glGetUniformLocation(program, "vTexture");
        vMatrix = GLES30.glGetUniformLocation(program, "vMatrix");
    }

    @Override
    protected void bindAttribute() {
        GLES30.glVertexAttribPointer(vPosition, 2, GLES31.GL_FLOAT, false, 16, 0);
        GLES30.glEnableVertexAttribArray(vPosition);
        GLES30.glVertexAttribPointer(vTexCoord, 2, GLES31.GL_FLOAT, false, 16, 8);
        GLES30.glEnableVertexAttribArray(vTexCoord);
    }

    @Override
    protected float[] getVertexArray() {
        return new float[]{
                -1.0f, -1.0f, 0.0f, 0.0f,
                1.0f, -1.0f, 1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f
        };
    }

    //FBO frame buffer object
    int[] frameBuffer;
    int[] frameData;

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        initFbo(width, height);
    }

    private void initFbo(int width, int height) {
        frameBuffer = new int[1];
        GLES30.glGenFramebuffers(1, frameBuffer, 0);
        frameData = new int[1];
        GLES30.glGenTextures(1, frameData, 0);
        //初始化纹理  绑定纹理的缩放展示方式
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, frameData[0]);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        //绑定纹理的颜色属性
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, frameData[0]);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, width, height, 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);
        // 把纹理绑定到制定buffer
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, frameData[0], 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
    }

    public void setMatrix(float[] mMatrix) {
        this.mMatrix = mMatrix;
    }

    @Override
    public void onDraw() {

    }
    /**
     *
     * @param texName 外部输入纹理
     * @return  返回绑定在fbp的纹理
     * */
    @Override
    public int onDraw(int texName) {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer[0]);
        GLES30.glUseProgram(program);
        if (mMatrix != null) {
            GLES30.glUniformMatrix4fv(vMatrix, 1, false, mMatrix, 0);
        }
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,texName);
        GLES30.glUniform1i(mTexture[0],0);
        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP,0,4);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        return frameData[0];
    }

    @Override
    public void release() {
        super.release();
        GLES30.glDeleteBuffers(1,frameBuffer,0);
        GLES30.glDeleteTextures(1,mTexture,0);
    }
}
