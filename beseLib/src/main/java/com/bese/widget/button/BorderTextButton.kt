package com.bese.widget.button

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
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
 * 简单文字按钮控件：可定义边框和边框粗细，定义按钮按下颜色
 */
class BorderTextButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val SHADOW_WIDTH_DEF = 0
        private const val BORDERLINE_SIZE_DEF = 0
        private const val CORNER_RADIUS_DEF = 1
        private const val BORDERLINE_COLOR_DEF = Color.TRANSPARENT
        private const val BG_COLOR_DEF = Color.WHITE
        private const val BG_COLOR_PRESSED_DEF = Color.TRANSPARENT
    }

    /**
     * 按钮圆角大小
     */
    private var cornerRadius: Float = CORNER_RADIUS_DEF.toFloat()

    /**
     * 按钮边框线粗细 默认为1dp
     */
    private var borderLineSize: Float = BORDERLINE_SIZE_DEF.toFloat()
    /**
     * 按钮边框线颜色 默认为透明
     */
    private var borderLineColor: Int = BORDERLINE_COLOR_DEF

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

    private var borderDrawable: Drawable? = null
    private var borderPressedDrawable: Drawable? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BorderTextButton)
        cornerRadius = typedArray.getDimension(R.styleable.BorderTextButton_borderButtonRadius, CORNER_RADIUS_DEF.toFloat())
        borderLineSize = typedArray.getDimension(R.styleable.BorderTextButton_borderLineSize,
            BORDERLINE_SIZE_DEF.toFloat())
        borderLineColor = typedArray.getColor(R.styleable.BorderTextButton_borderLineColor, BORDERLINE_COLOR_DEF)
        bgColor = typedArray.getColor(R.styleable.BorderTextButton_borderButtonBgColor, BG_COLOR_DEF)
        bgColorPressed = typedArray.getColor(R.styleable.BorderTextButton_borderButtonBgColorPressed, BG_COLOR_PRESSED_DEF)
        shadowWidth = typedArray.getDimension(R.styleable.BorderTextButton_borderButtonShadowWidth, SHADOW_WIDTH_DEF.toFloat())

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

    private fun updateDrawable() {
        borderDrawable = createBorderDrawable(cornerRadius, borderLineSize, borderLineColor, bgColor)
        borderPressedDrawable = createBorderDrawable(cornerRadius, borderLineSize, borderLineColor, bgColorPressed)

        val stateListDrawable = StateListDrawable()
        //给状态选择器添加状态
        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), borderPressedDrawable)
        stateListDrawable.addState(intArrayOf(), borderDrawable)

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

    /**
     * 获取圆角边框Drawable
     * @param radius 圆角大小
     * @param borderSize 边框尺寸
     * @param borderColor 背景色
     * @param solidColor 中心区域点击背景色
     * @return Drawable
     */
    private fun createBorderDrawable(radius: Float, borderSize: Float, borderColor: Int, solidColor: Int): Drawable? {
        if (borderSize <= 0) {
            // Drawable 返回 null  背景为空白  不会出现问题
            return null
        }
        val outerRadius = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
        val innerR = radius - borderSize
        val innerRadius = floatArrayOf(innerR, innerR, innerR, innerR, innerR, innerR, innerR, innerR)
        val rectF = RectF(borderSize, borderSize, borderSize, borderSize)

        val roundShape = RoundRectShape(outerRadius, rectF, innerRadius)
        val shapeDrawable = ShapeDrawable(roundShape)
        shapeDrawable.paint.color = borderColor

        val layerArr = arrayOf(createSolidDrawable(radius, solidColor), shapeDrawable)
        return LayerDrawable(layerArr)
    }
}
