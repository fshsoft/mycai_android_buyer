package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-13
 * Time: 14:14
 * 功能: 优惠券
 */
public class Promotion implements Parcelable{
    public int id; // 优惠券SN表编号
    public String sn; // 优惠券SN码
    public boolean status; // 是否已失效 0:否 1:是
    public String expireTimeStr; // 过期时间,直接显示(如:2016-01-01或 永久有效)
    public String name; // 优惠券名称
    public String brief; // 优惠群描述
    public String money; // 金额
    public String type; // 优惠券类型: offset :抵用券 money: 优惠券
    public double limitMoney;//满金额可使用


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.sn);
        dest.writeByte(status ? (byte) 1 : (byte) 0);
        dest.writeString(this.expireTimeStr);
        dest.writeString(this.name);
        dest.writeString(this.brief);
        dest.writeString(this.money);
        dest.writeString(this.type);
        dest.writeDouble(this.limitMoney);
    }

    public Promotion() {
    }

    protected Promotion(Parcel in) {
        this.id = in.readInt();
        this.sn = in.readString();
        this.status = in.readByte() != 0;
        this.expireTimeStr = in.readString();
        this.name = in.readString();
        this.brief = in.readString();
        this.money = in.readString();
        this.type = in.readString();
        this.limitMoney = in.readDouble();
    }

    public static final Creator<Promotion> CREATOR = new Creator<Promotion>() {
        public Promotion createFromParcel(Parcel source) {
            return new Promotion(source);
        }

        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };
}
