package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class SellerRateInfo implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public int id;
    public String userName;
    public String avatar;
    public String content;
    public String reply;
    public String replyTime;
    public String createTime;
    public int star;
    public int orderId;
    public List<String> images;

}
