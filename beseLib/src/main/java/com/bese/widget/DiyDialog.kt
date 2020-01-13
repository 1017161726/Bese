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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bese.R

import com.blankj.utilcode.util.SizeUtils

/**
 * 弹窗工具
 *      效果：展示自定义View的弹窗
 *
 */
class DiyDialog(private val mCtx: Context) {

    @ColorInt
    private var backgroundColor: Int = Color.WHITE
    private var backgroundDim: Float = 0.5f

    private var title: String? = null
    private var titleTextSize: Float = 16f
    @ColorInt
    private var titleTextColor: Int = Color.BLACK
    private var titleGravity: Int = Gravity.CENTER
    private var titleMarginTop: Int = getDp(20f)
    private var titleMarginBottom: Int = getDp(0f)
    private var titleIsBold: Boolean = false

    private var message: String? = null
    private var messageTextSize: Float = 15f
    @ColorInt
    private var messageTextColor: Int = Color.BLACK
    private var messageGravity: Int = Gravity.CENTER
    private var messageMarginTop: Int = getDp(12f)
    private var messageMarginBottom: Int = getDp(20f)
    private var messageMarginHorizontal: Int = getDp(15f)

    private var diyView: View? = null
    private var diyViewMarginTop: Int = getDp(10f)
    private var diyViewMarginBottom: Int = getDp(10f)
    private var diyViewMarginHorizontal: Int = getDp(10f)

    @ColorInt
    private var bottomLineColor: Int = ContextCompat.getColor(mCtx, R.color.color_ccc)
    private var bottomLayoutTopMargin: Int = getDp(5f)

    private var cancelText: String? = "取消"
    private var cancelTextSize: Float = 15f
    @ColorInt
    private var cancelTextColor: Int = ContextCompat.getColor(mCtx, R.color.color_333)

    private var confirmText: String? = "确定"
    private var confirmTextSize: Float = 15f
    @ColorInt
    private var confirmTextColor: Int = ContextCompat.getColor(mCtx, R.color.color_0099ff)

    private var titleLayoutVisible: Boolean = true
    private var bottomLayoutVisible: Boolean = true

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
    private var bottomLine: View? = null
    private var btnLine: View? = null

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
    fun setBackgroundColor(@ColorInt color: Int): DiyDialog {
        this.backgroundColor = color
        return this
    }

    /**
     * 设置Dialog的背景渲染色，ColorInt
     */
    fun setBackgroundColorRaw(@ColorInt color: Int): DiyDialog {
        this.backgroundColor = color
        return this
    }

    /**
     * 设置Dialog的背景渲染色
     */
    fun setBackgroundDim(dim: Float): DiyDialog {
        this.backgroundDim = dim
        return this
    }

    /**
     * 设置Dialog的标题
     */
    fun setTitle(title: String?): DiyDialog {
        this.title = title
        return this
    }

    /**
     * 设置Dialog的标题文字大小
     */

    fun setTitleTextSize(size: Float): DiyDialog {
        this.titleTextSize = size
        return this
    }

    /**
     * 设置Dialog的标题文字颜色
     */
    fun setTitleTextColor(@ColorInt color: Int): DiyDialog {
        this.titleTextColor = color
        return this
    }

    /**
     * 设置Dialog的标题对齐方式
     */
    fun setTitleGravity(gravity: Int): DiyDialog {
        this.titleGravity = gravity
        return this
    }

    /**
     * 设置Dialog的标题对齐方式
     */
    fun setTitleBold(isBold: Boolean): DiyDialog {
        this.titleIsBold = isBold
        return this
    }

    /**
     * 设置Dialog的标题顶部间距
     */
    fun setTitleMarginTop(topMargin: Float): DiyDialog {
        this.titleMarginTop = getDp(topMargin)
        return this
    }

    /**
     * 设置Dialog的标题底部间距
     */
    fun setTitleMarginBottom(bottomMargin: Float): DiyDialog {
        this.titleMarginBottom = getDp(bottomMargin)
        return this
    }

    /**
     * 设置Dialog的内容
     * 最好不要直接第一个设置Message，建议应先设置Title，保证文字距离顶部的距离
     */
    fun setMessage(msg: String?): DiyDialog {
        this.message = msg
        return this
    }

    /**
     * 设置Dialog的消息文字大小
     */
    fun setMessageTextSize(size: Float): DiyDialog {
        this.messageTextSize = size
        return this
    }

    /**
     * 设置Dialog的消息文字颜色
     */
    fun setMessageTextColor(@ColorInt color: Int): DiyDialog {
        this.messageTextColor = color
        return this
    }

