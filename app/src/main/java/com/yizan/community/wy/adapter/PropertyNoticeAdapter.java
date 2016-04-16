package com.yizan.community.wy.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fanwe.seallibrary.model.ArticleInfo;
import com.fanwe.seallibrary.model.PropertyFunc;
import com.yizan.community.R;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

public class PropertyNoticeAdapter extends CommonAdapter<ArticleInfo> {
    public PropertyNoticeAdapter(Context context, List<ArticleInfo> datas) {
        super(context, datas, R.layout.item_property_notice_list);
    }

    public void setList(List<ArticleInfo> list) {
        mDatas.clear();
        addAll(list);
    }

    @Override
    public void convert(final ViewHolder helper, final ArticleInfo item, int position) {
        if(TextUtils.isEmpty(item.readTime)){
            helper.getView(R.id.iv_new_flag).setVisibility(View.VISIBLE);
        }else{
            helper.getView(R.id.iv_new_flag).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_content, item.content);
        helper.setText(R.id.tv_time, item.createTime);
    }


}
