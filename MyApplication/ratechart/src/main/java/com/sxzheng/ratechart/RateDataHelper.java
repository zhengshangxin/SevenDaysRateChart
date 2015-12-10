package com.sxzheng.ratechart;

/**
 * Rate data calculate helper class.
 * Only be used in RateChartManager.
 */
class RateDataHelper {

    /**
     * Normalize the y values.
     *
     * @return normalized y values.
     */
    public static double[] getYValuesNormalization(double[] src) {
        if (src == null) {
            return null;
        }

        double max = getMax(src);
        double min = getMin(src);
        double deltaMM = max - min;

        double[] result = new double[src.length];

        for (int i = 0; i < src.length; i++) {
            result[i] = (src[i] - min) / deltaMM;
        }
        return result;
    }

    /**
     * Normalize the y values.
     *
     * @return normalized y values.
     */
    public static double getAbsDeltaMaxMin(double[] src) {
        if (src == null) {
            throw new NullPointerException("double array is null");
        }

        double max = getMax(src);
        double min = getMin(src);


        return  max - min;
    }

    public static double getMax(double[] doubles) {

        if (doubles == null) {
            throw new IllegalArgumentException("Argument is null!");
        }

        double largest;
        int size = doubles.length;
        if (size > 1) {
            largest = doubles[0];

            for (int i = 1; i < size; i++) {
                largest = Math.max(largest, doubles[i]);
            }
        } else {
            return doubles[0];
        }
        return largest;
    }

    public static double getMin(double[] doubles) {
        if (doubles == null) {
            throw new IllegalArgumentException("Argument is null!");
        }

        double lowest;
        int size = doubles.length;
        if (size > 1) {
            lowest = doubles[0];

            for (int i = 1; i < size; i++) {
                lowest = Math.min(lowest, doubles[i]);
            }
        } else {
            return doubles[0];
        }
        return lowest;
    }
}
