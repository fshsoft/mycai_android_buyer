package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
*
* @time 2015-3-23上午10:20:19
* 
*/
public class BuildingInfo implements Parcelable{

	/**
	 *
	 */
	public int id; //
	public String name; //
	public int sellerId;
	public int districtId;
	public String remark;


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeInt(this.sellerId);
		dest.writeInt(this.districtId);
		dest.writeString(this.remark);
	}

	public BuildingInfo() {
	}

	protected BuildingInfo(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.sellerId = in.readInt();
		this.districtId = in.readInt();
		this.remark = in.readString();
	}

	public static final Creator<BuildingInfo> CREATOR = new Creator<BuildingInfo>() {
		public BuildingInfo createFromParcel(Parcel source) {
			return new BuildingInfo(source);
		}

		public BuildingInfo[] newArray(int size) {
			return new BuildingInfo[size];
		}
	};
}
