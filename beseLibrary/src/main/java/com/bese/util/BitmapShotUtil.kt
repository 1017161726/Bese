package com.bese.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.LruCache
import android.view.View
import android.webkit.WebView
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 * 位图截图工具类
 *      ## 工具提供部分截图方法，ScreenUtils包含Activity截屏，如需要请 Double Shift 查看
 *
 */
object BitmapShotUtil {

    /**
     * 全屏截屏
     */
    private fun shotScreen(context: Activity?): Bitmap? {
        context?.run {
            val bmp = Bitmap.createBitmap(window.decorView.width, window.decorView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            window.decorView.draw(canvas)
            return bmp
        }
        return null
    }

    /**
     * 根据指定的view截图
     */
    fun shotView(view: View?): Bitmap? {
        view?.run {
            // build view cache
            isDrawingCacheEnabled = true
            buildDrawingCache()

            measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
            layout(x.toInt(), y.toInt(), x.toInt() + measuredWidth, y.toInt() + measuredHeight)
            val bitmap = Bitmap.createBitmap(drawingCache, 0, 0, measuredWidth, measuredHeight)

            // remove view cache
            isDrawingCacheEnabled = false
            destroyDrawingCache()

            return bitmap
        }
        return null
    }

    /**
     * Scrollview截屏
     */
    fun shotScrollView(scrollView: ScrollView?): Bitmap? {
        scrollView?.run {
            var h = 0
            for (i in 0 until childCount) {
                h += getChildAt(i).height
                getChildAt(i).setBackgroundColor(Color.parseColor("#f5f7fa"))
            }
            val bitmap = Bitmap.createBitmap(width, h, Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            draw(canvas)
            return bitmap
        }
        return null
    }

    /**
     * RecyclerView截屏
     */
    fun shotRecyclerView(view: RecyclerView?): Bitmap? {
        view?.run {
            adapter?.let {
                val size = it.itemCount
                val paint = Paint()
                var height = 0
                var drawHeight = 0f
                val bitmapCache = LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory() / 1024).toInt() / 8)
                for (i in 0 until size) {
                    val holder = it.createViewHolder(this, it.getItemViewType(i))
                    it.onBindViewHolder(holder, i)
                    holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    holder.itemView.layout(0, 0, holder.itemView.measuredWidth, holder.itemView.measuredHeight)
                    holder.itemView.isDrawingCacheEnabled = true
                    holder.itemView.buildDrawingCache()
                    holder.itemView.drawingCache?.run { bitmapCache.put(i.toString(), this) }
                    height += holder.itemView.measuredHeight
                }
                val bigBitmap = Bitmap.createBitmap(measuredWidth, height, Bitmap.Config.ARGB_8888)
                val bigCanvas = Canvas(bigBitmap)
                val bgDrawable = background
                if (bgDrawable is ColorDrawable) {
                    val lColor = bgDrawable.color
                    bigCanvas.drawColor(lColor)
                }
                for (i in 0 until size) {
                    val bitmap = bitmapCache[i.toString()]
                    bigCanvas.drawBitmap(bitmap, 0f, drawHeight, paint)
                    drawHeight += bitmap.height
                    bitmap.recycle()
                }
                return bigBitmap
            }
        }
        return null
    }

    /**
     * 截取webView快照(webView加载的整个内容的大小)
     * @param webView 前提：WebView要设置webView.setDrawingCacheEnabled(true)
     * @param shotAllView 是截图整个 WebView加载内容，还是截图屏幕显示的WebView部分内容
     * @return Bitmap
     */
    private fun shotWebView(webView: WebView?, shotAllView: Boolean): Bitmap? {
        webView?.run {
            if (shotAllView) { return drawingCache }
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            draw(canvas)
            return bmp
        }
        return null
    }

}