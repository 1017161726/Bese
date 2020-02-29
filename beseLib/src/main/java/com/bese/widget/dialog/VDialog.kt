package com.bese.widget.dialog

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
import com.bese.util.ConvertUtil

/**
 * 弹窗工具
 *      效果：展示自定义View的弹窗
 *
 */
class VDialog(private val mCtx: Context) {

    @ColorInt
    private var backgroundColor: Int = Color.WHITE
    private var backgroundDim: Float = 0.5f

    private var diyView: View? = null
    private var diyViewMarginTop: Int = getDp(00f)
    private var diyViewMarginBottom: Int = getDp(00f)
    private var diyViewMarginHorizontal: Int = getDp(00f)

    @ColorInt
    private var bottomLineColor: Int = ContextCompat.getColor(mCtx, R.color.color_ccc)
    private var bottomLayoutTopMargin: Int = getDp(1f)
    private var bottomLayoutVisible: Boolean = true

    private var cancelText: String? = "取消"
    private var cancelTextSize: Float = 15f
    @ColorInt
    private var cancelTextColor: Int = ContextCompat.getColor(mCtx, R.color.color_333)

    private var confirmText: String? = "确定"
    private var confirmTextSize: Float = 15f
    @ColorInt
    private var confirmTextColor: Int = ContextCompat.getColor(mCtx, R.color.color_0099ff)

    private var dialogLeftMargin: Int = getDp(50f)
    private var dialogRightMargin: Int = getDp(50f)

    private var backgroundDrawable: Drawable? = null

    private var hideDialogWhileClickRight: Boolean = true

    private var dismissClickOutside: Boolean = false

    private var cancelable: Boolean = true

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

    private fun getDp(dp750: Float): Int {
        return ConvertUtil.getSizedPx(dp750).toInt()
    }

    /**
     * 设置Dialog的背景渲染色
     */
    fun setBackgroundColor(@ColorInt color: Int): VDialog {
        this.backgroundColor = color
        return this
    }

    /**
     * 设置Dialog的背景渲染色，ColorInt
     */
    fun setBackgroundColorRaw(@ColorInt color: Int): VDialog {
        this.backgroundColor = color
        return this
    }

    /**
     * 设置Dialog的背景渲染色
     */
    fun setBackgroundDim(dim: Float): VDialog {
        this.backgroundDim = dim
        return this
    }

    /**
     * 设置自定义布局
     * 设置后提供的左右屏幕边距会无效
     */
    fun setDiyView(diyView: View): VDialog {
        this.diyView = diyView
        return this
    }

    /**
     * 设置Dialog的自定义View竖直间距
     */
    fun setDiyViewMarginTop(margin: Float): VDialog {
        this.diyViewMarginTop = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的自定义View竖直间距
     */
    fun setDiyViewMarginBottom(margin: Float): VDialog {
        this.diyViewMarginBottom = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的自定义View水平间距
     */
    fun setDiyViewMarginHorizontal(margin: Float): VDialog {
        this.diyViewMarginHorizontal = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的底部按钮分隔线颜色
     */
    fun setBottomLineColor(@ColorInt lineColor: Int): VDialog {
        this.bottomLineColor = lineColor
        return this
    }

    /**
     * 设置Dialog的取消按钮文案
     */
    fun setCancelText(cancelTxt: String?): VDialog {
        this.cancelText = cancelTxt
        return this
    }

    /**
     * 设置Dialog的取消按钮文字颜色
     */
    fun setCancelTextColor(@ColorInt color: Int): VDialog {
        this.cancelTextColor = color
        return this
    }

    /**
     * 设置Dialog的取消按钮文字大小
     */
    fun setCancelTextSize(size: Float): VDialog {
        this.cancelTextSize = size
        return this
    }

    /**
     * 设置Dialog的确认按钮文案
     */
    fun setEnterText(enterTxt: String?): VDialog {
        this.confirmText = enterTxt
        return this
    }

    /**
     * 设置Dialog的确认按钮文字颜色
     */
    fun setEnterTextColor(@ColorInt color: Int): VDialog {
        this.confirmTextColor = color
        return this
    }

    /**
     * 设置Dialog的确认按钮文字大小
     */
    fun setEnterTextSize(size: Float): VDialog {
        this.confirmTextSize = size
        return this
    }

    /**
     * 设置Dialog的监听
     */
    fun setListener(listener: DialogListener): VDialog {
        this.listener = listener
        return this
    }

    /**
     * 设置Dialog的左屏距
     */
    fun setLeftScreenMargin(margin: Float): VDialog {
        this.dialogLeftMargin = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的右屏距
     */
    fun setRightScreenMargin(margin: Float): VDialog {
        this.dialogRightMargin = getDp(margin)
        return this
    }

    /**
     * 设置Dialog的底部视图是否可见
     */
    fun setBottomLayoutVisible(visible: Boolean): VDialog {
        this.bottomLayoutVisible = visible
        return this
    }

    /**
     * 设置Dialog的根背景
     */
    fun setBackgroundDrawable(drawable: Drawable): VDialog {
        this.backgroundDrawable = drawable
        return this
    }

    /**
     * 设置确定按钮单击后是否弹窗消失
     */
    fun setRightClickHideDialog(hide: Boolean): VDialog {
        this.hideDialogWhileClickRight = hide
        return this
    }

    /**
     * 设置按钮外部蒙层弹窗是否消失
     */
    fun setDismissClickOutside(hide: Boolean): VDialog {
        this.dismissClickOutside = hide
        return this
    }

    /**
     * 设置返回按钮是否让弹窗消失
     */
    fun setDialogCancelable(cancelable: Boolean): VDialog {
        this.cancelable = cancelable
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
            dialogView = LayoutInflater.from(mCtx).inflate(R.layout.dialog_diy_view, null)
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
                // 无按钮，需要考虑如何让弹窗消失，不阻塞用户操作
//                setCanceledOnTouchOutside(true)
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
