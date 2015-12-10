package com.sxzheng.ratechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Line chart of seven days average benefit rates per year.
 */
public class RateChart extends View implements RateHandler {

    private String mTitle;
    private Paint mPaint = new Paint();
    private RateChartManager mChartManager;

    public RateChart(Context context) {
        this(context, null);
    }

    public RateChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RateChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChartManager = new RateChartManager(context);
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        mPaint.setTextSize(12 * fontScale + 0.5f);
        mPaint.setColor(getResources().getColor(android.R.color.black));
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        mChartManager.measure(width, height);
        mChartManager.draw(canvas);

        if (mTitle != null && !mTitle.isEmpty()) {
            float scale = getContext().getResources().getDisplayMetrics().density;
            canvas.drawText(mTitle, 0, 25 * scale + 0.5f, mPaint);
        }
    }

    @Override
    public void update(ArrayList<RateData> rateDatas) {
        mChartManager.update(rateDatas);
        postInvalidate();
    }

    /**
     * Set the title of this chart.
     */
    public void setTitle(String title) {
        mTitle = title;
        postInvalidate();
    }

    @Override
    public void clearData() {
        mChartManager.clearData();
        postInvalidate();
    }

}
