package com.fiftyonemycai365.buyer.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fanwe.seallibrary.model.InitInfo;
import com.fanwe.seallibrary.model.Promotion;
import com.fanwe.seallibrary.model.PromotionPack;
import com.fanwe.seallibrary.model.event.ExchangePromotionEvent;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.WebViewActivity;
import com.fiftyonemycai365.buyer.adapter.PromotionListAdapter;
import com.fiftyonemycai365.buyer.action.PromotionAction;
import com.fiftyonemycai365.buyer.bbs.fragment.BaseListFragment;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;


public class CouponListFragment extends BaseListFragment<PromotionPack> implements View.OnClickListener {
    private static final String ARG_STATUS = "status";
    private int mStatus = 0;
    private PromotionAction mPromotionAction;

    public static CouponListFragment newInstance(int status) {
        CouponListFragment fragment = new CouponListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.fragment_coupon_list, viewGroup, false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    public void onEventMainThread(ExchangePromotionEvent event) {
        if(mStatus == 3){
            if(mAdapter != null){
                initData();
            }
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStatus = getArguments().getInt(ARG_STATUS, 3);
        }
        setPageTag(TagManager.COUPON_LIST_FRAGMENT);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void initView() {
        super.initView();
        mPromotionAction = new PromotionAction(getActivity());
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ForumDetailActivity.start(getActivity(), (CommentListAdapter) mAdapter.getItem((int) id));
            }
        });
        mViewFinder.onClick(R.id.tv_help, this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        },100);
    }

    @Override
    protected CommonAdapter setAdapter() {
        return new PromotionListAdapter(getActivity(), new ArrayList<Promotion>());
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<PromotionPack> callback) {
        mPromotionAction.list(mStatus, currentPage,0,0, callback);
    }

    @Override
    protected List deliveryResult(PromotionPack result) {
        if(result != null) {
            mViewFinder.setText(R.id.tv_count, "" + result.count);
            return result.list;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_help:
                InitInfo info = PreferenceUtils.getObject(getActivity(), InitInfo.class);
                WebViewActivity.start(getActivity(), info.introUrl);
                break;
        }
    }
}
