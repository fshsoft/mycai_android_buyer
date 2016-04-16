package com.fanwe.seallibrary.model;

import java.io.Serializable;

/**
 * 用户地址信息实体
 * Created by ztl on 2015/9/21.
 */
public class UserAddressInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String address;
    public String name;
    public String mobile;
    public boolean isDefault;
    public ProvinceInfo province;
    public CityInfo city;
    public AreaInfo area;
    //收货地址坐标
    public PointInfo mapPoint;
    //详细地址
    public String detailAddress;
    //门牌号
    public String doorplate;
}
