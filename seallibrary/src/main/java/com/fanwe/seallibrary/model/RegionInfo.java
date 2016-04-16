package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by atlas on 15/11/14.
 */
public class RegionInfo  implements Parcelable{
    public int id;
    public int pid;
    public String name;
    public int level;
    public int sort;
    public boolean isDefault;
    public String firstChar;

    public RegionLocal toLocal(){
        RegionLocal local = new RegionLocal();
        local.i = String.valueOf(this.id);
        local.n = this.name;
        return local;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.pid);
        dest.writeString(this.name);
        dest.writeInt(this.level);
        dest.writeInt(this.sort);
        dest.writeByte(isDefault ? (byte) 1 : (byte) 0);
        dest.writeString(this.firstChar);
    }

    public RegionInfo() {
    }

    protected RegionInfo(Parcel in) {
        this.id = in.readInt();
        this.pid = in.readInt();
        this.name = in.readString();
        this.level = in.readInt();
        this.sort = in.readInt();
        this.isDefault = in.readByte() != 0;
        this.firstChar = in.readString();
    }

    public static final Creator<RegionInfo> CREATOR = new Creator<RegionInfo>() {
        public RegionInfo createFromParcel(Parcel source) {
            return new RegionInfo(source);
        }

        public RegionInfo[] newArray(int size) {
            return new RegionInfo[size];
        }
    };
}
