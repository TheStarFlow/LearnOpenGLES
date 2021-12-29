package com.zzs.learnopengl.renderer.charter1_3;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES31;

import com.zzs.learnopengl.R;
import com.zzs.learnopengl.util.BaseOpenGLES;

import java.nio.IntBuffer;
import java.util.Random;

/**
 * @author zzs
 * @Date 2021/12/29
 * @describe  画出来两个三角形形成一个矩形
 */
public class RectangleRenderer extends BaseOpenGLES {

    private int aPos;
    private int colorHandle;
    private static int[] indices = new int[]{0, 1, 3, 1, 2, 3};

    float color[] = {0.0f,1.0f, 0.5f, 1f};
    private IntBuffer indexBuffer;
    private Random random;



    public RectangleRenderer(Context context) {
        super(context, R.raw.chapter_1_3_triangle_vert, R.raw.chapter_1_3_triangle_frag);
    }

    @Override
    protected void init() {
        aPos = GLES31.glGetAttribLocation(program,"vPosition");
        colorHandle = GLES31.glGetUniformLocation(program,"sColor");
        indexBuffer = IntBuffer.allocate(indices.length);
        indexBuffer.clear();
        indexBuffer.put(indices);
        indexBuffer.position(0);
        random = new Random();


    }

    @Override
    protected float[] getVertexArray() {
        return  new float[]{
                0.5f, 0.5f, 0.0f,   // 右上角
                0.5f, -0.5f, 0.0f,  // 右下角
                -0.5f, -0.5f, 0.0f, // 左下角
                -0.5f, 0.5f, 0.0f   // 左上角
        };
    }




    @Override
    public void onDraw() {
        GLES31.glUseProgram(program);
        vertexBuffer.position(0);
        float r = (float) Math.sin(System.currentTimeMillis()) / 2.0f +0.5f;
        color[0] = r;
        GLES31.glVertexAttribPointer(aPos,3, GLES31.GL_FLOAT,false,12,vertexBuffer);
        GLES31.glEnableVertexAttribArray(aPos);
       // GLES31.glUniform4fv(colorHandle,1,color,0);
        GLES31.glUniform4f(colorHandle, random.nextFloat(),random.nextFloat(),random.nextFloat(),1.0f);
        GLES31.glDrawElements(GLES31.GL_TRIANGLES,6,GLES31.GL_UNSIGNED_INT,indexBuffer);
        GLES31.glDisableVertexAttribArray(aPos);
    }
}
