package com.yizan.community.wy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.DistrictInfo;
import com.yizan.community.R;
import com.yizan.community.action.PropertyAction;
import com.yizan.community.activity.BaseActivity;
import com.yizan.community.fragment.CustomDialogFragment;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.TagManager;
import com.zongyou.library.util.ToastUtils;

public class DistrictDetailActivity extends BaseActivity implements View.OnClickListener, BaseActivity.TitleListener {
    private DistrictInfo mDistrictInfo;
    private PropertyAction mAction;
    private ImageButton mRightTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_detail);
        setPageTag(TagManager.A_DISTRICT_DETAIL);
        mDistrictInfo = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if (mDistrictInfo == null) {
            finishActivity();
            return;
        }
        setTitleListener_RightImage(this);
        mAction = new PropertyAction(this);
        initView();
        loadData();
        mViewFinder.onClick(R.id.btn_commit, this);
    }

    private void initView() {
        if (mDistrictInfo == null) {
            return;
        }
        mViewFinder.setText(R.id.tv_name, mDistrictInfo.name);
        mViewFinder.setText(R.id.tv_nums, mDistrictInfo.houseNum + "户");
        mViewFinder.setText(R.id.tv_area, mDistrictInfo.areaNum + "平方米");
        mViewFinder.setText(R.id.tv_addr, mDistrictInfo.address);
        mViewFinder.setText(R.id.tv_type, mDistrictInfo.houseTypeName);
        mViewFinder.setText(R.id.tv_property, mDistrictInfo.sellerName);

    }

    private void initStatus() {
        if (mDistrictInfo == null) {
            return;
        }
        if(mDistrictInfo.isUser == 1){
            mRightTitle.setVisibility(View.VISIBLE);
        }
        if (mDistrictInfo.isEnter <= 0) {
            mViewFinder.find(R.id.ll_status).setVisibility(View.GONE);
            return;
        }
        Button btn = mViewFinder.find(R.id.btn_commit);
        mViewFinder.find(R.id.ll_status).setVisibility(View.VISIBLE);
        if (mDistrictInfo.isUser == 1) {
            btn.setText("物业");
        } else {
            btn.setText("加入我的小区");
        }
    }

    private void loadData() {
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.districtDetail(mDistrictInfo.id, new ApiCallback<DistrictInfo>() {
            @Override
            public void onSuccess(DistrictInfo data) {
                CustomDialogFragment.dismissDialog();
                mDistrictInfo = data;
                initStatus();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getActivity(), message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (mDistrictInfo.isUser == 1) {
                    PropertyActivity.start(getActivity(), mDistrictInfo);
                } else {
                    addDistrict();
                }
                break;
        }
    }

    private void addDistrict() {
        if (mDistrictInfo == null) {
            return;
        }

        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.addDistrict(mDistrictInfo.id, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomDialogFragment.dismissDialog();
                UserDistrictActivity.start(getActivity());
                finishActivity();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getActivity(), message);
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(mDistrictInfo.name);
        if(right instanceof ImageButton){
            mRightTitle = (ImageButton)right;
            mRightTitle.setImageResource(R.drawable.ic_clean);
            mRightTitle.setVisibility(View.INVISIBLE);
            mRightTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
                    mAction.delDistrict(mDistrictInfo.id, new ApiCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            ToastUtils.show(getActivity(), "删除成功");
                            CustomDialogFragment.dismissDialog();
                            UserDistrictActivity.start(getActivity());
                            finishActivity();
                        }

                        @Override
                        public void onFailure(int errorCode, String message) {
                            ToastUtils.show(getActivity(), message);
                            CustomDialogFragment.dismissDialog();
                        }
                    });
                }
            });
        }
    }

    public static void start(Context context, DistrictInfo info){
        Intent intent = new Intent(context, DistrictDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, info);
        context.startActivity(intent);
    }
}
