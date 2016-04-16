package com.fanwe.seallibrary.model;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-06
 * Time: 11:08
 * 功能:
 */
public class OrderCompute {
    public double goodsFee; //商品金额
    public double totalFee; //  总金额
    public double discountFee;//优惠金额
    public double freight;//  配送费
    public double payFee;//支付金额
    public int isCashOnDelivery;//是否支持货到付款 0：不支持 1：支持
    public int isShowPromotion; // 是否显示优惠券 0:否 1:是
    public int promotionCount; // 可选的优惠券数量 用于判断显示有无可选优惠券
    public int sellerId; // 商家编号
    public double totalMoney; // 总金额(商品金额+配送费),不算优惠券金额

}
