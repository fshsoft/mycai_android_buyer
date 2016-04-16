package com.yizan.community.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yizan.community.activity.BaseActivity;
import com.yizan.community.R;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.MessageInfo;
import com.yizan.community.utils.TagManager;

/**
 * Created by ztl on 2015/9/29.
 */
public class MessageDetailActivity extends BaseActivity implements BaseActivity.TitleListener {

    private MessageInfo messageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        setPageTag(TagManager.MESSAGE_DETAIL_ACTIVITY);
        setTitleListener(this);

        messageInfo = (MessageInfo) this.getIntent().getSerializableExtra(Constants.EXTRA_DATA);
        initViews();
    }

    private void initViews() {
        mViewFinder.setText(R.id.tv_title, messageInfo.title);
        mViewFinder.setText(R.id.tv_date, messageInfo.createTime);
        mViewFinder.setText(R.id.tv_content, messageInfo.content);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_message_detail);
    }
}
