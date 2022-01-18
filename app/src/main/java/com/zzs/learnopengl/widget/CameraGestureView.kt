package com.zzs.learnopengl.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

/**
@author  zzs
@Date 2022/1/17
@describe
 */
class CameraGestureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var mWidth = 0
    private var mHeight = 0
    var mSpeed = 0.002f
    var moveCallBack: OnMoveCallBack? = null

    override fun setOnTouchListener(l: OnTouchListener?) {

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w
        mHeight = h
    }

    private var mSecondPointer = -1
    private var mFirstPointer = -1
    private var mLastFX = 0f
    private var mLastFY = 0f
    private var mLastSX = 0f
    private var mLastSY = 0f

    private var mDownFX = 0f
    private var mDownFY = 0f
    private var mDownSX = 0f
    private var mDownSY = 0f

    private var mCurrDx = 0f
    private var mCurrDy = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mFirstPointer = event.actionIndex
                mDownFX = event.getX(mFirstPointer)
                mDownFY = event.getY(mFirstPointer)

            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mSecondPointer = event.actionIndex
                mDownSX = event.getX(mSecondPointer)
                mDownSY = event.getY(mSecondPointer)
            }
            MotionEvent.ACTION_MOVE -> {
                onMoveAction(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mSecondPointer = -1

            }
            MotionEvent.ACTION_UP -> {
                mFirstPointer = -1

            }
        }
        if (mSecondPointer != -1) {
            mLastSX = event.getX(mSecondPointer) //记录第二根手指手指上次的坐标
            mLastSY = event.getY(mSecondPointer)
        }
        if (mFirstPointer!=-1){
            mLastFX = event.getX(mFirstPointer)//记录第一个手指的上次坐标
            mLastFY = event.getY(mFirstPointer)
        }
        return true
    }

    private fun onMoveAction(event: MotionEvent) {
        when (event.actionIndex) {
            mFirstPointer -> {
                var dx = mDownFX - mLastFX
                var dy = mDownFY - mLastFY
                if (abs(dx) > touchSlop || abs(dy) > touchSlop) {
                    dx = event.x - mLastFX
                    dy = event.y - mLastFY
                    mCurrDx = dx
                    mCurrDy = dy
                }

            }
            mSecondPointer -> {
                var dx = event.x - mLastSX
                var dy = event.y - mLastSY
                if (abs(dx) > touchSlop || abs(dy) > touchSlop) {
                    dx = event.x - mLastSX
                    dy = event.y - mLastSY
                    mCurrDx = dx
                    mCurrDy = dy
                }
            }
        }
        moveCallBack?.onMove(mCurrDx, mCurrDy, mSpeed)
    }

    interface OnMoveCallBack {
        fun onMove(dx: Float, dy: Float, currSpeed: Float)
    }
}