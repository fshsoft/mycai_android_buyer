package com.fanwe.seallibrary.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 定位地址实体
 */
public class LocAddressInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public String address;
    public String city;
    public String province;
    //收货地址坐标
    public PointInfo mapPoint;
    //门牌号
    public String streetNo;
    public String street;

//    public LocAddressInfo(TencentLocation location){
//        this.address = location.getAddress();
//        this.city = location.getCity();
//        this.province = location.getProvince();
//        this.streetNo = location.getStreetNo();
//        this.street = location.getStreet();
//        this.mapPoint = new PointInfo(location.getLatitude() , location.getLongitude());
//    }

    public LocAddressInfo() {

    }

    public LocAddressInfo(UserAddressInfo userAddressInfo) {
        this.address=userAddressInfo.detailAddress ;
        this.mapPoint=userAddressInfo.mapPoint ;
    }

    public UserAddressInfo toUserAddr() {
        UserAddressInfo userAddressInfo = new UserAddressInfo();
        userAddressInfo.address = this.address;
        userAddressInfo.doorplate = this.streetNo;
        userAddressInfo.detailAddress = this.address;
        userAddressInfo.name = this.streetNo;
        userAddressInfo.mapPoint = this.mapPoint;
        userAddressInfo.detailAddress = "";

        if(!TextUtils.isEmpty(this.province)){
            userAddressInfo.detailAddress += this.province;
        }
        if(!TextUtils.isEmpty(this.city)){
            userAddressInfo.detailAddress += this.city;
        }
        if(!TextUtils.isEmpty(this.street)){
            userAddressInfo.detailAddress += this.street;
        }
        if(!TextUtils.isEmpty(this.streetNo)){
            userAddressInfo.detailAddress += this.streetNo;
        }

        if (TextUtils.isEmpty(this.streetNo)) {
            if (!TextUtils.isEmpty(this.city)) {
                this.streetNo = this.city;
            }
        }
        return userAddressInfo;
    }

    public boolean isUsefulAddr() {
        if (TextUtils.isEmpty(this.address)) {
            return false;
        }
        return true;
    }
}
