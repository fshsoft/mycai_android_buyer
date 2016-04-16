package com.fanwe.seallibrary.model.result.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tu on 15/12/25.
 */
public class GeoCoderInfo implements Parcelable {
    public POILocation location;
    public String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.title);
    }

    public GeoCoderInfo() {
    }

    protected GeoCoderInfo(Parcel in) {
        this.location = in.readParcelable(POILocation.class.getClassLoader());
        this.title = in.readString();
    }

    public static final Parcelable.Creator<GeoCoderInfo> CREATOR = new Parcelable.Creator<GeoCoderInfo>() {
        public GeoCoderInfo createFromParcel(Parcel source) {
            return new GeoCoderInfo(source);
        }

        public GeoCoderInfo[] newArray(int size) {
            return new GeoCoderInfo[size];
        }
    };
}
