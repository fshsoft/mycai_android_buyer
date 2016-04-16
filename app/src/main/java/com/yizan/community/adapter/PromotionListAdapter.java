package com.yizan.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.Promotion;
import com.fanwe.seallibrary.model.SellerCatesInfo;
import com.yizan.community.R;
import com.yizan.community.activity.BusinessClassificationActivity;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

public class PromotionListAdapter extends CommonAdapter<Promotion> {
    public PromotionListAdapter(Context context, List<Promotion> datas) {
        super(context, datas, R.layout.item_promotion);
    }

    public void setList(List<Promotion> list) {
        mDatas.clear();
        addAll(list);
    }
    public void remove(Promotion promotion){
        try {
            mDatas.remove(promotion);
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void add(Promotion promotion){
        if(promotion == null){
            return;
        }
        mDatas.add(promotion);
        notifyDataSetChanged();
    }

    @Override
    public void convert(final ViewHolder helper, final Promotion item, int position) {
        helper.setViewVisible(R.id.iv_lost, item.status  ? View.VISIBLE : View.INVISIBLE);
        TextView tv = helper.getView(R.id.tv_price);
        if(0 == item.type.toLowerCase().compareTo("offset")){
            helper.setText(R.id.tv_price, "抵");
            helper.setText(R.id.tv_type, "抵用卷");
            helper.setViewVisible(R.id.tv_price_flag, View.GONE);
            helper.getView(R.id.ll_container).setBackgroundResource(R.drawable.bg_vouchers);
            tv.setTextColor(mContext.getResources().getColor(R.color.coupon_yellow));
        }else{
            helper.setText(R.id.tv_price, String.valueOf(item.money));
            helper.setText(R.id.tv_type, "优惠券");
            helper.setViewVisible(R.id.tv_price_flag, View.VISIBLE);
            helper.getView(R.id.ll_container).setBackgroundResource(R.drawable.bg_promotion);
            tv.setTextColor(mContext.getResources().getColor(R.color.coupon_red));
        }
        helper.setText(R.id.tv_lost_time, item.expireTimeStr);
        helper.setText(R.id.tv_desc, item.brief);
        helper.setText(R.id.tv_limit, item.name);

    }


}
