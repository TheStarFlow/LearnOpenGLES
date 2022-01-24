package com.zzs.learnopengl.renderer.chapter1_5;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseBufferOpenGLES;

/**
 * @author zzs
 * @Date 2022/1/24
 * @describe  普通纹理渲染
 */
public class NormalTextureRenderer extends BaseBufferOpenGLES {

    private int vPosition;
    private int vCoord;
    private int mTexture;


    public NormalTextureRenderer(Context context) {
        super(context, R.raw.chapter_1_5_normal_vert, R.raw.chapter_1_5_normal_frag);
    }

    @Override
    protected void loadAttribute() {
        vPosition = GLES30.glGetAttribLocation(program,"vPosition");
        vCoord = GLES30.glGetAttribLocation(program,"vCoord");
        mTexture = GLES30.glGetUniformLocation(program,"vTexture");
    }

    @Override
    protected void bindAttribute() {
        GLES30.glVertexAttribPointer(vPosition,2, GLES20.GL_FLOAT,false,16,0);
        GLES30.glEnableVertexAttribArray(vPosition);

        GLES30.glVertexAttribPointer(vCoord,2, GLES20.GL_FLOAT,false,16,8);
        GLES30.glEnableVertexAttribArray(vCoord);
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

    @Override
    public void onDraw() {

    }

    @Override
    public int onDraw(int texName) {
        GLES30.glUseProgram(program);
        GLES30.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES30.glBindTexture(GLES20.GL_TEXTURE_2D,texName);
        GLES30.glUniform1ui(mTexture,0);
        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        GLES30.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        return super.onDraw(texName);
    }
}
