package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class ServiceInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int id;// 编号
    public String name;// 名称
    public double price;
    public int isCollect;
    public int duration; // 时长（分钟）
    public String logo;

}