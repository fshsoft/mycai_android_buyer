package com.fiftyonemycai365.buyer.wy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.DistrictAuthInfo;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.utils.TagManager;

public class OwnerDetailActivity extends BaseActivity implements BaseActivity.TitleListener {
    private DistrictAuthInfo mAuthInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_detail);
        setPageTag(TagManager.A_OWNER_DETAIL);
        mAuthInfo = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        initView();
        setTitleListener(this);
    }

    private void initView(){
        if(mAuthInfo == null){
            finishActivity();
            return;
        }
        mViewFinder.setText(R.id.tv_user_name, mAuthInfo.room.owner);
        mViewFinder.setText(R.id.tv_user_unit, mAuthInfo.build.name + "#" + mAuthInfo.room.roomNum);
        mViewFinder.setText(R.id.tv_area, mAuthInfo.room.structureArea + "平方米");
        mViewFinder.setText(R.id.tv_useful_area, mAuthInfo.room.roomArea + "平方米");
        mViewFinder.setText(R.id.tv_mobile, mAuthInfo.room.mobile);
        mViewFinder.setText(R.id.tv_time, mAuthInfo.room.intakeTime);
        mViewFinder.setText(R.id.tv_price, "¥" + mAuthInfo.room.propertyFee);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("业主信息");
    }

    public static void start(Context context, DistrictAuthInfo auth){
        if(auth == null){
            return;
        }
        Intent intent = new Intent(context, OwnerDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, auth);
        context.startActivity(intent);
    }
}
