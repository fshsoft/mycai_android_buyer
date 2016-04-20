package com.fiftyonemycai365.buyer.bbs.fragment;


import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.fanwe.seallibrary.ForumIndex;
import com.fanwe.seallibrary.model.ForumPlate;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.fiftyonemycai365.buyer.wy.activity.PropertyActivity;
import com.fiftyonemycai365.buyer.action.BbsAction;
import com.fiftyonemycai365.buyer.bbs.activity.BbsListActivity;
import com.fiftyonemycai365.buyer.bbs.activity.BbsMsgActivity;
import com.fiftyonemycai365.buyer.bbs.activity.ForumDetailActivity;
import com.fiftyonemycai365.buyer.bbs.activity.PlateListActivity;
import com.fiftyonemycai365.buyer.bbs.activity.UserForumActivity;
import com.fiftyonemycai365.buyer.bbs.adapter.BbsListAdapter;
import com.fiftyonemycai365.buyer.bbs.adapter.PlateGridAdapter;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fanwe.seallibrary.model.ForumPosts;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 生活圈
 */
public class BbsFragment extends BaseListFragment<ForumIndex> implements View.OnClickListener {
    private BbsAction mBbsAction;
    private GridView mGvPlate;
    private TextView mTvMyForum, mTvMyMsg;
    private PlateGridAdapter mPlateGridAdapter;

    @Override
    protected void initView() {
        setPageTag(TagManager.F_BBS_HOME);
        setNeedLoadNext(false);
        super.initView();
        mBbsAction = new BbsAction(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View header = layoutInflater.inflate(R.layout.bbs_layout_bbs_home_header, null);
        mGvPlate = (GridView)header.findViewById(R.id.gv_plate);
        mTvMyForum = (TextView)header.findViewById(R.id.tv_my_forum);
        mTvMyMsg = (TextView)header.findViewById(R.id.tv_my_msg);
        mTvMyMsg.setOnClickListener(this);
        mTvMyForum.setOnClickListener(this);
        mGridView.addHeaderView(header);

        mGvPlate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumPlate plate = mPlateGridAdapter.getItem(position);
                if(plate.id == 0){
                    PlateListActivity.start(getActivity(), 0);
                }else if(plate.id == 1){
                    PropertyActivity.start(getActivity(), null);
                }
                else {
                    BbsListActivity.start(getActivity(), plate);
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumDetailActivity.start(getActivity(), (ForumPosts)mAdapter.getItem((int)id));
            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//               initData();
//            }
//        },100);
    }


    protected void initViewData(ForumIndex forumIndex){
        if(forumIndex == null){
            return;
        }
        mTvMyForum.setText(getString(R.string.my_forums) + "(" + forumIndex.postsnum + ")");
        mTvMyMsg.setText(getString(R.string.my_forum_msg) + "(" + forumIndex.messagenum + ")");

        if(ArraysUtils.isEmpty(forumIndex.plates)){
            mGvPlate.setVisibility(View.GONE);
        }else{
            mGvPlate.setVisibility(View.VISIBLE);
            if(mPlateGridAdapter == null){
                mPlateGridAdapter = new PlateGridAdapter(getActivity(), forumIndex.plates);
                mGvPlate.setAdapter(mPlateGridAdapter);
            }else{
                mPlateGridAdapter.setList(forumIndex.plates);
            }
        }
    }

    @Override
    protected CommonAdapter setAdapter() {
        return new BbsListAdapter(getActivity(), new ArrayList<ForumPosts>());
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<ForumIndex> callback) {
        mBbsAction.index(callback);
    }

    @Override
    protected List deliveryResult(ForumIndex result) {
        initViewData(result);
        return result.posts;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_my_forum:
                if(!O2OUtils.turnLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), UserForumActivity.class));
                }else{
                    ToastUtils.show(getActivity(), R.string.msg_not_login);
                }
                break;
            case R.id.tv_my_msg:
                if(!O2OUtils.turnLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), BbsMsgActivity.class));
                }else{
                    ToastUtils.show(getActivity(), R.string.msg_not_login);
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                mGridView.setEmptyView(null);
                mViewFinder.find(android.R.id.empty).setVisibility(View.GONE);
            }
        }, 100);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mSwipeLayout != null){
            mSwipeLayout.setRefreshing(false);
        }
    }
}
