package com.fiftyonemycai365.buyer.wy.adapter;

import android.content.Context;

import com.fanwe.seallibrary.model.RepairType;
import com.fiftyonemycai365.buyer.R;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

public class RepairTypeListAdapter extends CommonAdapter<RepairType> {
    public RepairTypeListAdapter(Context context, List<RepairType> datas) {
        super(context, datas, R.layout.item_pop);
    }

    public void setList(List<RepairType> list) {
        mDatas.clear();
        addAll(list);
    }

    @Override
    public void convert(final ViewHolder helper, final RepairType item, int position) {
        helper.setText(R.id.tv_pop, item.name);
    }


}
