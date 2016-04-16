package com.fanwe.seallibrary.model.req;

import java.io.Serializable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-28
 * Time: 14:30
 * FIXME
 */
public class WeixinPayRequest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //商户号
    public String partnerid;
    //预支付交易会话标识
    public String prepayid;
    //扩展字段
    public String packages;
    //随机字符串
    public String noncestr;
    //时间戳
    public String timestamp;
    //签名
    public String sign;

    // 银联支付
    public String tn;
}
