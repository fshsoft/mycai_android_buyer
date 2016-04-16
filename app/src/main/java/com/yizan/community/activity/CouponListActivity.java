package com.yizan.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.model.Promotion;
import com.fanwe.seallibrary.model.PromotionPack;
import com.fanwe.seallibrary.model.event.ExchangePromotionEvent;
import com.yizan.community.R;
import com.yizan.community.action.PromotionAction;
import com.yizan.community.fragment.CouponListFragment;
import com.yizan.community.fragment.CustomDialogFragment;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.TagManager;
import com.yizan.community.widget.PagerSlidingTabStrip;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.util.ToastUtils;

public class CouponListActivity extends BaseActivity implements BaseActivity.TitleListener, OnClickListener {
    protected final static int REQUEST_CODE = 0x123;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private PromotionAction mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        mViewFinder.onClick(R.id.btn_commit, this);
        setPageTag(TagManager.COUPON_LIST_ACTIVITY);
        mViewPager = mViewFinder.find(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.margin_small));
        //向ViewPager绑定PagerSlidingTabStrip
        mPagerSlidingTabStrip = mViewFinder.find(R.id.tabs);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mAction = new PromotionAction(this);
//        setTitleListener(this, R.layout.titlebar_new_flag);
        setTitleListener(this);
        loadData();
    }

    private void loadData(){
        if(mTitleRight2 == null){
            return;
        }
        mAction.list(2, 1,0,0, new ApiCallback<PromotionPack>() {
            @Override
            public void onSuccess(PromotionPack data) {
                mTitleRight2.setVisibility(View.INVISIBLE);
                if(data != null && data.count > 0){
                    if(mTitleRight2 != null){
                        mTitleRight2.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                mTitleRight2.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_coupon_list);
//        if(right != null && right instanceof Button){
//            ((Button)right).setText("领卷");
//            right.setVisibility(View.VISIBLE);
//            right.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(CouponListActivity.this, CouponGetActivity.class);
//                    startActivityForResult(intent, REQUEST_CODE);
//                }
//            });
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                exchange();
                break;
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] title = {getResources().getString(R.string.tab_coupon_userful), getResources().getString(R.string.tab_coupon_lost)};
        CouponListFragment mFragment1, mFragment2;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mFragment1 = CouponListFragment.newInstance(3);
                    return mFragment1;
                case 1:
                    mFragment2 = CouponListFragment.newInstance(1);
                    return mFragment2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }

    private void exchange(){
        final EditText et = mViewFinder.find(R.id.et_code);
        String code = et.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            ToastUtils.show(getActivity(), "请输入兑换码");
            return;
        }
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());

        mAction.exchange(code,new ApiCallback<Promotion>() {
            @Override
            public void onSuccess(Promotion data) {
                CustomDialogFragment.dismissDialog();
                EventBus.getDefault().post(new ExchangePromotionEvent(data));
                ToastUtils.show(getActivity(), "兑换成功");
                et.setText("");
            }

            @Override
            public void onFailure(int errorCode, String message) {
                ToastUtils.show(getActivity(), message);
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            loadData();
        }
    }
}
