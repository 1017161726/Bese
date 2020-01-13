package com.bese.ui

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.bese.R
import com.bese.ui.base.BaseActivity
import kotlinx.android.synthetic.main.detail_imageview.*

class DetailImageViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_imageview)
        initView()
        initImage()

        /** 解决可横向滑动控件与DrawerLayout的滑动冲突 */
        seek_radius?.setOnTouchListener { _, _ ->
            drawer?.requestDisallowInterceptTouchEvent(true)
            false
        }
        seek_radius?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.e("拖动===", "$progress")
//                img_square?.setRatio(progress / 100f + 0.3f)


                img_square?.setRadius(progress.toFloat())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })
    }

    private fun initImage() {
        img_normal?.setImageResource(R.mipmap.seckill_bg)
        img_square?.setImageResource(R.mipmap.seckill_bg)
    }

    override fun initView() {
        initTitle("图片展示", false)

        val baseUseArea = "●部分使用限制图片宽高的场景"

        val baseUseRole = "●直接引用TextButton，定义属性：\n" +
                "●圆角值\n" +
                "●边框粗细（默认0无边框）\n" +
                "●边框颜色\n" +
                "●背景颜色\n" +
                "●按下背景色"

        val baseInteract = "●控件默认含有触摸事件。\n" +
                "●如果定义按下背景色，按下时颜色改变；如果没有设置，按下无感知（有点击事件）"

        val baseStyle = "●可定义圆角大小，圆角非常大时变成粗线形式 \n" +
                "●可定义是否有边框，以及各种颜色"

        initContent(baseUseArea, baseUseRole, baseInteract, baseStyle)

    }

}
