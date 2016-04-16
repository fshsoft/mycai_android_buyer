package com.yizan.community.wy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.PropertyCompany;
import com.yizan.community.R;
import com.yizan.community.action.PropertyAction;
import com.yizan.community.activity.BaseActivity;
import com.yizan.community.activity.ViewImageActivity;
import com.yizan.community.fragment.CustomDialogFragment;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.ImgUrl;
import com.yizan.community.utils.TagManager;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.ArrayList;

public class PropertyDetailActivity extends BaseActivity implements BaseActivity.TitleListener {
    private PropertyCompany mPropertyCompany;
    private PropertyAction mAction;
    private int mDistrictId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
        setPageTag(TagManager.A_PROPERTY_DETAIL);
        setTitleListener(this);
        mAction = new PropertyAction(getActivity());
        mDistrictId = getIntent().getIntExtra(Constants.EXTRA_DATA, -1);
        if(mDistrictId < 0){
            finishActivity();
            return;
        }
        loadData();
    }

    private void initView(){
        if(mPropertyCompany == null){
            return;
        }

        mViewFinder.setText(R.id.tv_name, mPropertyCompany.name);
        mViewFinder.setText(R.id.tv_contract, mPropertyCompany.contacts);
        mViewFinder.setText(R.id.tv_tel, mPropertyCompany.mobile);

        NetworkImageView iv = mViewFinder.find(R.id.iv_licences);
        iv.setImageUrl(ImgUrl.scaleUrl(R.dimen.licences_width, R.dimen.licences_heigth, mPropertyCompany.businessLicenceImg), RequestManager.getImageLoader());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                list.add(mPropertyCompany.businessLicenceImg);
                ViewImageActivity.show(getActivity(), list);
            }
        });
    }
    private void loadData(){
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.propertyDetail(mDistrictId, new ApiCallback<PropertyCompany>() {
            @Override
            public void onSuccess(PropertyCompany data) {
                CustomDialogFragment.dismissDialog();
                mPropertyCompany = data;
                initView();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                ToastUtils.show(getActivity(), message);
                CustomDialogFragment.dismissDialog();
                finishActivity();
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("物业介绍");
    }

    public static void start(Context context, int districtId){
        Intent intent = new Intent(context, PropertyDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, districtId);
        context.startActivity(intent);
    }
}
