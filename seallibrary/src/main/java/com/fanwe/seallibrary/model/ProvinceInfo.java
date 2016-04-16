package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @time 2015-3-23上午10:20:19
 */
public class ProvinceInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6035127890936099998L;
    public int id; //
    public String name; //
    public List<CityInfo> city;


}
