package com.fiftyonemycai365.buyer.activity;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.model.Promotion;
import com.fanwe.seallibrary.model.PromotionPack;
import com.fanwe.seallibrary.model.event.ExchangePromotionEvent;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.adapter.PromotionListAdapter;
import com.fiftyonemycai365.buyer.action.PromotionAction;
import com.fiftyonemycai365.buyer.bbs.activity.BaseListActivity;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class CouponGetActivity extends BaseListActivity<PromotionPack> implements BaseActivity.TitleListener {
    private PromotionAction mAction;

    @Override
    protected void initView() {
        mAction = new PromotionAction(this);
        setTitleListener(this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Promotion item = (Promotion) mAdapter.getItem(position);
                CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, CouponGetActivity.this.getClass().getName());
                mAction.exchange(item.sn, new ApiCallback<Promotion>() {
                    @Override
                    public void onSuccess(Promotion data) {
                        ToastUtils.show(getActivity(), "领取成功");
                        setResult(Activity.RESULT_OK);
                        CustomDialogFragment.dismissDialog();
                        EventBus.getDefault().post(new ExchangePromotionEvent(data));
                        ((PromotionListAdapter) mAdapter).remove(item);

                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        ToastUtils.show(getActivity(), message);
                        CustomDialogFragment.dismissDialog();
                    }
                });
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 200);
    }

    @Override
    protected CommonAdapter setAdapter() {
        return new PromotionListAdapter(this, new ArrayList<Promotion>());
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<PromotionPack> callback) {
//        mAction.list(2, currentPage, callback);
    }

    @Override
    protected List deliveryResult(PromotionPack result) {
        if(result != null) {
            return result.list;
        }
        return null;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("领卷");
        setPageTag(TagManager.COUPON_GET_ACTIVITY);
    }
}
