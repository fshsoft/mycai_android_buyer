package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ztl on 2015/9/29.
 */
public class SearchHistoryList implements Parcelable {
    public List<SearchHistoryInfo> searchHistoryList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(searchHistoryList);
    }

    public SearchHistoryList() {
    }

    protected SearchHistoryList(Parcel in) {
        this.searchHistoryList = in.createTypedArrayList(SearchHistoryInfo.CREATOR);
    }

    public static final Parcelable.Creator<SearchHistoryList> CREATOR = new Parcelable.Creator<SearchHistoryList>() {
        public SearchHistoryList createFromParcel(Parcel source) {
            return new SearchHistoryList(source);
        }

        public SearchHistoryList[] newArray(int size) {
            return new SearchHistoryList[size];
        }
    };
}
