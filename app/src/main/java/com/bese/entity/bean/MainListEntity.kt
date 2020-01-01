package com.bese.entity.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

class MainListEntity(var isTitle: Boolean, var content: String?) : MultiItemEntity {

    override fun getItemType(): Int {
        return if (isTitle) ITEM_TITLE else ITEM_CONTENT
    }

    companion object {
        const val ITEM_TITLE = 1
        const val ITEM_CONTENT = 2
    }
}