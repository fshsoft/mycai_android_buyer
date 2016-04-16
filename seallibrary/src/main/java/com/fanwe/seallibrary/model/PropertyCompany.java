package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-20
 * Time: 11:34
 * 功能: 物业
 */
public class PropertyCompany implements Parcelable{
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


    public String contacts; // 法人(商家),真实姓名(个人)
    public String serviceTel; // 服务电话
    public String businessLicenceImg; // 营业执照

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.logo);
        dest.writeInt(this.isCollect);
        dest.writeList(this.banner);
        dest.writeString(this.businessHours);
        dest.writeString(this.freight);
        dest.writeString(this.tel);
        dest.writeString(this.address);
        dest.writeString(this.detail);
        dest.writeSerializable(this.mapPoint);
        dest.writeInt(this.orderCount);
        dest.writeByte(isChisck ? (byte) 1 : (byte) 0);
        dest.writeInt(this.countGoods);
        dest.writeInt(this.countService);
        dest.writeString(this.mobile);
        dest.writeDouble(this.deliveryFee);
        dest.writeDouble(this.serviceFee);
        dest.writeString(this.image);
        dest.writeString(this.deliveryTime);
        dest.writeInt(this.isDelivery);
        dest.writeFloat(this.score);
        dest.writeInt(this.isCheck);
        dest.writeString(this.checkVal);
        dest.writeString(this.appurl);
        dest.writeString(this.contacts);
        dest.writeString(this.serviceTel);
        dest.writeString(this.businessLicenceImg);
    }

    public PropertyCompany() {
    }

    protected PropertyCompany(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.logo = in.readString();
        this.isCollect = in.readInt();
        this.banner = new ArrayList<AdvInfo>();
        in.readList(this.banner, List.class.getClassLoader());
        this.businessHours = in.readString();
        this.freight = in.readString();
        this.tel = in.readString();
        this.address = in.readString();
        this.detail = in.readString();
        this.mapPoint = (PointInfo) in.readSerializable();
        this.orderCount = in.readInt();
        this.isChisck = in.readByte() != 0;
        this.countGoods = in.readInt();
        this.countService = in.readInt();
        this.mobile = in.readString();
        this.deliveryFee = in.readDouble();
        this.serviceFee = in.readDouble();
        this.image = in.readString();
        this.deliveryTime = in.readString();
        this.isDelivery = in.readInt();
        this.score = in.readFloat();
        this.isCheck = in.readInt();
        this.checkVal = in.readString();
        this.appurl = in.readString();
        this.contacts = in.readString();
        this.serviceTel = in.readString();
        this.businessLicenceImg = in.readString();
    }

    public static final Creator<PropertyCompany> CREATOR = new Creator<PropertyCompany>() {
        public PropertyCompany createFromParcel(Parcel source) {
            return new PropertyCompany(source);
        }

        public PropertyCompany[] newArray(int size) {
            return new PropertyCompany[size];
        }
    };
}
