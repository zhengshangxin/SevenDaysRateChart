package com.sxzheng.ratechart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RateChartTest {

    public static ArrayList<RateData> generateDataArray(int length, double seed) {
        if(length <= 0) throw new IllegalArgumentException("length is no less than one");
        Random random = new Random();
        Date date = new Date();
        long curTimeL = date.getTime();

        long dayTimeL = 1000 * 60 * 60 * 24;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);


        ArrayList<RateData> rateDatas = new ArrayList<RateData>(length);
        for (int j = 0; j < length; j++) {

            RateData info = new RateData();
            random.nextInt(1);
            double randomD = random.nextFloat();
            info.value = seed + j * randomD;
            info.date = dateFormat.format(new Date(curTimeL - dayTimeL * (length - j)));
            rateDatas.add(info);
        }
        return rateDatas;
    }

    public static void main() {

    }

}
