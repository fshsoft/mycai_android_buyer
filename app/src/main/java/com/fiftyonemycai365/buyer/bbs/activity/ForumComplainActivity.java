package com.fiftyonemycai365.buyer.bbs.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.action.BbsAction;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.zongyou.library.util.ToastUtils;

public class ForumComplainActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    private BbsAction mAction;
    private int mForumsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_complain);
        setPageTag(TagManager.A_FORUM_COMPLAIN);
        setTitleListener(this);
        mForumsId = getIntent().getIntExtra(Constants.EXTRA_DATA, -1);
        mAction = new BbsAction(this);
        mViewFinder.onClick(R.id.btn_commit, this);
        if(mForumsId < 0){
            finishActivity();
        }
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("举报帖子");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                complainForum();
                break;
        }
    }

    private void complainForum(){
        EditText et = mViewFinder.find(R.id.et_value);
        String value = et.getText().toString().trim();

        if(TextUtils.isEmpty(value)) {
            ToastUtils.show(this, "请输入举报理由");
            return;
        }
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.forumComplain(mForumsId, value, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getActivity(), "举报成功");
                finishActivity();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getActivity(), message);
            }
        });
    }
}
