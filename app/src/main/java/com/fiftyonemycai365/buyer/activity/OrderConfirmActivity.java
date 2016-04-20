package com.fiftyonemycai365.buyer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.model.Promotion;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.adapter.OrderGoodGridAdapter;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.CartGoodsInfo;
import com.fanwe.seallibrary.model.CartSellerInfo;
import com.fanwe.seallibrary.model.InitInfo;
import com.fanwe.seallibrary.model.OrderCompute;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.event.OrderEvent;
import com.fanwe.seallibrary.model.req.CreateOrderRequest;
import com.fanwe.seallibrary.model.result.OrderComputeResult;
import com.fanwe.seallibrary.model.result.OrderResult;
import com.fiftyonemycai365.buyer.action.PromotionAction;
import com.fiftyonemycai365.buyer.comm.ShoppingCartMgr;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.NumFormat;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 订单确认ACtivity
 */
public class OrderConfirmActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    private final int LOC_REQUEST_CODE = 0x401;
    private final int COUPON_REQUEST_CODE = 0x402;
    private ArrayList<CartGoodsInfo> mCartGoodsInfoArrayList;
    private UserAddressInfo mUserAddressInfo;
    private CartSellerInfo mCartSellerInfo;
    private OrderGoodGridAdapter mOrderGoodGridAdapter;
    private OrderCompute mOrderCompute;
    private Promotion mPromotion, mPromotionTmp;
    private PromotionAction mAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        setTitleListener(this);
        setPageTag(TagManager.ORDER_CONFIRM_ACTIVITY);

        mCartGoodsInfoArrayList = (ArrayList<CartGoodsInfo>) this.getIntent().getSerializableExtra(Constants.EXTRA_DATA);
        mUserAddressInfo = (UserAddressInfo) this.getIntent().getSerializableExtra(Constants.EXTRA_ADDR);
        mCartSellerInfo = (CartSellerInfo) this.getIntent().getSerializableExtra(Constants.EXTRA_SELLER);
        if (mUserAddressInfo == null) {
            mUserAddressInfo = getDefaultAddress();
        }
        initView();


//        initCoupon(null);

