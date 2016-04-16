package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/11/11.
 */
public class MapPointInfo implements Parcelable {
    public String x;
    public String y;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.x);
        dest.writeString(this.y);
    }

    public MapPointInfo() {
    }

    protected MapPointInfo(Parcel in) {
        this.x = in.readString();
        this.y = in.readString();
    }

    public static final Parcelable.Creator<MapPointInfo> CREATOR = new Parcelable.Creator<MapPointInfo>() {
        public MapPointInfo createFromParcel(Parcel source) {
            return new MapPointInfo(source);
        }

        public MapPointInfo[] newArray(int size) {
            return new MapPointInfo[size];
        }
    };
}
