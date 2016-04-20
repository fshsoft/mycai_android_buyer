package com.fiftyonemycai365.buyer.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.PaymentInfo;
import com.fiftyonemycai365.buyer.R;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.DoubleUtil;
import com.zongyou.library.util.NumberUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.List;

/**
 * 支付方式ListAdapter
 */
public class PayWayListAdapter extends BaseAdapter {
    private boolean hasBalancePay = false;
    private boolean mUserOption = false;
    private boolean blancePay = true;
    private List<PaymentInfo> mDatas;
    private Context mContext;
    private double payMoney, balance;

    public PayWayListAdapter(Context context, List<PaymentInfo> datas, double payMoney, double balance) {
        mContext = context;
        mDatas = datas;
        this.payMoney = payMoney;
        if (!ArraysUtils.isEmpty(mDatas)) {
            for (PaymentInfo item : mDatas) {
                if (item.isDefault == 1) {
//                    item.bSel = true;
                }
                if (Constants.PAY_TYPE_BALANCE.equals(item.code))
                    hasBalancePay = true;
            }
        }
        this.balance = balance;

    }

    public void addFirst(PaymentInfo paymentInfo){
        if(mDatas != null){
            mDatas.add(0, paymentInfo);
        }
    }
    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
        if (balance > 0)
            notifyDataSetChanged();
    }

    public String getPayMoney() {
        return NumberUtils.formatDecimal2(DoubleUtil.sub(payMoney, balance));
    }

    @Override
    public int getViewTypeCount() {
        return hasBalancePay ? 2 : 1;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public PaymentInfo getItem(int position) {
        return mDatas.get(position);
    }

    public boolean isBalancePay() {
        return blancePay;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder helper;
        final PaymentInfo item = getItem(position);
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                helper = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_pay_way_balance, null);
                helper.mBalanceSwitch = (Button) convertView.findViewById(R.id.balance_switch);
                helper.mImage = (NetworkImageView) convertView.findViewById(R.id.iv_image);
                helper.mName = (TextView) convertView.findViewById(R.id.tv_name);
                helper.mBalanceAmount = (TextView) convertView.findViewById(R.id.pay_balance_amount);
                helper.mDivider = convertView.findViewById(R.id.line_sep);
                convertView.setTag(R.id.type_balance, helper);
            } else
                helper = (ViewHolder) convertView.getTag(R.id.type_balance);
        } else {
            if (convertView == null) {
                helper = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_pay_way, null);
                helper.mCheckbox = (ImageView) convertView.findViewById(R.id.iv_sel);
                helper.mImage = (NetworkImageView) convertView.findViewById(R.id.iv_image);
                helper.mName = (TextView) convertView.findViewById(R.id.tv_name);
                helper.mDivider = convertView.findViewById(R.id.line_sep);
                convertView.setTag(R.id.type_online, helper);
            } else
                helper = (ViewHolder) convertView.getTag(R.id.type_online);
        }
        helper.mImage.setDefaultImageResId(R.drawable.ic_default_square);
        helper.mImage.setErrorImageResId(R.drawable.ic_default_square);
        helper.mImage.setImageUrl(item.icon, RequestManager.getImageLoader());

        if (getItemViewType(position) == 0) {
            if (balance <= 0) {
                helper.mName.setText(item.name);
            } else {
                helper.mName.setText(mContext.getString(R.string.balance_pay_b, balance));
            }
            if (mUserOption) {
                if (blancePay) {
                    if (payMoney > balance) {
                        final String m = NumberUtils.formatDecimal2(DoubleUtil.sub(payMoney, balance));
                        SpannableString spanText = new SpannableString(mContext.getString(R.string.balance_pay_need_online, m));
                        spanText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.theme_price)), spanText.length() - m.length() - 1, spanText.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        helper.mBalanceAmount.setText(spanText);
                        helper.mBalanceAmount.setVisibility(View.VISIBLE);
                    } else {
                        helper.mBalanceAmount.setVisibility(View.GONE);
                    }
                }else{
                    helper.mBalanceAmount.setVisibility(View.GONE);
                }
            } else {
                blancePay = balance > 0;
                updateBalancePay(helper.mBalanceSwitch);
                if (blancePay) {
                    if (payMoney > balance) {
                        final String m = NumberUtils.formatDecimal2(DoubleUtil.sub(payMoney, balance));
                        SpannableString spanText = new SpannableString(mContext.getString(R.string.balance_pay_need_online, m));
                        spanText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.theme_price)), spanText.length() - m.length() - 1, spanText.length(),
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        helper.mBalanceAmount.setText(spanText);
                        helper.mBalanceAmount.setVisibility(View.VISIBLE);
                    } else {
                        helper.mBalanceAmount.setVisibility(View.GONE);
                    }
                }
            }
            helper.mBalanceSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUserOption = true;
                    blancePay = !blancePay;
                    updateBalancePay(v);
                }
            });
        } else {
            helper.mName.setText(item.name);
            if (item.bSel) {
                helper.mCheckbox.setImageResource(R.drawable.ic_checked_on);
            } else {
                helper.mCheckbox.setImageResource(R.drawable.ic_checked_off);
            }
        }
        if (position == getCount() - 1) {
            helper.mDivider.setVisibility(View.INVISIBLE);
        } else {
            helper.mDivider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void updateBalancePay(View balanceSwitch) {
        balanceSwitch.setBackgroundResource(blancePay ? R.drawable.btn_on : R.drawable.btn_off);
        if(blancePay && balance >= payMoney){
            for (PaymentInfo item: mDatas){
                item.bSel = false;
            }
//            notifyDataSetChanged();
        }
        notifyDataSetChanged();
//        if (!blancePay) {
//            notifyDataSetChanged();
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.PAY_TYPE_BALANCE.equalsIgnoreCase(getItem(position).code) ? 0 : 1;
    }

    public void setList(List<PaymentInfo> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void setSelect(String payment) {
        if (TextUtils.isEmpty(payment)) {
            return;
        }
        int index = -1;
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).code.compareTo(payment) == 0) {
                index = i;
                break;
            }
        }
        selectItem(index);
    }

    public void selectItem(int index) {
        if (index >= getCount()) {
            return;
        }
        for (PaymentInfo item : mDatas) {
            item.bSel = false;
        }
        mDatas.get(index).bSel = true;
        notifyDataSetChanged();
    }

    public PaymentInfo getSelItem() {
        for (PaymentInfo item : mDatas) {
            if (item.bSel) {
                return item;
            }
        }
        return null;
    }

    private class ViewHolder {
        private NetworkImageView mImage;
        private ImageView mCheckbox;
        private Button mBalanceSwitch;
        private TextView mName, mBalanceAmount;
        private View mDivider;
    }

}
