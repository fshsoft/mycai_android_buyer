package com.fanwe.seallibrary.model.event;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-06
 * Time: 15:02
 * FIXME
 */
public class OrderEvent {
    public Object viewObj;
    public int op;

    public OrderEvent(Object viewObj, int op){
        this.viewObj = viewObj;
        this.op = op;
    }

    public OrderEvent(Object viewObj){
        this.viewObj = viewObj;
        this.op = 0;
    }
}
