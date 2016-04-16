package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-20
 * Time: 11:39
 * 功能:
 */
public class Repair implements Parcelable{
    public int id; // 编号
    public String content; // 内容
    public String repairType; // 报修类型
    public List<String> images; // 图片数组
    public String statusStr; // 状态
    public String createTime; // 报修时间
    public DistrictAuthInfo puser; // 业主对象
    public BuildingInfo build; // 楼栋对象
    public DistrictInfo district; // 小区编号
    public RoomInfo room; // 楼栋号
    public int districtId; // 小区编号

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.content);
        dest.writeString(this.repairType);
        dest.writeStringList(this.images);
        dest.writeString(this.statusStr);
        dest.writeString(this.createTime);
        dest.writeParcelable(this.puser, 0);
        dest.writeParcelable(this.build, 0);
        dest.writeParcelable(this.district, 0);
        dest.writeParcelable(this.room, 0);
        dest.writeInt(this.districtId);
    }

    public Repair() {
    }

    protected Repair(Parcel in) {
        this.id = in.readInt();
        this.content = in.readString();
        this.repairType = in.readString();
        this.images = in.createStringArrayList();
        this.statusStr = in.readString();
        this.createTime = in.readString();
        this.puser = in.readParcelable(DistrictAuthInfo.class.getClassLoader());
        this.build = in.readParcelable(BuildingInfo.class.getClassLoader());
        this.district = in.readParcelable(DistrictInfo.class.getClassLoader());
        this.room = in.readParcelable(RoomInfo.class.getClassLoader());
        this.districtId = in.readInt();
    }

    public static final Creator<Repair> CREATOR = new Creator<Repair>() {
        public Repair createFromParcel(Parcel source) {
            return new Repair(source);
        }

        public Repair[] newArray(int size) {
            return new Repair[size];
        }
    };
}
