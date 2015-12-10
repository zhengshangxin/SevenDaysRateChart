package com.sxzheng.ratechart;

import java.util.ArrayList;

interface RateHandler {
    void update(ArrayList<RateData> rateDatas);
    void clearData();
}
