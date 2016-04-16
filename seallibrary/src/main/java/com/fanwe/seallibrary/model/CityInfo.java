package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

/**
* 经纬度实体
* @author haojiahe
* 
* @time 2015-3-23上午10:20:19
* 
*/
public class CityInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6035127890936099998L;
    public int id; //
    public String name; //
	public List<AreaInfo> area;



}
