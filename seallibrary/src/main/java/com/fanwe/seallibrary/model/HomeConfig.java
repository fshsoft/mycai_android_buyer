package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ztl on 2015/9/18.
 */
public class HomeConfig implements Serializable{
    public List<AdvInfo> banner;
    public List<AdvInfo> menu;
    public List<AdvInfo> notice;
    public List<GoodInfo> goods;
    public List<SellerInfo> sellers;
}
