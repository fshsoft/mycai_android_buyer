package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 收藏
 * Created by ztl on 2015/9/21.
 */
public class CollectionInfo implements Parcelable {

    public int id;
    public String name;
    public String businessHours;
    public String logo;
    public double price;
    public int isCollect;
    public String freight;//配送费
    public String serviceFee;//起送费
    public int type;
    public MapPointInfo mapPoint;
    public String deliveryTime;//配送时段
    public int isDelivery;//是否营业0商家休息1商家营业（显示配送时段）
    public float score;

    public int countGoods;
    public int countService;
    public int orderCount;
    public int salesCount;

    public CollectionInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.businessHours);
        dest.writeString(this.logo);
        dest.writeDouble(this.price);
        dest.writeInt(this.isCollect);
        dest.writeString(this.freight);
        dest.writeString(this.serviceFee);
        dest.writeInt(this.type);
        dest.writeParcelable(this.mapPoint, 0);
        dest.writeString(this.deliveryTime);
        dest.writeInt(this.isDelivery);
        dest.writeFloat(this.score);
        dest.writeInt(this.countGoods);
        dest.writeInt(this.countService);
        dest.writeInt(this.orderCount);
        dest.writeInt(this.salesCount);
    }

    protected CollectionInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.businessHours = in.readString();
        this.logo = in.readString();
        this.price = in.readDouble();
        this.isCollect = in.readInt();
        this.freight = in.readString();
        this.serviceFee = in.readString();
        this.type = in.readInt();
        this.mapPoint = in.readParcelable(MapPointInfo.class.getClassLoader());
        this.deliveryTime = in.readString();
        this.isDelivery = in.readInt();
        this.score = in.readFloat();
        this.countGoods = in.readInt();
        this.countService = in.readInt();
        this.orderCount = in.readInt();
        this.salesCount = in.readInt();
    }

    public static final Creator<CollectionInfo> CREATOR = new Creator<CollectionInfo>() {
        public CollectionInfo createFromParcel(Parcel source) {
            return new CollectionInfo(source);
        }

        public CollectionInfo[] newArray(int size) {
            return new CollectionInfo[size];
        }
    };
}
