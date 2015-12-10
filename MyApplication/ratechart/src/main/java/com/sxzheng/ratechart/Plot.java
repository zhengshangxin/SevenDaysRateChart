package com.sxzheng.ratechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.text.DecimalFormat;

/**
 * Basic plot class for drawing.
 */
abstract class Plot {

    protected Paint mPaint;
    protected Context mContext;

    protected float mRegionWidth;
    protected float mRegionHeight;
    protected float mMarginLeft;
    protected float mMarginRight;
    protected float mMarginTop;
    protected float mMarginBottom;

    protected float mTitleHeight;

    protected Position mPosition;

    protected RateChartManager mManager;

    protected DecimalFormat mDecimalFormat;

    public Plot(Context context, RateChartManager manager) {
        mDecimalFormat = new DecimalFormat("#");
        mPaint = new Paint();
        mContext = context;
        mManager = manager;
        mPaint.setColor(context.getResources().getColor(android.R.color.background_dark));
    }

    public abstract void draw(Canvas canvas);

    public final void measure(int width, int height) {
        mRegionHeight = height;
        mRegionWidth = width;
    }

    public final void layout(Position position, int marginLeft, int marginTop, int marginRight,
            int marginBottom, int titleHeight) {

        mPosition = position;
        mMarginLeft = marginLeft;
        mMarginTop = marginTop;
        mMarginRight = marginRight;
        mMarginBottom = marginBottom;
        mTitleHeight = titleHeight;
    }
}
