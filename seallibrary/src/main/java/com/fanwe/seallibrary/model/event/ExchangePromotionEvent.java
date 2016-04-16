package com.fanwe.seallibrary.model.event;

import com.fanwe.seallibrary.model.Promotion;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-13
 * Time: 18:01
 * 功能:
 */
public class ExchangePromotionEvent {
    public Promotion mPromotion;
    public ExchangePromotionEvent(Promotion promotion){
        this.mPromotion = promotion;
    }
}
