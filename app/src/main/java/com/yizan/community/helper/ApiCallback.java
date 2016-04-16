package com.yizan.community.helper;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 17:36
 * 功能:
 */
public interface ApiCallback<T> {
    public void onSuccess(T data);
    void onFailure(int errorCode, String message);
}
