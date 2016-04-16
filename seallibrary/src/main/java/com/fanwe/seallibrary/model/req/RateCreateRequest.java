package com.fanwe.seallibrary.model.req;

import java.io.Serializable;
import java.util.List;

public class RateCreateRequest implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public int orderId;
    public String content;
    public int star;
    public List<String> images;
    public int isAno;
}
