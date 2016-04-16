package com.fanwe.seallibrary.model.result.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tu on 15/12/15.
 */
public class POILocation implements Parcelable {
    public double lat;
    public double lng;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }

    public POILocation() {
    }

    protected POILocation(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Parcelable.Creator<POILocation> CREATOR = new Parcelable.Creator<POILocation>() {
        public POILocation createFromParcel(Parcel source) {
            return new POILocation(source);
        }

        public POILocation[] newArray(int size) {
            return new POILocation[size];
        }
    };
}
