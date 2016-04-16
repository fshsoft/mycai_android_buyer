package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class AdvInfo implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public int id;
    public String name;
    public String image;
    public int type; // 1：商户类型 2：服务类型 3：商品详情 4：商家详情 5：URL
    public String arg; // 1、商户类型ID 2：服务类型ID 3：商品、服务ID 4：商家ID 5:URL地址
}
