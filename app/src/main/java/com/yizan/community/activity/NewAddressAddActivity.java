package com.yizan.community.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.PoiInfo;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.AddressListResult;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.GeoPoint;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapController;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.OnMapCameraChangeListener;
import com.yizan.community.BuildConfig;
import com.yizan.community.R;
import com.yizan.community.adapter.PoiAdapter;
import com.yizan.community.fragment.CustomDialogFragment;
import com.yizan.community.utils.ApiUtils;
import com.yizan.community.utils.RefreshLayoutUtils;
import com.yizan.community.utils.TagManager;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 选择地址Activity
 * Created by tlzou on 2016/1/26.
 */
public class NewAddressAddActivity extends BaseActivity implements  TencentLocationListener,SwipeRefreshLayout.OnRefreshListener,BaseActivity.TitleListener,View.OnClickListener{

    private ListView mListView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private int mPage = 1;
    protected boolean mLoadMore = false;
    private PoiAdapter adapter;
    private List<PoiInfo> listData = new ArrayList<>();
    private UserAddressInfo mAddrInfo;
    private boolean isAdd;
    private String addr;
    MapView mapView;
    MapController mapController;
    TencentLocationManager locationManager;
    public static NewAddressAddActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        setPageTag(TagManager.NEW_ADDRESS_ADD_ACTIVITY);
        activity = this;
        isAdd = getIntent().getBooleanExtra("isAdd",false);
        addr = getIntent().getStringExtra("addr");
        mAddrInfo = (UserAddressInfo) getIntent().getSerializableExtra(Constants.EXTRA_DATA);
        setTitleListener_RightImage(this);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapController = mapView.getController();
        mapController.setOnMapCameraChangeListener(new OnMapCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LatLng latLng = mapView.getMapCenter();
                volleyGet(latLng.getLatitude(),latLng.getLongitude(),BuildConfig.MAP_ID,true);
            }
        });
        locationManager = TencentLocationManager.getInstance(this);
        locationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        if (!isAdd){
            mapController.setCenter(new GeoPoint((int) mAddrInfo.mapPoint.x, (int) mAddrInfo.mapPoint.x));
            LatLng latLng1 = new LatLng(mAddrInfo.mapPoint.x,mAddrInfo.mapPoint.y);
            mapController.setZoom(16);
            mapController.animateTo(new GeoPoint((int) (latLng1.getLatitude() * 1e6), (int) (latLng1.getLongitude() * 1e6)));
        }else{
            startLocation();
        }

        initView();
    }

    private void initView() {
        mSwipeRefreshLayout = mViewFinder.find(R.id.swipe_container);
        adapter = new PoiAdapter(this,listData);
        mListView = mViewFinder.find(R.id.poi_list);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mViewFinder.find(android.R.id.empty));
        RefreshLayoutUtils.initSwipeRefreshLayout(NewAddressAddActivity.this, mSwipeRefreshLayout, this, true);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && adapter.getCount() >= 20 && adapter.getCount() % 20 == 0) {
                    LatLng latLng = mapView.getMapCenter();
                    volleyGet(latLng.getLatitude(),latLng.getLongitude(),BuildConfig.MAP_ID,false);
                    mLoadMore = true;
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("mapPoint", listData.get(i).mapPoint);
                intent.putExtra("address", listData.get(i).title);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        setViewClickListener(R.id.shuru, this);
    }

    private void startLocation() {
        //定位请求设置
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        request.setAllowCache(false);
        //开始定位
        locationManager.requestLocationUpdates(request, this);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.choose_address);
        ((ImageButton) right).setImageResource(R.drawable.hook);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("mapPoint", listData.get(0).mapPoint);
                intent.putExtra("address", listData.get(0).title);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shuru:
                Intent intent = new Intent(NewAddressAddActivity.this,ChooseAddressByWebActivity.class);
                intent.putExtra(Constants.EXTRA_DATA,mAddrInfo);
                intent.putExtra("isAdd",isAdd);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String reason) {
        if (error == TencentLocation.ERROR_OK){
            //定位成功
            double lat = tencentLocation.getLatitude();
            double lng = tencentLocation.getLongitude();
            LatLng latLng1 = new LatLng(lat, lng);
            mapController.setZoom(16);
            mapController.animateTo(new GeoPoint((int) (latLng1.getLatitude() * 1e6), (int) (latLng1.getLongitude() * 1e6)));
            locationManager.removeUpdates(this);
        }else{
            //定位失败
            ToastUtils.show(NewAddressAddActivity.this, reason);
            locationManager.removeUpdates(this);
        }
    }

    private void volleyGet(final double lat,double lng,String key,final boolean isRefresh) {
        if (!checkLoadState(isRefresh)) {
            return;
        }
        CustomDialogFragment.show(getSupportFragmentManager(),R.string.msg_loading,NewAddressAddActivity.class.getName());
        HashMap<String, String> paras = new HashMap<>(2);
        paras.put("boundary","nearby("+lat+","+lng+",1000)");
        paras.put("page_size","20");
        paras.put("page_index",String.valueOf(mPage));
        paras.put("orderby","_distance");
        paras.put("key",key);
        ApiUtils.get(URLConstants.POI_SEARCH, paras, AddressListResult.class, new Response.Listener<AddressListResult>() {
            @Override
            public void onResponse(AddressListResult addressListResult) {
                CustomDialogFragment.dismissDialog();
                mLoadMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
                if (addressListResult.data.size() > 0){
                    List<PoiInfo> list = new ArrayList<PoiInfo>();
                    for (int i=0;i<addressListResult.data.size();i++){
                        PoiInfo info = new PoiInfo();
                        info.title = addressListResult.data.get(i).title;
                        info.address = addressListResult.data.get(i).address;
                        info.mapPoint = addressListResult.data.get(i).location.lat+","+addressListResult.data.get(i).location.lng;
                        list.add(info);
                    }
                    if (mAddrInfo != null && !TextUtils.isEmpty(addr)){
                        boolean isHave = false;
                        int index = 0;
                        for (int j=0;j<list.size();j++){
                            if (addr.equals(list.get(j).title)){
                                isHave = true;
                                index = j;
                            }
                        }
                        if (isHave){
                            PoiInfo p = new PoiInfo();
                            p.mapPoint = list.get(index).mapPoint;
                            p.title = list.get(index).title;
                            p.address = list.get(index).address;
                            p.request_id = list.get(index).request_id;
                            list.remove(index);
                            list.add(0,p);
                        }else{
                            PoiInfo p = new PoiInfo();
                            p.title = mAddrInfo.detailAddress;
                            p.address = mAddrInfo.detailAddress;
                            p.mapPoint = mAddrInfo.mapPoint.x+","+mAddrInfo.mapPoint.y;
                            list.add(0,p);
                        }
                    }
                    if (isRefresh){
                        listData.clear();
                        listData = list;
                    }else{
                        listData.addAll(list);
                    }
                    adapter.setList(listData);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mLoadMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    protected boolean checkLoadState(boolean isRefresh) {
        if (mLoadMore) {
            return false;
        }
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return false;
        }
        mLoadMore = true;
        if (isRefresh) {
            mSwipeRefreshLayout.setRefreshing(true);
            mPage = 1;
        } else {
            mPage += 1;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        LatLng latLng = mapView.getMapCenter();
        volleyGet(latLng.getLatitude(),latLng.getLongitude(),BuildConfig.MAP_ID,true);
    }


    //添加地图标注
//    private void addMarker(final double lats, final double lngs){
//        mapView.clearAllOverlays();
//        GeoPoint poi = new GeoPoint((int) (lats * 1e6), (int) (lngs * 1e6));
//        Drawable markerImg = getResources().getDrawable(R.drawable.locate_logo);
//        OverlayItem marker = new OverlayItem(poi, "定位", "标注", markerImg);
//        mapView.add(marker);
//    }


}