//        loadFirstCoupon();
        orderCompute(false);
    }

    private void loadFirstCoupon() {
        mAction = new PromotionAction(this);
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.first(new ApiCallback<Promotion>() {
            @Override
            public void onSuccess(Promotion data) {
                mPromotionTmp = data;
                orderCompute(true);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                orderCompute(true);
            }
        });
    }

    private UserAddressInfo getDefaultAddress() {
        try {
            UserInfo userInfo = PreferenceUtils.getObject(this, UserInfo.class);
            if (ArraysUtils.isEmpty(userInfo.address)) {
                return null;
            } else {
                for (UserAddressInfo addressInfo : userInfo.address) {
                    if (addressInfo.isDefault) {
                        return addressInfo;
                    }
                }
                return userInfo.address.get(0);
            }
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_order_verify);
    }

    private void initAddrView() {
        if (mUserAddressInfo != null) {
            mViewFinder.setText(R.id.tv_user_name, mUserAddressInfo.name);
            mViewFinder.setText(R.id.tv_user_tel, mUserAddressInfo.mobile);
            mViewFinder.setText(R.id.tv_addr, mUserAddressInfo.address);
            mViewFinder.find(R.id.ll_container).setVisibility(View.VISIBLE);
            mViewFinder.find(R.id.tv_notice).setVisibility(View.INVISIBLE);
        } else {
            mViewFinder.find(R.id.ll_container).setVisibility(View.INVISIBLE);
            mViewFinder.find(R.id.tv_notice).setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        initAddrView();
        mViewFinder.onClick(R.id.ll_address_container, this);
        int nums = 0;
        List<String> list = new ArrayList<String>();
        for (CartGoodsInfo item : mCartGoodsInfoArrayList) {
            list.add(item.logo);
            nums += item.num;
        }
        mViewFinder.setText(R.id.tv_goods_num, "共" + nums + "件");
        mOrderGoodGridAdapter = new OrderGoodGridAdapter(this, list);
        GridView gv = mViewFinder.find(R.id.gv_list);
        gv.setAdapter(mOrderGoodGridAdapter);

        mViewFinder.onClick(R.id.tv_pay, this);
        mViewFinder.onClick(R.id.ll_deliver_time, this);

        if (mCartSellerInfo.type == 2) {
            mViewFinder.find(R.id.tv_deliver_title).setVisibility(View.GONE);
            mViewFinder.find(R.id.ll_deliver_container).setVisibility(View.GONE);
            mViewFinder.setText(R.id.tv_deliver_time_head, R.string.order_d_service_time);

            if (!ArraysUtils.isEmpty(mCartGoodsInfoArrayList) && !TextUtils.isEmpty(mCartGoodsInfoArrayList.get(0).serviceTime)) {
                mViewFinder.setText(R.id.tv_deliver_time, mCartGoodsInfoArrayList.get(0).serviceTime);
            }
        }

        RadioButton rb = mViewFinder.find(R.id.rb_online_pay);
        rb.setChecked(true);

        InitInfo info = PreferenceUtils.getObject(getActivity(), InitInfo.class);
        if (info != null) {
            mViewFinder.setText(R.id.tv_order_pass_hint, getString(R.string.msg_plase_complete_pay, info.systemOrderPass));
        }

        mViewFinder.onClick(R.id.ll_coupon, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay:
                buyGoods();
                break;
            case R.id.ll_deliver_time:
                selDeliverTime();
                break;
            case R.id.ll_address_container:
                Intent intent = new Intent(this, SwitchAddressActivity.class);
                intent.putExtra("isLocate", "false");
                startActivityForResult(intent, LOC_REQUEST_CODE);
                break;
            case R.id.ll_coupon:
                CouponUseActivity.start(this, COUPON_REQUEST_CODE, mOrderCompute.sellerId, mOrderCompute.totalMoney);
                break;
        }
    }

    private void buyGoods() {
        if (mOrderCompute == null) {
            return;
        }
        if (mUserAddressInfo == null) {
            ToastUtils.show(this, R.string.home_notice_sel_location);
            return;
        }

        RadioButton rb = mViewFinder.find(R.id.rb_delay_pay);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.addressId = mUserAddressInfo.id;
        createOrderRequest.appTime = ((TextView) mViewFinder.find(R.id.tv_deliver_time)).getText().toString();
        createOrderRequest.giftContent = ((EditText) mViewFinder.find(R.id.et_greeting)).getText().toString();
        createOrderRequest.buyRemark = ((EditText) mViewFinder.find(R.id.et_remark)).getText().toString();
        createOrderRequest.invoiceTitle = ((EditText) mViewFinder.find(R.id.et_invoice)).getText().toString();
        createOrderRequest.payment = rb.isChecked() ? 0 : 1;
        if (mPromotion != null) {
            createOrderRequest.promotionSnId = String.valueOf(mPromotion.id);
        }
        List<Integer> list = new ArrayList<Integer>();
        for (CartGoodsInfo item : mCartGoodsInfoArrayList) {
            list.add(item.id);
        }
        createOrderRequest.cartIds = list;
        if (TextUtils.isEmpty(createOrderRequest.appTime)) {
            if (mCartSellerInfo.type == 1) {
                ToastUtils.show(this, R.string.select_dispatching_time);
            } else {
                ToastUtils.show(this, R.string.select_server_time);
            }
            return;
        }
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.loading_err_net);
            return;
        }
        CustomDialogFragment.show(this.getSupportFragmentManager(), R.string.loading, this.getLocalClassName());
        ApiUtils.post(this, URLConstants.ORDER_CREATE,
                createOrderRequest,
                OrderResult.class, new Response.Listener<OrderResult>() {

                    @Override
                    public void onResponse(final OrderResult response) {
                        if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                            if (response.data != null) {
                                Intent intent = new Intent(OrderConfirmActivity.this, OrderDetailActivity.class);
                                intent.putExtra(Constants.EXTRA_DATA, response.data.id);

                                ShoppingCartMgr.getInstance().loadCart(getApplicationContext());
                                EventBus.getDefault().post(new OrderEvent(null));
                                intent.putExtra(Constants.EXTRA_AUTO, response.data.isCanPay);
                                startActivity(intent);
                                finish();
                            }
                        }

                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(getApplicationContext(), R.string.loading_err_nor);
                        CustomDialogFragment.dismissDialog();
                    }
                });
    }


    private void selDeliverTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_date_time, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
        builder.setView(view);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis() + 15 * 60 * 1000);
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));


        builder.setTitle(R.string.select_dispatching_time);
        builder.setPositiveButton(R.string.sure_plus_null, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuffer sb = new StringBuffer();
                sb.append(String.format(getResources().getString(R.string.time_format),
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                sb.append(" ");
                sb.append(timePicker.getCurrentHour())
                        .append(":").append(timePicker.getCurrentMinute()).append(":00");

                SimpleDateFormat format = new SimpleDateFormat(getResources().getString(R.string.time_format_2));
                try {
                    Date date = format.parse(sb.toString());
                    Date nowDate = new Date();
                    if (date.compareTo(nowDate) == 1) {
                        mViewFinder.setText(R.id.tv_deliver_time, sb.toString());
                        dialog.cancel();
                    } else {
                        ToastUtils.show(OrderConfirmActivity.this, R.string.msg_error_vaild_time);
                    }

                } catch (Exception e) {

                }

            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case LOC_REQUEST_CODE:
                if (data != null) {
                    UserAddressInfo addressInfo = (UserAddressInfo) data.getSerializableExtra(Constants.EXTRA_DATA);
                    if (addressInfo != null) {
                        mUserAddressInfo = addressInfo;
                        initAddrView();
                    }
                }
                break;
            case COUPON_REQUEST_CODE:
                if (data != null) {
                    mPromotionTmp = data.getParcelableExtra(Constants.EXTRA_DATA);
                } else {
                    mPromotionTmp = null;
                }
                orderCompute(false);
                break;
        }
    }

    private void initCoupon(Promotion promotion) {
        mPromotion = promotion;

        if (mPromotion != null) {
            mViewFinder.setText(R.id.is_have_coppun, "-" + mPromotion.money);
        } else {
            mViewFinder.setText(R.id.is_have_coppun, "");
        }

    }

    private void orderCompute(final boolean first) {
        if (ArraysUtils.isEmpty(mCartGoodsInfoArrayList)) {
            finishActivity();
            return;
        }
        List<Integer> ids = new ArrayList<>();
        for (CartGoodsInfo item : mCartGoodsInfoArrayList) {
            ids.add(item.id);
        }
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.msg_loading, OrderDetailActivity.class.getName());
        HashMap<String, Object> data = new HashMap<>();
        data.put("cartIds", ids);
        if (mPromotionTmp != null) {
            data.put("promotionSnId", mPromotionTmp.id);
        }
        ApiUtils.post(this, URLConstants.ORDER_COMPUTE,
                data,
                OrderComputeResult.class, new Response.Listener<OrderComputeResult>() {
                    @Override
                    public void onResponse(final OrderComputeResult response) {

                        if (O2OUtils.checkResponse(getActivity(), response)) {
                            CustomDialogFragment.dismissDialog();
                            mOrderCompute = response.data;
                            initViewData();
                        } else {
                            if (first && mPromotionTmp != null) {
                                mPromotionTmp = null;
                                orderCompute(false);
                            } else {
                                CustomDialogFragment.dismissDialog();
                                finishActivity();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        CustomDialogFragment.dismissDialog();
                        ToastUtils.show(getActivity(), R.string.msg_error_network);
                        finishActivity();
                    }
                });
    }

    private void initViewData() {
        if (mOrderCompute == null) {
            finishActivity();
            return;
        }
        if (mPromotionTmp != null) {
            initCoupon(mPromotionTmp);
        } else if (mOrderCompute.promotionCount > 0) {
            mViewFinder.setText(R.id.is_have_coppun, "可选择优惠券");
        } else {
            mViewFinder.setText(R.id.is_have_coppun, "无可用优惠券");
        }
        if (mOrderCompute.isCashOnDelivery == 1) {
            mViewFinder.find(R.id.rb_delay_pay).setVisibility(View.VISIBLE);
        } else {
            mViewFinder.find(R.id.rb_delay_pay).setVisibility(View.GONE);
        }

        mViewFinder.setText(R.id.tv_goods_price, String.format(getResources().getString(R.string.RMB_sign), NumFormat.formatPrice(mOrderCompute.goodsFee)));
        mViewFinder.setText(R.id.tv_carriage, String.format(getResources().getString(R.string.RMB_sign), NumFormat.formatPrice(mOrderCompute.freight)));
        mViewFinder.setText(R.id.tv_total_price, String.format(getResources().getString(R.string.RMB_sign), NumFormat.formatPrice(mOrderCompute.payFee)));
        mViewFinder.setText(R.id.tv_total, String.format(getResources().getString(R.string.RMB_sign), NumFormat.formatPrice(mOrderCompute.payFee)));
//        initCoupon(mPromotionTmp);
    }
}
