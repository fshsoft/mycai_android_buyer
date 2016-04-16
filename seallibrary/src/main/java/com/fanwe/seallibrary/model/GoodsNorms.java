package com.fanwe.seallibrary.model;

import java.io.Serializable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-22
 * Time: 12:01
 * FIXME
 */
public class GoodsNorms implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;// 编号
    public int goodsId;
    public int sellerId;
    public String name;// 名称
    public double price;
    public int stock;
    public boolean selected;
}
