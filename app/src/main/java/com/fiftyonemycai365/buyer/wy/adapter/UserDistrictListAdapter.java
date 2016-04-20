package com.fiftyonemycai365.buyer.wy.adapter;

import android.content.Context;
import android.view.View;

import com.fanwe.seallibrary.model.DistrictInfo;
import com.fiftyonemycai365.buyer.R;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

public class UserDistrictListAdapter extends CommonAdapter<DistrictInfo> {
    private IGoProperty mIGoProperty;
    public UserDistrictListAdapter(Context context, List<DistrictInfo> datas, IGoProperty iGoProperty) {
        super(context, datas, R.layout.item_user_district_list);
        mIGoProperty = iGoProperty;
    }

    public void setList(List<DistrictInfo> list) {
        mDatas.clear();
        if(!ArraysUtils.isEmpty(list)) {
            addAll(list);
        }
    }

    @Override
    public void convert(final ViewHolder helper, final DistrictInfo item, int position) {
        helper.setText(R.id.tv_name, item.name);
        helper.setText(R.id.tv_addr, item.address);

        if (item.isEnter > 0){
            helper.getView(R.id.btn_commit).setVisibility(View.VISIBLE);
            helper.getView(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mIGoProperty != null){
                        mIGoProperty.gotoProperty(item);
                    }

                }
            });
        }else{
            helper.getView(R.id.btn_commit).setVisibility(View.GONE);
        }
    }

    public void addFirst(DistrictInfo districtInfo){
        mDatas.add(0, districtInfo);
        notifyDataSetChanged();
    }

    public static interface IGoProperty{
        void gotoProperty(DistrictInfo info);
    }

}
