package com.fiftyonemycai365.buyer.bbs.adapter;

import android.content.Context;

import com.fanwe.seallibrary.model.ForumPosts;
import com.fiftyonemycai365.buyer.R;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 18:14
 * 功能:
 */
public class BbsTopAdapter extends CommonAdapter<ForumPosts>{
    public BbsTopAdapter(Context context, List<ForumPosts> datas) {
        super(context, datas, R.layout.bbs_item_top_bbs_list);
    }

    @Override
    public void convert(ViewHolder viewHolder, ForumPosts forumPosts, int i) {
        viewHolder.setText(R.id.tv_title, forumPosts.title);
    }

}
