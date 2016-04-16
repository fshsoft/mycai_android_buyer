package com.yizan.community.widget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * RecyclerView,支持emptyview
 * Created by atlas on 15/10/9.
 * Email:atlas.tufei@gmail.com
 */
public class AdvancedRecyclerView extends RecyclerView {
    /**
     * View to show if there are no items to show.
     */
    private View mEmptyView;
    private AdapterDataObserver mEmptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Adapter adapter = getAdapter();
            if (mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    AdvancedRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    AdvancedRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public AdvancedRecyclerView(Context context) {
        super(context);
    }

    public AdvancedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * When the current adapter is empty, the AdapterView can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    private static Adapter mAdapter;

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
        if (null != adapter) {
            adapter.registerAdapterDataObserver(mEmptyObserver);
        }
        mEmptyObserver.onChanged();
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 去除header
         * @return
         */
        public final int getItemPosition() {
            if (null != mAdapter) {
                if(mAdapter instanceof AdvancedAdapter){
                    AdvancedAdapter adapter = (AdvancedAdapter) mAdapter;
                    final int headerCount = adapter.getHeaderCount();
                    if (headerCount > 0)
                        return getAdapterPosition() - headerCount;
                }
                return getAdapterPosition();
            }
            return NO_POSITION;
        }
    }

}
