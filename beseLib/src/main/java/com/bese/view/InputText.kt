package com.bese.view

import android.content.Context
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText


/**
 * 自定义输入框组件，控制复制粘贴功能是否可用
 */
class InputText : EditText {

    private var shouldCopyAndPaste = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setShouldCopyAndPaste(shouldCopyAndPaste: Boolean) {
        this.shouldCopyAndPaste = shouldCopyAndPaste
        isLongClickable = false
        setTextIsSelectable(false)
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        }
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        if (!shouldCopyAndPaste && id == android.R.id.paste) {
            return false
        }
        if (!shouldCopyAndPaste && id == android.R.id.copy) {
            return false
        }
        return if (!shouldCopyAndPaste && id == android.R.id.selectAll) {
            false
        } else super.onTextContextMenuItem(id)
    }

    fun canPaste(): Boolean {
        return false
    }

    fun canCut(): Boolean {
        return false
    }

    fun canCopy(): Boolean {
        return false
    }

    fun canSelectAllText(): Boolean {
        return false
    }

    fun canSelectText(): Boolean {
        return false
    }

    fun textCanBeSelected(): Boolean {
        return false
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        // 设置光标自动右移
        setSelection(text?.length ?: 0)
    }

}
