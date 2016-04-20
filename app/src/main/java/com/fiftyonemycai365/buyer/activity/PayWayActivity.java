package com.fiftyonemycai365.buyer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.InitInfo;
import com.fanwe.seallibrary.model.OrderInfo;
import com.fanwe.seallibrary.model.PaymentInfo;
import com.fanwe.seallibrary.model.req.WeixinPayRequest;
import com.fanwe.seallibrary.model.result.AliPayResult;
import com.fanwe.seallibrary.model.result.BalanceResult;
import com.fanwe.seallibrary.model.result.PayLogResult;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.fiftyonemycai365.buyer.BuildConfig;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.adapter.PayWayListAdapter;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.NumFormat;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 支付Activity
 */
public class PayWayActivity extends BaseActivity implements View.OnClickListener, BaseActivity.TitleListener {
    private PayWayListAdapter mPayWayListAdapter;
    private OrderInfo mOrderInfo;
    private UpdateBroadCastReceiver mUpdateBroadCastReceiver;
    public final static int REQUEST_CODE = 0x201;
    public final static int REQUEST_CODE_RECHARGE = 0x202;
    private int type;
    private EditText mMoneyEditText;

    private double mCurrBalance = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_way);
        setTitleListener(this);
        Intent intent = getIntent();
        if (intent != null)
            type = intent.getIntExtra(Constants.EXTRA_TYPE, 0);
        int viewstubId = R.id.pay_goods_viewstub;
        if (type == 0) {
            mOrderInfo = (OrderInfo) getIntent().getSerializableExtra(Constants.EXTRA_DATA);
            viewstubId = R.id.pay_goods_viewstub;

        } else
            viewstubId = R.id.pay_recharge_viewstub;
        ViewStub stub = (ViewStub) findViewById(viewstubId);
        stub.inflate();
        mViewFinder.onClick(R.id.btn_pay, this);
        initView();
        setPageTag(TagManager.PAY_WAY_ACTIVITY);
        registerBoradcastReceiver();
    }

    private void initView() {
        if (type == 0) {
            if (mOrderInfo == null) {
                finishActivity();
                return;
            }
            if (!TextUtils.isEmpty(mOrderInfo.shopName)) {
                mViewFinder.setText(R.id.tv_seller, mOrderInfo.shopName);
            } else {
                mViewFinder.setText(R.id.tv_seller, mOrderInfo.sellerName);
            }
            mViewFinder.setText(R.id.tv_total, "¥" + NumFormat.formatPrice(mOrderInfo.payFee - mOrderInfo.payMoney));
        } else {
            mMoneyEditText = mViewFinder.find(R.id.pay_recharge_et);
            mMoneyEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s.toString()))
                        mPayWayListAdapter.setPayMoney(Double.valueOf(s.toString()));
                }
            });
        }
        if(type == 0){
            loadUserBalance();
        }else{
            initPayWayList();
        }
    }

    protected void loadUserBalance(){
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.msg_loading, PayWayActivity.class.getName());
        ApiUtils.post(this, URLConstants.BALANCE_QUERY,
                new HashMap<String, String>(),
                BalanceResult.class, new Response.Listener<BalanceResult>() {
                    @Override
                    public void onResponse(BalanceResult response) {
                        CustomDialogFragment.dismissDialog();
                        if (response != null && response.data != null && !TextUtils.isEmpty(response.data.balance)) {
                            mCurrBalance = Double.valueOf(response.data.balance);
                        }
                        initPayWayList();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomDialogFragment.dismissDialog();
                        ToastUtils.show(PayWayActivity.this, R.string.err_get_balance_pay);
                        initPayWayList();
                    }
                });
    }
    protected void initPayWayList(){
        InitInfo initInfo = PreferenceUtils.getObject(this, InitInfo.class);
        if (null != initInfo && !ArraysUtils.isEmpty(initInfo.payment)) {
            ListView listView = mViewFinder.find(R.id.lv_list);
            List<PaymentInfo> removes = new ArrayList<>();
            PaymentInfo paymentInfo = null;
            for (PaymentInfo item : initInfo.payment) {
                if (Constants.PAY_TYPE_CASH_ON_DELIVERY.equalsIgnoreCase(item.code) || Constants.PAY_TYPE_BALANCE.equalsIgnoreCase(item.code)) {
                    removes.add(item);
                }
                if(type == 0 && Constants.PAY_TYPE_BALANCE.equalsIgnoreCase(item.code)){
                    paymentInfo = item;
                }

            }
            initInfo.payment.removeAll(removes);
            if(paymentInfo != null && mCurrBalance > 0.0f) {
                initInfo.payment.add(0, paymentInfo);
            }
            mPayWayListAdapter = new PayWayListAdapter(this, initInfo.payment, type == 0 ? mOrderInfo.payFee : 0, mCurrBalance);

            listView.setAdapter(mPayWayListAdapter);


            mPayWayListAdapter.selectItem(0);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (type == 0) {
                        if (mCurrBalance >= mOrderInfo.payFee && mPayWayListAdapter.isBalancePay()) {
                            return;
                        }
                    }
                    mPayWayListAdapter.selectItem(position);
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if(mPayWayListAdapter == null){
                    return;
                }
                if (type == 1) {
                    final String money = mMoneyEditText.getText().toString();
                    if (TextUtils.isEmpty(money) || Double.valueOf(money) <= 0) {
                        ToastUtils.show(this, R.string.msg_error_recharge_money);
                        return;
                    }
                }
                PaymentInfo paymentInfo = mPayWayListAdapter.getSelItem();
                if (paymentInfo == null || Constants.PAY_TYPE_BALANCE.equalsIgnoreCase(paymentInfo.code)) {
                    if(type == 1){
                        ToastUtils.show(this, R.string.msg_select_pay_way);
                        return;
                    }else{
                        if(!mPayWayListAdapter.isBalancePay()){
                            ToastUtils.show(this, R.string.msg_select_pay_way);
                            return;
                        }
                        else if(mCurrBalance < mOrderInfo.payFee) {
                            ToastUtils.show(this, R.string.err_balance_not_enough);
                            return;
                        }
                    }

                }

                buyGoods(paymentInfo == null? "" : paymentInfo.code);
                break;
        }
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_pay_way);
    }

    private void buyGoods(final String payWay) {
//        if (TextUtils.isEmpty(payWay)) {
//            ToastUtils.show(this, R.string.msg_select_pay_way);
//            return;
//        }
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }

        if (Constants.PAY_TYPE_WEICHAT.equals(payWay)) {
            IWXAPI iwxapi = WXAPIFactory.createWXAPI(PayWayActivity.this, BuildConfig.WX_APP_ID);
            if (!iwxapi.isWXAppInstalled()) {
                ToastUtils.show(getApplicationContext(), getString(R.string.msg_weixin_un_installed));
                return;
            }
        }
