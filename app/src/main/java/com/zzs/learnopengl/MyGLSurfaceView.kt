package com.zzs.learnopengl

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.SurfaceHolder
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import com.zzs.learnopengl.renderer.chapter1_8.GestureCameraRenderer

/**
@author  zzs
@Date 2021/12/28
@describe
 */
class MyGLSurfaceView : GLSurfaceView {

    private val renderer: MyRender

    private val sHandler = Handler(Looper.getMainLooper())
    private lateinit var sRunnable: Runnable
    private val interval = 1000L
    private var postInterval = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000
        if (supportsEs3) {
            setEGLContextClientVersion(3)
        } else {
            setEGLContextClientVersion(2)
        }
        renderer = MyRender(context, this)
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        if (postInterval) {
            sRunnable = Runnable {
                requestRender()
                sHandler.postDelayed(sRunnable, interval)
            }
            doOnAttach {
                sHandler.postDelayed(sRunnable, interval)
            }
            doOnDetach {
                sHandler.removeCallbacksAndMessages(null)
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        super.surfaceDestroyed(holder)
        Log.i("MyGlSurfaceView", "surfaceDestroyed thread name = ${Thread.currentThread().name}")

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        renderer.release()
        Log.i("MyGlSurfaceView", "onDetach thread name = ${Thread.currentThread().name}")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.i("MyGlSurfaceView", "onAttach thread name = ${Thread.currentThread().name}")
    }

    fun onRotateChange(progress: Int) {
        renderer.setOnRotateChange(progress)
        //  requestRender()
    }

    fun onZChange(progress: Int) {
        renderer.setOnZChange(progress)
        //  requestRender()
    }

    fun setOnMove(dx: Float, dy: Float, currSpeed: Float) {
        renderer.setMoveOn(dx, dy, currSpeed)
    }
}