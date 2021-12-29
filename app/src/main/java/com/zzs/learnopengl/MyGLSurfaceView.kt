package com.zzs.learnopengl

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.core.view.doOnAttach

/**
@author  zzs
@Date 2021/12/28
@describe
 */
class MyGLSurfaceView : GLSurfaceView {

    private val renderer: MyRender

    private val sHandler = Handler(Looper.getMainLooper())
    private lateinit var sRunnable :Runnable


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
        renderer = MyRender(context)
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        sRunnable = Runnable {
            requestRender()
            sHandler.postDelayed(sRunnable, 3000)
        }
        doOnAttach {
            sHandler.postDelayed(sRunnable, 3000)
        }
    }
}