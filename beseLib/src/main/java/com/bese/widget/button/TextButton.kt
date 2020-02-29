package com.bese.widget.button

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.bese.R

/**
 * 简单文字按钮控件：可定义按钮背景和按下颜色
 */
class TextButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val SHADOW_WIDTH_DEF = 0
        private const val CORNER_RADIUS_DEF = 0
        private const val BG_COLOR_DEF = Color.WHITE
        private const val BG_COLOR_PRESSED_DEF = Color.TRANSPARENT

    }

    /**
     * 按钮圆角大小
     */
    private var cornerRadius: Float = CORNER_RADIUS_DEF.toFloat()

    /**
     * 按钮背景色 默认白色
     */
    private var bgColor: Int = BG_COLOR_DEF
    /**
     * 按钮按下背景色 默认背景色加深一层
     */
    private var bgColorPressed: Int = BG_COLOR_PRESSED_DEF

    /**
     * 阴影shadow大小 默认为0
     */
    private var shadowWidth: Float = SHADOW_WIDTH_DEF.toFloat()

    private var normalDrawable: Drawable? = null
    private var pressedDrawable: Drawable? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextButton)
        cornerRadius = typedArray.getDimension(R.styleable.TextButton_buttonRadius, CORNER_RADIUS_DEF.toFloat())
        bgColor = typedArray.getColor(R.styleable.TextButton_buttonBgColor, BG_COLOR_DEF)
        bgColorPressed = typedArray.getColor(R.styleable.TextButton_buttonBgColorPressed, BG_COLOR_PRESSED_DEF)
        shadowWidth = typedArray.getDimension(R.styleable.TextButton_buttonShadowWidth, SHADOW_WIDTH_DEF.toFloat())

        // TypedArray must be Recycle after used
        typedArray.recycle()

        if (bgColorPressed == 0) {
            // 未指定按下颜色时，按下颜色跟随常规色。
            // 如果是边框，也可定义按下颜色，按下时边框颜色改变。
            bgColorPressed = bgColor
        }

        gravity = Gravity.CENTER

        updateDrawable()
    }

    fun setBgColor(@ColorRes bgColor: Int) {
        this.bgColor = ContextCompat.getColor(context, bgColor)
    }

    fun setBgColorPressed(@ColorRes pressBgColor: Int) {
        this.bgColorPressed = ContextCompat.getColor(context, pressBgColor)
    }

    /**
     * 更新背景Drawable
     */
    private fun updateDrawable() {
        normalDrawable = createSolidDrawable(cornerRadius, bgColor)
        pressedDrawable = createSolidDrawable(cornerRadius, bgColorPressed)

        val stateListDrawable = StateListDrawable()

        //给状态选择器添加状态
        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        stateListDrawable.addState(intArrayOf(), normalDrawable)

        background = stateListDrawable

    }

    /**
     * 设置背景Drawable
     */
    fun updateBg(drawable: Drawable?) {
        drawable?.run { background = this }
    }

    /**
     * 获取实心圆角Drawable
     * @param radius 圆角大小
     * @param color 背景色
     * @return Drawable
     */
    private fun createSolidDrawable(radius: Float, color: Int): Drawable {
        //外边圆角弧度：1-2 左上  3-4 右上   5-6 右下  7-8 左下
        val outerRadius = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
        // 圆角矩形区域Shape
        val shapeDrawable = ShapeDrawable(RoundRectShape(outerRadius, null, null))
        // 区域背景色
        shapeDrawable.paint.color = color
        return shapeDrawable
    }

}
