package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class GoodsPackInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int id;// 编号
    public String name;// 名称
    public List<GoodInfo> goods;
    public boolean selected;
    public int firstIndex;

}