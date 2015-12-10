package com.sxzheng.ratechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Rates line.
 * Author zhengsx.
 */
class LinePlot extends Plot {

    Paint mShaderPaint = new Paint();
    private Path mShaderPath;
    private Path mLinePath;
    private float mFontSize;

    public LinePlot(Context context, RateChartManager manager) {
        super(context, manager);
        mShaderPaint.setStyle(Style.FILL);
        mShaderPaint.setColor(context.getResources().getColor(android.R.color.holo_orange_light));
        mShaderPath = new Path();
        mLinePath = new Path();
        mFontSize = context.getResources().getDisplayMetrics().scaledDensity;
    }

    @Override
    public void draw(Canvas canvas) {
        //must be located here
        mPaint.setColor(mContext.getResources().getColor(android.R.color.holo_orange_light));
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(RateChartConst.sLineWidth);
        mPaint.setAntiAlias(true);

        RateChartManager manager = mManager;
        int columns = manager.getColumnNum();

        double[] YValues = manager.getYValues();
        if (YValues == null || YValues.length < columns) return;

        double[] yValues = new double[YValues.length];
        System.arraycopy(YValues, 0, yValues, 0, yValues.length);

        double[] yCoordinates = manager.getYCoordinates();
        double deltaYCoordinateMM = RateDataHelper.getAbsDeltaMaxMin(yCoordinates);
        double maxYCoordinate = RateDataHelper.getMax(yCoordinates);

        //draw y coordinate value.
        float[] drawYValue = new float[YValues.length];

        for (int m = 0; m < yValues.length; m++) {
            drawYValue[m] =
                    (float) (mRegionHeight * (maxYCoordinate - yValues[m]) / deltaYCoordinateMM);
        }

        // line path
        mLinePath.reset();
        mLinePath.moveTo(mMarginLeft, mMarginTop + drawYValue[0]);
        mShaderPath.reset();
        mShaderPath.moveTo(mMarginLeft, mMarginTop + drawYValue[0]);

        float deltaX = mRegionWidth / (columns - 1);

        if (columns < RateChartConst.sColumnNum) {
            deltaX = mRegionWidth / (RateChartConst.sColumnNum - 1);
        }
        for (int i = 1; i < columns; i++) {
            mLinePath.lineTo(mMarginLeft + deltaX * i, drawYValue[i] + mMarginTop);
            mShaderPath.lineTo(mMarginLeft + deltaX * i, drawYValue[i] + mMarginTop);
        }

        //draw shader
        mShaderPath.lineTo(mMarginLeft + deltaX * (columns - 1), mRegionHeight + mMarginTop);
        mShaderPath.lineTo(mMarginLeft, mRegionHeight + mMarginTop);
        mShaderPath.lineTo(mMarginLeft, mMarginTop + drawYValue[0]);
        mShaderPath.setFillType(FillType.EVEN_ODD);
        canvas.drawPath(mShaderPath, mShaderPaint);

        //draw line
        canvas.drawPath(mLinePath, mPaint);

        //draw orange circle
        mPaint.setStrokeWidth(RateChartConst.sCircleWidth);
        canvas.drawCircle(mMarginLeft + deltaX * (columns - 1),
                drawYValue[columns - 1] + mMarginTop, RateChartConst.sCircleOuterRadius, mPaint);

        //draw inner circle.
        mPaint.setColor(mContext.getResources().getColor(android.R.color.white));
        mPaint.setStyle(Style.FILL);
        canvas.drawCircle(mMarginLeft + deltaX * (columns - 1),
                drawYValue[columns - 1] + mMarginTop, RateChartConst.sCircleInnerRadius, mPaint);

        //draw black pop window.
        float mLatestX = mMarginLeft + deltaX * (columns - 1);
        float mLatestY = mMarginTop + drawYValue[columns - 1];
        showPopText(canvas,
                mDecimalFormat.format(manager.getYValues()[manager.getYValues().length - 1]),
                mLatestX, mLatestY);
    }

    //draw black pop window.
    private void showPopText(Canvas canvas, String content, float x, float y) {
        int heightOffSet = 40;
        int margin = 10;
        int rectRadius = 4;

        //draw rounded rectangle
        Rect popupTextRect = new Rect();
        Paint paint = new Paint();
        paint.getTextBounds(content + "  ", 0, content.length() + 2, popupTextRect);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setTextSize(heightOffSet / 2);
        RectF r =
                new RectF(x - popupTextRect.width() - margin, y - (mFontSize * heightOffSet + 0.5f),
                        x + popupTextRect.width() / 6 + (mFontSize * margin + 0.5f),
                        y - (mFontSize * heightOffSet * 2 / 5));
        canvas.drawRoundRect(r, (mFontSize * rectRadius), (mFontSize * rectRadius), paint);

        //draw triangle.
        Path path = new Path();
        path.moveTo(x, y - (mFontSize * heightOffSet * 2 / 5));
        path.lineTo(x, y - (mFontSize * 10));
        path.lineTo(x - (mFontSize * 5), y - (mFontSize * heightOffSet / 2));
        path.close();
        canvas.drawPath(path, paint);

        //draw text
        paint.setColor(Color.WHITE);
        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline =
                (int) (r.top + (r.bottom - r.top - fontMetrics.bottom + fontMetrics.top) / 2 -
                        fontMetrics.top);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(content, r.centerX(), baseline, paint);
    }


}
