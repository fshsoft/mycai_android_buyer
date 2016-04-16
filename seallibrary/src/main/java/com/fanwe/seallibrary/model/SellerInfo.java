package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class SellerInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int id;// 编号
    public String name;// 名称
    public String logo; // 头像
    public int isCollect;
    public List<AdvInfo> banner;
    public String businessHours;
    public String freight;
    public String tel;
    public String address;
    public String detail;
    public PointInfo mapPoint;
    public int orderCount;
    public boolean isChisck = false;
    public int countGoods;
    public int countService;
    public String mobile;

    public double deliveryFee; // 配送费
    public double serviceFee; // 起送费

    public String image; // 商家背景图
    public String deliveryTime;//配送时段
    public int isDelivery;//是否营业0商家休息1商家营业（显示配送时段）

    public float score; //

    public int isCheck; // 0:审核中 1：审核通过 -1：审核拒绝
    public String checkVal;
    public String appurl; //
}