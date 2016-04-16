package com.fanwe.seallibrary.model;

import android.text.TextUtils;

import com.fanwe.seallibrary.model.result.map.GeoCoderInfo;
import com.fanwe.seallibrary.model.result.map.POIAddress;
import com.fanwe.seallibrary.model.result.map.POILocation;

import java.io.Serializable;

/**
* 经纬度实体
* @author haojiahe
* 
* @time 2015-3-23上午10:20:19
* 
*/
public class PointInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6035127890936099998L;
    public double x; //lat
    public double y; //lnt
    public String address;
    public String detailAddress;
    public PointInfo(){}
    public PointInfo(double x, double y){
    	this.x=x;
    	this.y=y;
    }
	@Override
	public String toString() {
		return "" + this.x + "," + this.y;
	}

	// 格式 lat,lng
	public PointInfo(String mapPoint){
		if(TextUtils.isEmpty(mapPoint)){
			x = 0.0;
			y = 0.0;
		}else {
			try {
				String[] map = mapPoint.split(",");
				x = Double.valueOf(map[0]).doubleValue();
				y = Double.valueOf(map[1]).doubleValue();
			}catch (Exception e){

			}
		}
	}
	public PointInfo(POIAddress info){
		address = info.address+info.title;
		final POILocation location = info.location;
		if (null != location) {
			x = location.lat;
			y = location.lng;
		}
	}
	public PointInfo(GeoCoderInfo info){
		address = info.title;
		final POILocation location = info.location;
		if (null != location) {
			x = location.lat;
			y = location.lng;
		}
	}
}
