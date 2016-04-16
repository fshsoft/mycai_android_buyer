package com.yizan.community.bbs.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.model.ForumPosts;
import com.fanwe.seallibrary.model.event.ForumReplyEvent;
import com.yizan.community.R;
import com.yizan.community.utils.ImgUrl;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 18:14
 * 功能:
 */
public class BbsReplyListAdapter extends CommonAdapter<ForumPosts>{
    private int mUserId;
    public BbsReplyListAdapter(Context context, List<ForumPosts> datas, int userId) {
        super(context, datas, R.layout.bbs_reply_item_list);
        this.mUserId = userId;
    }

    @Override
    public void convert(ViewHolder viewHolder, final ForumPosts forumPosts, int i) {
        viewHolder.setText(R.id.tv_content, forumPosts.content);
        viewHolder.setText(R.id.tv_time, forumPosts.createTimeStr);
        viewHolder.setText(R.id.tv_floor, forumPosts.flood + "楼");
        if(forumPosts.address != null){
            viewHolder.setText(R.id.tv_addr, forumPosts.address.detailAddress);
        }else{
            viewHolder.setText(R.id.tv_addr, "");
        }
        viewHolder.getView(R.id.tv_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ForumReplyEvent(forumPosts));
            }
        });

        if(forumPosts.user != null){
            viewHolder.getView(R.id.tv_floor_host).setVisibility(forumPosts.user.id == mUserId ? View.VISIBLE : View.INVISIBLE);
            viewHolder.setText(R.id.tv_name, forumPosts.user.name);
            NetworkImageView iv = viewHolder.getView(R.id.iv_head);
            iv.setImageUrl(ImgUrl.squareUrl(R.dimen.image_height_small, forumPosts.user.avatar), RequestManager.getImageLoader());
        }

        View llReply = viewHolder.getView(R.id.ll_reply);
        if(!TextUtils.isEmpty(forumPosts.replyContent) && forumPosts.replyPosts != null){
            llReply.setVisibility(View.VISIBLE);
            viewHolder.setText(R.id.tv_reply_content, forumPosts.replyPosts.content);
            viewHolder.setText(R.id.tv_reply_post, forumPosts.replyContent);
        }else{
            llReply.setVisibility(View.GONE);
        }
    }

    public void addFirst(ForumPosts posts){
        if(posts == null){
            return;
        }
        mDatas.add(0, posts);
        notifyDataSetChanged();
    }
}
