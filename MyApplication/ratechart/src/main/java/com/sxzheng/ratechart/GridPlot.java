package com.sxzheng.ratechart;

import android.content.Context;
import android.graphics.Canvas;

class GridPlot extends Plot {

    public GridPlot(Context context, RateChartManager manager) {
        super(context, manager);

        int color = context.getResources().getColor(android.R.color.background_dark);
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        RateChartManager manager = mManager;
        double[] ys = manager.getYCoordinates();
        int columns;
        if (ys == null || ys.length < RateChartConst.sColumnNum) {
            columns = RateChartConst.sColumnNum;
        } else {
            columns = ys.length;
        }

        int rows = RateChartConst.sRowsNum;

        //draw vertical line
        float deltaX = mRegionWidth / (columns - 1);
        for (int i = 0; i <= (columns - 1); i++) {
            canvas.drawLine(mMarginLeft + i * deltaX, mTitleHeight, mMarginLeft + i * deltaX,
                    mRegionHeight + mMarginTop, mPaint);
        }

        //draw horizontal line
        float deltaY = mRegionHeight / (rows - 1);
        for (int j = 0; j <= (rows - 1); j++) {
            canvas.drawLine(mMarginLeft, j * deltaY + mMarginTop,
                    mRegionWidth + mMarginLeft + mMarginRight, j * deltaY + mMarginTop, mPaint);
        }

        canvas.restore();
    }

}
