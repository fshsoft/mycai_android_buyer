package com.fanwe.seallibrary.model.event;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-06
 * Time: 15:02
 * FIXME
 */
public class OrderListCommentsEvent {
    public Object viewObj;
    public int comments;

    public OrderListCommentsEvent(Object viewObj, int comments){
        this.viewObj = viewObj;
        this.comments = comments;
    }

    public OrderListCommentsEvent(Object viewObj){
        this.viewObj = viewObj;
        this.comments = 0;
    }
}
