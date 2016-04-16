package com.yizan.community.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.yizan.community.R;
import com.fanwe.seallibrary.model.CartGoodsInfo;
import com.fanwe.seallibrary.model.CartSellerInfo;
import com.yizan.community.activity.SellerDetailActivity;
import com.yizan.community.activity.SellerGoodsActivity;
import com.yizan.community.activity.SellerServicesActivity;
import com.yizan.community.comm.ShoppingCartMgr;
import com.yizan.community.utils.NumFormat;
import com.yizan.community.widget.PageStaggeredGridView;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

public class ShoppingCartListAdapter extends CommonAdapter<CartSellerInfo> {
    private FragmentActivity mFragmentActivity;
    public ICartListener mICartListener;
    private Intent intent;
    public PageStaggeredGridView gridView;
    private ViewHolder holder;
    public ShoppingCartListAdapter(FragmentActivity context, List<CartSellerInfo> datas,PageStaggeredGridView grid) {
        super(context, datas, R.layout.item_shopping_cart);
        mFragmentActivity = context;
        gridView = grid;
    }

    public void setICartListener(ICartListener listener){
        mICartListener = listener;
    }
    public void setList(List<CartSellerInfo> list) {
        mDatas.clear();
        if(ArraysUtils.isEmpty(list)){
            notifyDataSetChanged();
        }else {
            addAll(list);
        }
    }

    public void updateCartItem(int goodsId, int normsId, int num){
        if(getCount() <= 0){
            return;
        }
        for (CartSellerInfo item: mDatas){
            if(ArraysUtils.isEmpty(item.goods)){
                continue;
            }
            for (CartGoodsInfo goodsInfo : item.goods){
                if(goodsInfo.goodsId != goodsId || goodsInfo.normsId != normsId){
                    continue;
                }
                item.price = ShoppingCartMgr.getInstance().getTotalPrice(item.id);
                goodsInfo.num = num;
                if(num == 0){
                    item.goods.remove(goodsInfo);
                    if(ArraysUtils.isEmpty(item.goods)){
                        mDatas.remove(item);
                    }
                    notifyDataSetChanged();
                }
                updateItem(item.id, item.price, item.serviceFee);
                return;
            }

        }

    }

    public void selectAll(){
        if(getCount() <= 0){
            return;
        }
        for (CartSellerInfo item: mDatas){
            item.isChecked = true;
            for (CartGoodsInfo goodsInfo : item.goods){
                goodsInfo.isChecked = true;
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public void convert(final ViewHolder helper, final CartSellerInfo item, int position) {
        holder = helper;
        helper.setText(R.id.tv_name, item.name);
        helper.setText(R.id.tv_price, "¥" + NumFormat.formatPrice(item.price));
        TextView tvPrice = helper.getView(R.id.tv_price);
        tvPrice.setTag(String.valueOf(item.id));
        final ListView lv = helper.getView(R.id.lv_list);
        CartGoodsListAdapter adapter = new CartGoodsListAdapter(mFragmentActivity, item, this);
        lv.setAdapter(adapter);

        final View btnBuy = helper.getView(R.id.btn_buy);
        final CheckBox cb = helper.getView(R.id.cb_sel);
        cb.setOnCheckedChangeListener(null);
        cb.setChecked(item.isChecked);
        cb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                item.isChecked = !item.isChecked;
                cb.setChecked(item.isChecked);
                CartGoodsListAdapter adapter = (CartGoodsListAdapter) lv.getAdapter();
                if (adapter != null) {
                    adapter.setAllChecked(item.isChecked);
                }
                btnBuy.setEnabled(item.isChecked);
            }
        });

        TextView tv = helper.getView(R.id.tv_name);
        tv.setFocusable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.countGoods > 0 && item.countService <= 0) {
                    intent = new Intent(mContext, SellerGoodsActivity.class);
                } else if (item.countGoods <= 0 && item.countService > 0) {
                    intent = new Intent(mContext, SellerServicesActivity.class);
                } else {
                    intent = new Intent(mContext, SellerDetailActivity.class);
                }
                intent.putExtra(Constants.EXTRA_DATA, item.id);
                mFragmentActivity.startActivity(intent);
            }
        });

        btnBuy.setEnabled(adapter.haveOneChecked());
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mICartListener != null) {
                    mICartListener.onBuyClick(item);
                }
            }
        });
    }


    public interface ICartListener {
        public void onBuyClick(CartSellerInfo info);
    }

    public void updateItem(int id,double pricess,double serviceFeess){
        for (int i= 0; i <= gridView.getCount(); i++){
            View view = gridView.getChildAt(i);
            if(view == null || view.getTag() == null){
                continue;
            }
            ViewHolder holder = (ViewHolder)view.getTag();
            TextView tv_price = holder.getView(R.id.tv_price);
            Button btn_buy = holder.getView(R.id.btn_buy);
            if (serviceFeess > pricess){
                btn_buy.setEnabled(false);
                btn_buy.setText(String.format(mContext.getResources().getString(R.string.order_car_base_servicefee), NumFormat.formatPrice(serviceFeess - pricess)));
            }else{
                btn_buy.setEnabled(true);
                btn_buy.setText(mContext.getResources().getString(R.string.order_car_balance));
            }
            if(tv_price != null && tv_price.getTag() != null){
                int sellerId = Integer.parseInt(tv_price.getTag().toString());
                if(sellerId == id) {
                    tv_price.setText("¥" + NumFormat.formatPrice(pricess));
                    break;
                }
            }

        }

    }
}
