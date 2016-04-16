package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable {

    /**
     *
     */
    public int id;// 编号
    public String mobile; // 手机号码
    public String name;// 名称
    public String avatar; // 头像
    public List<UserAddressInfo> address;//地址
    public DistrictAuthInfo propertyUser;

}