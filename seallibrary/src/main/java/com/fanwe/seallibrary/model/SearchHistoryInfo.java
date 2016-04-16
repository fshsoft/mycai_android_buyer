package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ztl on 2015/9/25.
 */
public class SearchHistoryInfo implements Parcelable {
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public SearchHistoryInfo() {
    }

    protected SearchHistoryInfo(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<SearchHistoryInfo> CREATOR = new Parcelable.Creator<SearchHistoryInfo>() {
        public SearchHistoryInfo createFromParcel(Parcel source) {
            return new SearchHistoryInfo(source);
        }

        public SearchHistoryInfo[] newArray(int size) {
            return new SearchHistoryInfo[size];
        }
    };
}
