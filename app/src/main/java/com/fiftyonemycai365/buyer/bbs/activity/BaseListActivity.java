package com.fiftyonemycai365.buyer.bbs.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.fanwe.seallibrary.comm.Constants;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.widget.LoadingFooter;
import com.fiftyonemycai365.buyer.widget.OnLoadNextListener;
import com.fiftyonemycai365.buyer.widget.PageStaggeredGridView;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-30
 * Time: 17:18
 * 功能:
 */
public abstract class BaseListActivity<T> extends BaseActivity
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

    private boolean mNeedLoadMore = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mSwipeLayout = mViewFinder.find(R.id.sp_container);
        mGridView = mViewFinder.find(R.id.gv_list);
        mSwipeLayout.setOnRefreshListener(this);

        setNeedLoadNext(mNeedLoadMore);
        mCallBackHandler = new CallBackHandler();

        initView();
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

    protected int getLayoutId(){
        return R.layout.activity_base_list;
    }

    protected abstract void initView();
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

        mCurrentPageIndex = 1;

        if (mAdapter == null || mGridView.getAdapter() == null) {
            mAdapter = setAdapter();
            mGridView.setAdapter(mAdapter);
            mGridView.setEmptyView(mViewFinder.find(android.R.id.empty));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                loadData();
            }
        }, 100);
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
    protected void onDestroy() {
        super.onDestroy();
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
            if (ArraysUtils.isEmpty(temp) || Constants.PAGE_SIZE > temp.size()) {
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
