package com.fiftyonemycai365.buyer.bbs.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.fanwe.seallibrary.model.SearchHistory;
import com.fiftyonemycai365.buyer.R;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-30
 * Time: 15:24
 * 功能:
 */
public class BbsSearchHistoryAdapter extends CommonAdapter<String> {
    private final int MAX_COUNT = 5;
    private ListView mListView;
    public BbsSearchHistoryAdapter(Context context, ListView listView) {
        super(context, new ArrayList<String>(), R.layout.bbs_item_search_history_list);
        loadCache();
        mListView = listView;
    }

    public void loadCache(){
        SearchHistory history = PreferenceUtils.getObject(mContext, SearchHistory.class);
        if(history == null || ArraysUtils.isEmpty(history.datas)){
            clear();
        }else{
            setList(history.datas);
        }
        hideList();
    }
    public void setList(List<String> list) {
        mDatas.clear();
        addAll(list);

    }

    public void removeItem(int position){
        if(getCount() <= position){
            return;
        }
        mDatas.remove(position);
        notifyDataSetChanged();
        hideList();
    }

    protected void hideList(){
        if(mListView == null){
            return;
        }
        if(getCount() <= 0){
            mListView.setVisibility(View.GONE);
        }else{
            mListView.setVisibility(View.VISIBLE);
        }
    }

    public void addItem(String value){
        if(TextUtils.isEmpty(value)){
            return;
        }
        mDatas.add(0, value);
        if(getCount()>MAX_COUNT){
            mDatas.remove(getCount()-1);
        }
        notifyDataSetChanged();
        cacheDatas();
        hideList();
    }

    public void cacheDatas(){
        if(getCount() <= 0){
            return;
        }
        PreferenceUtils.setObject(mContext, new SearchHistory(getmDatas()));
    }

    @Override
    public void convert(ViewHolder helper, final String item, final int position) {
        helper.setText(R.id.tv_name, item);
        helper.getView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
    }


}
