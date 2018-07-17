package com.qingmei2.rximagepicker_extension_wechat.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet

import com.qingmei2.rximagepicker_extension.ui.widget.CheckView

open class WechatCheckView : CheckView {

    private var rect: RectF? = null
    private var drawableRect: Rect? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun init(context: Context) {
        super.init(context)
        mStrokePaint.strokeWidth = WECHAT_STROKE_WIDTH * mDensity

        // draw white stroke
        val dx = mDensity * SIZE / 4
        val dy = mDensity * SIZE / 4
        rect = RectF(2 * dx - PROOFREAD_SIZE, dy - PROOFREAD_SIZE,
                3 * dx + PROOFREAD_SIZE, 2 * dy + PROOFREAD_SIZE)
        drawableRect = Rect(rect!!.left.toInt(), rect!!.top.toInt(), rect!!.right.toInt(), rect!!.bottom.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        // draw outer and inner shadow
        initShadowPaint()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawRoundRectCheckView(canvas, rect, mStrokePaint)
        } else {
            drawRectCheckView(canvas, rect, mStrokePaint)
        }

        // draw content
        if (mCountable) {
            if (mCheckedNum != CheckView.UNCHECKED) {
                initBackgroundPaint()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawRoundRectCheckView(canvas, rect, mBackgroundPaint)
                } else {
                    drawRectCheckView(canvas, rect, mBackgroundPaint)
                }
                initTextPaint()
                val text = mCheckedNum.toString()
                val baseX = (rect!!.width() - mTextPaint!!.measureText(text)).toInt() / 2
                val baseY = (rect!!.height() - mTextPaint!!.descent() - mTextPaint!!.ascent()).toInt() / 2
                canvas.drawText(text, baseX + rect!!.left, baseY + rect!!.top, mTextPaint!!)
            }
        } else {
            if (mChecked) {
                initBackgroundPaint()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawRoundRectCheckView(canvas, rect, mBackgroundPaint)
                } else {
                    drawRectCheckView(canvas, rect, mBackgroundPaint)
                }
                mCheckDrawable!!.bounds = drawableRect!!
                mCheckDrawable!!.draw(canvas)
            }
        }

        // enable hint
        alpha = if (mEnabled) 1.0f else 0.5f
    }

    private fun drawRectCheckView(canvas: Canvas, rect: RectF?, paint: Paint?) {
        canvas.drawRect(rect!!, paint!!)
    }

    @TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
    private fun drawRoundRectCheckView(canvas: Canvas, rect: RectF?, paint: Paint?) {
        canvas.drawRoundRect(rect!!, WECHAT_STROKE_CONNER, WECHAT_STROKE_CONNER, paint!!)
    }

    companion object {

        private const val WECHAT_STROKE_WIDTH = 1.5f // dp
        private const val WECHAT_STROKE_CONNER = 5f // dp
        private const val SIZE = 48 // dp

        private const val PROOFREAD_SIZE = 8
    }
}
