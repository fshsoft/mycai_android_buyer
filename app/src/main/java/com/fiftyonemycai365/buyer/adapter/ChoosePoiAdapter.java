package com.fiftyonemycai365.buyer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.seallibrary.PoiInfo;
import com.fiftyonemycai365.buyer.R;

import java.util.List;

/**
 * Created by admin on 2016/1/26.
 */
public class ChoosePoiAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<PoiInfo> mDatas;

    public ChoosePoiAdapter(Context c, List<PoiInfo> list){
        this.context = c;
        this.mDatas = list;
        this.mInflater = LayoutInflater.from(c);
    }

    public void setList(List<PoiInfo> list){
        if (list != null && list.size() > 0){
            this.mDatas = list;
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mDatas != null && mDatas.size() != 0) {
            count = mDatas.size();
        }
        return count;
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_poistr_layout,null);
            holder.title = (TextView)view.findViewById(R.id.addr);
            holder.address = (TextView)view.findViewById(R.id.street);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        PoiInfo item = mDatas.get(i);
        if (!TextUtils.isEmpty(item.title) && !TextUtils.isEmpty(item.address)){
                holder.title.setText(item.title);
                holder.title.setTextColor(Color.BLACK);
                holder.address.setText(item.address);
        }
        return view;
    }

    private class ViewHolder{
        private TextView title,address;
    }
}
