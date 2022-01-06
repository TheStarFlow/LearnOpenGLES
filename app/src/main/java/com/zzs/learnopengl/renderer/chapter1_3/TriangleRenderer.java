package com.zzs.learnopengl.renderer.chapter1_3;

import android.content.Context;
import android.opengl.GLES31;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseOpenGLES;

/**
 * @author zzs
 * @Date 2021/12/29
 * @describe
 */
public class TriangleRenderer extends BaseOpenGLES {

    private int aPos;
    private int colorHandle;

    private int[] VBO,VAO;

    float color[] = {0.0f,1.0f, 0.5f, 1f/*,0.0f, 1.0f, 0.5f, 1f,*/ };


    public TriangleRenderer(Context context) {
        super(context, R.raw.chapter_1_3_triangle_vert, R.raw.chapter_1_3_triangle_frag);
    }

    @Override
    protected void init() {
        aPos = GLES31.glGetAttribLocation(program,"vPosition");//获取顶点数据，或者标志layout(location = 0) 这样才能将把它连接到顶点数据
        colorHandle = GLES31.glGetUniformLocation(program,"sColor");



//        GLES31.glEnableVertexAttribArray(aPos);
//        GLES31.glVertexAttribPointer(aPos,3, GLES31.GL_FLOAT,false,12,vertexBuffer);

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
        //把缓冲区的数据链接到顶点属性
        //stride 步骤，下一个数据距离多少个字节，4 * 4（float长度） = 16 ，offset 数据的偏移 4,既一个float
        GLES31.glVertexAttribPointer(aPos,4,GLES31.GL_FLOAT,false,16,4);
        GLES31.glEnableVertexAttribArray(aPos);
        //解绑VBO
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER,0);
        //解绑VAO
        GLES31.glBindVertexArray(0);
    }

    @Override
    protected float[] getVertexArray() {
        return new float[]{
                //x,y,z,w
                1.0f,-0.5f, -0.5f, 0.0f,1.0f,
                0.5f, -0.5f, 0.0f,1.0f,
                0.0f,  0.5f, 0.0f,1.0f
        };
    }

    @Override
    public void onDraw() {
        GLES31.glUseProgram(program);
        GLES31.glUniform4fv(colorHandle,1,color,0);


        //在不使用VBO VAO 的情况 使用以下两行代码设置顶点数据


        //使用了VAO 的情况下，直接绑定VAO 就可以了  不必每次都像上面那样绑定数据，绑定VAO可以只执行一次
        GLES31.glBindVertexArray(VAO[0]);
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES,0,3);
    }
}
