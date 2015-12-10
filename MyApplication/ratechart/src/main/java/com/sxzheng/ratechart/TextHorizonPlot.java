package com.sxzheng.ratechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;


/**
 * Draw the horizontal coordinate.
 */
class TextHorizonPlot extends Plot {
    public TextHorizonPlot(Context context,RateChartManager manager) {
        super(context,manager );
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        mPaint.setTextSize(RateChartConst.sCoordinatesTextWidth * fontScale);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        RateChartManager manager = mManager;
        int columns = manager.getColumnNum();

        String[] xCoordinates = manager.getXCoordinates();

        if (xCoordinates == null || !(xCoordinates.length == columns)) {
            return;
        }

        int drawColumns = columns;

        if(drawColumns < RateChartConst.sColumnNum) {
            drawColumns = RateChartConst.sColumnNum;
        }

        float deltaX = mRegionWidth / (drawColumns - 1);

        Rect rect = new Rect();

        mPaint.getTextBounds(xCoordinates[0], 0, xCoordinates[0].length(), rect);
        int halfTextWidth = rect.width() >> 1;

        for (int i = 0; i < columns; i++) {

            canvas.drawText(xCoordinates[i], mMarginLeft + deltaX * i - halfTextWidth,
                    mMarginTop + rect.height() + 5, mPaint);
        }

        canvas.restore();
    }
}
