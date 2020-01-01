package com.bese.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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
class ImageRectView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ImageView(context, attrs, defStyleAttr) {

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
        // zero is not support in there, default value is 1.
        if (mRatio == 0f) mRatio = 1f
        // negative number will be compile to {Inverse Proportional Number(etc: -2 will be convert to 0.5)}.
        if (mRatio < 0f) mRatio = -1 / mRatio

        // recycle the attribute asset.
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
        val heightSize = MeasureSpec.makeMeasureSpec((measuredWidth * mRatio).toInt(), MeasureSpec.EXACTLY)
        setMeasuredDimension(MeasureSpec.getSize(widthSize), MeasureSpec.getSize(heightSize))
    }

    override fun onDraw(canvas: Canvas?) {
        // universal radius should be less than either width or height.
        if (mRadius > 0 && mRadius > min(width, height) / 2) {
            mRadius = min(width, height).toFloat() / 2
        }

        // universal radius is unsupported when need diy radius.
        if (mRadiusLeftTop == 0f && mRadiusRightTop == 0f && mRadiusLeftBottom == 0f && mRadiusRightBottom == 0f) {
            mRadiusLeftTop = mRadius
            mRadiusRightTop = mRadius
            mRadiusLeftBottom = mRadius
            mRadiusRightBottom = mRadius
        }

        // one of the corner radius is large than zero means need clip the paint to draw radius.
        if (mRadiusLeftTop > 0f || mRadiusRightTop > 0f || mRadiusLeftBottom > 0f || mRadiusRightBottom > 0f) {
            canvas?.clipPath(Path().apply {
                moveTo(mRadiusLeftTop, 0f)
                lineTo(width - mRadiusRightTop, 0f)
                arcTo(RectF(width.toFloat() - mRadiusRightTop * 2, 0f, width.toFloat(), mRadiusRightTop * 2), 270f, 90f)
                lineTo(width.toFloat(), height - mRadiusRightBottom)
                arcTo(RectF(width.toFloat() - mRadiusRightBottom * 2, height.toFloat() - mRadiusRightBottom * 2, width.toFloat(), height.toFloat()), 0f, 90f)
                lineTo(mRadiusLeftBottom, height.toFloat())
                arcTo(RectF(0f, height.toFloat() - mRadiusLeftBottom * 2, mRadiusLeftBottom * 2, height.toFloat()), 90f, 90f)
                lineTo(0f, mRadiusLeftTop)
                arcTo(RectF(0f, 0f, mRadiusLeftTop * 2, mRadiusLeftTop * 2), 180f, 90f)
            })
            canvas?.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        }
        super.onDraw(canvas)

    }

}