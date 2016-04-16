package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class OrderInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int id;// 编号
    public String mobile; // 手机号码
    public String name;// 名称
    public String avatar; // 头像
    public String sn;
    public List<CartSellerInfo> cartSellers;
    public String address;
    public String giftContent;
    public String invoiceTitle;
    public String buyRemark;
    public String payType;
    public String freType;
    public String freTime;
    public double freight;
    public double totalFee;
    public int count;
    public String refuseReason;
    public int orderType;
    public int status;
    public String orderStatusStr;
    public String createTime;
    public boolean isCanDelete;
    public boolean isCanRate;
    public boolean isCanCancel;
    public boolean isCanPay;
    public boolean isCanConfirm;
    public boolean isCanRefund;
    public String refundContent;

    public String serviceName;
    public SellerInfo seller;

    // public UserAddressInfo address;//会员默认地址
//	public RegionInfo province;
//	public RegionInfo city;
//	public RegionInfo area;
    public String refundTime;
    public StaffInfo staff;
    public String cancelRemark;
    //状态流程图片url地址
    public String statusFlowImage;
    //是否可以催单
    public boolean isCanReminder;

    public String appTime; // 预约时间
    public String shopName;
    public int sellerId; // 商家编号
    public String sellerName; // 商家名称
    public String sellerTel; // 商家联系电话
    public String staffName; // 配送/服务人员名称
    public String staffMobile; // 配送/服务人员联系电话
    public List<String> goodsImages; //

    public double goodsFee; //商品金额
    public double discountFee; //  优惠金额
    public double payFee; //支付金额

    public double sellerFee; // 商家所得金额
    public double drawnFee; // 平台提成金额

    public int refundCount;//订单退款日志数,大于0时表示有退款

    public boolean isContactCancel;//是否为联系商家取消订单

    public int promotionIsShow; // 优惠信息显示了没有 0没显示 1显示了

    public double payMoney;

    public OrderInfo() {
    }


}