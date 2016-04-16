package com.yizan.community.wy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.ArticleInfo;
import com.fanwe.seallibrary.model.DistrictInfo;
import com.yizan.community.action.PropertyAction;
import com.yizan.community.activity.BaseActivity;
import com.yizan.community.activity.WebViewActivity;
import com.yizan.community.bbs.activity.BaseListActivity;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.TagManager;
import com.yizan.community.wy.adapter.PropertyNoticeAdapter;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class NoticeListActivity extends BaseListActivity<List<ArticleInfo>> implements BaseActivity.TitleListener {
    private PropertyAction mAction;
    private int mDistrictId;

    @Override
    protected void initView() {
        setPageTag(TagManager.A_NOTICE_LIST);
        mAction = new PropertyAction(this);
        mDistrictId = getIntent().getIntExtra(Constants.EXTRA_DATA, -1);
        if (mDistrictId < 0) {
            finishActivity();
            return;
        }
        setTitleListener(this);
        setNeedLoadNext(false);
        initData();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PropertyNoticeAdapter adapter = getAdapter();
                ArticleInfo info = adapter.getItem(position);
                WebViewActivity.start(getActivity(), info.url);
                if (TextUtils.isEmpty(info.readTime)) {
                    info.readTime = "read";
                    adapter.notifyDataSetChanged();
                    mAction.articleRead(info.id,  null);
                }
            }
        });
    }

    @Override
    protected CommonAdapter setAdapter() {
        return new PropertyNoticeAdapter(getActivity(), new ArrayList<ArticleInfo>());
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<List<ArticleInfo>> callback) {
        mAction.articleList(mDistrictId, callback);
    }

    @Override
    protected List deliveryResult(List<ArticleInfo> result) {
        return result;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("社区公告");
    }

    public static void start(Context context, int districtId) {
        Intent intent = new Intent(context, NoticeListActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, districtId);
        context.startActivity(intent);
    }

}
