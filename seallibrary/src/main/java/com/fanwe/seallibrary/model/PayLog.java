package com.fanwe.seallibrary.model;

import com.fanwe.seallibrary.model.req.WeixinPayRequest;

import java.io.Serializable;

public class PayLog implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public int id;
    public String sn;
    public double money;
    //weixin|weixinJs|alipay|alipayWap
    public String paymentType;
    public WeixinPayRequest payRequest;
}
