package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

/**
 * 机构员工信息实体
 * @author atlas
 * @email atlas.tufei@gmail.com
 * @time 2015-5-13 上午11:12:14
 */
public class StaffInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8114529873373495685L;

	public int id;
	public String name;
	public String mobile;
	public List<String> photos;
	public String avatar;
	public String sexStr;
	public int age;
	public int sex;
	public String logo;
	public String banner;
	public String brief;
	public String address;
//	public PointInfo mapPoint;
	public String mapPointStr;
	public String mapPosStr;
	public int status;
//	public RegionInfo province;
//	public RegionInfo city;
//	public RegionInfo area;
	public int sort;
//	public SellerInfo seller;
//	public UserCollectStaff collect;
	public int birthday;
	public String authentication;
	public String recruitment;
	public String hobbies;
	public String constellation;
//	public StaffExtend extend;
	public int sellerType;
	public double totalMoney;
	public double withdrawMoney;
	public double frozenMoney;



}
