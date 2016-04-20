package com.fiftyonemycai365.buyer.bbs.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.fanwe.seallibrary.model.ForumPosts;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.action.BbsAction;
import com.fiftyonemycai365.buyer.bbs.activity.BbsAddActivity;
import com.fiftyonemycai365.buyer.bbs.activity.ForumDetailActivity;
import com.fiftyonemycai365.buyer.bbs.adapter.BbsListAdapter;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserForumsFragment extends BaseListFragment<List<ForumPosts>> implements BbsListAdapter.OnLongClick {
    private static final String ARG_TYPE = "type";
    private BbsAction mAction;
    private int mType = 1;
    private boolean mHasLoadedOnce = false;

    public static UserForumsFragment newInstance(int type) {
        UserForumsFragment fragment = new UserForumsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE, 1);
        }
        mAction = new BbsAction(getActivity());
        if(mType == 1){
            setPageTag(TagManager.F_USER_BBS_ADD);
        }else if(mType == 2){
            setPageTag(TagManager.F_USER_BBS_REPLY);
        }else if(mType == 3){
            setPageTag(TagManager.F_USER_BBS_PRAISE);
        }else{
            setPageTag(TagManager.F_BBS_LIST);
        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    protected void delForums(final ForumPosts forumPosts){
        if(forumPosts == null){
            return;
        }

        new AlertDialog.Builder(getActivity(), R.style.MyDialog)
                .setTitle("")
                .setMessage("是否删除这条帖子？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomDialogFragment.show(getFragmentManager(), R.string.loading, UserForumsFragment.this.getClass().getName());
                        mAction.forumDel(forumPosts.id, new ApiCallback<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                CustomDialogFragment.dismissDialog();
                                ToastUtils.show(getActivity(), "删除成功");
                                ((BbsListAdapter)mAdapter).removeById(forumPosts.id);
                            }

                            @Override
                            public void onFailure(int errorCode, String message) {
                                CustomDialogFragment.dismissDialog();
                                ToastUtils.show(getActivity(), message);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

    @Override
    protected CommonAdapter setAdapter() {
        return new BbsListAdapter(getActivity(), new ArrayList<ForumPosts>(), this);
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<List<ForumPosts>> callback) {
        mAction.userForumList(mType, currentPage, callback);
    }

    @Override
    protected List deliveryResult(List<ForumPosts> result) {
        return result;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (this.isVisible()) {

            if (isVisibleToUser) {
                if (!mHasLoadedOnce) {
                    mHasLoadedOnce = true;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            initData();
                        }
                    }, 200);
                }
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (mType == 1) {
            setUserVisibleHint(true);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLongClickListener(View v, final ForumPosts forumPosts) {
        new AlertDialog.Builder(getActivity(), R.style.MyDialog)
                .setTitle("编辑")
                .setItems(new String[]{"编辑帖子", "删除帖子"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            BbsAddActivity.start(getActivity(), null, forumPosts);
                        }else if(which == 1){
                            delForums(forumPosts);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public void onClickListener(View v, ForumPosts forumPosts) {
        ForumDetailActivity.start(getActivity(), forumPosts);
    }
}
