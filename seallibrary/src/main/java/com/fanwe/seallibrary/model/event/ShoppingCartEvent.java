package com.fanwe.seallibrary.model.event;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-24
 * Time: 20:51
 * FIXME
 */
public class ShoppingCartEvent {
    public int goodsId;
    public int normsId;
    public boolean success;
    public String tag;
    public int nums;

    public ShoppingCartEvent(int goodsId, int normsId, boolean isSucc, String tag, int nums) {
        this.goodsId = goodsId;
        this.success = isSucc;
        this.tag = tag;
        this.nums = nums;
        this.normsId = normsId;

    }
}