    /**
     * 设置Dialog的消息对齐方式
     */
    fun setMessageGravity(gravity: Int): DiyDialog {
        this.messageGravity = gravity
        return this
    }

    /**
     * 设置Dialog的消息顶部间距
     */
    fun setMessageMarginTop(topMargin: Float): DiyDialog {
        this.messageMarginTop = getDp(topMargin)
        return this
    }

    /**
     * 设置Dialog的消息底部间距
     */
    fun setMessageMarginBottom(bottomMargin: Float): DiyDialog {
        this.messageMarginBottom = getDp(bottomMargin)
        return this
    }

    /**
     * 设置Dialog的消息水平间距
     */
    fun setMessageMarginHorizontal(horizontalMargin: Float): DiyDialog {
        this.messageMarginHorizontal = getDp(horizontalMargin)
        return this
    }

    /**
     * 设置自定义布局
     * 设置后提供的左右屏幕边距会无效
     */
    fun setDiyView(diyView: View): DiyDialog {
        this.diyView = diyView
        return this
    }

    /**
     * 设置Dialog的自定义View竖直间距
     */
    fun setDiyViewMarginTop(margin: Float): DiyDialog {
        this.diyViewMarginTop = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的自定义View竖直间距
     */
    fun setDiyViewMarginBottom(margin: Float): DiyDialog {
        this.diyViewMarginBottom = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的自定义View水平间距
     */
    fun setDiyViewMarginHorizontal(margin: Float): DiyDialog {
        this.diyViewMarginHorizontal = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的底部按钮分隔线颜色
     */
    fun setBottomLineColor(@ColorInt lineColor: Int): DiyDialog {
        this.bottomLineColor = lineColor
        return this
    }

    /**
     * 设置Dialog的取消按钮文案
     */
    fun setCancelText(cancelTxt: String?): DiyDialog {
        this.cancelText = cancelTxt
        return this
    }

    /**
     * 设置Dialog的取消按钮文字颜色
     */
    fun setCancelTextColor(@ColorInt color: Int): DiyDialog {
        this.cancelTextColor = color
        return this
    }

    /**
     * 设置Dialog的取消按钮文字大小
     */
    fun setCancelTextSize(size: Float): DiyDialog {
        this.cancelTextSize = size
        return this
    }

    /**
     * 设置Dialog的确认按钮文案
     */
    fun setEnterText(enterTxt: String?): DiyDialog {
        this.confirmText = enterTxt
        return this
    }

    /**
     * 设置Dialog的确认按钮文字颜色
     */
    fun setEnterTextColor(@ColorInt color: Int): DiyDialog {
        this.confirmTextColor = color
        return this
    }

    /**
     * 设置Dialog的确认按钮文字大小
     */
    fun setEnterTextSize(size: Float): DiyDialog {
        this.confirmTextSize = size
        return this
    }

    /**
     * 设置Dialog的监听
     */
    fun setListener(listener: DialogListener): DiyDialog {
        this.listener = listener
        return this
    }

    /**
     * 设置Dialog的左屏距
     */
    fun setLeftScreenMargin(margin: Float): DiyDialog {
        this.dialogLeftMargin = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的右屏距
     */
    fun setRightScreenMargin(margin: Float): DiyDialog {
        this.dialogRightMargin = getDp(margin)
        return this
    }

    /**
     * 设置title框是否可见
     */
    fun setTitleLayoutVisible(titleVisible: Boolean): DiyDialog {
        this.titleLayoutVisible = titleVisible
        return this
    }

    /**
     * 设置Dialog的底部视图是否可见
     */
    fun setBottomLayoutVisible(visible: Boolean): DiyDialog {
        this.bottomLayoutVisible = visible
        return this
    }

    /**
     * 设置Dialog的根背景
     */
    fun setBackgroundDrawable(drawable: Drawable): DiyDialog {
        this.backgroundDrawable = drawable
        return this
    }

    /**
     * 设置确定按钮单击后是否弹窗消失
     */
    fun setRightClickHideDialog(hide: Boolean): DiyDialog {
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
            dialogView = LayoutInflater.from(mCtx).inflate(R.layout.dialog_x, null)
            // 绑定控件
            dialogView?.run {
                customLayout = findViewById(R.id.layout_custom)
                contentLayout = findViewById(R.id.dialog_custom_content)
                bottomBtnLayout = findViewById(R.id.layout_bottom_button)
                titleTv = findViewById(R.id.dialog_custom_title)
                msgTv = findViewById(R.id.dialog_custom_msg)
                leftBtn = findViewById(R.id.dialog_custom_left)
                rightBtn = findViewById(R.id.dialog_custom_right)
                bottomLine = findViewById(R.id.bottom_line)
                btnLine = findViewById(R.id.dialog_custom_btn_line)
                customLayout?.backgroundTintList = ColorStateList.valueOf(backgroundColor)
            }

            backgroundDrawable?.run { customLayout?.background = backgroundDrawable }

            if (dialogLeftMargin != 0 || dialogRightMargin != 0) {
                val dialogParam = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                dialogParam.setMargins(dialogLeftMargin, 0, dialogRightMargin, 0)
                customLayout?.layoutParams = dialogParam
            }

            if (titleLayoutVisible) {
                titleTv?.visibility = View.VISIBLE
                // 标题显隐
                if (TextUtils.isEmpty(title)) {
                    title = if (!TextUtils.isEmpty(message)) {
                        message
                    } else {
                        ""
                    }
                    // 消息内容传递给标题，消息置空
                    message = ""
                }

                titleTv?.text = title
                titleTv?.setTextColor(titleTextColor)
                titleTv?.textSize = titleTextSize
                titleTv?.gravity = titleGravity
                titleTv?.paint?.isFakeBoldText = titleIsBold
                val titleParam = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                titleParam.setMargins(30, titleMarginTop, 30, titleMarginBottom)
                titleTv?.layoutParams = titleParam
            } else {
                titleTv?.visibility = View.GONE
            }

            // 消息显隐
            if (!TextUtils.isEmpty(message)) {
                msgTv?.visibility = View.VISIBLE
                msgTv?.text = message
                msgTv?.setTextColor(messageTextColor)
                msgTv?.textSize = messageTextSize
                msgTv?.gravity = messageGravity

                if (messageMarginHorizontal != 0 || messageMarginTop != 0 || messageMarginBottom != 0) {
                    val msgParam = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    msgParam.setMargins(
                        messageMarginHorizontal,
                        messageMarginTop,
                        messageMarginHorizontal,
                        messageMarginBottom
                    )
                    msgTv?.layoutParams = msgParam
                }
            } else {
                msgTv?.visibility = View.GONE
            }

            if (diyView != null) {
                contentLayout?.visibility = View.VISIBLE
                msgTv?.visibility = View.GONE
                contentLayout?.removeAllViews()
                contentLayout?.removeView(diyView)
                contentLayout?.addView(diyView)
                if (diyViewMarginHorizontal != 0 || diyViewMarginTop != 0 || diyViewMarginBottom != 0) {
                    val diyViewParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    diyViewParam.setMargins(
                        diyViewMarginHorizontal,
                        diyViewMarginTop,
                        diyViewMarginHorizontal,
                        diyViewMarginBottom
                    )
                    contentLayout?.layoutParams = diyViewParam
                }
            } else {
                contentLayout?.visibility = View.VISIBLE
                contentLayout?.removeAllViews()
            }

            if (bottomLayoutVisible) {
                bottomBtnLayout?.visibility = View.VISIBLE

                if (bottomLayoutTopMargin != 0) {
                    val bottomParam = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    bottomParam.setMargins(0, bottomLayoutTopMargin, 0, 0)
                    bottomBtnLayout?.layoutParams = bottomParam
                }

                bottomLine?.setBackgroundColor(bottomLineColor)

                if (cancelText?.isNotEmpty() == true) {
                    leftBtn?.visibility = View.VISIBLE
                    leftBtn?.text = cancelText

                    leftBtn?.textSize = cancelTextSize
                    leftBtn?.setTextColor(cancelTextColor)
                    leftBtn?.setOnClickListener { _ ->
                        listener?.doCancel(mDialog)
                        dismiss()
                    }
                } else {
                    leftBtn?.visibility = View.GONE
                    btnLine?.visibility = View.GONE
                }

                if (!TextUtils.isEmpty(confirmText)) {
                    rightBtn?.visibility = View.VISIBLE
                    rightBtn?.text = confirmText
                    rightBtn?.textSize = confirmTextSize
                    rightBtn?.setTextColor(confirmTextColor)
                    rightBtn?.setOnClickListener { _ ->
                        listener?.doEnter(mDialog)
                        if (hideDialogWhileClickRight) { dismiss() }
                    }
                } else {
                    rightBtn?.visibility = View.GONE
                    btnLine?.visibility = View.GONE
                }
            } else {
                bottomBtnLayout?.visibility = View.GONE
                setCanceledOnTouchOutside(true)
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