//        else if (Constants.PAY_TYPE_UNION.equals(payWay)) {
//            if (!UPPayAssistEx.checkInstalled(this)) {
//                //当判断用户手机上已安装银联Apk，商户客户端可以做相应个性化处理
//                ToastUtils.show(getApplicationContext(), "未找到银联支付APP");
//                return;
//            }
//        }
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.msg_loading, OrderDetailActivity.class.getName());
        HashMap<String, Object> data = new HashMap<String, Object>(2);
        if (type == 0) {
            data.put("id", String.valueOf(mOrderInfo.id));
            PayExtend payExtend = new PayExtend();
            payExtend.balancePay = mPayWayListAdapter.isBalancePay() ? 1 : 0;
            data.put("extend", payExtend);
        } else
            data.put("money", mMoneyEditText.getText().toString());
        if(!TextUtils.isEmpty(payWay)) {
            data.put("payment", payWay);
        }else if(mPayWayListAdapter.isBalancePay()){
            data.put("payment", Constants.PAY_TYPE_BALANCE);
        }
        ApiUtils.post(this, type == 0 ? URLConstants.ORDER_PAY : URLConstants.USER_RECHARGE, data, PayLogResult.class, new Response.Listener<PayLogResult>() {

            @Override
            public void onResponse(PayLogResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(PayWayActivity.this, response)) {
                    if(null == response.data){
                        ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
                        return;
                    }
                    if(Constants.PAY_TYPE_BALANCE.equalsIgnoreCase(response.data.paymentType)){
                        payResult(true, "");
                    }else if (null != response.data.payRequest) {
                        if (Constants.PAY_TYPE_WEICHAT.equals(payWay))
                            weichatPay(response.data.payRequest);
                        else if (Constants.PAY_TYPE_ALIPAY.equals(payWay)) {
                            //  支付宝支付
                            try {
                                if (!TextUtils.isEmpty(response.data.payRequest.packages)) {
                                    alipay(PayWayActivity.this, mHandler, response.data.payRequest.packages);
                                } else {
                                    ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error_get);
                                    closeSelf(false);
                                }
                            } catch (Exception e) {
                                ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
                                closeSelf(false);
                            }
                        } else if (Constants.PAY_TYPE_UNION.equals(payWay)) {
                            if (!TextUtils.isEmpty(response.data.payRequest.tn)) {
                                unionPay(response.data.payRequest.tn);
                            } else {
                                ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error_get);
                                closeSelf(false);
                            }

                        } else {
                            ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
                            closeSelf(false);
                        }
                    }else{
                        ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
                        closeSelf(false);
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
                closeSelf(false);
            }
        });

    }

    IWXAPI mIWXAPI;

    private void weichatPay(WeixinPayRequest info) {

        mIWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID);
        mIWXAPI.registerApp(BuildConfig.WX_APP_ID);

        registerPayBroadCast();

        PayReq request = new PayReq();
        request.appId = BuildConfig.WX_APP_ID;
        request.partnerId = info.partnerid;
        request.prepayId = info.prepayid;
        request.packageValue = info.packages;
        request.nonceStr = info.noncestr;
        request.timeStamp = info.timestamp;
        request.sign = info.sign;
        mIWXAPI.registerApp(BuildConfig.WX_APP_ID);
        mIWXAPI.sendReq(request);

        // 暂时解决方法
