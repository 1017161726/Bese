package com.ved.ui

import android.os.Bundle
import com.ved.R
import com.ved.ui.base.BaseActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.detail_listview.*
import kotlinx.android.synthetic.main.item_list_flow.view.*

class DetailListViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_listview)
        initView()

        initList()

    }

    private fun initList() {
        rec?.run {
            adapter = listAdapter

            val list = arrayListOf<String>()
            var ind = 1
            for (i in 1..50) {
                list.add("name$i ${if (ind % 2 == 1) "ct$i" else "\n ct$i"}")
                ind++
            }
            listAdapter.replaceData(list)
        }
    }

    private var listAdapter = object : BaseQuickAdapter<String?, BaseViewHolder>(R.layout.item_list_flow) {
        override fun convert(helper: BaseViewHolder, item: String?) {
            item?.run {
                helper.itemView.tv_goods_name?.text = this
            }
        }

    }

    override fun initView() {
        initTitle("列表展示", false)

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
