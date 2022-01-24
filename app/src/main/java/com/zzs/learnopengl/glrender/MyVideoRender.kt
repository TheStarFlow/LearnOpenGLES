package com.zzs.learnopengl.glrender

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.opengl.GLES20
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.view.Surface
import com.zzs.learnopengl.R
import com.zzs.learnopengl.renderer.chapter1_5.CameraTextureRenderer
import com.zzs.learnopengl.renderer.chapter1_5.NormalTextureRenderer
import com.zzs.learnopengl.renderer.chapter1_8.GestureVideoCameraRenderer
import com.zzs.learnopengl.util.BaseOpenGLES
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
@author  zzs
@Date 2022/1/21
@describe   尝试将视频画面渲染在正方体块上
 */
class MyVideoRender(val context: Context, private val glSurfaceView: GLSurfaceView) :
    GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private lateinit var mRenderer: BaseOpenGLES
    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var mSurfaceTexture: SurfaceTexture
    private val mTex = IntArray(1)
    private val mMatrix = FloatArray(16)
    private lateinit var mTextBuffer: CameraTextureRenderer
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES31.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        GLES20.glGenTextures(1, mTex, 0)
        createMediaPlayer()
        mTextBuffer = CameraTextureRenderer(context)
        mRenderer = GestureVideoCameraRenderer(context, R.mipmap.background,R.mipmap.wall)
        (mRenderer as GestureVideoCameraRenderer).setGlSurface(glSurfaceView)

    }

    private fun createMediaPlayer() {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setOnPreparedListener {
            it.start()
        }
        mSurfaceTexture = SurfaceTexture(mTex[0])
        mSurfaceTexture.setOnFrameAvailableListener(this)
        val path = "https://vfx.mtime.cn/Video/2021/12/14/mp4/211214174109164133.mp4"
        mMediaPlayer?.setSurface(Surface(mSurfaceTexture))
        mMediaPlayer?.setDataSource(path)
        mMediaPlayer?.prepareAsync()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        mTextBuffer.setSize(width, height)
        mRenderer.setSize(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
        mSurfaceTexture.updateTexImage()
        mSurfaceTexture.getTransformMatrix(mMatrix)
        mTextBuffer.setMatrix(mMatrix)
        val texBuffer = mTextBuffer.onDraw(mTex[0])
        mRenderer.onDraw(texBuffer)

    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        glSurfaceView.requestRender()
    }

    fun release() {
        mRenderer.release()
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
    }

    fun setOnRotateChange(progress: Int) {
        mRenderer.setOnRotateChange(progress)
    }

    fun setOnZChange(progress: Int) {
        mRenderer.setOnZChange(progress)
    }

    fun setMoveOn(dx: Float, dy: Float, currSpeed: Float) {
        (mRenderer as GestureVideoCameraRenderer).moveCamera(dx, dy, currSpeed)
    }
}