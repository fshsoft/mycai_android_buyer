package com.fiftyonemycai365.buyer.action;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.zongyou.library.util.NetworkUtils;

import java.lang.reflect.Field;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 19:00
 * 功能:
 */
public class BaseAction {
    protected Context mContext;

    public BaseAction(Context context) {
        this.mContext = context;
    }

    public boolean isOk(BaseResult response) {
        if (response == null || response.code != 0) {
            return false;
        }
        return true;
    }

    protected <T extends BaseResult, X> void call(String url, Object params, Class<T> cls, final ApiCallback<X> callback) {
        if (!NetworkUtils.isNetworkAvaiable(mContext)) {
            if (callback != null) {
                callback.onFailure(ErrCode.ERR_NETWORK_DISCONNECT, mContext.getString(R.string.loading_err_net));
            }
            return;
        }
        ApiUtils.post(mContext,
                url,
                params, cls,
                new ResponseListener(callback), new ErrListener(callback));
    }

    protected class ResponseListener<T extends BaseResult> implements Response.Listener<T> {
        protected ApiCallback mCallback;

        public ResponseListener(ApiCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onResponse(T response) {
            if (mCallback == null) {
                return;
            }
            if (isOk(response)) {
                try {
                    Field field = response.getClass().getField("data");
                    if(field != null) {
                        mCallback.onSuccess(field.get(response));
                    }else{
                        mCallback.onSuccess(null);
                    }
                }catch (Exception e){
                    mCallback.onSuccess(null);
                }
            } else {
                if (response != null) {
                    mCallback.onFailure(response.code, response.msg);
                } else {
                    mCallback.onFailure(ErrCode.ERR_DATA_PARSE, "数据解析失败");
                }
            }
        }

    }

    protected class ErrListener implements Response.ErrorListener {
        protected ApiCallback mCallback;

        public ErrListener(ApiCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (mCallback != null) {
                mCallback.onFailure(ErrCode.ERR_NETWORK_DISCONNECT, "网络连接失败");
            }
        }
    }

}
