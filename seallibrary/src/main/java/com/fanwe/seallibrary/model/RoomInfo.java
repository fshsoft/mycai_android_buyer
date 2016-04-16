package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * 经纬度实体
 *
 * @author haojiahe
 * @time 2015-3-23上午10:20:19
 */
public class RoomInfo implements Parcelable {
    public int id; // 编号
    public String roomNum; // 房间号
    public int sellerId; //     物业编号
    public int districtId; // 小区编号
    public String owner; // 业主
    public String mobile; // 电话
    public String remark; // 备注
    public int buildId; // 楼栋号
    public double propertyFee; // 物业费
    public String roomArea; //    套内面积
    public String structureArea; // 建筑面积
    public String intakeTime; //     入住时间


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.roomNum);
        dest.writeInt(this.sellerId);
        dest.writeInt(this.districtId);
        dest.writeString(this.owner);
        dest.writeString(this.mobile);
        dest.writeString(this.remark);
        dest.writeInt(this.buildId);
        dest.writeDouble(this.propertyFee);
        dest.writeString(this.roomArea);
        dest.writeString(this.structureArea);
        dest.writeString(this.intakeTime);
    }

    public RoomInfo() {
    }

    protected RoomInfo(Parcel in) {
        this.id = in.readInt();
        this.roomNum = in.readString();
        this.sellerId = in.readInt();
        this.districtId = in.readInt();
        this.owner = in.readString();
        this.mobile = in.readString();
        this.remark = in.readString();
        this.buildId = in.readInt();
        this.propertyFee = in.readDouble();
        this.roomArea = in.readString();
        this.structureArea = in.readString();
        this.intakeTime = in.readString();
    }

    public static final Creator<RoomInfo> CREATOR = new Creator<RoomInfo>() {
        public RoomInfo createFromParcel(Parcel source) {
            return new RoomInfo(source);
        }

        public RoomInfo[] newArray(int size) {
            return new RoomInfo[size];
        }
    };
}
