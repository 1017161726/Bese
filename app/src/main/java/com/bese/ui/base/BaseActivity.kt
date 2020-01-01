package com.bese.ui.base

import android.os.Bundle
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

}
