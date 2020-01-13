package com.bese.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.bese.R

/**
 * Round ImageView Component
 *      View allowed ImageView show in a square area, which will resolved the image variable.
 *      View can also defined the ratio to show{formula: ratio = height / width}.
 *      Inverse ratio will be compiled to negative number that apply to {Inverse Proportional Function}.
 *
 * @attr squareRatio R.SquareImageView_squareRatio
 * @author Fires 2019.12.28
 */
class ImageRoundView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ImageView(context, attrs, defStyleAttr) {

    private var mRatio = 1f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageRectView)
        mRatio = typedArray.getFloat(R.styleable.ImageRectView_squareRatio, 1f)
        // zero is not support in there
        if (mRatio == 0f) mRatio = 1f
        // negative number will be compile to {Inverse Proportional Number}.
        if (mRatio < 0f) mRatio = -1 / mRatio
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
        val heightSize = MeasureSpec.makeMeasureSpec((measuredWidth * mRatio).toInt(), MeasureSpec.EXACTLY)
        setMeasuredDimension(MeasureSpec.getSize(widthSize), MeasureSpec.getSize(heightSize))
    }

}