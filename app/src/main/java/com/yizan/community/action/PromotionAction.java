package com.yizan.community.action;

import android.content.Context;

import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.Promotion;
import com.fanwe.seallibrary.model.PromotionPack;
import com.yizan.community.action.result.PromotionListResult;
import com.yizan.community.action.result.PromotionResult;
import com.yizan.community.helper.ApiCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 18:53
 * 功能:
 */
public class PromotionAction extends BaseAction {
    public PromotionAction(Context context) {
        super(context);
    }

    /**
     * 会员优惠券列表
     *
     * @param status   状态 1:已失效 2:可领取 3:未使用
     * @param page
     * @param callback
     */
    public void list(int status, int page,int sellerId,double money, ApiCallback<PromotionPack> callback) {
        Map<String, Object> data = new HashMap<>();
        if (status == 2){
            data.put("sellerId",sellerId);
            data.put("money",money);
        }
        data.put("status", status);
        data.put("page", page);
        call(URLConstants.USER_PROMOTION_LIST, data, PromotionListResult.class, callback);
    }

    /**
     * 优惠券兑换 / 领取
     *
     * @param sn
     * @param callback
     */
    public void exchange(String sn, ApiCallback<Promotion> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("sn", sn);
        call(URLConstants.USER_PROMOTION_EXCHANGE, data, PromotionResult.class, callback);
    }

    /**
     * 优惠券信息
     *
     * @param callback
     */
    public void info(int id, ApiCallback<Promotion> callback) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        call(URLConstants.USER_PROMOTION_INFO, data, PromotionResult.class, callback);
    }

    /**
     * 获取会员可用的第一个优惠券信息
     *
     * @param callback
     */
    public void first(ApiCallback<Promotion> callback) {

        call(URLConstants.USER_PROMOTION_FIRST, null, PromotionResult.class, callback);
    }


}
