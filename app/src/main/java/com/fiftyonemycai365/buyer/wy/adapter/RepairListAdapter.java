package com.fiftyonemycai365.buyer.wy.adapter;

import android.content.Context;
import android.view.View;
import android.widget.GridView;

import com.fanwe.seallibrary.model.Repair;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.adapter.NetPhotoGridAdapter;
import com.fiftyonemycai365.buyer.wy.activity.RepairDetailActivity;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

public class RepairListAdapter extends CommonAdapter<Repair> {
    public RepairListAdapter(Context context, List<Repair> datas) {
        super(context, datas, R.layout.item_repair_list);
    }

    public void setList(List<Repair> list) {
        mDatas.clear();
        addAll(list);
    }

    @Override
    public void convert(final ViewHolder helper, final Repair item, int position) {

        helper.setText(R.id.tv_title, item.repairType);
        helper.setText(R.id.tv_content, item.content);
        helper.setText(R.id.tv_time, item.createTime);
        if (ArraysUtils.isEmpty(item.images)) {
            helper.setViewVisible(R.id.gv_pics, View.GONE);
        } else {
            NetPhotoGridAdapter photoGridAdapter = new NetPhotoGridAdapter(mContext, item.images);
            GridView gv = helper.getView(R.id.gv_pics);
            gv.setVisibility(View.VISIBLE);
            gv.setAdapter(photoGridAdapter);
            gv.setEnabled(false);
            gv.setClickable(false);
//            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(mContext, ViewImageActivity.class);
//                    ArrayList<String> list = new ArrayList<String>();
//                    list.addAll(item.images);
//                    ViewImageActivity.show(mContext, list);
//                }
//            });
        }
        helper.getView(R.id.ll_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepairDetailActivity.start(mContext, item);
            }
        });
    }


}
