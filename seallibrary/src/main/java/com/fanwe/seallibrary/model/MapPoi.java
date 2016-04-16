package com.fanwe.seallibrary.model;

/**
 * Created by admin on 2016/1/26.
 */
public class MapPoi {
    public String id;
    public String title;
    public String address;
    public Location location;


    public static class Location {
        public double lat;
        public double lng;
    }
}
