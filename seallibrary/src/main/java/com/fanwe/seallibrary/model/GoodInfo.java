package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class GoodInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int id;// 编号
    public String name;// 名称
    public double price;
    public List<GoodsNorms> norms;
    public int stock;
    public int type;
    public int unit;
    public int duration;
    public List<String> images;
    public String brief;
    public int buyLimit;
    public int status;
    public int iscollect;

    public String logo;
    public String url;
    public SellerInfo seller;

    public int chgCount;
//    public int chgNormsId;

    public int salesCount;

    public boolean isClicked; // 临时变量，表示是否点击过
}