package com.fanwe.seallibrary.model.result.map;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * geocoder结果实体
 * Created by Tu on 15/12/25.
 */
public class GeoCoderResult implements Parcelable {
    public int status;
    public GeoCoderInfo result;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeParcelable(this.result, flags);
    }

    public GeoCoderResult() {
    }

    protected GeoCoderResult(Parcel in) {
        this.status = in.readInt();
        this.result = in.readParcelable(GeoCoderInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<GeoCoderResult> CREATOR = new Parcelable.Creator<GeoCoderResult>() {
        public GeoCoderResult createFromParcel(Parcel source) {
            return new GeoCoderResult(source);
        }

        public GeoCoderResult[] newArray(int size) {
            return new GeoCoderResult[size];
        }
    };
}
