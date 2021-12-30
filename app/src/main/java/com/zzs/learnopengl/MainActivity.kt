package com.zzs.learnopengl

import android.opengl.GLES32
import android.opengl.GLUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var mMyGLSurfaceView: MyGLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        mMyGLSurfaceView = findViewById(R.id.mRenderer)
    }

    override fun onResume() {
        super.onResume()
        mMyGLSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMyGLSurfaceView.onPause()
    }
}