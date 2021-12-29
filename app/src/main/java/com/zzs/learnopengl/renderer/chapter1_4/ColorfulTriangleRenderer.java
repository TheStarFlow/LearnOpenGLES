package com.zzs.learnopengl.renderer.chapter1_4;

import android.content.Context;

import com.zzs.learnopengl.util.BaseOpenGLES;

/**
 * @author zzs
 * @Date 2021/12/29
 * @describe
 */
public class ColorfulTriangleRenderer extends BaseOpenGLES {

    public ColorfulTriangleRenderer(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);
    }

    @Override
    protected void init() {

    }

    @Override
    protected float[] getVertexArray() {
        return new float[0];
    }

    @Override
    public void onDraw() {

    }
}
