package com.bese.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.bese.R

/**
 * Created by dell on 2017/12/31.
 * 绘制虚线自定义控件
 */
class DashView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    /**间距宽度 */
    private var dashWidth = DASH_WIDTH
    /**线段宽度 */
    private var lineWidth = LINE_WIDTH
    /**线段高度 */
    private var lineThick = LINE_HEIGHT
    /**线段颜色 */
    private var lineColor = LINE_COLOR
    /**线条默认方向 */
    private var dashOrientation = DASH_ORIENTATION
    private var widthSize = 0
    private var heightSize = 0
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    companion object {
        const val DASH_WIDTH = 100f
        const val LINE_WIDTH = 100f
        const val LINE_HEIGHT = 10f
        const val LINE_COLOR = 0xAAAAAA
        /**虚线的方向 */
        const val ORIENTATION_HORIZONTAL = 0
        const val ORIENTATION_VERTICAL = 1
        /**默认为水平方向 */
        const val DASH_ORIENTATION = ORIENTATION_HORIZONTAL
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DashView)
        dashWidth = typedArray.getDimension(R.styleable.DashView_dashWidth, DASH_WIDTH)
        lineWidth = typedArray.getDimension(R.styleable.DashView_solidWidth, LINE_WIDTH)
        lineThick = typedArray.getDimension(R.styleable.DashView_solidThick, LINE_HEIGHT)
        lineColor = typedArray.getColor(R.styleable.DashView_solidColor, LINE_COLOR)
        dashOrientation = typedArray.getInteger(R.styleable.DashView_dashOrientation, DASH_ORIENTATION)
        mPaint.color = lineColor
        mPaint.strokeWidth = lineThick
        typedArray.recycle()
    }

    fun setDashWidth(dashWidth: Float) {
        this.dashWidth = dashWidth
    }

    fun setSolidWidth(solidWidth: Float) {
        lineWidth = solidWidth
    }

    fun setSolidThick(solidThick: Float) {
        lineThick = solidThick
    }
    fun setSolidColor(@ColorInt color: Int) {
        lineColor = color
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSize = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        heightSize = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        if (dashOrientation == ORIENTATION_HORIZONTAL) { ////不管在布局文件中虚线高度设置为多少，控件的高度统一设置为线段的高度
            setMeasuredDimension(widthSize, lineThick.toInt())
        } else { //当为竖直方向时，控件宽度设置为虚线的高度
            setMeasuredDimension(lineThick.toInt(), heightSize)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (dashOrientation) {
            ORIENTATION_VERTICAL -> drawVerticalLine(canvas)
            else -> drawHorizontalLine(canvas)
        }
    }

    /**
     * 画水平方向虚线
     * @param canvas
     */
    private fun drawHorizontalLine(canvas: Canvas) {
        var totalWidth = 0f
        canvas.save()
        val pts = floatArrayOf(0f, 0f, lineWidth, 0f)
        //在画线之前需要先把画布向下平移办个线段高度的位置，目的就是为了防止线段只画出一半的高度
//因为画线段的起点位置在线段左下角
        canvas.translate(0f, lineThick / 2)
        while (totalWidth <= widthSize) {
            canvas.drawLines(pts, mPaint)
            canvas.translate(lineWidth + dashWidth, 0f)
            totalWidth += lineWidth + dashWidth
        }
        canvas.restore()
    }

    /**
     * 画竖直方向虚线
     * @param canvas
     */
    private fun drawVerticalLine(canvas: Canvas) {
        var totalWidth = 0f
        canvas.save()
        val pts = floatArrayOf(0f, 0f, 0f, lineWidth)
        //在画线之前需要先把画布向右平移半个线段高度的位置，目的就是为了防止线段只画出一半的高度
//因为画线段的起点位置在线段左下角
        canvas.translate(lineThick / 2, 0f)
        while (totalWidth <= heightSize) {
            canvas.drawLines(pts, mPaint)
            canvas.translate(0f, lineWidth + dashWidth)
            totalWidth += lineWidth + dashWidth
        }
        canvas.restore()
    }

}