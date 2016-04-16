package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class SellerRateStatistics implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public double star;
    public int totalCount;
    public int goodCount;
    public int neutralCount;
    public int badCount;
}
