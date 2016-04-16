package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class CartSellerInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int id;// 编号
    public String name;// 名称
    public double price;

    public List<CartGoodsInfo> goods;
    public long serviceTime; // 服务时间/配送时间

    public boolean isChecked; // 是否被选择

    public int sellerId;
    public double serviceFee;
    public String goodsName;
    public String goodsDuration;
    public String goodsNorms;
    public String goodsImages;
    public int num;

    public double deliveryFee;
    public int type;

    public int goodsId;

    public int countGoods; // 商品数量
    public int countService; // 服务数量
}