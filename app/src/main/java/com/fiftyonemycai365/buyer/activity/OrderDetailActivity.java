package com.fiftyonemycai365.buyer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.fanwe.seallibrary.model.CupponActivityInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.CupponActivityResult;
import com.fanwe.seallibrary.utils.DeviceUtils;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.InitInfo;
import com.fanwe.seallibrary.model.OrderInfo;
import com.fanwe.seallibrary.model.event.OrderEvent;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.OrderResult;
import com.fiftyonemycai365.buyer.BuildConfig;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.adapter.OrderGoodListAdapter;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.NumFormat;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.OrderService;
import com.fiftyonemycai365.buyer.utils.ShareUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.app.IntentUtils;
import com.zongyou.library.util.BitmapUtils;
import com.zongyou.library.util.LogUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 订单详情Activity
 */
public class OrderDetailActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    private int mOrderId;
    private OrderInfo mOrderInfo;
    private OrderGoodListAdapter mOrderGoodListAdapter;
    private boolean mAutoPay;
    private UpdateBroadCastReceiver mUpdateBroadCastReceiver;
    private ImageView mFlowImage,pop_delete;
    private TextView cuppon_num,cuppon_send;
    private PopupWindow popupWindow,popupWindow1;
    private RelativeLayout pop_bottom;
    private CupponActivityInfo cupponActivityInfo;
    private boolean mLoadShareOK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setTitleListener(this);
        ShareSDK.initSDK(OrderDetailActivity.this, BuildConfig.SHARE_SDK);
        setPageTag(TagManager.ORDER_DETAIL_ACTIVITY);
        mOrderId = this.getIntent().getIntExtra(Constants.EXTRA_DATA, 0);
        mAutoPay = this.getIntent().getBooleanExtra(Constants.EXTRA_AUTO, false);
        if (mOrderId == 0) {
            finish();
            return;
        }else{

        }

        registerBoradcastReceiver();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_order_detail);
    }

    private void registerBoradcastReceiver() {
        if (mUpdateBroadCastReceiver == null) {
            mUpdateBroadCastReceiver = new UpdateBroadCastReceiver();
        }
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.ACTION_NAME);
        registerReceiver(mUpdateBroadCastReceiver, myIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        if (mUpdateBroadCastReceiver != null) {
            unregisterReceiver(mUpdateBroadCastReceiver);
        }
        super.onDestroy();

    }

    private void loadData() {

        // 加载数据
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.loading_err_net);
            return;
        }
        CustomDialogFragment.show(this.getSupportFragmentManager(), R.string.loading, this.getLocalClassName());
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(mOrderId));
        ApiUtils.post(this, URLConstants.ORDER_DETAIL,
                params,
                OrderResult.class, responseListener(), errorListener());
    }

    private Response.Listener<OrderResult> responseListener() {
        return new Response.Listener<OrderResult>() {
            @Override
            public void onResponse(final OrderResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    try {
                        initViewData(response.data);
                        if(response.data != null && !mLoadShareOK && !mAutoPay) {
                            getShare(mOrderId);
                        }
                    } catch (Exception e) {
                        LogUtils.e("order", e);
//                        ToastUtils.show(OrderDetailActivity.this, R.string.msg_error_array);
                    }
                }
            }
        };
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(getApplicationContext(), R.string.loading_err_nor);
                CustomDialogFragment.dismissDialog();
            }
        };
    }

    private void checkIsRefount(OrderInfo orderInfo){
        if (orderInfo != null){
            if (orderInfo.refundCount > 0){
                mViewFinder.find(R.id.order_refount_detail).setVisibility(View.VISIBLE);
                mViewFinder.onClick(R.id.order_refount_detail,this);
            }else{
                mViewFinder.find(R.id.order_refount_detail).setVisibility(View.GONE);
            }
        }
    }

    private void initViewData(OrderInfo orderInfo) {
        if (orderInfo == null) {
            return;
        }
        mOrderInfo = orderInfo;
        initOrderFlowImage();
        checkIsRefount(mOrderInfo);

        mViewFinder.setText(R.id.tv_order_sn, mOrderInfo.sn);
        Log.e("订单状态", "--" + mOrderInfo.orderStatusStr);

        mViewFinder.setText(R.id.tv_order_state, getString(R.string.msg_order_detail) + mOrderInfo.orderStatusStr);
        mViewFinder.setText(R.id.tv_order_time, mOrderInfo.createTime);
        mViewFinder.setText(R.id.tv_user_name, mOrderInfo.name);
        mViewFinder.setText(R.id.tv_user_tel, mOrderInfo.mobile);
        mViewFinder.setText(R.id.tv_addr, mOrderInfo.address);
        mViewFinder.setText(R.id.tv_deliver_time, mOrderInfo.appTime);
        mViewFinder.setText(R.id.tv_remark, TextUtils.isEmpty(mOrderInfo.buyRemark) ? getResources().getString(R.string.msg_not_available) : mOrderInfo.buyRemark);
        mViewFinder.setText(R.id.tv_seller_name, mOrderInfo.sellerName);

        mViewFinder.setText(R.id.tv_deliver_man, TextUtils.isEmpty(mOrderInfo.staffName) ? getResources().getString(R.string.msg_not_available) : mOrderInfo.staffName);
        mViewFinder.setText(R.id.tv_pay_way, TextUtils.isEmpty(mOrderInfo.payType) ? getResources().getString(R.string.msg_not_available) : mOrderInfo.payType);
        mViewFinder.setText(R.id.tv_carriage, String.format(getResources().getString(R.string.RMB_sign), NumFormat.formatPrice(mOrderInfo.freight)));
        mViewFinder.setText(R.id.tv_total, String.format(getResources().getString(R.string.RMB_sign), NumFormat.formatPrice(mOrderInfo.payFee)));
        mViewFinder.onClick(R.id.ll_seller_container, this);

        if (mOrderInfo.orderType == 2) {
            mViewFinder.setText(R.id.tv_deliver_title, R.string.msg_server_staff);
            mViewFinder.find(R.id.ll_deliver_price).setVisibility(View.GONE);
            mViewFinder.find(R.id.ll_deliver_time).setVisibility(View.GONE);
        }

        initGoodsList();
        initButtomStatus();


        initOrderQuick();

        if(mOrderInfo.discountFee > 0.0f){
            mViewFinder.setText(R.id.tv_coupon, "-" + NumFormat.formatPrice(mOrderInfo.discountFee));
        }else{
            mViewFinder.find(R.id.ll_coupon_price).setVisibility(View.GONE);
        }
        mViewFinder.onClick(R.id.send_cuppon,this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAutoPay) {
                    mAutoPay = false;
                    mViewFinder.find(R.id.tv_right_button).performClick();
                }
            }
        }, 50);
    }

    private void initOrderFlowImage() {
        if (mOrderInfo == null || TextUtils.isEmpty(mOrderInfo.orderStatusStr)) {
            return;
        }
        mFlowImage = mViewFinder.find(R.id.order_flow_image);
        ImageRequest imageRequest = new ImageRequest(
                mOrderInfo.statusFlowImage,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        try {
                            int width = mFlowImage.getWidth();
                            int bmpWidth = response.getWidth();
                            if (bmpWidth < width) {
                                mFlowImage.setMinimumHeight(response.getHeight() * width / bmpWidth);
                                mFlowImage.setImageBitmap(response);
                            } else {
                                mFlowImage.setImageBitmap(BitmapUtils.zoomBitmap(response, width, response.getHeight() * width / bmpWidth));
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestManager.getRequestQueue().add(imageRequest);

    }

    /**
     * 催单
     */
    private void initOrderQuick() {
        if (mOrderInfo.isCanReminder) {
            mViewFinder.find(R.id.order_quik).setVisibility(View.VISIBLE);
            mViewFinder.find(R.id.order_quik).setOnClickListener(this);

        } else {
            mViewFinder.find(R.id.order_quik).setVisibility(View.GONE);
        }
    }

    private void initGoodsList() {
        ListView listView = mViewFinder.find(R.id.lv_goods);
        mOrderGoodListAdapter = new OrderGoodListAdapter(this, mOrderInfo);
        listView.setAdapter(mOrderGoodListAdapter);
    }

    private void initButtomStatus() {
        // left button
        mViewFinder.find(R.id.tv_left_button).setVisibility(View.VISIBLE);
        if (mOrderInfo.isCanCancel) {
            mViewFinder.setText(R.id.tv_left_button, R.string.msg_order_cancel);
        } else if (mOrderInfo.isCanRate) {
            mViewFinder.setText(R.id.tv_left_button, R.string.msg_go_evaluate);
        } else if (mOrderInfo.isCanDelete) {
            mViewFinder.setText(R.id.tv_left_button, R.string.msg_order_delete);
        } else {
            mViewFinder.find(R.id.tv_left_button).setVisibility(View.GONE);
        }
        // right button
        if (mOrderInfo.isCanPay) {
            mViewFinder.setText(R.id.tv_right_button, R.string.msg_go_pay);

        } else if (mOrderInfo.isCanConfirm) {
            mViewFinder.setText(R.id.tv_right_button, R.string.msg_affirm_goods);
        } else {
            mViewFinder.setText(R.id.tv_right_button, R.string.msg_go_stroll);
        }
        mViewFinder.onClick(R.id.tv_right_button, this);
        mViewFinder.onClick(R.id.tv_left_button, this);
        mViewFinder.onClick(R.id.rl_call_customer, this);
        mViewFinder.onClick(R.id.rl_call_seller, this);
    }

    private void onLeftButtonClick() {
        if (mOrderInfo.isCanCancel) {
            OrderService.cancelOrder(this, mOrderInfo, responseListener(), errorListener());
        } else if (mOrderInfo.isCanRate) {
            Intent intent = new Intent(this, AddCommentActivity.class);
            intent.putExtra(Constants.EXTRA_DATA, mOrderInfo);
            startActivityForResult(intent, AddCommentActivity.REQUEST_CODE);
        } else if (mOrderInfo.isCanDelete) {
            orderDel();
        }
    }


    private void onRightButtonClick() {
        if (mOrderInfo.isCanPay) {
            gotoPayActivity();
        } else if (mOrderInfo.isCanConfirm) {
            OrderService.confirmOrder(this, mOrderInfo, responseListener(), errorListener());
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOrderInfo == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.send_cuppon:
//                showPop(v);
                sendCuppon(v);
                break;
            case R.id.order_refount_detail:
                Intent intent1 = new Intent(OrderDetailActivity.this,WebViewActivity.class);
                intent1.putExtra(Constants.EXTRA_URL,URLConstants.URL_REFUND
                        +"userId="+PreferenceUtils.getObject(OrderDetailActivity.this, UserInfo.class).id
                        +"&orderId="+mOrderInfo.id);
                startActivity(intent1);
                break;
            case R.id.tv_left_button:
                onLeftButtonClick();
                break;
            case R.id.tv_right_button:
                onRightButtonClick();
                break;
            case R.id.rl_call_customer:
                try {
                    InitInfo initInfo = PreferenceUtils.getObject(getApplicationContext(), InitInfo.class);
                    String tel = initInfo.serviceTel.replace("-", "");
                    IntentUtils.dial(OrderDetailActivity.this, tel);
                } catch (Exception e) {
                    LogUtils.e("dial", e);
                }
                break;
            case R.id.rl_call_seller:
                try {
                    String tel = mOrderInfo.sellerTel.replace("-", "");
                    IntentUtils.dial(OrderDetailActivity.this, tel);
                } catch (Exception e) {
                    LogUtils.e("dial", e);
                }
                break;
            case R.id.order_quik:
                // 15/11/14  催单
                reminder();
                break;
            case R.id.ll_seller_container:
                if (mOrderInfo.orderType == 2) {
                    Intent intent = new Intent(this, SellerServicesActivity.class);
                    intent.putExtra(Constants.EXTRA_DATA, mOrderInfo.sellerId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, SellerGoodsActivity.class);
                    intent.putExtra(Constants.EXTRA_DATA, mOrderInfo.sellerId);
                    startActivity(intent);
                }
                break;

        }
    }

    private void reminder() {
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.msg_loading, OrderDetailActivity.class.getName());
        HashMap<String, String> data = new HashMap<String, String>(1);
        data.put("id", String.valueOf(mOrderInfo.id));
        ApiUtils.post(this, URLConstants.ORDER_REMINDER,
                data,
                OrderResult.class, new Response.Listener<OrderResult>() {
                    @Override
                    public void onResponse(final OrderResult response) {
                        CustomDialogFragment.dismissDialog();
                        if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                            ToastUtils.show(OrderDetailActivity.this, R.string.msg_success_reminder);
                            try {
                                initViewData(response.data);
                            } catch (Exception e) {
                                LogUtils.e("order", e);
                                ToastUtils.show(OrderDetailActivity.this, R.string.loading_err_nor);
                            }
                        }
                    }
                }, errorListener());
    }

    private void gotoPayActivity() {
        Intent intent = new Intent(this, PayWayActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, mOrderInfo);
        startActivityForResult(intent, PayWayActivity.REQUEST_CODE);
//        startActivity(intent);
    }


    public void orderDel() {
        if (mOrderInfo == null) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.loading_err_net);
            return;
        }
        CustomDialogFragment.show(this.getSupportFragmentManager(), R.string.loading, this.getLocalClassName());
        HashMap<String, String> params = new HashMap<>(1);
        params.put("id", String.valueOf(mOrderInfo.id));
        ApiUtils.post(this, URLConstants.ORDER_DEL,
                params,
                BaseResult.class, new Response.Listener<BaseResult>() {
                    @Override
                    public void onResponse(BaseResult response) {
                        CustomDialogFragment.dismissDialog();
                        if (O2OUtils.checkResponse(OrderDetailActivity.this, response)) {
                            OrderEvent orderEvent = new OrderEvent(null);
                            EventBus.getDefault().post(orderEvent);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    finishActivity();
                                }
                            }, 200);

                        }
                    }
                }, errorListener());
    }

    private void getShare(int orderId){
        HashMap<String, String> params = new HashMap<>(1);
        params.put("orderId", String.valueOf(mOrderId));
        ApiUtils.post(this, URLConstants.ORDER_GETSHARE, params, CupponActivityResult.class, new Response.Listener<CupponActivityResult>() {
            @Override
            public void onResponse(CupponActivityResult cupponActivityResult) {
                if (O2OUtils.checkResponse(OrderDetailActivity.this, cupponActivityResult)) {
                    if (cupponActivityResult.data != null) {
                        mLoadShareOK = true;
                        cupponActivityInfo = cupponActivityResult.data;
                        mViewFinder.find(R.id.send_cuppon).setVisibility(View.VISIBLE);
                        if(mOrderInfo != null && mOrderInfo.promotionIsShow == 0){
                            try {
                                showPop(mViewFinder.find(R.id.send_cuppon));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        }, errorListener());
    }

    private void showShareCupponFlag(){
        HashMap<String, String> params = new HashMap<>(1);
        params.put("orderId", String.valueOf(mOrderId));
        ApiUtils.post(this, URLConstants.ORDER_NOTSHOW, params, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult result) {

            }
        }, errorListener());
    }


    class UpdateBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ACTION_NAME)) {
                loadData();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case AddCommentActivity.REQUEST_CODE:
                if (data != null) {
                    initViewData((OrderInfo) data.getSerializableExtra(Constants.EXTRA_DATA));
                }
                break;
            case PayWayActivity.REQUEST_CODE:
                mAutoPay = false;
                if (data != null) {
                    loadData();
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
            popupWindow = null;
        }
        return super.onTouchEvent(event);
    }

    private void showPop(final View views){
        View view = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.popupwindow_cuppon,null);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        view.setBackgroundDrawable(dw);
        WindowManager wm = (WindowManager) OrderDetailActivity.this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
//        popupWindow = new PopupWindow(view,width,height,true);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        pop_bottom = (RelativeLayout) view.findViewById(R.id.pop_bottom);
        pop_bottom.setBackgroundResource(R.drawable.cornor_border_cuppon);
//        popupWindow.showAtLocation(views, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.showAtLocation(views, Gravity.CENTER, 0, 0);
        mViewFinder.find(R.id.v_half_bg).setVisibility(View.VISIBLE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mViewFinder.find(R.id.v_half_bg).setVisibility(View.GONE);
            }
        });
