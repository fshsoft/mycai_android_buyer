package com.fiftyonemycai365.buyer.bbs.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fanwe.seallibrary.model.ForumMessage;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.bbs.activity.ForumDetailActivity;
import com.fiftyonemycai365.buyer.utils.ImgUrl;
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
public class BbsMsgAdapter extends CommonAdapter<ForumMessage> {
    private IOpMessage mIOpMessage;

    public BbsMsgAdapter(Context context, List<ForumMessage> datas, IOpMessage callback) {
        super(context, datas, R.layout.bbs_item_bbs_msg);
        mIOpMessage = callback;
    }

    @Override
    public void convert(final ViewHolder viewHolder, final ForumMessage forumMessage, final int i) {
        if (forumMessage.type == 1) {
            viewHolder.setImageResource(R.id.iv_head, R.drawable.ic_sys_msg);
        } else {
            if (forumMessage.relatedUser != null) {
                viewHolder.setImageUrl(R.id.iv_head, ImgUrl.squareUrl(R.dimen.image_height_small, forumMessage.relatedUser.avatar), RequestManager.getImageLoader(), R.drawable.ic_default_circle);
            }
        }
        viewHolder.setText(R.id.tv_title, forumMessage.title);
        viewHolder.setText(R.id.tv_time, forumMessage.sendTime);
        viewHolder.setText(R.id.tv_msg, forumMessage.content);
        if (TextUtils.isEmpty(forumMessage.readTime)) {
            viewHolder.getView(R.id.iv_new).setVisibility(View.VISIBLE);
        } else {
            viewHolder.getView(R.id.iv_new).setVisibility(View.INVISIBLE);
        }
        if (forumMessage.posts != null) {
            TextView textView = viewHolder.getView(R.id.tv_from);
            textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            textView.getPaint().setAntiAlias(true);
            viewHolder.setText(R.id.tv_from, forumMessage.posts.title);
        } else {
            viewHolder.setText(R.id.tv_from, "");
        }
        viewHolder.getView(R.id.rl_container).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mIOpMessage != null) {
                    mIOpMessage.onDel(forumMessage, i);
                }
                return true;
            }
        });
        viewHolder.getView(R.id.rl_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(forumMessage.posts == null){
                    return;
                }
                ForumDetailActivity.start(mContext, forumMessage.posts);
                if (TextUtils.isEmpty(forumMessage.readTime)) {
                    forumMessage.readTime = "read";
                    if (mIOpMessage != null) {
                        mIOpMessage.onRead(forumMessage, position);
                        viewHolder.getView(R.id.iv_new).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    public void removeById(int id) {
        for (ForumMessage item : mDatas) {
            if (item.id == id) {
                mDatas.remove(item);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public static interface IOpMessage {
        void onDel(ForumMessage forumMessage, int position);

        void onRead(ForumMessage forumMessage, int position);
    }
}
