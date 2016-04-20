package com.fiftyonemycai365.buyer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.BalanceListInfo;
import com.fanwe.seallibrary.model.PayLogsList;
import com.fanwe.seallibrary.model.result.BalanceResult;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.action.UserAction;
import com.fiftyonemycai365.buyer.adapter.BalanceListAdapter;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.fiftyonemycai365.buyer.widget.recycler.AdvancedRecyclerView;
import com.fiftyonemycai365.buyer.widget.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的余额Activity
 */
public class BalanceListActivity extends BaseActivity implements BaseActivity.TitleListener, OnClickListener {
    private UserAction mAction;
    private List<BalanceListInfo> mData = new ArrayList<>();
    private BalanceListAdapter mAdapter;
    private AdvancedRecyclerView mRecyclerView;
    private TextView mBalanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recyclerview);
        setPageTag(TagManager.BALANCE_LIST_ACTIVITY);
        mAction = new UserAction(this);
        setTitleListener(this);

        mRecyclerView = mViewFinder.find(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //设置布局显示方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        //设置添加删除item时候的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        View headerView = getLayoutInflater().inflate(R.layout.item_header_balance, mRecyclerView, false);
        mBalanceTextView = (TextView) headerView.findViewById(R.id.balance_balance);
        mBalanceTextView.setText(getIntent().getStringExtra(Constants.EXTRA_DATA));
        headerView.findViewById(R.id.balance_recharge).setOnClickListener(this);
        mAdapter = new BalanceListAdapter(this, mData);
        mAdapter.addHeaderView(headerView);
        mRecyclerView.setEmptyView(mViewFinder.find(android.R.id.empty));
        mRecyclerView.setAdapter(mAdapter);
        updateBalance();
        loadData();
    }

    private void loadData() {
        mAction.balanceRecord(mData.size() / Constants.PAGE_SIZE, new ApiCallback<PayLogsList>() {
            @Override
            public void onSuccess(PayLogsList data) {
                if (data != null && data.paylogs != null && !data.paylogs.isEmpty()) {
                    mData.addAll(data.paylogs);
//                    mAdapter.notifyItemRangeChanged(mData.size() - data.paylogs.size(), mData.size());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.personal_balance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.balance_recharge:
                Intent intent = new Intent(this, PayWayActivity.class);
                intent.putExtra(Constants.EXTRA_TYPE, 1);
                startActivityForResult(intent, PayWayActivity.REQUEST_CODE_RECHARGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            updateBalance();
            loadData();
        }
    }
    private void updateBalance(){
        ApiUtils.post(this, URLConstants.BALANCE_QUERY,
                new HashMap<String, String>(),
                BalanceResult.class, new Response.Listener<BalanceResult>() {
                    @Override
                    public void onResponse(BalanceResult response) {
                        if (response != null && response.data != null && !TextUtils.isEmpty(response.data.balance)) {
                            mBalanceTextView.setText(response.data.balance);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
}
