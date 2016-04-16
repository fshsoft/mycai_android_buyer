package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

/**
 * 初始化配置信息
 */
public class InitInfo implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public List<PaymentInfo> payment;
    public String appVersion;
    public boolean forceUpgrade;
    public String appDownUrl;
    public String upgradeInfo;
    public String serviceTel;
    public String serviceTime;
    public String aboutUrl;
    public String protocolUrl;
    public String helpUrl;
    public String fileUploadType;
    public OSSInfo fileUploadConfig;
    public int systemOrderPass;
//    public List<ProvinceInfo> province;
    public String introUrl; // 优惠券使用说明url
    public String shareContent; // 邀请好友分享内容
    public boolean isOpenProperty;
}