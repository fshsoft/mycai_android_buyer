package com.yizan.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.InitInfo;
import com.fanwe.seallibrary.model.Promotion;
import com.fanwe.seallibrary.model.PromotionPack;
import com.yizan.community.R;
import com.yizan.community.adapter.PromotionListAdapter;
import com.yizan.community.action.PromotionAction;
import com.yizan.community.bbs.activity.BaseListActivity;
import com.yizan.community.fragment.CustomDialogFragment;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.TagManager;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class CouponUseActivity extends BaseListActivity<PromotionPack> implements BaseActivity.TitleListener, View.OnClickListener {
    private PromotionAction mAction;
    private static int sellerIds;
    private static double moneys;

    @Override
    protected void initView() {
        mAction = new PromotionAction(this);
        setTitleListener(this);
        setPageTag(TagManager.COUPON_USE_ACTIVITY);
        mViewFinder.onClick(R.id.btn_commit, this);
        mViewFinder.onClick(R.id.tv_help, this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Promotion item = ((PromotionListAdapter)mAdapter).getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_DATA, item);
                setResult(Activity.RESULT_OK, intent);
                finishActivity();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 200);
    }

    protected int getLayoutId(){
        return R.layout.activity_coupon_use;
    }
    @Override
    protected CommonAdapter setAdapter() {
        return new PromotionListAdapter(this, new ArrayList<Promotion>());
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<PromotionPack> callback) {
        mAction.list(2, currentPage,sellerIds,moneys, callback);
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
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("选择优惠券");
        if(right instanceof Button){
            ((Button)right).setText("取消选择");
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(Activity.RESULT_OK);
                    finishActivity();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                exchange();
                break;
            case R.id.tv_help:
                InitInfo info = PreferenceUtils.getObject(this, InitInfo.class);
                WebViewActivity.start(getActivity(), info.introUrl);
                break;
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

        mAction.exchange(code, new ApiCallback<Promotion>() {
            @Override
            public void onSuccess(Promotion data) {
                CustomDialogFragment.dismissDialog();
                initData();
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

    public static void start(Activity context, int requestCode,int sellerId,double money){
        Intent intent = new Intent(context, CouponUseActivity.class);
        sellerIds = sellerId;
        moneys = money;
        context.startActivityForResult(intent, requestCode);
    }
}
