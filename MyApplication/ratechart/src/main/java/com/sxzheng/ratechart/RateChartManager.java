package com.sxzheng.ratechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import java.util.ArrayList;

/**
 * Manager class.
 */
class RateChartManager implements RateHandler {

    ArrayList<Plot> mPlots = new ArrayList<>();
    //Subclass of Plot.
    private BackgroundPlot mBackgroundPlot;
    private GridPlot mGridPlot;
    private TextHorizonPlot mTextHorizonPlot;
    private TextVerticalPlot mTextVerticalPlot;
    private LinePlot mLinePlot;

    private int mColumnNum = 0;
    //private int mRowNum = 0;

    private double[] mYValues;
    private String[] mXCoordinates = null;
    //private String[] mYCoordinates = null;

    private Paint mPaint = new Paint();

    private float mFontSizeSp;
    private float mFontSizeDp;

    public RateChartManager(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mFontSizeDp = metrics.density;
        mFontSizeSp = metrics.scaledDensity;

        mBackgroundPlot = new BackgroundPlot(context, this);
        mPlots.add(mBackgroundPlot);
        mTextHorizonPlot = new TextHorizonPlot(context, this);
        mPlots.add(mTextHorizonPlot);
        mTextVerticalPlot = new TextVerticalPlot(context, this);
        mPlots.add(mTextVerticalPlot);
        mGridPlot = new GridPlot(context, this);
        mPlots.add(mGridPlot);
        mLinePlot = new LinePlot(context, this);
        mPlots.add(mLinePlot);
    }

    /**
     * Get the column number.
     */
    public int getColumnNum() {
        return mColumnNum;
    }

    /**
     * Get the dates to be drawn on the horizontal coordinate.
     * eg. 05-01,05-02,...
     */
    public String[] getXCoordinates() {
        return mXCoordinates;
    }

    /**
     * Get the Y (Vertical) values to be drawn on the vertical coordinate.
     */
    public double[] getYValues() {
        if (mYValues == null || mYValues.length < 1) return null;
        double[] yValues = new double[mYValues.length];
        for (int i = 0; i < yValues.length; i++) {
            yValues[i] = mYValues[i] * 100;
        }
        return yValues;
    }

    /**
     * @return normalized value.
     */
    public double[] getYValuesNormalization() {

        return RateDataHelper.getYValuesNormalization(mYValues);
    }

    public void measure(int width, int height) {

        float textSize = mFontSizeSp * 10;

        mPaint.setTextSize(textSize);
        Rect rect = new Rect();
        mPaint.getTextBounds("00000", 0, 5, rect);

        int marginLeft = rect.width() + 2;
        int marginTop = (int) (mFontSizeDp * (15 + 50) + 0.5f);
        int marginBottom = (int) (mFontSizeDp * 35 + 0.5f);
        int marginRight = (int) (mFontSizeDp * 15 + 0.5f);
        int titleHeight = (int) (mFontSizeDp * 50 + 0.5f);

        //measure region
        mBackgroundPlot.measure(width, height);

        mTextVerticalPlot.measure(marginLeft, height - marginBottom - marginTop);
        mTextHorizonPlot.measure(width - marginLeft - marginRight, marginBottom);
        mLinePlot.measure(width - marginLeft - marginRight, height - marginTop - marginBottom);
        mGridPlot.measure(width - marginLeft - marginRight, height - marginTop - marginBottom);

        //layout region
        mBackgroundPlot.layout(Position.center, 0, 0, 0, 0, titleHeight);

        mTextHorizonPlot
                .layout(Position.bottom, marginLeft, height - marginLeft, 0, 0, titleHeight);
        mTextVerticalPlot
                .layout(Position.left, 0, marginTop, width - marginLeft, marginBottom, titleHeight);

        mGridPlot.layout(Position.center, marginLeft, marginTop, marginRight, marginBottom,
                titleHeight);
        mLinePlot.layout(Position.center, marginLeft, marginTop, marginRight, marginBottom,
                titleHeight);

    }

    public void draw(Canvas canvas) {
        if (mPlots != null) {
            for (Plot plot : mPlots) {
                plot.draw(canvas);
            }
        }
    }

    /**
     * Update data.
     */
    @Override
    public void update(ArrayList<RateData> rateDatas) {
        //rateDatas = RateChartTest.generateDataArray(1,5);
        if (rateDatas == null || rateDatas.size() < 1) return;
        int size = rateDatas.size();

        mColumnNum = size;

        if (mXCoordinates == null || mXCoordinates.length != size) {
            mXCoordinates = new String[size];
        }

        if (mYValues == null || mYValues.length != size) {
            mYValues = new double[size];
        }

        //Convert date to showing style.
        for (int j = 0; j < size; j++) {
            RateData info = rateDatas.get(j);

            mYValues[j] = info.value;
            String[] split = info.date.split("\\-");
            if (split.length > 2) {
                mXCoordinates[j] = split[1] + "-" + split[2];
            } else {
                mXCoordinates[j] = info.date;
            }
        }
    }

    @Override
    public void clearData() {
        mYValues = null;
    }

    public double[] getYCoordinates() {
        //from top to bottom.
        double[] ys = getYValues();
        int length = 0;
        if (ys == null || ys.length < 1) {
            return null;
        }
        length = ys.length;
        length = RateChartConst.sRowsNum;

        double[] yCoordinates = new double[length];

        double[] yValues = new double[ys.length];

        System.arraycopy(ys, 0, yValues, 0, ys.length);

        double deltaMM = RateDataHelper.getAbsDeltaMaxMin(yValues);

        if(ys.length < 2) {
            deltaMM = yValues[0] * 0.2 ;
        }

        double topYValue = RateDataHelper.getMax(yValues) + (deltaMM / 2);

        double deltaYValue = deltaMM * 2 / (length - 1);
        for (int j = 0; j < length; j++) {
            yCoordinates[j] = topYValue - deltaYValue * j;
        }
        return yCoordinates;
    }
}
