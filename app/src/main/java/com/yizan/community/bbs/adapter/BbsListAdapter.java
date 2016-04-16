package com.yizan.community.bbs.adapter;

import android.content.Context;
import android.view.View;

import com.fanwe.seallibrary.model.ForumPosts;
import com.yizan.community.R;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 18:14
 * 功能:
 */
public class BbsListAdapter extends CommonAdapter<ForumPosts> {
    private OnLongClick mOnLongClick;

    public BbsListAdapter(Context context, List<ForumPosts> datas) {
        super(context, datas, R.layout.bbs_item_bbs_list);
    }

    public BbsListAdapter(Context context, List<ForumPosts> datas, OnLongClick longClick) {
        super(context, datas, R.layout.bbs_item_bbs_list);
        mOnLongClick = longClick;
    }

    @Override
    public void convert(ViewHolder viewHolder, final ForumPosts forumPosts, int i) {
        viewHolder.setText(R.id.tv_title, forumPosts.title);
        viewHolder.setText(R.id.tv_time, forumPosts.createTimeStr);
        if (null == forumPosts.plate)
            viewHolder.setText(R.id.tv_plate, "未知");
        else
            viewHolder.setText(R.id.tv_plate, forumPosts.plate.name);
        viewHolder.setText(R.id.tv_reply, String.valueOf(forumPosts.rateNum));

        if (mOnLongClick != null) {
            viewHolder.getView(R.id.ll_container).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnLongClick.onLongClickListener(v, forumPosts);
                    return true;
                }
            });
            viewHolder.getView(R.id.ll_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnLongClick.onClickListener(v, forumPosts);
                }
            });
        }
    }

    public void removeById(int forumsId) {
        if (ArraysUtils.isEmpty(mDatas)) {
            return;
        }
        for (ForumPosts item : mDatas) {
            if (item.id == forumsId) {
                mDatas.remove(item);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public interface OnLongClick {
        void onLongClickListener(View v, ForumPosts forumPosts);

        void onClickListener(View v, ForumPosts forumPosts);
    }
}
