package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by atlas on 15/11/14.
 */
public class JoinBusinessInfo implements Parcelable {
    public int id;
    public String address;
    public String name;
    public int type;
    public String logo;
    public String mobile;
    public String idcardSn;
    public String idcardPositiveImg;
    public String idcardNegativeImg;
    public String certificateImg ;
    public String brief;
    public int isCheck;
    public String checkVal;
    public String appurl;
    public List<SellerCatesInfo> cateIds;

    public String addressDetail; // 店铺详细地址 (如门牌号)
    public String serviceRange; // 服务范围
    public String mapPointStr;
    public String mapPosStr;
    public String serviceTel;
    public String contacts;
    public RegionInfo province;
    public RegionInfo city;
    public RegionInfo area;

    public int provinceId;
    public int cityId;
    public int areaId;


    public JoinBusinessInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.address);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeString(this.logo);
        dest.writeString(this.mobile);
        dest.writeString(this.idcardSn);
        dest.writeString(this.idcardPositiveImg);
        dest.writeString(this.idcardNegativeImg);
        dest.writeString(this.certificateImg);
        dest.writeString(this.brief);
        dest.writeInt(this.isCheck);
        dest.writeString(this.checkVal);
        dest.writeString(this.appurl);
        dest.writeTypedList(cateIds);
        dest.writeString(this.addressDetail);
        dest.writeString(this.serviceRange);
        dest.writeString(this.mapPointStr);
        dest.writeString(this.mapPosStr);
        dest.writeString(this.serviceTel);
        dest.writeString(this.contacts);
        dest.writeParcelable(this.province, 0);
        dest.writeParcelable(this.city, 0);
        dest.writeParcelable(this.area, 0);
        dest.writeInt(this.provinceId);
        dest.writeInt(this.cityId);
        dest.writeInt(this.areaId);
    }

    protected JoinBusinessInfo(Parcel in) {
        this.id = in.readInt();
        this.address = in.readString();
        this.name = in.readString();
        this.type = in.readInt();
        this.logo = in.readString();
        this.mobile = in.readString();
        this.idcardSn = in.readString();
        this.idcardPositiveImg = in.readString();
        this.idcardNegativeImg = in.readString();
        this.certificateImg = in.readString();
        this.brief = in.readString();
        this.isCheck = in.readInt();
        this.checkVal = in.readString();
        this.appurl = in.readString();
        this.cateIds = in.createTypedArrayList(SellerCatesInfo.CREATOR);
        this.addressDetail = in.readString();
        this.serviceRange = in.readString();
        this.mapPointStr = in.readString();
        this.mapPosStr = in.readString();
        this.serviceTel = in.readString();
        this.contacts = in.readString();
        this.province = in.readParcelable(RegionInfo.class.getClassLoader());
        this.city = in.readParcelable(RegionInfo.class.getClassLoader());
        this.area = in.readParcelable(RegionInfo.class.getClassLoader());
        this.provinceId = in.readInt();
        this.cityId = in.readInt();
        this.areaId = in.readInt();
    }

    public static final Creator<JoinBusinessInfo> CREATOR = new Creator<JoinBusinessInfo>() {
        public JoinBusinessInfo createFromParcel(Parcel source) {
            return new JoinBusinessInfo(source);
        }

        public JoinBusinessInfo[] newArray(int size) {
            return new JoinBusinessInfo[size];
        }
    };
}
