package com.fiftyonemycai365.buyer.bbs.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.bbs.adapter.BbsSearchHistoryAdapter;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.zongyou.library.util.ToastUtils;

public class BbsSearchActivity extends BaseActivity implements BaseActivity.TitleListener {
    private BbsSearchHistoryAdapter mListAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_search);
        setPageTag(TagManager.A_BBS_SEARCH);
        setTitleListener(this);
        mListView = mViewFinder.find(R.id.lv_list);
        mListAdapter = new BbsSearchHistoryAdapter(getActivity(), mListView);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BbsListActivity.startSearch(getActivity(), mListAdapter.getItem(position));
            }
        });
        mViewFinder.onClick(R.id.iv_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = mViewFinder.find(R.id.et_content);
                String text = et.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    ToastUtils.show(getActivity(), R.string.bbs_search_empty);
                    return;
                }
                BbsListActivity.startSearch(getActivity(), text);
                et.setText("");
                mListAdapter.addItem(text);
            }
        });

    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.search);
    }
}