//        this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.half_translucence));
        pop_delete = (ImageView) view.findViewById(R.id.cuppon_top_delete);
        cuppon_send = (TextView) view.findViewById(R.id.cuppon_send);
        cuppon_num = (TextView) view.findViewById(R.id.cuppon_top_num);
        cuppon_num.setText(cupponActivityInfo.sharePromotionNum);
        cuppon_num.setText(getString(R.string.cuppon_pop_tv1, cupponActivityInfo.sharePromotionNum));
        pop_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        cuppon_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //转到新的pop
                sendCuppon(views);
            }
        });

        showShareCupponFlag();
    }

    private void sendCuppon(View v){
        View view = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.popupwindow_share,null);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        view.setBackgroundDrawable(dw);
        WindowManager wm = (WindowManager) OrderDetailActivity.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        int navHeight = DeviceUtils.getNavigationBarHeight(this);
        height -= (int)(navHeight/outMetrics.density);
        popupWindow1 = new PopupWindow(view,width,height,true);
        popupWindow1.setOutsideTouchable(true);
        popupWindow1.setFocusable(true);
        popupWindow1.setTouchable(true);
        popupWindow1.setBackgroundDrawable(new BitmapDrawable());
        popupWindow1.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        View emptyView = view.findViewById(R.id.v_empty);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow1.dismiss();
            }
        });
        final RelativeLayout show_to_wx = (RelativeLayout)view.findViewById(R.id.share_to_wx);
        show_to_wx.setTag(ShareUtils.PLATFORMS[1]);
        final RelativeLayout show_to_wx_chament = (RelativeLayout)view.findViewById(R.id.share_to_wx_chament);
        show_to_wx_chament.setTag(ShareUtils.PLATFORMS[0]);
        show_to_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow1.dismiss();
                ShareUtils.share(OrderDetailActivity.this, Wechat.NAME, cupponActivityInfo.image, cupponActivityInfo.title, cupponActivityInfo.detail,cupponActivityInfo.linkUrl, new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        ToastUtils.show(OrderDetailActivity.this,R.string.share_completed);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        ToastUtils.show(OrderDetailActivity.this,R.string.share_failed);
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        ToastUtils.show(OrderDetailActivity.this,R.string.share_canceled);
                    }
                });
            }
        });
        show_to_wx_chament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow1.dismiss();
                Log.e("WX_SHARE", "share click...");
                ShareUtils.share(OrderDetailActivity.this, WechatMoments.NAME, cupponActivityInfo.image, cupponActivityInfo.title, cupponActivityInfo.detail,cupponActivityInfo.linkUrl, new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Log.e("WX_SHARE", "share ok...");
                        if(Thread.currentThread() == Looper.getMainLooper().getThread()){
                            Log.e("WX_SHARE", "is main thread...");
                        }else{
                            Log.e("WX_SHARE", "is sub thread...");
                        }

                        mHandler.sendEmptyMessageDelayed(0, 500);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Log.e("WX_SHARE", "share err...");
                        mHandler.sendEmptyMessageDelayed(1, 500);
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        mHandler.sendEmptyMessageDelayed(2, 500);
                    }
                });
            }
        });
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
//                    ToastUtils.show(OrderDetailActivity.this, getString(R.string.share_completed));
                    ToastUtils.show(OrderDetailActivity.this, "分享成功");
                    break;
                case 1:
                    ToastUtils.show(OrderDetailActivity.this, "分享失败");
                    break;
                case 2:
                    ToastUtils.show(OrderDetailActivity.this, "分享已取消");
                    break;
            }

        }

    };
}
