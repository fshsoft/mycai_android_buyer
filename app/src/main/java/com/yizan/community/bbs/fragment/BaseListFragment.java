package com.yizan.community.bbs.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.yizan.community.R;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.widget.LoadingFooter;
import com.yizan.community.widget.OnLoadNextListener;
import com.yizan.community.widget.PageStaggeredGridView;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.List;

/**
 * 列表fragment基类
 * user: WY825
 * create time: 2015/12/10.
 */
public abstract class BaseListFragment<T> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, OnLoadNextListener {

    /**
     * 是否正在刷新 true:正在执行刷新操作，false :没有执行
     */
    private boolean mIsRefreshing = false;

    /**
     * 是否应该遗弃返回的数据
     */
    private boolean mDiscardData = false;

    /**
     * 当前第几页
     */
    protected int mCurrentPageIndex = 1;

    protected CommonAdapter mAdapter;
    protected PageStaggeredGridView mGridView;

    protected SwipeRefreshLayout mSwipeLayout;
    private CallBackHandler mCallBackHandler;

    protected ViewGroup mContent;

    private boolean mNeedLoadMore = true;

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.fragment_base_list, viewGroup, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContent = (ViewGroup) view;
    }

    @Override
    protected void initView() {

        mSwipeLayout = mViewFinder.find(R.id.sp_container);
        mGridView = mViewFinder.find(R.id.gv_list);
        mSwipeLayout.setOnRefreshListener(this);

        setNeedLoadNext(mNeedLoadMore);
        mCallBackHandler = new CallBackHandler();
    }

    protected void setNeedLoadNext(boolean need){
        mNeedLoadMore = need;
        if(mGridView == null){
            return;
        }
        if(mNeedLoadMore){
            mGridView.setLoadNextListener(this);
        }else{
            mGridView.setLoadNextListener(null);
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    private void setRefreshing(boolean refreshing) {
        mIsRefreshing = refreshing;
        Log.e("setRefreshing()", "" + refreshing);
        mSwipeLayout.setRefreshing(refreshing);
    }

    /**
     * 加载数据
     */
    protected void initData() {
        setRefreshing(true);
        mDiscardData = false;
        mCurrentPageIndex = 1;

        if (mAdapter == null || mGridView.getAdapter() == null) {
            mAdapter = setAdapter();
            mGridView.setAdapter(mAdapter);
            mGridView.setEmptyView(mViewFinder.find(android.R.id.empty));
        }

        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        mGridView.setState(LoadingFooter.State.Loading, 100);
        //请求数据
        requestData(mCurrentPageIndex, mCallBackHandler);
    }

    @Override
    public void onLoadNext() {
        ++mCurrentPageIndex;
        loadData();

        //设置PageStaggeredGridView的状态
        mGridView.setState(LoadingFooter.State.Loading, 100);
    }

    /**
     * 添加顶部view
     *
     * @param topView 需要添加的view
     */
    protected void addTopView(View topView) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        topView.measure(w, h);
        int height = topView.getMeasuredHeight();
        addTopView(topView, height);
    }

    protected void addTopView(View topView, int viewHeight) {


        RelativeLayout.LayoutParams headViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, viewHeight);
        RelativeLayout.LayoutParams mRefreshLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mRefreshLp.addRule(RelativeLayout.BELOW, topView.getId());
        mSwipeLayout.setLayoutParams(mRefreshLp);
        topView.setLayoutParams(headViewLp);
        mContent.addView(topView);
    }

    /**
     * 设置adapter
     */
    protected abstract CommonAdapter setAdapter();

    /**
     * 请求数据
     *
     * @param currentPage 请求的页数
     */
    protected abstract void requestData(int currentPage, ApiCallback<T> callback);


    protected <X extends CommonAdapter> X getAdapter() {
        return (X) mAdapter;
    }

    /**
     * 结果交付
     *
     * @param result 结果
     * @return 处理后的数据集合
     */
    protected abstract List deliveryResult(T result);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDiscardData = true;
    }

    class CallBackHandler implements ApiCallback<T> {

        @Override
        public void onSuccess(T data) {

            //判读是否不再处理返回数据
            if (mDiscardData) {
                return;
            }
            if (mIsRefreshing) {
                setRefreshing(false);
                mAdapter.clear();
            }

            List temp = deliveryResult(data);

            if (!ArraysUtils.isEmpty(temp)) {
                mAdapter.addAll(temp);
            }

            //判断是否拥有更多数据
            if (ArraysUtils.isEmpty(temp) || 20 > temp.size()) {
                mGridView.setState(LoadingFooter.State.TheEnd, 1000);
            } else {
                mGridView.setState(LoadingFooter.State.Idle, 1000);
            }
        }

        @Override
        public void onFailure(int errorCode, String message) {
            if (mIsRefreshing) {
                setRefreshing(false);
            }
            ToastUtils.show(getActivity(), message);
            mGridView.setState(LoadingFooter.State.Idle, 1000);
        }
    }

}
