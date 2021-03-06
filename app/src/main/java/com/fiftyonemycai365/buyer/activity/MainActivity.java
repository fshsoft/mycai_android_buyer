package com.fiftyonemycai365.buyer.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.LocAddressInfo;
import com.fanwe.seallibrary.model.MessageStatusInfo;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.event.GotoHomeEvent;
import com.fanwe.seallibrary.model.event.LoginEvent;
import com.fanwe.seallibrary.model.event.ShoppingCartEvent;
import com.fanwe.seallibrary.model.result.MessageStatusResult;
import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
import com.fiftyonemycai365.buyer.BuildConfig;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.YizanApp;
import com.fiftyonemycai365.buyer.bbs.fragment.BbsFragment;
import com.fiftyonemycai365.buyer.comm.ShoppingCartMgr;
import com.fiftyonemycai365.buyer.fragment.HomePageFragment;
import com.fiftyonemycai365.buyer.fragment.PersonalCenterFragment;
import com.fiftyonemycai365.buyer.fragment.ShoppingCartFragment;
import com.fiftyonemycai365.buyer.helper.DoorMgr;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.AppUpdate;
import com.fiftyonemycai365.buyer.utils.ImgUrl;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.PushUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.fiftyonemycai365.buyer.wy.activity.OpenDoorActivity;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.app.DeviceUtils;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;

/**
 * MainActivity
 */
