package com.bese.widget

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable

import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bese.R

import com.blankj.utilcode.util.SizeUtils

/**
 * 增强弹窗
 *      弹窗样式强调按钮点击视觉引导，功能较为单一
 *
 */
class LDialog(private val mCtx: Context) {

    @ColorInt
    private var backgroundColor: Int = Color.WHITE
    private var backgroundDim: Float = 0.5f

    private var title: String? = null
    private var message: String? = null

    @ColorInt
    private var cancelTextColor: Int = ContextCompat.getColor(mCtx, R.color.color_333)
    private var cancelText: String? = "取消"

    @ColorInt
    private var confirmTextColor: Int = ContextCompat.getColor(mCtx, R.color.color_fff)
    private var confirmTextBgColor: Int = ContextCompat.getColor(mCtx, R.color.color_0099ff)
    private var confirmText: String? = "确定"

    private var dialogLeftMargin: Int = getDp(30f)
    private var dialogRightMargin: Int = getDp(30f)

    private var backgroundDrawable: Drawable? = null

    private var hideDialogWhileClickRight: Boolean = true

    private var listener: DialogListener? = null

    /**
     * 创建XDialog
     */
    private var mDialog: Dialog? = null
    private var dialogView: View? = null
    private var customLayout: LinearLayout? = null
    private var contentLayout: LinearLayout? = null
    private var bottomBtnLayout: LinearLayout? = null

    private var titleTv: TextView? = null
    private var msgTv: TextView? = null
    private var leftBtn: TextView? = null
    private var rightBtn: TextView? = null

    /**
     * Dialog监听接口
     */
    interface DialogListener {
        /** 选择 */
        fun doEnter(view: Dialog?)

        /** 取消事件 */
        fun doCancel(view: Dialog?) {}
        /**
         * 取消事件
         */
        fun doDismiss() {}
    }

    private fun getDp(dp: Float): Int {
        return SizeUtils.dp2px(dp)
    }

    /**
     * 设置Dialog的背景渲染色
     */
    fun setBackgroundColor(@ColorInt color: Int): LDialog {
        this.backgroundColor = color
        return this
    }

    /**
     * 设置Dialog的背景渲染色，ColorInt
     */
    fun setBackgroundColorRaw(@ColorInt color: Int): LDialog {
        this.backgroundColor = color
        return this
    }

    /**
     * 设置Dialog的背景渲染色
     */
    fun setBackgroundDim(dim: Float): LDialog {
        this.backgroundDim = dim
        return this
    }

    /**
     * 设置Dialog的标题
     */
    fun setTitle(title: String?): LDialog {
        this.title = title
        return this
    }

    /**
     * 设置Dialog的消息
     */
    fun setMessage(msg: String?): LDialog {
        this.message = msg
        return this
    }

    /**
     * 设置Dialog的取消按钮文案
     */
    fun setCancelText(cancelTxt: String?): LDialog {
        this.cancelText = cancelTxt
        return this
    }

    /**
     * 设置Dialog的取消按钮文字颜色
     */
    fun setCancelTextColor(@ColorInt color: Int): LDialog {
        this.cancelTextColor = color
        return this
    }

    /**
     * 设置Dialog的确认按钮文案
     */
    fun setEnterText(enterTxt: String?): LDialog {
        this.confirmText = enterTxt
        return this
    }

    /**
     * 设置Dialog的确认按钮文字颜色
     */
    fun setEnterTextColor(@ColorInt color: Int): LDialog {
        this.confirmTextColor = color
        return this
    }

    /**
     * 设置Dialog的确认按钮文字颜色
     */
    fun setEnterTextBgColor(@ColorInt color: Int): LDialog {
        this.confirmTextBgColor = color
        return this
    }

    /**
     * 设置Dialog的监听
     */
    fun setListener(listener: DialogListener): LDialog {
        this.listener = listener
        return this
    }

    /**
     * 设置Dialog的左屏距
     */
    fun setLeftScreenMargin(margin: Float): LDialog {
        this.dialogLeftMargin = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的右屏距
     */
    fun setRightScreenMargin(margin: Float): LDialog {
        this.dialogRightMargin = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的根背景
     */
    fun setBackgroundDrawable(drawable: Drawable): LDialog {
        this.backgroundDrawable = drawable
        return this
    }

    /**
     * 设置确定按钮单击后是否弹窗消失
     */
    fun setRightClickHideDialog(hide: Boolean): LDialog {
        this.hideDialogWhileClickRight = hide
        return this
    }

    fun build() {
        // 自定义视图
        if (mDialog == null) {
            mDialog = Dialog(mCtx, R.style.XDialog)
        }
        mDialog?.run {
            setCanceledOnTouchOutside(false)
            setCancelable(true)
            window?.run {
                decorView.setPadding(0, 0, 0, 0)
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setDimAmount(backgroundDim)
            }
            dialogView = LayoutInflater.from(mCtx).inflate(R.layout.dialog_line, null)
            // 绑定控件
            dialogView?.run {
                customLayout = findViewById(R.id.layout_dialog_line)
                titleTv = findViewById(R.id.dialog_line_title)
                msgTv = findViewById(R.id.dialog_line_msg)
                leftBtn = findViewById(R.id.dialog_line_left)
                rightBtn = findViewById(R.id.dialog_line_right)
                customLayout?.backgroundTintList = ColorStateList.valueOf(backgroundColor)
            }

            backgroundDrawable?.run { customLayout?.background = backgroundDrawable }

            if (title?.isNotEmpty() == true) {
                // 标题不为空，消息可有可无
                titleTv?.visibility = View.VISIBLE
                if (message?.isNotEmpty() == true) {
                    msgTv?.visibility = View.VISIBLE
                    msgTv?.text = message
                } else {
                    msgTv?.visibility = View.GONE
                }
            } else {
                // 如果标题为空隐藏了 消息不能隐藏 显示空消息
                titleTv?.visibility = View.GONE
                msgTv?.visibility = View.VISIBLE
                msgTv?.text = "$message"
            }

            if (cancelText?.isNotEmpty() == true) {
                leftBtn?.visibility = View.VISIBLE
                leftBtn?.text = cancelText

                leftBtn?.setTextColor(cancelTextColor)
                leftBtn?.setOnClickListener { _ ->
                    listener?.doCancel(mDialog)
                    dismiss()
                }
            } else {
                leftBtn?.visibility = View.GONE
            }

            if (TextUtils.isEmpty(confirmText)) {
                confirmText = "确定"
            }
            rightBtn?.text = confirmText
            rightBtn?.setTextColor(confirmTextColor)
            rightBtn?.backgroundTintList = ColorStateList.valueOf(confirmTextBgColor)
            rightBtn?.setOnClickListener { _ ->
                listener?.doEnter(mDialog)
                if (hideDialogWhileClickRight) { dismiss() }
            }

            // 绑定视图
            dialogView?.run { setContentView(this) }

            // dialog展示
            show()
        }
    }

    fun hideDialog() {
        mDialog?.dismiss()
    }

    fun showDialog() {
        mDialog?.run {
            if (!isShowing) {
                show()
            }
        }
    }

}
