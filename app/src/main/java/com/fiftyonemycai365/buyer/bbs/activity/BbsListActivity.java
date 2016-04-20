package com.fiftyonemycai365.buyer.bbs.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.seallibrary.model.ForumPlate;
import com.fanwe.seallibrary.model.ForumPosts;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.action.BbsAction;
import com.fiftyonemycai365.buyer.bbs.adapter.BbsTopAdapter;
import com.fiftyonemycai365.buyer.bbs.adapter.ExBbsListAdapter;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class BbsListActivity extends BaseListActivity<List<ForumPosts>> implements BaseActivity.TitleListener, View.OnClickListener {
    protected final static String ARG_PLATE = "plate";
    protected final static String ARG_SEARCH = "search";
    private BbsAction mAction;
    private ForumPlate mForumPlate;
    private String mSearchKeys;
    private ListView mTopListView;
    private BbsTopAdapter mBbsTopAdapter;
    @Override
    protected void initView() {
        setPageTag(TagManager.A_BBS_LIST);
        mForumPlate = getIntent().getParcelableExtra(ARG_PLATE);
        mSearchKeys = getIntent().getStringExtra(ARG_SEARCH);
        setTitleListener_RightImage(this);
        mAction = new BbsAction(getApplicationContext());
        if(isSearch()){
            mViewFinder.find(R.id.iv_add).setVisibility(View.GONE);
        }else {
            mViewFinder.onClick(R.id.iv_add, this);
        }

        initTopHeader();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumPosts posts = (ForumPosts) mAdapter.getItem((int) id);
                ForumDetailActivity.start(getActivity(), posts);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 200);

    }
    private boolean isSearch(){
        return !TextUtils.isEmpty(mSearchKeys);
    }

    private void initTopHeader(){
        if (isSearch()){
            return;
        }

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View header = layoutInflater.inflate(R.layout.bbs_layout_bbs_top_header, null);
        mTopListView = (ListView)header.findViewById(R.id.lv_list);
        mGridView.addHeaderView(header);
        mBbsTopAdapter = new BbsTopAdapter(getActivity(), new ArrayList<ForumPosts>());
        mTopListView.setAdapter(mBbsTopAdapter);
        mTopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumPosts posts = mBbsTopAdapter.getItem(position);
                ForumDetailActivity.start(getActivity(), posts);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bbs_activity_bbs_list;
    }

    @Override
    protected CommonAdapter setAdapter() {
        return new ExBbsListAdapter(getActivity(), new ArrayList<ForumPosts>(), !isSearch());
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<List<ForumPosts>> callback) {
        if(currentPage == 1 ){
            if(mBbsTopAdapter != null) {
                mBbsTopAdapter.clear();
            }
            mAdapter.clear();
        }
        if (!TextUtils.isEmpty(mSearchKeys)){
            mAction.search(mSearchKeys, currentPage, callback);
        } else if(mForumPlate != null){
            mAction.list(mForumPlate.id, 0, callback);
        }else {
            mAction.list(0, 0, callback);
        }
    }

    @Override
    protected List deliveryResult(List<ForumPosts> result) {
        List<ForumPosts> tops = new ArrayList<>();
        if(!ArraysUtils.isEmpty(result) && mBbsTopAdapter != null){
            for (ForumPosts item : result){
                if(item.top == 1){
                    tops.add(item);
                }
            }
            if(!ArraysUtils.isEmpty(tops)){
                mBbsTopAdapter.addAll(tops);
            }
        }
        return result;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        if(!TextUtils.isEmpty(mSearchKeys)) {
            title.setText(mSearchKeys);
        }else if(mForumPlate == null || TextUtils.isEmpty(mForumPlate.name)){
            title.setText(R.string.title_bbs_list);
        }else{
            title.setText(mForumPlate.name);
        }
        if(right instanceof ImageView){
            ((ImageView)right).setImageResource(R.drawable.ic_search);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), BbsSearchActivity.class));
                }
            });
        }
    }

    public static void start(Context context, ForumPlate forumPlate){
        Intent intent = new Intent(context, BbsListActivity.class);
        if(forumPlate != null) {
            intent.putExtra(ARG_PLATE, forumPlate);
        }
        context.startActivity(intent);
    }

    public static void startSearch(Context context, String searchKeys){
        Intent intent = new Intent(context, BbsListActivity.class);
        if(!TextUtils.isEmpty(searchKeys)) {
            intent.putExtra(ARG_SEARCH, searchKeys);
        }
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add:
                BbsAddActivity.start(getActivity(), mForumPlate, null);
                break;
        }
    }
}