public class MainActivity extends BaseActivity implements BaseActivity.ContainerTitleListener {
    private static final int LOC_REQUEST_CODE = 0x102;
    private String[] mTabText;
    int[] mImageViewArray = new int[]{
            R.drawable.tab_home_selector,
            R.drawable.tab_shopping_selector,
            R.drawable.tab_bbs_selector,
            R.drawable.tab_user_selector
    };
    private LayoutInflater mLayoutInflater;
    private FragmentTabHost mTabHost;
    private View mBtnSearch;
    private UserAddressInfo mAddressInfo;
    private PopupWindow popupWindow;
    private OpenDoorActivity mOpenDoorActivity;
    private RelativeLayout mTitleCon, mTitleRight33;
    private View mTitleBot;
    private ImageButton mTitleLeft22;
    private TextView mTitleText11;
    public static MainActivity activity;
    private boolean flag =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPageTag(TagManager.MAIN_ACTIVITY);
        activity = this;
        resetCurrUserAddr();
        setTitleListener_Container(this, R.layout.titlebar_main);
        mTabText = new String[]{getResources().getString(R.string.home_text), getResources().getString(R.string.shop_cart_text), getResources().getString(R.string.bbs_text), getResources().getString(R.string.my_text)};
        ImgUrl.init(this);
        JPinit();
        initTabSpec();
        reflashState();
        EventBus.getDefault().register(this);
        versionUpgrade();
        initMessagePushReceiver();
        initOpenDoor();
        initHomeTitle(mAddressInfo);
    }

    private void initOpenDoor() {

        if (!O2OUtils.isOpenProperty()) {
            return;
        }
        mOpenDoorActivity = OpenDoorActivity.getInstance(this);
        mOpenDoorActivity.initMiaodou();
        mOpenDoorActivity.initAllDoorsFormCache();
        initDoorNeedSensor();

    }

    private void initDoorListFromCache() {
        if (!O2OUtils.isOpenProperty()) {
            return;
        }
        mOpenDoorActivity.initAllDoorsFormCache();
    }

    private void initDoorNeedSensor() {
        if (!O2OUtils.isOpenProperty()) {
            return;
        }
        if (!O2OUtils.isLogin(this)) {
            MiaodouKeyAgent.setNeedSensor(false);
            return;
        }

        if (mTabHost.getCurrentTab() == 0 && DoorMgr.getInstance().getDoorCount() > 0) {
            MiaodouKeyAgent.setNeedSensor(true);
        } else {
            MiaodouKeyAgent.setNeedSensor(false);
        }
    }

    private void setDoorArgentListener() {
        if (!O2OUtils.isOpenProperty()) {
            return;
        }
        MiaodouKeyAgent.setMDActionListener(mOpenDoorActivity);
    }

    private void uninitDoorAgent() {
        if (!O2OUtils.isOpenProperty()) {
            return;
        }
        MiaodouKeyAgent.unregisterMiaodouAgent();
    }

    public void onEventMainThread(ShoppingCartEvent event) {
        setTabStatus(1, ShoppingCartMgr.getInstance().getTotalCount(0));
    }

    public void onEventMainThread(GotoHomeEvent event) {
        int tab = mTabHost.getCurrentTab();
        if (event.mTabIndex != tab) {
            mTabHost.getTabWidget().getChildAt(event.mTabIndex).performClick();
        }

    }

    public void onEventMainThread(LoginEvent event) {
        try {
            if (O2OUtils.isLogin(this)) {
                initDoorListFromCache();
                initDoorNeedSensor();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        reflashState();
        resetCurrUserAddr();
        try {
            PersonalCenterFragment fragment = (PersonalCenterFragment) getSupportFragmentManager().findFragmentByTag(PersonalCenterFragment.class.getName());
            fragment.reFlashUI();

            ShoppingCartFragment fragmentCart = (ShoppingCartFragment) getSupportFragmentManager().findFragmentByTag(ShoppingCartFragment.class.getName());
            fragmentCart.reflashUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPinit();
    }

    private void reflashState() {
        if (O2OUtils.isLogin(this)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    loadMessageStatus();
                }
            }, 100);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ShoppingCartMgr.getInstance().loadCart(getApplicationContext());
                }
            }, 200);

        } else {
            updateMessageStatus(null);
            ShoppingCartMgr.getInstance().clearCart();

        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mMessagePushReceiver != null)
            unregisterReceiver(mMessagePushReceiver);
        uninitDoorAgent();
    }

    private void JPinit() {
        // 推送
        PushUtils.initTagsAndAlias(this);
    }

    protected void addTabFragment(int index, Class<?> clss) {
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(
                clss.getName()).setIndicator(
                getTabItemView(index));
        // 将Tab按钮添加进Tab选项卡中
        mTabHost.addTab(tabSpec, clss, null);
    }

    private void initTabSpec() {
        // 实例化布局对象
        mLayoutInflater = LayoutInflater.from(this);
        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.fl_container);

        addTabFragment(0, HomePageFragment.class);
        addTabFragment(1, ShoppingCartFragment.class);
        addTabFragment(2, BbsFragment.class);
        addTabFragment(3, PersonalCenterFragment.class);

        // 显示
        mTabHost.setCurrentTab(0);

        // 栈上面叠加了很多Fragment的时候，如果想再次点击TabHost的首页，能返回到最初首页的页面的话，那就要把首页的Fragment上面的Fragment的弹出
        for (int i = 0, size = mTabText.length; i < size; i++) {
            final int j = i;
            mTabHost.getTabWidget().getChildAt(i)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refreshTitle(j);
                            getSupportFragmentManager().popBackStack(null,
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            mTabHost.setCurrentTab(j);
                            initDoorNeedSensor();
                            reloadHomePage();
                        }
                    });
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    @SuppressLint("InflateParams")
    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.main_tab_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setBackgroundResource(mImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTabText[index]);

        return view;
    }

    @Override
    public void onBackPressed() {
        AppUtils.startHome(this);
    }

    private void deleteShopping() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.item_pop_delete, null);
        View parent = LayoutInflater.from(this).inflate(R.layout.fragment_shopping_cart, null);
        convertView.setBackgroundResource(R.drawable.style_edt_boder);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        float width = (wm.getDefaultDisplay().getWidth() / 3) * 2;
        popupWindow = new PopupWindow(convertView, (int) width, (int) (width / 5) * 3, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        convertView.findViewById(R.id.sure_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                if (ShoppingCartMgr.getInstance().deleteShopping(MainActivity.this, MainActivity.class.getName())) {

                }
            }
        });
        convertView.findViewById(R.id.cancel_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void logout() {
        ToastUtils.show(MainActivity.this,
                R.string.msg_success_logout);
        initDoorNeedSensor();
        O2OUtils.logout(MainActivity.this);
        PushUtils.initAlias(MainActivity.this);
        reflashState();
        PersonalCenterFragment fragment = (PersonalCenterFragment) getSupportFragmentManager().findFragmentByTag(PersonalCenterFragment.class.getName());
        if(fragment != null) {
            fragment.reFlashUI();
        }
        resetCurrUserAddr();
    }


    private void loadMessageStatus() {
        if (!O2OUtils.isLogin(this)) {
            return;
        }
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        ApiUtils.post(this, URLConstants.MSG_STATUS,
                params,
                MessageStatusResult.class, responseListener(), errorListener());
    }

    private Response.Listener<MessageStatusResult> responseListener() {
        return new Response.Listener<MessageStatusResult>() {
            @Override
            public void onResponse(final MessageStatusResult response) {
                if (response != null && response.data != null) {
                    updateMessageStatus(response.data);
                }
            }
        };

    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
    }

    Handler updateMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //我的->系统消息，数字
            Fragment findFragment = getSupportFragmentManager().findFragmentByTag(PersonalCenterFragment.class.getName());
            if (null != findFragment && findFragment instanceof PersonalCenterFragment) {
                ((PersonalCenterFragment) findFragment).updateMsgCount((MessageStatusInfo) (msg.obj));
            }
            MessageStatusInfo info = (MessageStatusInfo) (msg.obj);
            if (info.newMsgCount > 0) {
                mTitleText11.setVisibility(View.VISIBLE);
                mTitleText11.setText(String.valueOf(info.newMsgCount));
            } else {
                mTitleText11.setVisibility(View.INVISIBLE);
            }

        }
    };

    /**
     * 处理消失状态，显示数字
     */
    public void updateMessageStatus(MessageStatusInfo messageStatusInfo) {
        if (messageStatusInfo == null) {
            messageStatusInfo = new MessageStatusInfo();
        }
        setTabStatus(1, messageStatusInfo.cartGoodsCount);
        setTabStatus(3, messageStatusInfo.newMsgCount);
        Message msg = new Message();
        msg.obj = messageStatusInfo;
        updateMessageHandler.sendMessageDelayed(msg, 2000);
    }

    private void setTabStatus(int index, int num) {
        TextView tv = (TextView) mTabHost.getTabWidget().getChildTabViewAt(index).findViewById(R.id.tv_msg_count);
        if (num > 0) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(String.valueOf(num));
        } else {
            tv.setVisibility(View.INVISIBLE);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loadMessageStatus();
        PushUtils.init(this);
        if (intent.getIntExtra("cart", 0) == 1) {
            mTabHost.getTabWidget().getChildAt(1).performClick();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    selectAllCart();
                }
            }, 100);

        } else {
            mTabHost.getTabWidget().getChildAt(0).performClick();
        }
    }

    protected void selectAllCart() {
        try {
            ShoppingCartFragment fragment = (ShoppingCartFragment) getSupportFragmentManager().findFragmentByTag(ShoppingCartFragment.class.getName());
            if (fragment != null) {
                fragment.selectAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reflashDefaultAddress() {
        UserAddressInfo userAddressInfo = PreferenceUtils.getObject(this, UserAddressInfo.class);
        if (O2OUtils.isLogin(this) && userAddressInfo != null && userAddressInfo.id > 0) {
            return;
        }

        userAddressInfo = null;

        if (userAddressInfo == null) {
            LocAddressInfo locAddressInfo = PreferenceUtils.getObject(this, LocAddressInfo.class);
            if (locAddressInfo != null && locAddressInfo.isUsefulAddr()) {
                userAddressInfo = locAddressInfo.toUserAddr();
            }
        }
        if (userAddressInfo == null && O2OUtils.isLogin(this)) {
            UserInfo userInfo = PreferenceUtils.getObject(this, UserInfo.class);
            if (!ArraysUtils.isEmpty(userInfo.address)) {
                for (UserAddressInfo addressInfo : userInfo.address) {
                    if (addressInfo.isDefault) {
                        userAddressInfo = addressInfo;
                        break;
                    }
                }
            }
        }
        if (userAddressInfo != null) {
            PreferenceUtils.setObject(this, userAddressInfo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case LOC_REQUEST_CODE:
                initHomeTitle(null);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomePageFragment.class.getName());
                if (null != fragment && fragment instanceof HomePageFragment)
                    ((HomePageFragment) fragment).reflashData();
                break;
        }
    }

    private void reloadHomePage() {
        if (mTabHost.getCurrentTab() != 0) {
            return;
        }
        try {
            if (YizanApp.getInstance().isNeedReload()) {
                YizanApp.getInstance().unNeedReload();
                HomePageFragment fragment = (HomePageFragment) getSupportFragmentManager().findFragmentByTag(HomePageFragment.class.getName());
                if (fragment != null) {
                    fragment.reflashData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private boolean needUpdate(String appVersion) {
        boolean bNeed = false;
        try {
            if (TextUtils.isEmpty(appVersion)) {
                return bNeed;
            }
            float newVersion = Float.parseFloat(appVersion);
            float currVersion = Float.parseFloat(DeviceUtils.getPackageInfo(MainActivity.this).versionName);
            if (newVersion > currVersion) {
                bNeed = true;
            }

        } catch (Exception e) {

        }
        return bNeed;
    }

    private void versionUpgrade() {
        AppUpdate.checkUpdate(this, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMessageStatus();
        initDoorNeedSensor();
        setDoorArgentListener();
        checkNeedReloadHomePage();
        refreshTitle(mTabHost.getCurrentTab());
    }

    private void refreshTitle(int index) {
        switch (index) {
            case 0:
                Drawable arrowDown = getResources().getDrawable(R.drawable.ic_arrow_right);
                arrowDown.setBounds(0, 0, arrowDown.getMinimumWidth(), arrowDown.getMinimumHeight());
                Drawable arrowDown1 = getResources().getDrawable(R.drawable.big_locate);
                arrowDown1.setBounds(0, 0, arrowDown1.getMinimumWidth(), arrowDown1.getMinimumHeight());
                mTitle.setCompoundDrawables(arrowDown1, null, arrowDown, null);
                mTitle.setTextColor(getResources().getColor(R.color.theme_title_text));
                mBtnSearch.setVisibility(View.VISIBLE);
                ((ImageButton) mBtnSearch).setImageResource(R.drawable.ic_search);
                mTitleCon.setBackgroundResource(R.drawable.mine_title_top_empty);
                mTitleBot.setVisibility(View.VISIBLE);
                mTitleLeft22.setVisibility(View.GONE);
                mTitleRight33.setVisibility(View.GONE);
                initHomeTitle(PreferenceUtils.getObject(MainActivity.this, UserAddressInfo.class));
                break;
            case 1:
                if(flag){
                    mTabHost.setCurrentTab(0);
                    flag =false;
                }else {
                    if (O2OUtils.turnLogin(MainActivity.this.getApplicationContext())) {
                        flag = true;
                        return;
                    }
                }

                setTitle(mTabText[1]);
                mTitle.setCompoundDrawables(null, null, null, null);
                mTitle.setTextColor(getResources().getColor(R.color.theme_title_text));
                mBtnSearch.setVisibility(View.VISIBLE);
                mTitleBot.setVisibility(View.VISIBLE);
                mTitleLeft22.setVisibility(View.GONE);
                mTitleRight33.setVisibility(View.GONE);
                ((ImageButton) mBtnSearch).setImageResource(R.drawable.ic_clean);
                mTitleCon.setBackgroundResource(R.drawable.mine_title_top_empty);
                break;
            case 2:
                setTitle(mTabText[2]);
                mTitle.setTextColor(getResources().getColor(R.color.theme_title_text));
                mTitle.setCompoundDrawables(null, null, null, null);
                mBtnSearch.setVisibility(View.INVISIBLE);
                mTitleBot.setVisibility(View.VISIBLE);
                mTitleLeft22.setVisibility(View.GONE);
                mTitleRight33.setVisibility(View.GONE);
                mTitleCon.setBackgroundResource(R.drawable.mine_title_top_empty);
                break;
            case 3:
                setTitle(mTabText[3]);
                mTitle.setCompoundDrawables(null, null, null, null);
                mTitle.setTextColor(Color.WHITE);
                mBtnSearch.setVisibility(View.INVISIBLE);
                mTitleBot.setVisibility(View.GONE);
                mTitleLeft22.setVisibility(View.VISIBLE);
                mTitleRight33.setVisibility(View.VISIBLE);
                loadMessageStatus();
                mTitleCon.setBackgroundResource(R.drawable.mine_title_top);
                break;
        }
    }

    protected void checkNeedReloadHomePage() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reloadHomePage();
            }
        }, 1);

    }

    /**
     * title地址显示
     */
    public void initHomeTitle(UserAddressInfo info) {
        if (info == null) {
            info = PreferenceUtils.getObject(MainActivity.this, UserAddressInfo.class);
        }
        if (info == null) {
            this.setTitle(R.string.main_sel_location);
            return;
        }
        String addr = TextUtils.isEmpty(info.detailAddress) ? info.address : info.detailAddress;
        if (TextUtils.isEmpty(addr)) {
            this.setTitle(R.string.main_sel_location);
        } else {
            this.setTitle(addr);
        }
    }


    private void resetCurrUserAddr() {
        reflashDefaultAddress();
        mAddressInfo = PreferenceUtils.getObject(this, UserAddressInfo.class);
    }

    /**
     * 初始化消息接收器
     */
    private void initMessagePushReceiver() {
        mMessagePushReceiver = new MessagePushReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_MSG);
        registerReceiver(mMessagePushReceiver, filter);
    }

    MessagePushReceiver mMessagePushReceiver;

    @Override
    public void setTitle(TextView title, ImageButton left, View right, RelativeLayout viewGroup) {
        mBtnSearch = mTitleRight2;
        mTitleCon = mTitleContainer;
        mTitleBot = mTitleBottom;
        mTitleLeft22 = mTitleLeft2;
        mTitleRight33 = mTitleRight3;
        mTitleText11 = mTitleText;
        mTitleRight33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (O2OUtils.isLogin(MainActivity.this)) {
                    if (BuildConfig.WEB_MSG) {
                        startActivity(new Intent(MainActivity.this, WebMessageActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, ServerMessageActivity.class));
                    }

                } else {
                    ToastUtils.show(MainActivity.this, R.string.msg_error_not_login);
                    O2OUtils.turnLogin(MainActivity.this);
                }
            }
        });
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == mTabHost.getCurrentTab()) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                } else if (1 == mTabHost.getCurrentTab()) {
                    if (ShoppingCartMgr.getInstance().getCart() != null && ShoppingCartMgr.getInstance().getCart().size() > 0) {
                        deleteShopping();
                    } else {
                        View convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_pop_delete, null);
                        View parent = LayoutInflater.from(MainActivity.this).inflate(R.layout.fragment_shopping_cart, null);
                        convertView.setBackgroundResource(R.drawable.style_edt_boder);
                        WindowManager wm = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
                        float width = (wm.getDefaultDisplay().getWidth() / 3) * 2;
                        popupWindow = new PopupWindow(convertView, (int) width, (int) (width / 5) * 3, true);
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                        ((TextView) convertView.findViewById(R.id.hint_text)).setText(getString(R.string.msg_err_shopcart_not_null));
                        ((TextView) convertView.findViewById(R.id.delete_title)).setText(getString(R.string.msg_err_not_delete));
                        convertView.findViewById(R.id.cancel_delete).setVisibility(View.GONE);
                        convertView.findViewById(R.id.sure_delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                            }
                        });
                    }
                } else {

                }

            }
        });
        mTitleLeft22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SetUpActivity.class));
            }
        });
        initHomeTitle(PreferenceUtils.getObject(MainActivity.this, UserAddressInfo.class));
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == mTabHost.getCurrentTab()) {
                    Intent intent = new Intent(MainActivity.this, SwitchAddressActivity.class);
                    intent.putExtra("isLocate", "true");
                    startActivityForResult(intent, LOC_REQUEST_CODE);
                } else if (1 == mTabHost.getCurrentTab()) {

                } else {

                }
            }
        });
    }


    /**
     * 消息Receiver
     */
    class MessagePushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_MSG.equals(intent.getAction())) {
                loadMessageStatus();
            }
        }
    }

    @Override
    protected void onPause() {
        MiaodouKeyAgent.setNeedSensor(false);
        super.onPause();
    }
}
