package com.ved.ui

import android.content.Intent
import android.util.Log
import com.ved.R
import com.ved.entity.bean.MainListEntity
import com.ved.ui.base.BaseActivity
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.netlib.BaseCallback
import com.ved.net.request.AppBaseUrlRequest
import com.ved.net.request.CheckUpdateRequest
import com.ved.net.response.AppBaseUrlResponse
import com.ved.net.response.CheckUpdateResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_vd_text.view.*
import kotlinx.android.synthetic.main.item_vd_title.view.*
import retrofit2.Call

class MainActivity : BaseActivity() {

    private var mainList: ArrayList<MainListEntity>? = null

    override fun initView() {
        setContentView(R.layout.activity_main)

        rec_main?.adapter = mainAdapter

        getMainListFlow()

        replaceBaseUrl()
        checkUpdate()

        mainList?.run { mainAdapter.replaceData(this) }

        Log.e("DPI===", "${ScreenUtils.getScreenDensityDpi()} - ${ScreenUtils.getScreenDensity()} - ${ScreenUtils.getScreenWidth()}")
    }

    private var mainAdapter = object : BaseMultiItemQuickAdapter<MainListEntity, BaseViewHolder>(null) {
        init {
            addItemType(MainListEntity.ITEM_TITLE, R.layout.item_vd_title)
            addItemType(MainListEntity.ITEM_CONTENT, R.layout.item_vd_text)
        }
        override fun convert(helper: BaseViewHolder, item: MainListEntity?) {
            item?.run {
                when (itemType) {
                    MainListEntity.ITEM_TITLE -> {
                        helper.itemView.tv_title.text = content
                    }
                    MainListEntity.ITEM_CONTENT -> {
                        helper.itemView.tv_text.text = content
                        helper.itemView.tv_text.setOnClickListener { jumpListener.doJump(content) }
                    }
                    else -> { }
                }
            }
        }
    }

    private var jumpListener = object : JumpEventListener {
        override fun doJump(tag: String?) {
            when(tag) {
                arr1[0] -> {
                }
                arr1[1] -> {
                }
                arr1[2] -> 0
                arr1[3] -> 0
                arr1[4] -> 0
                arr1[5] -> 0
                arr2[0] -> 0
                arr2[1] -> startActivity(Intent(applicationContext, DetailDialogActivity::class.java))
                arr2[2] -> 0
                arr2[3] -> 0
                arr3[0] -> 0
                arr4[0] -> 0
                arr5[0] -> startActivity(Intent(applicationContext, DetailImageViewActivity::class.java))
                arr5[1] -> 0
                arr5[2] -> 0
                arr5[3] -> 0
                arr5[4] -> 0
                arr5[5] -> startActivity(Intent(applicationContext, DetailListViewActivity::class.java))
            }
        }
    }

    private fun getMainListFlow() {
        map[arr[0]] = arr1
        map[arr[1]] = arr2
        map[arr[2]] = arr3
        map[arr[3]] = arr4
        map[arr[4]] = arr5
        mainList = ArrayList()
        map.forEach {
            mainList?.add(MainListEntity(true, it.key))
            it.value.forEach { v ->
                mainList?.add(MainListEntity(false, v))
            }
        }
    }

    companion object {
        var arr = arrayOf("基础控件", "提示框", "组件", "动画", "功能模块")
        var arr1 = arrayOf("按钮", "文本框", "输入框", "Icon图标", "徽标", "CheckBox")
        var arr2 = arrayOf("吐司", "弹窗", "底部弹窗", "顶部弹窗")
        var arr3 = arrayOf("步进器")
        var arr4 = arrayOf("Lottie")
        var arr5 = arrayOf("图片展示", "图片选择", "省市区选择", "时间选择", "图片压缩", "列表")
        var map = LinkedHashMap<String, Array<String>>()
    }

    interface JumpEventListener {
        fun doJump(tag: String?)
    }

    private fun replaceBaseUrl() {
        AppBaseUrlRequest().request(AppBaseUrlRequest.Param("1"), object : BaseCallback<AppBaseUrlResponse>() {
            override fun onSuccess(response: AppBaseUrlResponse?) {
                response?.data?.appUrl?.run {
                }
            }

        })
    }

    private fun checkUpdate() {
        CheckUpdateRequest().request(CheckUpdateRequest.Param("1"), object : BaseCallback<CheckUpdateResponse>() {
            override fun onSuccess(response: CheckUpdateResponse?) {
                response?.data?.run {
                    ToastUtils.showShort(this.updateContent)
                }
            }

        })
    }

}
