package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class OrderListPack implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public int commentNum;
    public List<OrderInfo> orderList;
}
