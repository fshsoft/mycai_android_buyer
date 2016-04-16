package com.fanwe.seallibrary.model.event;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-06
 * Time: 15:02
 * FIXME
 */
public class DoorUpdateEvent {
    public int doorId;
    public String doorName;

    public DoorUpdateEvent(int doorId, String doorName){
        this.doorId = doorId;
        this.doorName = doorName;
    }
}
