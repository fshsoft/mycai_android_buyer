package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DistrictAuthInfo implements Parcelable{
    public int id; // 编号
    public BuildingInfo build; // 楼栋对象
    public DistrictInfo district; // 小区编号
    public RoomInfo room; // 楼栋号
    public String name; // 业主
    public String mobile; //电话
    public int status; // 身份认证状态0待审核1通过-1拒绝
    public int accessStatus; // 门禁申请状态默认是 0 不能进入开锁界面 1  申请成功，可以进入开锁界面
    public int isProperty; // 是否开通物业1、需要开通
    public int isVerify; // 是否申请验证1、需要申请
    public int isCheck; // 是否通过验证1、需要验证

    public DistrictAuthInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeParcelable(this.build, 0);
        dest.writeParcelable(this.district, 0);
        dest.writeParcelable(this.room, 0);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeInt(this.status);
        dest.writeInt(this.accessStatus);
        dest.writeInt(this.isProperty);
        dest.writeInt(this.isVerify);
        dest.writeInt(this.isCheck);
    }

    protected DistrictAuthInfo(Parcel in) {
        this.id = in.readInt();
        this.build = in.readParcelable(BuildingInfo.class.getClassLoader());
        this.district = in.readParcelable(DistrictInfo.class.getClassLoader());
        this.room = in.readParcelable(RoomInfo.class.getClassLoader());
        this.name = in.readString();
        this.mobile = in.readString();
        this.status = in.readInt();
        this.accessStatus = in.readInt();
        this.isProperty = in.readInt();
        this.isVerify = in.readInt();
        this.isCheck = in.readInt();
    }

    public static final Creator<DistrictAuthInfo> CREATOR = new Creator<DistrictAuthInfo>() {
        public DistrictAuthInfo createFromParcel(Parcel source) {
            return new DistrictAuthInfo(source);
        }

        public DistrictAuthInfo[] newArray(int size) {
            return new DistrictAuthInfo[size];
        }
    };
}
