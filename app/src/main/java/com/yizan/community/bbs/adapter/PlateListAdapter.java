package com.yizan.community.bbs.adapter;

import android.content.Context;

import com.fanwe.seallibrary.model.ForumPlate;
import com.yizan.community.R;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-30
 * Time: 15:24
 * 功能:
 */
public class PlateListAdapter extends CommonAdapter<ForumPlate> {
    public PlateListAdapter(Context context, List<ForumPlate> datas) {
        super(context, datas, R.layout.bbs_item_plate_list);

    }

    public void setList(List<ForumPlate> list) {
        mDatas.clear();
        addAll(list);
    }


    @Override
    public void convert(ViewHolder helper, final ForumPlate item, int position) {
        helper.setImageUrl(R.id.iv_image, item.icon, RequestManager.getImageLoader(), R.drawable.ic_default_circle);
        helper.setText(R.id.tv_name, item.name);
    }
}
