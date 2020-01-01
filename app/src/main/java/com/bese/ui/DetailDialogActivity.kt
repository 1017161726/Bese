package com.vedview

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bese.R
import com.bese.ui.base.BaseActivity
import com.bese.widget.LDialog
import com.bese.widget.XDialog
import com.blankj.utilcode.util.ColorUtils
import kotlinx.android.synthetic.main.detail_dialog.*

class DetailDialogActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_dialog)
        initView()
        initListener()
    }

    private fun initListener() {
        // 仿iOS
        dialog_ios_two?.setOnClickListener {
            XDialog(this)
            .setTitle("这是一个标题")
            .setMessage("这里是内容，可以很长，也可以没有，没有就不展示")
            .setEnterText("关闭吧")
            .build()
        }
        dialog_ios_two_no_title?.setOnClickListener {
            XDialog(this)
                .setMessage("这里是内容，可以很长，也可以没有，没有就不展示")
                .build()
        }
        dialog_ios_one?.setOnClickListener {
            XDialog(this)
                .setTitle("这里是标题")
                .setMessage("这里是内容，可以很长，也可以没有，没有就不展示")
                .setCancelText("")
                .build()
        }
        dialog_ios_one_no_title?.setOnClickListener {
            XDialog(this)
                .setMessage("这里是内容，可以很长，也可以没有，没有就不展示")
                .setCancelText("")
                .build()
        }
        dialog_ios_diy?.setOnClickListener {
//            val diyView = LayoutInflater.from(this).inflate(R.layout.diyview, null)
            val v = LinearLayout(this)
            v.addView(ImageView(this).apply { setImageDrawable(getDrawable(R.mipmap.ic_launcher)) })
            XDialog(this)
                .setTitleLayoutVisible(false)
                .setBottomLayoutVisible(false)
                .setDiyView(v)
                .setCancelText("")
                .build()
        }
        // LDialog
        dialog_line_blue?.setOnClickListener {
            LDialog(this)
                .setTitle("我是标题")
                .setMessage("我是消息内容 \n 并且有两行。。。。。。")
                .build()
        }
        dialog_line_delete?.setOnClickListener {
            LDialog(this)
                .setTitle("我是标题")
                .setMessage("我是消息内容 \n 并且有两行。。。。。。")
                .setEnterTextBgColor(ColorUtils.getColor(R.color.color_ff3300))
                .setEnterText("删除")
                .build()
        }
        dialog_line_delete_no_title?.setOnClickListener {
            LDialog(this)
                .setMessage("我是消息内容 \n 并且有两行。。。。。。")
                .setEnterTextBgColor(ColorUtils.getColor(R.color.color_ff3300))
                .setEnterText("删除")
                .build()
        }
        dialog_line_blue_no_cancel?.setOnClickListener {
            LDialog(this)
                .setTitle("我是标题")
                .setMessage("我是消息内容 \n 并且有两行。。。。。。")
                .setCancelText("")
                .build()

        }
        dialog_line_blue_no_cancel_no_title?.setOnClickListener {
            LDialog(this)
                .setMessage("我是消息内容 \n 并且有两行。。。。。。")
                .setCancelText("")
                .build()
        }

    }

    override fun initView() {
        initTitle("弹窗", false)

        val baseUseArea = "●所有使用弹窗的场景"

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
