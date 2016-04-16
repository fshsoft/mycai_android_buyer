package com.yizan.community.bbs.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.fanwe.seallibrary.model.ForumPosts;
import com.yizan.community.R;
import com.yizan.community.utils.ImgUrl;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 18:14
 * 功能:
 */
public class ExBbsListAdapter extends CommonAdapter<ForumPosts>{
    private boolean mRmTop;
    public ExBbsListAdapter(Context context, List<ForumPosts> datas, boolean rmTop) {
        super(context, datas, R.layout.bbs_exitem_bbs_list);
        mRmTop = rmTop;
    }

    @Override
    public void addAll(List<ForumPosts> list) {
        if(ArraysUtils.isEmpty(list)){
            return;
        }
        if(mRmTop) {
            List<ForumPosts> tmp = new ArrayList<>();
            for (ForumPosts item : list) {
                if (item.top != 1) {
                    tmp.add(item);
                }
            }
            super.addAll(tmp);
        }else{
            super.addAll(list);
        }
    }

    @Override
    public void convert(ViewHolder viewHolder, ForumPosts forumPosts, int i) {
        if(ArraysUtils.isEmpty(forumPosts.imagesArr)){
            viewHolder.getView(R.id.iv_image).setVisibility(View.GONE);
        }else{
            viewHolder.getView(R.id.iv_image).setVisibility(View.VISIBLE);
            viewHolder.setImageUrl(R.id.iv_image, ImgUrl.squareUrl(R.dimen.image_height, forumPosts.imagesArr.get(0)), RequestManager.getImageLoader(), R.drawable.ic_default_square);
        }
        if(forumPosts.user != null) {
            viewHolder.setImageUrl(R.id.iv_head, ImgUrl.squareUrl(R.dimen.image_height_min, forumPosts.user.avatar), RequestManager.getImageLoader(), R.drawable.ic_default_circle);
            viewHolder.setText(R.id.tv_name, forumPosts.user.name);
        }
        viewHolder.setText(R.id.tv_content, forumPosts.title);
        viewHolder.setText(R.id.tv_time, forumPosts.createTimeStr);

        viewHolder.setText(R.id.tv_heart, String.valueOf(forumPosts.goodNum));
        viewHolder.setText(R.id.tv_reply, String.valueOf(forumPosts.rateNum));

        TextView tvHeart = viewHolder.getView(R.id.tv_heart);
        int resId = R.drawable.bbs_ic_red_heart;
        if(forumPosts.isPraise == 0){
            resId = R.drawable.bbs_ic_gray_heart;
        }
        Drawable heart = mContext.getResources().getDrawable(resId);
        heart.setBounds(0, 0, heart.getMinimumWidth(), heart.getMinimumHeight());
        tvHeart.setCompoundDrawables(heart, null, null, null);
    }
}
