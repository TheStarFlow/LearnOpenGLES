package com.zzs.learnopengl.util;

import android.content.Context;
import android.opengl.GLES31;

import androidx.annotation.CallSuper;

/**
 * @author zzs
 * @Date 2021/12/30
 * @describe 使用VAO VBO 缓冲
 */
public abstract class BaseBufferOpenGLES extends BaseOpenGLES {
    protected int[] VBO,VAO;


    public BaseBufferOpenGLES(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);
    }

    @Override
    protected final void init() {
        loadAttribute();
        //使用 VAO VBO 缓存对象来存储数据  当需要绘制多个物体的时候 ，使用VAO VBO 可以快捷方便的切换
        VBO = new int[1];
        VAO = new int[1];
        /**
         * 声明一个1个 VAO
         * 参数1: 需要生成VAO的个数
         * 参数2: 存储 VAO id 的int数组
         * 参数3： offset
         * */
        GLES31.glGenVertexArrays(1,VAO,0);
        /**
         * 声明一个1个 VBO
         * 参数1: 需要生成 VBO 的个数
         * 参数2: 存储 VBO id 的int数组
         * 参数3： offset
         * */
        GLES31.glGenBuffers(1,VBO,0);
        //绑定声明的VAO，用于接下来绑定数据
        GLES31.glBindVertexArray(VAO[0]);
        //把VBO绑定到 GL_ARRAY_BUFFER 这个类型上
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,VBO[0]);
        //把顶点数据复制到定义的VBO缓冲区
        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER,getVertexLength()*4,vertexBuffer,GLES31.GL_STATIC_DRAW);
        bindAttribute();
        //解绑VBO
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,0);
        //解绑VAO
        GLES31.glBindVertexArray(0);
    }
    /**
     * 加载并初始化
     * */
    protected abstract void loadAttribute();
    /**
     * 绑定数组数据 因为步长不定，需要单独编写
     *
     * */
    protected abstract void bindAttribute();

    @Override
    public void release() {
        super.release();
        GLES31.glDeleteVertexArrays(1,VAO,0);
        GLES31.glDeleteBuffers(1,VBO,0);
    }
}
