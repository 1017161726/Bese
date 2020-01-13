package com.bese.ui.base

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bese.R
import kotlinx.android.synthetic.main.base_content.*
import kotlinx.android.synthetic.main.base_title_bar.*

/**
 * 基类Activity 处理公共布局
 * @author Fires
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    protected fun initTitle(title: String, showRight: Boolean) {
        base_back?.setOnClickListener(this)
        base_right?.setOnClickListener(this)
        base_title?.text = "$title"
        base_right?.visibility = if (showRight) View.VISIBLE else View.GONE
    }

    fun initContent(tvArea: String, tvRole: String, tvInteract: String, tvStyle: String) {
        tv_use_area?.text = tvArea
        tv_use_role?.text = tvRole
        tv_interact?.text = tvInteract
        tv_style?.text = tvStyle
    }

    protected abstract fun initView()

    override fun onClick(v: View) {
        when(v.id) {
            R.id.base_back -> clickLeft()
            R.id.base_right -> clickRight()
        }
    }

    fun clickLeft() { finish() }

    fun clickRight() {}

    /**
     * 此处捕获用户手势事件，不论是否可点击，都可以先从这里走。
     * 但范围仅限所有的Activity，无法对Dialog，DialogFragment做捕获。
     */
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                Log.e("按下===", "手指按下")
            }
            MotionEvent.ACTION_DOWN -> {
                Log.e("按下===", "普通按下")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e("移动===", "AAAAAAA")
            }
            MotionEvent.ACTION_UP -> {
                Log.e("抬起===", "普通抬起")
            }
            MotionEvent.ACTION_POINTER_UP -> {
                Log.e("抬起===", "手指抬起")
            }
            else -> {
                Log.e("其他触摸===", "${event?.action}")
            }
        }

        return super.dispatchTouchEvent(event)
    }

}
