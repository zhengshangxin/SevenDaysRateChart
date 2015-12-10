package com.sxzheng.ratechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Draw the vertical coordinate.
 */
class TextVerticalPlot extends Plot {
    public TextVerticalPlot(Context context,RateChartManager manager) {
        super(context, manager);
        float fontSize = context.getResources().getDisplayMetrics().scaledDensity;
        mPaint.setTextSize(RateChartConst.sCoordinatesTextWidth * fontSize);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        RateChartManager manager = mManager;

        double[] yCoordinates = manager.getYCoordinates();
        if(yCoordinates == null || yCoordinates.length < 2) return;
        String[] drawYCoordinates = new String[yCoordinates.length];

        for(int j = 0; j < yCoordinates.length;j++) {
            drawYCoordinates[j] = mDecimalFormat.format(yCoordinates[j]);
        }

        int rows = yCoordinates.length;

        float deltaY = mRegionHeight / (rows - 1);
        Rect rect = new Rect();
        mPaint.getTextBounds(drawYCoordinates[0], 0, drawYCoordinates[0].length(), rect);

        int halfTextHeight = rect.height() >> 1;

        for (int i = 0; i < rows; i++) {
            canvas.drawText(drawYCoordinates[i], 0,
                    mMarginTop + deltaY * i + halfTextHeight, mPaint);
        }

        canvas.restore();
    }
}
