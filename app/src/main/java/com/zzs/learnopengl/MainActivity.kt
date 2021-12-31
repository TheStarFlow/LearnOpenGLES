package com.zzs.learnopengl

import android.opengl.GLES32
import android.opengl.GLUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

class MainActivity : AppCompatActivity() {

    private lateinit var mMyGLSurfaceView: MyGLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        mMyGLSurfaceView = findViewById(R.id.mRenderer)
        val seekBar = findViewById<SeekBar>(R.id.rotateSeekBar)
        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mMyGLSurfaceView.onRotateChange(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        val zBAR = findViewById<AppCompatSeekBar>(R.id.zBar);
        zBAR.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mMyGLSurfaceView.onZChange(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                
            }

        })
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