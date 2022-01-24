package com.zzs.learnopengl.util;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.util.Log;

import androidx.annotation.CallSuper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author zzs
 * @Date 2021/6/17
 * @describe
 */
public abstract class BaseOpenGLES {

    private Context mContext;
    protected int program;

    protected FloatBuffer vertexBuffer; //顶点坐标缓存区
    protected float[] mVertex;

    protected float width;
    protected float height;


    public BaseOpenGLES(Context context, int vertexShaderId, int fragmentShaderId) {
        this.mContext = context.getApplicationContext();
        float[] sVertex = getVertexArray();
        mVertex = sVertex;
        vertexBuffer = ByteBuffer.allocateDirect(sVertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(sVertex);
        vertexBuffer.position(0);
        String fragmentShader = OpenGLKit.readRawTextFile(mContext, fragmentShaderId);
        String vertexShader = OpenGLKit.readRawTextFile(mContext, vertexShaderId);
        program = OpenGLKit.loadProgram(fragmentShader, vertexShader);
        init();
        int[] maxVertex = new int[1];
        GLES31.glGetIntegerv(GLES31.GL_MAX_VERTEX_ATTRIBS, maxVertex, 0);
        Log.d("BaseOpenGLES", "max vertex = " + maxVertex[0]);
    }

    public int getVertexLength() {
        if (mVertex == null) {
            return 0;
        }
        return mVertex.length;
    }

    /**
     * 链接句柄 需要在 EGL CONTEXT 下
     */
    protected abstract void init();

    protected abstract float[] getVertexArray();

    public abstract void onDraw();


    /**
     * 接受外部纹理
     */
    public int onDraw(int texName) {
        return texName;
    }

    @CallSuper
    public void release() {
        GLES20.glDeleteProgram(program);
    }
    @CallSuper
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setOnRotateChange(int progress) {

    }

    public void setOnZChange(int progress) {

    }
}

