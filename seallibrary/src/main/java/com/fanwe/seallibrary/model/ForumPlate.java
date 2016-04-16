package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-30
 * Time: 09:25
 * 功能:
 */
public class ForumPlate implements Parcelable {
    public int id;
    public String name;
    public String icon;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
    }

    public ForumPlate() {
    }

    protected ForumPlate(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.icon = in.readString();
    }

    public static final Creator<ForumPlate> CREATOR = new Creator<ForumPlate>() {
        public ForumPlate createFromParcel(Parcel source) {
            return new ForumPlate(source);
        }

        public ForumPlate[] newArray(int size) {
            return new ForumPlate[size];
        }
    };
}
