package com.zzs.learnopengl

import android.content.Context
import android.opengl.*
import com.zzs.learnopengl.renderer.chapter1_4.ColorfulTriangleRenderer
import com.zzs.learnopengl.renderer.chapter1_5.TextureRenderer
import com.zzs.learnopengl.util.BaseOpenGLES
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
@author  zzs
@Date 2021/12/28
@describe
 */
class MyRender(val context: Context) :GLSurfaceView.Renderer {

    private lateinit var mRenderer: BaseOpenGLES


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES31.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        mRenderer = TextureRenderer(context,R.mipmap.wall,R.mipmap.awesomeface)
        //mRenderer = ColorfulTriangleRenderer(context)
        //mRenderer = TriangleRenderer(context)
       // mRenderer = RectangleRenderer(context)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
        mRenderer.onDraw()
    }

    public fun release(){
        mRenderer.release()
    }
}