package com.bese.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bese.R
import kotlin.math.min


/**
 * Square ImageView Component
 *      View allowed ImageView show in a square area, which will resolved the image variable.
 *      View can also defined the ratio to show{formula: ratio = height / width}.
 *      Inverse ratio will be compiled to negative number that apply to {Inverse Proportional Function}.
 *
 * @attr squareRatio R.SquareImageView_squareRatio
 * @author Fires 2019.12.28
 */
class ImageRectView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {

    private var mRatio = 1f
    private var mRadius = 0f
    private var mRadiusLeftTop = 0f              // Radius1
    private var mRadiusRightTop = 0f             // Radius2
    private var mRadiusLeftBottom = 0f           // Radius3
    private var mRadiusRightBottom = 0f          // Radius4

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageRectView)
        mRatio = typedArray.getFloat(R.styleable.ImageRectView_squareRatio, 1f)
        mRadius = typedArray.getDimension(R.styleable.ImageRectView_squareRadius, 0f)
        mRadiusLeftTop = typedArray.getDimension(R.styleable.ImageRectView_squareLeftTopRadius, 0f)
        mRadiusRightTop = typedArray.getDimension(R.styleable.ImageRectView_squareRightTopRadius, 0f)
        mRadiusLeftBottom = typedArray.getDimension(R.styleable.ImageRectView_squareLeftBottomRadius, 0f)
        mRadiusRightBottom = typedArray.getDimension(R.styleable.ImageRectView_squareRightBottomRadius, 0f)

        setRatio(mRatio)

        setRadius(mRadius)

        setRadius(mRadiusLeftTop, mRadiusRightTop, mRadiusLeftBottom, mRadiusRightBottom)

        setPadding(0, 0, 0, 0)

        // recycle the attribute asset.
        typedArray.recycle()
    }

    /** set ratio value */
    fun setRatio(ratio: Float) {
        // zero is not support in there, default value is 1.
        if (mRatio == 0f) mRatio = 1f
        // negative number will be compile to {Inverse Proportional Number(etc: -2 will be convert to 0.5)}.
        if (mRatio < 0f) mRatio = -1 / mRatio
        updateView()
    }

    /** set universal radius value */
    fun setRadius(radius: Float) {
        mRadius = if (radius > 0) radius else 0f
        visibility = View.VISIBLE
        updateView()
    }

    /** set per radius value */
    fun setRadius(leftTop: Float, rightTop: Float, leftBottom: Float, rightBottom: Float) {
        mRadiusLeftTop = if (leftTop > 0) leftTop else 0f
        mRadiusRightTop = if (rightTop > 0) rightTop else 0f
        mRadiusLeftBottom = if (leftBottom > 0) leftBottom else 0f
        mRadiusRightBottom = if (rightBottom > 0) rightBottom else 0f
        updateView()
    }

    /** variable value will request update view to show */
    private fun updateView() {
        /**
         * api < 14 invalidate function will spark onDraw function, api >=14 will not.
         * {class suggest use in static state view.}
         * this function may be not work in trend view.
         */
    }

    /**
     * padding value is unsupported in this view. Unpredictable size in paddingBottom.
     *   So there need reset the padding value to disable giving numbers.
     */
    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(0, 0, 0, 0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
        val heightSize = MeasureSpec.makeMeasureSpec((measuredWidth * mRatio).toInt(), MeasureSpec.EXACTLY)
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        // universal radius should be less than either width or height.
        if (mRadius > 0 && mRadius > min(width, height) / 2) {
            mRadius = min(width, height).toFloat() / 2
        }

        // diy radius is disable when universal radius has value.
        if (mRadiusLeftTop == 0f && mRadiusRightTop == 0f && mRadiusLeftBottom == 0f && mRadiusRightBottom == 0f || mRadius > 0) {
            mRadiusLeftTop = mRadius
            mRadiusRightTop = mRadius
            mRadiusLeftBottom = mRadius
            mRadiusRightBottom = mRadius
        }

        // one of the corner radius is large than zero means need clip the paint to draw radius.
        if (mRadiusLeftTop > 0f || mRadiusRightTop > 0f || mRadiusLeftBottom > 0f || mRadiusRightBottom > 0f) {
            canvas?.clipPath(Path().apply {
                // padding value will take part in the operation with empty value: 0. Just adjust the really operation rules.
                moveTo(mRadiusLeftTop + paddingLeft, paddingTop.toFloat())
                lineTo(width - mRadiusRightTop - paddingRight, paddingTop.toFloat())
                arcTo(RectF(width - mRadiusRightTop * 2 - paddingRight, paddingTop.toFloat(), width.toFloat() - paddingRight, mRadiusRightTop * 2 + paddingTop), 270f, 90f)
                lineTo(width.toFloat() - paddingRight, height - mRadiusRightBottom - paddingBottom)
                arcTo(RectF(width.toFloat() - mRadiusRightBottom * 2 - paddingRight, height.toFloat() - mRadiusRightBottom * 2 - paddingBottom, width.toFloat() - paddingRight, height.toFloat() - paddingBottom), 0f, 90f)
                lineTo(mRadiusLeftBottom + paddingLeft, height.toFloat() - paddingBottom)
                arcTo(RectF(paddingLeft.toFloat(), height - mRadiusLeftBottom * 2 - paddingBottom, mRadiusLeftBottom * 2 + paddingLeft, height.toFloat() - paddingBottom), 90f, 90f)
                lineTo(paddingLeft.toFloat(), mRadiusLeftTop + paddingTop)
                arcTo(RectF(paddingLeft.toFloat(), paddingTop.toFloat(), mRadiusLeftTop * 2 + paddingLeft, mRadiusLeftTop * 2 + paddingTop), 180f, 90f)
            })
            canvas?.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        }
        super.onDraw(canvas)

    }

    fun clipShape() {

    }

}