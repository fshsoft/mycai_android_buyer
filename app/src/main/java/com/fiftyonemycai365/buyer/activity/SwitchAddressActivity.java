
package com.fiftyonemycai365.buyer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.LocAddressInfo;
import com.fanwe.seallibrary.model.PointInfo;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.AddressResult;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.adapter.AddressAdapter;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址选择Activity
 * Created by tlzou on 2015/9/18.
 */
public class SwitchAddressActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener, TencentLocationListener {
    private static final String TAG = SwitchAddressActivity.class.getName();
    private ListView mListView;
    private List<UserAddressInfo> listData = new ArrayList<UserAddressInfo>();
    private AddressAdapter mAdapter;
    private View mEmptyView;
    private boolean mLoadMore;
    private String mLocateFlag;
    private RelativeLayout mRelativeLayoutLocate;
    private int mPosition;
    private TencentLocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_addr);
        setPageTag(TagManager.SWITCH_ADDRESS_ACTIVITY);
        Intent intent = getIntent();
        mLocateFlag = intent.getStringExtra("isLocate");
        initViews();
        setTitleListener(this);

    }

    private void initViews() {

        mRelativeLayoutLocate = (RelativeLayout) findViewById(R.id.switch_locate);
        mRelativeLayoutLocate.setOnClickListener(this);
        if (("true").equals(mLocateFlag)) {
            mRelativeLayoutLocate.setVisibility(View.VISIBLE);
        } else {
            mRelativeLayoutLocate.setVisibility(View.GONE);
        }

        mListView = (ListView) findViewById(R.id.switch_addr_list);
        mAdapter = new AddressAdapter(SwitchAddressActivity.this, listData,this);
        mEmptyView = findViewById(android.R.id.empty);
        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && mAdapter.getCount() >= Constants.PAGE_SIZE && mAdapter.getCount() % Constants.PAGE_SIZE == 0) {
                    getAddrList(false);

                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                switch (mLocateFlag) {
                    case "true":
                        PreferenceUtils.setObject(getApplicationContext(), listData.get(mPosition));
                    case "false":
                        Intent intent = new Intent();
                        intent.putExtra(Constants.EXTRA_DATA, listData.get(mPosition));
                        setResult(Activity.RESULT_OK, intent);
                        finishActivity();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (O2OUtils.isLogin(getApplicationContext()))
            getAddrList(true);
    }

    private void getAddrList(final boolean isRefresh) {
        if (mLoadMore)
            return;
        if (!NetworkUtils.isNetworkAvaiable(SwitchAddressActivity.this)) {
            ToastUtils.show(SwitchAddressActivity.this, R.string.msg_error_network);
            return;
        }
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.msg_loading, SwitchAddressActivity.class.getName());
        Map<String, String> data = new HashMap<>();
        ApiUtils.post(this, URLConstants.USERADDRESSLISTS, data, AddressResult.class, new Response.Listener<AddressResult>() {
            @Override
            public void onResponse(AddressResult response) {
                if (isRefresh)
                    listData.clear();
                if (O2OUtils.checkResponse(SwitchAddressActivity.this, response)) {
                    if (!ArraysUtils.isEmpty(response.data))
                        listData.addAll(response.data);
                }
                mAdapter.notifyDataSetChanged();
                CustomDialogFragment.dismissDialog();
                mLoadMore = false;
                //更新本地地址
                UserInfo userinfo = PreferenceUtils.getObject(SwitchAddressActivity.this, UserInfo.class);
                if (userinfo != null) {
                    userinfo.address = listData;
                    PreferenceUtils.setObject(SwitchAddressActivity.this, userinfo);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                mLoadMore = false;
            }
        });
    }

    private void selCurrAddr(){
        PreferenceUtils.setObject(getApplicationContext(), PreferenceUtils.getObject(SwitchAddressActivity.this, LocAddressInfo.class).toUserAddr());
        setResult(Activity.RESULT_OK);
        finishActivity();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_locate:
                startLocation();
                break;
        }
    }


    private void startLocation() {
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.msg_location, TAG);
        mLocationManager = TencentLocationManager.getInstance(this);
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        //定位请求设置
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(5000);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME);
        request.setAllowCache(false);
        //开始定位
        mLocationManager.requestLocationUpdates(request, this);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        if (("true").equals(mLocateFlag)) {
            title.setText(R.string.switch_addr);
        } else if (("false").equals(mLocateFlag)) {
            title.setText(getResources().getString(R.string.addr_management_title));
        } else if (("my").equals(mLocateFlag)){
            title.setText(getResources().getString(R.string.addr_management_title));
        }

        ((TextView) right).setText(R.string.add);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (O2OUtils.turnLogin(getApplicationContext()))
                    return;
                addressEdit(new Intent());
            }
        });
    }

    /**
     * 地址编辑
     */
    private void addressEdit(Intent intent) {
        intent.setClass(this,AddressAddCommonActivity.class);
        intent.putExtra("isAdd",true);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
        if (error == TencentLocation.ERROR_OK) {
            LocAddressInfo locAddressInfo = new LocAddressInfo();
            locAddressInfo.address = tencentLocation.getAddress()+tencentLocation.getName();
            locAddressInfo.city = tencentLocation.getCity();
            locAddressInfo.province = tencentLocation.getProvince();
            locAddressInfo.streetNo = tencentLocation.getStreetNo();
            locAddressInfo.street = tencentLocation.getStreet();
            locAddressInfo.mapPoint = new PointInfo(tencentLocation.getLatitude(), tencentLocation.getLongitude());
            PreferenceUtils.setObject(SwitchAddressActivity.this, locAddressInfo);
            mLocationManager.removeUpdates(this);
            selCurrAddr();
        } else {
            ToastUtils.show(getApplicationContext(), R.string.msg_error_location);
            mLocationManager.removeUpdates(this);
        }
        CustomDialogFragment.dismissDialog();
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}
