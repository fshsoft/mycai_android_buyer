package com.yizan.community.action;

import android.content.Context;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.BalanceListInfo;
import com.fanwe.seallibrary.model.PayLogsList;
import com.yizan.community.action.result.BalanceListResult;
import com.yizan.community.helper.ApiCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户相关 action
 * Created by Tu on 16/2/24.
 */
public class UserAction extends BaseAction{
    public UserAction(Context context) {
        super(context);
    }
    /**
     * balance change list
     * @param page
     * @param callback
     */
    public void balanceRecord(int page, ApiCallback<PayLogsList> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("page", page);
        data.put("pageSize", Constants.PAGE_SIZE);
        call(URLConstants.BALANCE_LIST, data, BalanceListResult.class, callback);
    }
}
