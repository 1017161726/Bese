package com.bese.widget

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.MotionEvent

class ADialog(ctx: Context, theme: Int = 0) : Dialog(ctx, theme) {

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                Log.e("按下===", "DDDDD手指按下")
            }
            MotionEvent.ACTION_DOWN -> {
                Log.e("按下===", "DDDDD普通按下")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e("移动===", "DDDDDDDDD")
            }
            MotionEvent.ACTION_UP -> {
                Log.e("抬起===", "DDDDD普通抬起")
            }
            MotionEvent.ACTION_POINTER_UP -> {
                Log.e("抬起===", "DDDDD手指抬起")
            }
            else -> {
                Log.e("其他触摸===", "DDDDD ${event?.action}")
            }
        }
        return super.dispatchTouchEvent(event)
    }

}