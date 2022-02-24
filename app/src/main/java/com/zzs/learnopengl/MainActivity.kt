package com.zzs.learnopengl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zzs.learnopengl.widget.CameraGestureView

class MainActivity : AppCompatActivity() {

    private lateinit var mMyGLSurfaceView: MyGLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        mMyGLSurfaceView = findViewById(R.id.mRenderer)
        val gesture = findViewById<CameraGestureView>(R.id.mGesture)
        gesture.moveCallBack = object :CameraGestureView.OnMoveCallBack{

            override fun onMove(dx: Float, dy: Float, currSpeed: Float) {
               // Log.d("Gesture","dx:$dx  dy:$dy currSpeed:$currSpeed")
                mMyGLSurfaceView.setOnMove(-dx,dy,currSpeed)
            }
        }
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