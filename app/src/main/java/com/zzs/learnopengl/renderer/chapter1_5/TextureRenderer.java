package com.zzs.learnopengl.renderer.chapter1_5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLU;
import android.opengl.GLUtils;

import androidx.annotation.IdRes;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseBufferOpenGLES;
import com.zzs.learnopengl.util.BaseOpenGLES;
import com.zzs.learnopengl.util.OpenGLKit;
import java.nio.IntBuffer;
/**
 * @author zzs
 * @Date 2021/12/30
 * @describe
 */
public class TextureRenderer extends BaseBufferOpenGLES {

    private int vPosition;
    private int sColor;
    private int vTexCoord;
    private static int[] indices = new int[]{0, 1, 3, 1, 2, 3};

    private IntBuffer indexBuffer;
    private int mTexture1;
    private int mTexture2;

    private int[] mTextureId;
    private int[] mTextureId2;


    public TextureRenderer(Context context,int resId,int resId2) {
        super(context, R.raw.chapter_1_5_texture_vert, R.raw.chapter_1_5_texture_frag);
        mTextureId = new int[1];
        mTextureId2 = new int[1];
        //创建纹理
        mTextureId[0] = OpenGLKit.createTexture(context,resId);
        mTextureId2[0] = OpenGLKit.createTexture(context,resId2,GLES31.GL_NEAREST,GLES31.GL_LINEAR,GLES31.GL_CLAMP_TO_EDGE,GLES31.GL_CLAMP_TO_EDGE);
    }


    @Override
    protected void loadAttribute() {
        vPosition = GLES31.glGetAttribLocation(program,"vPosition");
        sColor = GLES31.glGetAttribLocation(program,"sColor");
        vTexCoord = GLES31.glGetAttribLocation(program,"aTexCoord");
        mTexture1 = GLES31.glGetUniformLocation(program,"outTexture");
        mTexture2 = GLES31.glGetUniformLocation(program,"outTexture2");
        indexBuffer = IntBuffer.allocate(indices.length);
        indexBuffer.clear();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    @Override
    protected void bindAttribute() {
        //绑定VBO的数据 VBO绑定在VAO上
        GLES31.glVertexAttribPointer(vPosition,3, GLES31.GL_FLOAT,false,32,0);
        GLES31.glEnableVertexAttribArray(vPosition);

        GLES31.glVertexAttribPointer(sColor,3, GLES31.GL_FLOAT,false,32,12);
        GLES31.glEnableVertexAttribArray(sColor);

        GLES31.glVertexAttribPointer(vTexCoord,2, GLES31.GL_FLOAT,false,32,24);
        GLES31.glEnableVertexAttribArray(vTexCoord);

    }

    @Override
    protected float[] getVertexArray() {
        return  new float[]{
                //     ---- 位置 ----       ---- 颜色 ----     - 纹理坐标 -
                0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // 右上
                0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // 右下
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // 左下
                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // 左上
//                0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1-1.0f,   // 右上 纹理翻转解决方案1 翻转纹理坐标
//                0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 1-0.0f,   // 右下
//                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 1-0.0f,   // 左下
//                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1-1.0f    // 左上
        };
    }

    @Override
    public void onDraw() {
        GLES31.glUseProgram(program);
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
        GLES31.glDrawElements(GLES31.GL_TRIANGLES,6,GLES31.GL_UNSIGNED_INT,indexBuffer);

        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,0);


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
