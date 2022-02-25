package com.zzs.learnopengl.glrender

import android.content.Context
import android.opengl.*
import com.zzs.learnopengl.MyGLSurfaceView
import com.zzs.learnopengl.R
import com.zzs.learnopengl.renderer.chapter1_8.GestureCameraRenderer
import com.zzs.learnopengl.renderer.practice.AlienBiology
import com.zzs.learnopengl.renderer.practice.Ripples
import com.zzs.learnopengl.util.BaseOpenGLES
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
@author  zzs
@Date 2021/12/28
@describe
 */
class MyRender(val context: Context, val myGLSurfaceView: MyGLSurfaceView) :
    GLSurfaceView.Renderer {

    private lateinit var mRenderer: BaseOpenGLES


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES31.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        mRenderer = Ripples(context)
        //mRenderer = AlienBiology(context)
        // mRenderer = GestureCameraRenderer(context, R.mipmap.background, R.mipmap.wall)
        //(mRenderer as GestureCameraRenderer).setGlSurface(myGLSurfaceView)
        // mRenderer = Coords3DRenderer(context,R.mipmap.background,R.mipmap.girl)
        // mRenderer = MatrixRenderer(context,R.mipmap.nums,R.mipmap.awesomeface)
        // mRenderer = TextureRenderer(context,R.mipmap.nums,R.mipmap.awesomeface)
        //mRenderer = ColorfulTriangleRenderer(context)
        //mRenderer = TriangleRenderer(context)
        // mRenderer = RectangleRenderer(context)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)
        mRenderer.setSize(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
        mRenderer.onDraw()
    }

    fun release() {
        mRenderer.release()
    }

    fun setOnRotateChange(progress: Int) {
        mRenderer.setOnRotateChange(progress)
    }

    fun setOnZChange(progress: Int) {
        mRenderer.setOnZChange(progress)
    }

    fun setMoveOn(dx: Float, dy: Float, currSpeed: Float) {
        if (mRenderer is GestureCameraRenderer) {
            (mRenderer as GestureCameraRenderer).moveCamera(dx, dy, currSpeed)
        }
    }

}