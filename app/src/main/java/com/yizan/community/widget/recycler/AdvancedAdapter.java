package com.yizan.community.widget.recycler;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 可以添加多个Header、Footer的适配器
 * Created by atlas on 15/9/30.
 * Email:atlas.tufei@gmail.com
 */
public abstract class AdvancedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final String TAG = AdvancedAdapter.class.getName();
    private OnItemClickListener mOnItemClickListener;
    private ArrayList<View> mHeaderViews = new ArrayList<>(); //头视图
    private ArrayList<View> mFooterViews = new ArrayList<>();   //尾视图
    private ArrayList<Integer> mHeaderViewTypes = new ArrayList<>();
    private ArrayList<Integer> mFooterViewTypes = new ArrayList<>();

    public void addHeaderView(View headerView) {
        mHeaderViews.add(headerView);
    }

    public void addFooterView(View footerView) {
        mFooterViews.add(footerView);
    }

    @Override
    public int getItemViewType(int position) {

        if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
            //用position作为HeaderView 的   ViewType标记
            //记录每个ViewType标记
            mHeaderViewTypes.add(position * 100000);
            return position * 100000;
        }


        if (mFooterViews.size() > 0 && position > getAdvanceCount() - 1 + mHeaderViews.size()) {
            //用position作为FooterView 的   ViewType标记
            //记录每个ViewType标记
            mFooterViewTypes.add(position * 100000);
            return position * 100000;
        }

        if (mHeaderViews.size() > 0) {
            return getAdvanceViewType(position - mHeaderViews.size());
        }


        return getAdvanceViewType(position);
    }

    public abstract int getAdvanceViewType(int position);

    /**
     * !! 不能为0！！！
     *
     * @return
     */
    protected abstract int getAdvanceCount();

    protected abstract void onBindAdvanceViewHolder(RecyclerView.ViewHolder holder, int i);

    protected abstract RecyclerView.ViewHolder onCreateAdvanceViewHolder(ViewGroup parent, int viewType);

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getFooterCount() {
        return mFooterViews.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViewTypes.contains(viewType)) {
            return new HeaderHolder(mHeaderViews.get(viewType / 100000));
        }

        if (mFooterViewTypes.contains(viewType)) {
            int index = viewType / 100000 - getAdvanceCount() - mHeaderViews.size();
            return new FooterHolder(mFooterViews.get(index));
        }

        return onCreateAdvanceViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mFooterViews.size() > 0 && (position > getAdvanceCount() - 1 + mHeaderViews.size())) {
            return;
        }


        if (mHeaderViews.size() > 0) {
            if (position < mHeaderViews.size()) {
                return;
            }
            onBindAdvanceViewHolder(holder, position - mHeaderViews.size());
            return;
        }
        if (null != mOnItemClickListener && null != holder.itemView)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        onBindAdvanceViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        if (mHeaderViews.size() > 0 && mFooterViews.size() > 0) {
            return getAdvanceCount() + mHeaderViews.size() + mFooterViews.size();
        }
        if (mHeaderViews.size() > 0) {
            return getAdvanceCount() + mHeaderViews.size();
        }
        if (mFooterViews.size() > 0) {
            return getAdvanceCount() + mFooterViews.size();
        }

        return getAdvanceCount();
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 设置监听方法
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    /**
     * 内部接口回调方法
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}