package com.fanwe.seallibrary.model.event;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-06
 * Time: 15:02
 * FIXME
 */
public class OpenDoorEvent {
    public String userId;
    public String keyName;
    public String community;
    public String keyId;
    public int doorId;

    public OpenDoorEvent(String userId, String keyName, String community, String keyId, int doorId){
        this.userId = userId;
        this.keyName = keyName;
        this.community = community;
        this.keyId = keyId;
        this.doorId = doorId;
    }
}
