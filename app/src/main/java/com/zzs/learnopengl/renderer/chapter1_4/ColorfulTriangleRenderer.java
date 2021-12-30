package com.zzs.learnopengl.renderer.chapter1_4;

import android.content.Context;
import android.opengl.GLES31;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseOpenGLES;

/**
 * @author zzs
 * @Date 2021/12/29
 * @describe 这个图片可能不是你所期望的那种，因为我们只提供了3个颜色，而不是我们现在看到的大调色板。这是在片段着色器中进行的所谓片段
 * 插值(Fragment Interpolation)的结果。当渲染一个三角形时，光栅化(Rasterization)阶段通常会造成比原指定顶点更多的片段。
 * 光栅会根据每个片段在三角形形状上所处相对位置决定这些片段的位置。基于这些位置，它会插值(Interpolate)所有片段着色器的输入变量。
 * 比如说，我们有一个线段，上面的端点是绿色的，下面的端点是蓝色的。如果一个片段着色器在线段的70%的位置运行，
 * 它的颜色输入属性就会是一个绿色和蓝色的线性结合；更精确地说就是30%蓝 + 70%绿。
 * 这正是在这个三角形中发生了什么。我们有3个顶点，和相应的3个颜色，从这个三角形的像素来看它可能包含50000左右的片段，
 * 片段着色器为这些像素进行插值颜色。如果你仔细看这些颜色就应该能明白了：红首先变成到紫再变为蓝色。
 * 片段插值会被应用到片段着色器的所有输入属性上
 */
public class ColorfulTriangleRenderer extends BaseOpenGLES {

    private int vPosition;
    private int vColor;
    private int[] VBO, VAO;
    private int hor;

    public ColorfulTriangleRenderer(Context context) {
        super(context, R.raw.chapter_1_4_colorful_triangle_vert, R.raw.chapter_1_4_colorful_triangle_frag);
    }

    @Override
    protected void init() {
        vPosition = GLES31.glGetAttribLocation(program, "vPosition");
        vColor = GLES31.glGetAttribLocation(program, "vColor");
        hor = GLES31.glGetUniformLocation(program,"hor");
        VBO = new int[1];
        VAO = new int[1];
        /**
         * 声明一个1个 VAO
         * 参数1: 需要生成VAO的个数
         * 参数2: 存储 VAO id 的int数组
         * 参数3： offset
         * */
        GLES31.glGenVertexArrays(1, VAO, 0);
        /**
         * 声明一个1个 VBO
         * 参数1: 需要生成 VBO 的个数
         * 参数2: 存储 VBO id 的int数组
         * 参数3： offset
         * */
        GLES31.glGenBuffers(1, VBO, 0);
        //绑定声明的VAO，用于接下来绑定数据
        GLES31.glBindVertexArray(VAO[0]);
        //把VBO绑定到 GL_ARRAY_BUFFER 这个类型上
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, VBO[0]);
        //把顶点数据复制到定义的VBO缓冲区
        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, getVertexLength() * 4, vertexBuffer, GLES31.GL_STATIC_DRAW);
        //把缓冲区的数据链接到顶点属性
        GLES31.glVertexAttribPointer(vPosition, 3, GLES31.GL_FLOAT, false, 24, 0);
        GLES31.glEnableVertexAttribArray(vPosition);
        //顶点数据里面的颜色数据
        GLES31.glVertexAttribPointer(vColor, 3, GLES31.GL_FLOAT, false, 24, 12);
        GLES31.glEnableVertexAttribArray(vColor);

        //解绑VBO
        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);
        //解绑VAO
        GLES31.glBindVertexArray(0);
    }

    @Override
    protected float[] getVertexArray() {
        return new float[]{
                //x,y,z  r,g,b
                -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f
        };
    }

    @Override
    public void onDraw() {
        GLES31.glUseProgram(program);
        //在不使用VBO VAO 的情况 使用以下两行代码设置顶点数据

        //添加水平变量 让三角形居右
        //uniform 对象必须在 glUseProgram 后面使用赋值
        GLES31.glUniform1f(hor,0.5f);
        //使用了VAO 的情况下，直接绑定VAO 就可以了  不必每次都像上面那样绑定数据，绑定VAO可以只执行一次
        GLES31.glBindVertexArray(VAO[0]);


        GLES31.glDrawArrays(GLES31.GL_TRIANGLES,0,3);
    }

    @Override
    public void release() {
        super.release();
        GLES31.glDisableVertexAttribArray(vColor);
        GLES31.glDisableVertexAttribArray(vPosition);
    }
}
