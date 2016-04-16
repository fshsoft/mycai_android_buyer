package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DistrictInfo implements Parcelable {

    public int id;
    public String name;
    public String address;
    public int provinceId;
    public int cityId;
    public int areaId;
    public String mapPointStr;

    public String houseTypeName; // 房产类型
    public int houseNum; // 户数
    public int areaNum; // 面积
    public int isEnter; // 是否开通物业0否1是
    public int countDistrict; // 小区数量（大于1时显示切换按钮）
    public String location; // 地区
    public String sellerName; // 物业公司名称
    public PropertyCompany seller; // 物业公司对象
    public int sellerId; // 物业编号 0 表示未入驻 1 表示入驻
    public int isUser; // 会员是否加入 0 未加入 1 加入

    public DistrictInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeInt(this.provinceId);
        dest.writeInt(this.cityId);
        dest.writeInt(this.areaId);
        dest.writeString(this.mapPointStr);
        dest.writeString(this.houseTypeName);
        dest.writeInt(this.houseNum);
        dest.writeInt(this.areaNum);
        dest.writeInt(this.isEnter);
        dest.writeInt(this.countDistrict);
        dest.writeString(this.location);
        dest.writeString(this.sellerName);
        dest.writeParcelable(this.seller, 0);
        dest.writeInt(this.sellerId);
        dest.writeInt(this.isUser);
    }

    protected DistrictInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.address = in.readString();
        this.provinceId = in.readInt();
        this.cityId = in.readInt();
        this.areaId = in.readInt();
        this.mapPointStr = in.readString();
        this.houseTypeName = in.readString();
        this.houseNum = in.readInt();
        this.areaNum = in.readInt();
        this.isEnter = in.readInt();
        this.countDistrict = in.readInt();
        this.location = in.readString();
        this.sellerName = in.readString();
        this.seller = in.readParcelable(PropertyCompany.class.getClassLoader());
        this.sellerId = in.readInt();
        this.isUser = in.readInt();
    }

    public static final Creator<DistrictInfo> CREATOR = new Creator<DistrictInfo>() {
        public DistrictInfo createFromParcel(Parcel source) {
            return new DistrictInfo(source);
        }

        public DistrictInfo[] newArray(int size) {
            return new DistrictInfo[size];
        }
    };
}
