package com.marcoscherzer.minigui.lib.msimplegui.mlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.view.View;

import java.util.Collections;
import java.util.EnumSet;

/**
 * @version 0.0.1 author Marco Scherzer, Copyright Marco Scherzer , All rights reserved
 */
public class MBorderDrawableBuilder {


    private int fillColor;
    private int rippleColor;
    private float cornerRadiusDp;
    private int strokeColor;
    private float strokeWidthDp;
    private EnumSet<BorderEdge> strokeEdges;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MBorderDrawableBuilder() {
        this.fillColor = 0x00000000;  // transparent
        this.rippleColor = 0;
        this.cornerRadiusDp = 0f;
        this.strokeColor = 0;
        this.strokeWidthDp = 0f;
        this.strokeEdges = EnumSet.noneOf(BorderEdge.class);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final void styleViewWithBackground(View view, MBorderDrawableBuilder style, final int tp, final int lp, final int bp, final int rp) {
        final Drawable bg = style != null ? style.create(view.getContext()) : null;
        if (bg != null) view.setBackground(bg);
        final int strokePx = style != null ? style.getStrokePx(view.getContext()) : 0;
        final EnumSet<MBorderDrawableBuilder.BorderEdge> edges = style != null ? style.getStrokeEdges() : EnumSet.noneOf(MBorderDrawableBuilder.BorderEdge.class);
        final int padLeft = lp + (edges.contains(MBorderDrawableBuilder.BorderEdge.LEFT) ? strokePx : 0);
        final int padTop = tp + (edges.contains(MBorderDrawableBuilder.BorderEdge.TOP) ? strokePx : 0);
        final int padRight = rp + (edges.contains(MBorderDrawableBuilder.BorderEdge.RIGHT) ? strokePx : 0);
        final int padBottom = bp + (edges.contains(MBorderDrawableBuilder.BorderEdge.BOTTOM) ? strokePx : 0);
        view.setPadding(padLeft, padTop, padRight, padBottom);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MBorderDrawableBuilder setFillColor(int color) {
        this.fillColor = color;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MBorderDrawableBuilder setCornerRadius(float radiusDp) {
        this.cornerRadiusDp = radiusDp;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MBorderDrawableBuilder setRippleColor(int color) {
        this.rippleColor = color;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Setzt Rahmen nur an den angegebenen Rändern.
     * Wenn kein Edge übergeben wird, werden alle Ränder gezeichnet.
     */
    public MBorderDrawableBuilder setStroke(int color, float widthDp, BorderEdge... edges) {
        this.strokeColor = color;
        this.strokeWidthDp = widthDp;

        if (edges == null || edges.length == 0) {
            this.strokeEdges = EnumSet.allOf(BorderEdge.class);
        } else {
            this.strokeEdges = EnumSet.noneOf(BorderEdge.class);
            Collections.addAll(this.strokeEdges, edges);
        }
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public int getStrokePx(Context ctx) {
        return dpToPx(ctx, strokeWidthDp);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public EnumSet<BorderEdge> getStrokeEdges() {
        return EnumSet.copyOf(strokeEdges);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private int dpToPx(Context ctx, float dp) {
        return Math.round(dp * ctx.getResources().getDisplayMetrics().density);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private float dpToPxF(Context ctx, float dp) {
        return dp * ctx.getResources().getDisplayMetrics().density;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public Drawable create(Context context) {
        int strokePx = dpToPx(context, strokeWidthDp);
        float radiusPx = dpToPxF(context, cornerRadiusDp);

        Drawable content = new MBorderDrawable(
                fillColor,
                strokeColor,
                strokePx,
                strokeEdges,
                radiusPx
        );

        if (rippleColor != 0) {
            ColorStateList cs = ColorStateList.valueOf(rippleColor);
            return new RippleDrawable(cs, content, null);
        }
        return content;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public enum BorderEdge {TOP, LEFT, BOTTOM, RIGHT}

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static class MBorderDrawable extends Drawable {

        private final int fillColor;
        private final int strokeColor;
        private final int strokePx;
        private final EnumSet<BorderEdge> edges;
        private final float radiusPx;

        private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF rect = new RectF();
        private final Path roundClip = new Path();

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        MBorderDrawable(int fillColor,
                        int strokeColor,
                        int strokePx,
                        EnumSet<BorderEdge> edges,
                        float radiusPx) {
            this.fillColor = fillColor;
            this.strokeColor = strokeColor;
            this.strokePx = strokePx;
            this.edges = (edges != null) ? EnumSet.copyOf(edges) : EnumSet.noneOf(BorderEdge.class);
            this.radiusPx = radiusPx;

            fillPaint.setStyle(Paint.Style.FILL);
            fillPaint.setColor(fillColor);

            strokePaint.setAntiAlias(true);
            strokePaint.setColor(strokeColor);
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public void draw(Canvas canvas) {
            rect.set(getBounds());

            // 1) Fill (mit Corner-Radius)
            if ((fillColor >>> 24) != 0) {
                if (radiusPx > 0f) {
                    canvas.drawRoundRect(rect, radiusPx, radiusPx, fillPaint);
                } else {
                    canvas.drawRect(rect, fillPaint);
                }
            }

            // 2) Stroke
            if (strokePx > 0 && (strokeColor >>> 24) != 0 && !edges.isEmpty()) {
                boolean allEdges =
                        edges.contains(BorderEdge.TOP) &&
                                edges.contains(BorderEdge.LEFT) &&
                                edges.contains(BorderEdge.BOTTOM) &&
                                edges.contains(BorderEdge.RIGHT);

                if (allEdges) {
                    // Voller, runder Rahmen: echte Stroke-Zeichnung auf RoundRect
                    strokePaint.setStyle(Paint.Style.STROKE);
                    strokePaint.setStrokeWidth(strokePx);
                    strokePaint.setStrokeJoin(Paint.Join.ROUND);
                    strokePaint.setStrokeCap(Paint.Cap.BUTT);

                    float inset = strokePx / 2f;
                    RectF strokeRect = new RectF(
                            rect.left + inset,
                            rect.top + inset,
                            rect.right - inset,
                            rect.bottom - inset
                    );
                    canvas.drawRoundRect(strokeRect, Math.max(0f, radiusPx - inset), Math.max(0f, radiusPx - inset), strokePaint);
                } else {
                    // Selektive Ränder: per Clip auf runde Außenkontur, dann Kantenstreifen zeichnen
                    strokePaint.setStyle(Paint.Style.FILL);

                    int save = canvas.save();
                    if (radiusPx > 0f) {
                        roundClip.reset();
                        roundClip.addRoundRect(rect, radiusPx, radiusPx, Path.Direction.CW);
                        canvas.clipPath(roundClip);
                    }

                    float l = rect.left;
                    float t = rect.top;
                    float r = rect.right;
                    float b = rect.bottom;

                    if (edges.contains(BorderEdge.TOP)) {
                        canvas.drawRect(l, t, r, Math.min(t + strokePx, b), strokePaint);
                    }
                    if (edges.contains(BorderEdge.BOTTOM)) {
                        canvas.drawRect(l, Math.max(b - strokePx, t), r, b, strokePaint);
                    }
                    if (edges.contains(BorderEdge.LEFT)) {
                        canvas.drawRect(l, t, Math.min(l + strokePx, r), b, strokePaint);
                    }
                    if (edges.contains(BorderEdge.RIGHT)) {
                        canvas.drawRect(Math.max(r - strokePx, l), t, r, b, strokePaint);
                    }

                    canvas.restoreToCount(save);
                }
            }
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public void setAlpha(int alpha) {
            fillPaint.setAlpha(alpha);
            strokePaint.setAlpha(alpha);
            invalidateSelf();
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            fillPaint.setColorFilter(colorFilter);
            strokePaint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }
}



