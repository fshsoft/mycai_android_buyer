package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class CartGoodsInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int id;
    public int goodsId;
    public int num;
    public String name;// 名称
    public String logo;
    public double price;
    public int normsId; // 规格ID（商品规格）
    public String serviceTime; // 服务时间/配送时间

    public boolean isChecked;

}