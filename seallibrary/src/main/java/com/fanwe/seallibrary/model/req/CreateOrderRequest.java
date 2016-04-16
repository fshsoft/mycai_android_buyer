package com.fanwe.seallibrary.model.req;

import java.io.Serializable;
import java.util.List;

public class CreateOrderRequest implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public List<Integer> cartIds;
    public int addressId;
    public String giftContent;
    public String invoiceTitle;
    public String buyRemark;
    public String appTime;
    public int payment;
    public String promotionSnId;

}
