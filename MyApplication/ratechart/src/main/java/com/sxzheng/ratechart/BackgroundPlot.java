package com.sxzheng.ratechart;

import android.content.Context;
import android.graphics.Canvas;


class BackgroundPlot extends Plot {

    int color;
    public BackgroundPlot(Context context,RateChartManager manager) {
        super(context,manager );
        color = mContext.getResources().getColor(android.R.color.transparent);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        canvas.drawColor(color);
        canvas.restore();
    }
}