//        mbNeedReloadForWx = true;
    }

    /**
     * 支付宝支付
     *
     * @param activity
     * @param handler
     * @param orderInfo
     */
    public static void alipay(final Activity activity, final Handler handler, final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo);

                Message msg = new Message();
                msg.what = AliPayResult.ALIPAY_FLAG;
                msg.obj = new AliPayResult(result);
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    PayBroadCastReceiver mPayBroadCastReceiver;

    private void registerPayBroadCast() {
        if (mPayBroadCastReceiver != null) {
            return;
        }
        // 生成广播处理
        mPayBroadCastReceiver = new PayBroadCastReceiver();
        // 实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_PAY_RESULT);
        // 注册广播
        registerReceiver(mPayBroadCastReceiver, intentFilter);
    }

    private void unregisterPayBroadCast() {
        if (mPayBroadCastReceiver != null) {
            unregisterReceiver(mPayBroadCastReceiver);
        }
    }

    class UpdateBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ACTION_NAME)) {
                closeSelf(true);
            }
        }
    }

    private void registerBoradcastReceiver() {
        if (mUpdateBroadCastReceiver == null) {
            mUpdateBroadCastReceiver = new UpdateBroadCastReceiver();
        }
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_NAME);
        registerReceiver(mUpdateBroadCastReceiver, myIntentFilter);
    }

    class PayBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final int code = intent.getIntExtra("code", BaseResp.ErrCode.ERR_COMM);
            Log.e("WXPay", "broadcast receiver: " + code + "");
            switch (code) {
                case BaseResp.ErrCode.ERR_OK:
                    payResult(true, "");
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    payResult(false, "");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtils.show(PayWayActivity.this, R.string.msg_pay_cancel);
                    closeSelf(false);
                    break;
            }
        }

    }

    private void closeSelf(boolean succ) {
        setResult(Activity.RESULT_OK);
        if (succ || type == 0) {
            finishActivity();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AliPayResult.ALIPAY_FLAG) {
                AliPayResult aliPayResult = (AliPayResult) msg.obj;

                if (AliPayResult.ALI_PAY_RESULT_OK.equals(aliPayResult.getResultStatus())) {
                    payResult(true, "");
                } else {
                    ToastUtils.show(PayWayActivity.this, aliPayResult.getResult());
                    closeSelf(false);
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        if (mUpdateBroadCastReceiver != null) {
            unregisterReceiver(mUpdateBroadCastReceiver);
        }
        unregisterPayBroadCast();
        super.onDestroy();

    }

    protected void payResult(boolean succ, String result) {
        if (succ) {
            ToastUtils.show(PayWayActivity.this, R.string.msg_pay_success);

        } else {
            if (TextUtils.isEmpty(result)) {
                ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
            } else {
                ToastUtils.show(PayWayActivity.this, result);
            }
        }
        closeSelf(succ);
//        if (type == 0) {
//            Intent intents = new Intent(PayWayActivity.this, PayResultActivity.class);
//            intents.putExtra(Constants.EXTRA_DATA, mOrderInfo.sn);
//            intents.putExtra(Constants.EXTRA_PAY, succ);
//            startActivity(intents);
//            if(succ){
//                setResult(Activity.RESULT_OK);
//                finishActivity();
//            }
//        } else {
//            setResult(Activity.RESULT_OK);
//            finishActivity();
//        }

    }

    protected void unionPay(String tn) {

        String serverMode = "00";
        if (BuildConfig.DEBUG) {
            serverMode = "01";
        }
        UPPayAssistEx.startPay(this, null, null, tn, serverMode);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (data == null) {
            return;
        }

        String str = data.getExtras().getString("pay_result");
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.equalsIgnoreCase("success")) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String sign = data.getExtras().getString("result_data");
                // 验签证书同后台验签证书
                // 此处的verify，商户需送去商户后台做验签
                if (verifySign(sign)) {
                    //验证通过后，显示支付结果
                    payResult(true, "");
                } else {
                    // 验证不通过后的处理
                    // 建议通过商户后台查询支付结果
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
            }
        } else if (str.equalsIgnoreCase("fail")) {
            payResult(false, "");
        } else if (str.equalsIgnoreCase("cancel")) {
            ToastUtils.show(getActivity(), R.string.msg_pay_cancel);
            closeSelf(false);
        }
    }

    protected boolean verifySign(String sign) {
        return true;
    }

    public static class PayExtend {
        public int balancePay;
    }
}

