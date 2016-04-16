package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 优惠券活动实体类
 * Created by ztl on 2016/2/26.
 */
public class CupponActivityInfo implements Parcelable {
    //分享出去的标题
    public String title;
    //分享出去的图片
    public String image;
    //分享出去的内容
    public String detail;
    //单次分享出去的优惠券
    public String sharePromotionNum;

    public String linkUrl;

    public CupponActivityInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.detail);
        dest.writeString(this.sharePromotionNum);
        dest.writeString(this.linkUrl);
    }

    protected CupponActivityInfo(Parcel in) {
        this.title = in.readString();
        this.image = in.readString();
        this.detail = in.readString();
        this.sharePromotionNum = in.readString();
        this.linkUrl = in.readString();
    }

    public static final Creator<CupponActivityInfo> CREATOR = new Creator<CupponActivityInfo>() {
        public CupponActivityInfo createFromParcel(Parcel source) {
            return new CupponActivityInfo(source);
        }

        public CupponActivityInfo[] newArray(int size) {
            return new CupponActivityInfo[size];
        }
    };
}
