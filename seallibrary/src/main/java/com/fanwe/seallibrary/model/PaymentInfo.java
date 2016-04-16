package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class PaymentInfo implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public String code; // 支付代码
    public String name;
    public String icon;
    public int isDefault; // 1：是
    public String content; // 支付说明
    public UnionAppConfig unionappConfig;

    public boolean bSel;

    public static class UnionAppConfig{
        public String merId;
    }
}
