package com.bese.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.util.LinkedList

/**
 * 线性自动换行布局
 */
class WrapLayout : ViewGroup {

    private var adjustChildWidthWithParent: Boolean = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        measure(widthMeasureSpec, heightMeasureSpec, widthMode, widthSize, heightMode, heightSize)
    }

    private fun measure(widthMeasureSpec: Int, heightMeasureSpec: Int, widthMode: Int, widthSize: Int, heightMode: Int, heightSize: Int) {
        // 可用宽度
        val availableWidth = widthSize - paddingLeft - paddingRight
        // 实际需要的高度
        var parentViewHeight = 0
        // 记录行宽
        var rowWidth = 0
        // 记录行高
        var rowHeight = 0
        // 记录子View宽度
        var childViewWidth: Int
        // 记录子View高度
        var childViewHeight: Int
        var childView: View
        val rowViews = LinkedList<View>()
        val widthSizeUnspecified = widthMode == View.MeasureSpec.UNSPECIFIED
        for (position in 0 until childCount) {
            childView = getChildAt(position)
            if (childView.visibility == View.GONE) {
                continue
            }
            // 测量并计算当前View需要的宽高
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val lp = childView.layoutParams as LayoutParams
            childViewWidth = lp.leftMargin + childView.measuredWidth + lp.rightMargin
            childViewHeight = lp.topMargin + childView.measuredHeight + lp.bottomMargin

            // 如果宽度方面是包括，那么就记录总宽度，否则就换行并调整子Vie的宽度
            if (widthSizeUnspecified) {
                rowWidth += childViewWidth
                // 更新行高
                if (childViewHeight > parentViewHeight) {
                    parentViewHeight = childViewHeight
                }
            } else {
                // 如果宽度方面加上当前View就超过了可用宽度
                if (rowWidth + childViewWidth > availableWidth) {
                    // 就调整之前的View的宽度以充满
                    if (adjustChildWidthWithParent) {
                        adjustChildWidthWithParent(rowViews, availableWidth, widthMeasureSpec, heightMeasureSpec)
                    }
                    rowViews.clear()
                    //  清空行宽
                    rowWidth = 0
                    // 增加View高度
                    parentViewHeight += rowHeight
                    // 清空行高
                    rowHeight = 0
                }

                rowViews.add(childView)
                // 增加行宽
                rowWidth += childViewWidth

                // 更新行高
                if (childViewHeight > rowHeight) {
                    rowHeight = childViewHeight
                }
            }
        }

        if (!widthSizeUnspecified && !rowViews.isEmpty()) {
            // 就调整剩余的View的宽度以充满
            if (adjustChildWidthWithParent) {
                adjustChildWidthWithParent(rowViews, availableWidth, widthMeasureSpec, heightMeasureSpec)
            }
            rowViews.clear()
            // 增加View高度
            parentViewHeight += rowHeight
        }

        var finalWidth = 0
        var finalHeight = 0
        when (widthMode) {
            MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> finalWidth = widthSize
            MeasureSpec.UNSPECIFIED -> finalWidth = rowWidth + paddingLeft + paddingRight
            else -> {
            }
        }
        when (heightMode) {
            MeasureSpec.EXACTLY -> finalHeight = heightSize
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> finalHeight =
                parentViewHeight + paddingTop + paddingBottom
            else -> {
            }
        }
        setMeasuredDimension(finalWidth, finalHeight)
    }

    /**
     * 调整views集合中的View，让所有View的宽度加起来正好等于parentViewWidth
     *
     * @param views                   子View集合
     * @param parentViewWidth         父Vie的宽度
     * @param parentWidthMeasureSpec  父View的宽度规则
     * @param parentHeightMeasureSpec 父View的高度规则
     */
    private fun adjustChildWidthWithParent(views: LinkedList<View>, parentWidth: Int, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        var parentViewWidth = parentWidth
        // 先去掉所有子View的外边距
        for (view in views) {
            if (view.layoutParams is ViewGroup.MarginLayoutParams) {
                val lp = view.layoutParams as ViewGroup.MarginLayoutParams
                parentViewWidth -= lp.leftMargin + lp.rightMargin
            }
        }

        // 去掉宽度大于平均宽度的View后再次计算平均宽度
        var averageWidth = parentViewWidth / views.size
        var bigTabCount = views.size
        while (true) {
            val iterator = views.iterator()
            while (iterator.hasNext()) {
                val view = iterator.next()
                if (view.measuredWidth > averageWidth) {
                    parentViewWidth -= view.measuredWidth
                    bigTabCount--
                    iterator.remove()
                }
            }
            averageWidth = parentViewWidth / bigTabCount
            var end = true
            for (view in views) {
                if (view.measuredWidth > averageWidth) {
                    end = false
                }
            }
            if (end) {
                break
            }
        }

        // 修改宽度小于新的平均宽度的View的宽度
        for (view in views) {
            if (view.measuredWidth < averageWidth) {
                val layoutParams = view.layoutParams
                layoutParams.width = averageWidth
                view.layoutParams = layoutParams
                // 再次测量让新宽度生效
                if (layoutParams is MarginLayoutParams) {
                    measureChildWithMargins(view, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, 0)
                } else {
                    measureChild(view, parentWidthMeasureSpec, parentHeightMeasureSpec)
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val viewWidth = r - l
        var leftOffset = paddingLeft
        var topOffset = paddingTop
        var rowMaxHeight = 0
        var childView: View
        var w = 0
        val count = childCount
        while (w < count) {
            childView = getChildAt(w)
            val lp = childView.layoutParams as LayoutParams

            // 如果加上当前子View的宽度后超过了ViewGroup的宽度，就换行
            val occupyWidth = lp.leftMargin + childView.measuredWidth + lp.rightMargin
            if (leftOffset + occupyWidth + paddingRight > viewWidth) {
                // 回到最左边
                leftOffset = paddingLeft
                // 换行
                topOffset += rowMaxHeight
                rowMaxHeight = 0
            }

            val left = leftOffset + lp.leftMargin
            val top = topOffset + lp.topMargin
            val right = leftOffset + lp.leftMargin + childView.measuredWidth
            val bottom = topOffset + lp.topMargin + childView.measuredHeight
            childView.layout(left, top, right, bottom)

            // 横向偏移
            leftOffset += occupyWidth

            // 试图更新本行最高View的高度
            val occupyHeight = lp.topMargin + childView.measuredHeight + lp.bottomMargin
            if (occupyHeight > rowMaxHeight) {
                rowMaxHeight = occupyHeight
            }
            w++
        }
    }

    /**
     * Returns a set of layout parameters with a width of
     * [ViewGroup.LayoutParams.MATCH_PARENT],
     * and a height of [ViewGroup.LayoutParams.MATCH_PARENT].
     */
    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    /**
     * 设置当一行的子View宽度没有充满LineWrapLayout时是否调整这一行所有子View的宽度以充满LineWrapLayout
     *
     * @param adjustChildWidthWithParent 是否调整
     */
    fun setAdjustChildWidthWithParent(adjustChildWidthWithParent: Boolean) {
        this.adjustChildWidthWithParent = adjustChildWidthWithParent
        requestLayout()
    }

    class LayoutParams : MarginLayoutParams {

        /**
         * Copy constructor. Clones the width, height, margin values, and
         * gravity of the source.
         *
         * @param source The layout params to copy from.
         */
        constructor(source: LayoutParams) : super(source) {}
        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {}

        constructor(width: Int, height: Int) : super(width, height) {}

        constructor(source: ViewGroup.LayoutParams) : super(source) {}

        constructor(source: ViewGroup.MarginLayoutParams) : super(source) {}

    }
}
