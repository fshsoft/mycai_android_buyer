package com.fanwe.seallibrary.model.event;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-24
 * Time: 20:51
 * FIXME
 */
public class LoginEvent {
    public boolean success;

    public LoginEvent(boolean isSucc) {
        this.success = isSucc;
    }
}
