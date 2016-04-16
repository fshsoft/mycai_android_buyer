package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.fanwe.seallibrary.utils.ArraysUtils;

import java.util.ArrayList;
import java.util.List;


public class SellerCatesInfo implements Parcelable {
    public int id;
    public String name;
    public int type;
    public int sort;
    public String logo;
    public boolean checked;
    public boolean selected;

    public int pid;
    public List<SellerCatesInfo> childs;

    public String showName;


    public SellerCatesInfo() {
    }

    public SellerCatesInfo(String name) {
        this.name = name;
    }

    public SellerCatesInfo clone() {
        SellerCatesInfo item = new SellerCatesInfo();
        item.id = this.id;
        item.name = this.name;
        item.type = this.type;
        item.sort = this.sort;
        item.logo = this.logo;
        item.checked = this.checked;
        item.selected = this.selected;
        item.pid = this.pid;
        item.childs = new ArrayList<>();

        if (!ArraysUtils.isEmpty(childs)) {
            item.childs = new ArrayList<>(this.childs);
        }
        return item;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeInt(this.sort);
        dest.writeString(this.logo);
        dest.writeByte(checked ? (byte) 1 : (byte) 0);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.pid);
        dest.writeTypedList(childs);
        dest.writeString(this.showName);
    }

    protected SellerCatesInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.type = in.readInt();
        this.sort = in.readInt();
        this.logo = in.readString();
        this.checked = in.readByte() != 0;
        this.selected = in.readByte() != 0;
        this.pid = in.readInt();
        this.childs = in.createTypedArrayList(SellerCatesInfo.CREATOR);
        this.showName = in.readString();
    }

    public static final Creator<SellerCatesInfo> CREATOR = new Creator<SellerCatesInfo>() {
        public SellerCatesInfo createFromParcel(Parcel source) {
            return new SellerCatesInfo(source);
        }

        public SellerCatesInfo[] newArray(int size) {
            return new SellerCatesInfo[size];
        }
    };
}
