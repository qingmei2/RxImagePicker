package com.qingmei2.rximagepicker_extension_wechat.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import com.qingmei2.rximagepicker_extension.ui.widget.CheckView;

public class WechatCheckView extends CheckView {

    private static final float WECHAT_STROKE_WIDTH = 1.5f; // dp
    private static final float WECHAT_STROKE_CONNER = 5f; // dp

    private RectF rect;
    private Rect drawableRect;

    private static final int PROOFREAD_SIZE = 8;

    public WechatCheckView(Context context) {
        super(context);
    }

    public WechatCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WechatCheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        getMStrokePaint().setStrokeWidth(WECHAT_STROKE_WIDTH * getMDensity());

        // draw white stroke
        float dx = getMDensity() * Companion.getSIZE() / 4;
        float dy = getMDensity() * Companion.getSIZE() / 4;
        rect = new RectF(2 * dx - PROOFREAD_SIZE, dy - PROOFREAD_SIZE,
                3 * dx + PROOFREAD_SIZE, 2 * dy + PROOFREAD_SIZE);
        drawableRect = new Rect((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw outer and inner shadow
        initShadowPaint();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawRoundRectCheckView(canvas, rect, getMStrokePaint());
        } else {
            drawRectCheckView(canvas, rect, getMStrokePaint());
        }

        // draw content
        if (getMCountable()) {
            if (getMCheckedNum() != Companion.getUNCHECKED()) {
                initBackgroundPaint();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawRoundRectCheckView(canvas, rect, getMBackgroundPaint());
                } else {
                    drawRectCheckView(canvas, rect, getMBackgroundPaint());
                }
                initTextPaint();
                String text = String.valueOf(getMCheckedNum());
                int baseX = (int) (rect.width() - getMTextPaint().measureText(text)) / 2;
                int baseY = (int) (rect.height() - getMTextPaint().descent() - getMTextPaint().ascent()) / 2;
                canvas.drawText(text, baseX + rect.left, baseY + rect.top, getMTextPaint());
            }
        } else {
            if (getMChecked()) {
                initBackgroundPaint();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawRoundRectCheckView(canvas, rect, getMBackgroundPaint());
                } else {
                    drawRectCheckView(canvas, rect, getMBackgroundPaint());
                }
                getMCheckDrawable().setBounds(drawableRect);
                getMCheckDrawable().draw(canvas);
            }
        }

        // enable hint
        setAlpha(getMEnabled() ? 1.0f : 0.5f);
    }

    private void drawRectCheckView(Canvas canvas, RectF rect, Paint paint) {
        canvas.drawRect(rect, paint);
    }

    @TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
    private void drawRoundRectCheckView(Canvas canvas, RectF rect, Paint paint) {
        canvas.drawRoundRect(rect, WECHAT_STROKE_CONNER, WECHAT_STROKE_CONNER, paint);
    }
}
