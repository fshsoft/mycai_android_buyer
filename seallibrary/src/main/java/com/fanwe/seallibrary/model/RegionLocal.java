package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地地区实体
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-24
 * Time: 03:46
 */
public class RegionLocal implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public String i;
    public String n;
    public List<RegionLocal> child;
    public List<RegionLocal> group;

    public RegionLocal(String i, String n, List<RegionLocal> child, List<RegionLocal> group) {
        this.i = i;
        this.n = n;
        this.child = child;
        this.group = group;
    }

    public RegionLocal() {
        i="0";
        n="";
        child=new ArrayList<>();
        group=new ArrayList<>();
    }
}
