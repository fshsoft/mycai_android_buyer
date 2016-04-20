package com.fiftyonemycai365.buyer.adapter;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.seallibrary.model.BalanceListInfo;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.utils.NumFormat;
import com.fiftyonemycai365.buyer.widget.recycler.AdvancedAdapter;

import java.util.List;


/**
 * Created by tu on 16/2/24.
 * Email:enum@foxmail.com
 */
public class BalanceListAdapter extends AdvancedAdapter {
    public static final String TAG = BalanceListAdapter.class.getName();
    private FragmentActivity mContext;
    private List<BalanceListInfo> mList;
    private Resources mResources;

    public BalanceListAdapter(FragmentActivity context, List<BalanceListInfo> list) {
        this.mContext = context;
        mResources = context.getResources();
        this.mList = list;
    }

    @Override
    public int getAdvanceViewType(int position) {
        return 1;
    }

    @Override
    protected int getAdvanceCount() {
        return mList.size();
    }

    @Override
    protected void onBindAdvanceViewHolder(RecyclerView.ViewHolder holder, int position) {
            final BalanceListInfo bean = mList.get(position);
            BalanceListViewHolder itemViewHolder = (BalanceListViewHolder) holder;
            itemViewHolder.typeTV.setText(bean.payTypeStr);
            if(bean.payType == 1){
                itemViewHolder.amountTV.setText("-" + NumFormat.formatPrice(bean.money));
                itemViewHolder.amountTV.setTextColor(mResources.getColor(R.color.theme_black_text));
            }else{
                itemViewHolder.amountTV.setText("+" + NumFormat.formatPrice(bean.money));
                itemViewHolder.amountTV.setTextColor(mResources.getColor(R.color.theme_price));
            }
            itemViewHolder.balanceTV.setText("余额：" + bean.balance);
            itemViewHolder.snTV.setText("订单号：" + bean.sn);
            itemViewHolder.timeTV.setText(bean.createTime);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateAdvanceViewHolder(ViewGroup parent, int viewType) {
        return new BalanceListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_balance, parent, false));
    }


    public class BalanceListViewHolder extends RecyclerView.ViewHolder {

        private TextView typeTV, timeTV,snTV,balanceTV,amountTV;

        public BalanceListViewHolder(View view) {
            super(view);
            typeTV = (TextView) view.findViewById(R.id.balance_type);
            timeTV = (TextView) view.findViewById(R.id.balance_time);
            snTV = (TextView) view.findViewById(R.id.balance_sn);
            balanceTV = (TextView) view.findViewById(R.id.balance_balance);
            amountTV = (TextView) view.findViewById(R.id.balance_amount);
        }

    }
}
