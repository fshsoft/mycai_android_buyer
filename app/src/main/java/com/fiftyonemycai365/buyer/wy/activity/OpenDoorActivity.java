package com.fiftyonemycai365.buyer.wy.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.DistrictAuthInfo;
import com.fanwe.seallibrary.model.DoorKeysInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.event.DoorUpdateEvent;
import com.fanwe.seallibrary.model.event.OpenDoorEvent;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.DoorKeyListResult;
import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
import com.hzblzx.miaodou.sdk.core.bluetooth.MDAction;
import com.hzblzx.miaodou.sdk.core.bluetooth.MDActionListener;
import com.hzblzx.miaodou.sdk.core.bluetooth.MDResCode;
import com.hzblzx.miaodou.sdk.core.model.MDVirtualKey;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.helper.DoorMgr;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.fiftyonemycai365.buyer.wy.adapter.DoorListAdapter;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenDoorActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener, MDActionListener {
    private DistrictAuthInfo mDistrictAuthInfo;
    private DoorListAdapter mDoorListAdapter;
    private int mCurrDoorId = 0;
    private boolean mIsOpening = false;

    private FragmentActivity mTmpActivity;

    private DoorMgr.DistrictDoor mDistrictDoor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_door);
        setPageTag(TagManager.A_DOOR_LIST);
        EventBus.getDefault().register(this);
        setTitleListener(this);
        mViewFinder.onClick(R.id.cb_open_shake, this);
        mDistrictAuthInfo = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if (null == mDistrictAuthInfo) {
            finishActivity();
            return;
        }

        mDoorListAdapter = new DoorListAdapter(this, new ArrayList<DoorKeysInfo>());
        ListView lv = mViewFinder.find(R.id.lv_list);
        lv.setAdapter(mDoorListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        lv.setEmptyView(mViewFinder.find(android.R.id.empty));

        initView();
        loadData();
    }

    @Override
    public FragmentActivity getActivity() {
        if (mTmpActivity != null) {
            return mTmpActivity;
        }
        return super.getActivity();
    }

    private void initView() {
//        initMiaodou();
        ToggleButton cb = mViewFinder.find(R.id.cb_open_shake);
        mDistrictDoor = DoorMgr.getInstance().getDistrictDoor(mDistrictAuthInfo.district.id);
        if (mDistrictDoor == null) {
            mDistrictDoor = new DoorMgr.DistrictDoor();
        }
        cb.setChecked(mDistrictDoor.isAutoOpen());
        initDoorListData(mDistrictDoor.list);
    }

    public static OpenDoorActivity getInstance(FragmentActivity activity) {
        OpenDoorActivity openDoorActivity = new OpenDoorActivity();
        openDoorActivity.mTmpActivity = activity;
        return openDoorActivity;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_open_door);
    }

    private void initDoorListData(List<DoorKeysInfo> list) {
        if (ArraysUtils.isEmpty(list)) {
            return;
        }
        mDoorListAdapter.setList(list);
        initMDKeyList();

    }

    private void loadData() {
        if (mDistrictAuthInfo == null) {
            ToastUtils.show(getActivity(), R.string.msg_select_district);
            return;
        }

        if (!NetworkUtils.isNetworkAvaiable(getActivity())) {
            ToastUtils.show(getActivity(), R.string.msg_error_network);
            return;
        }
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        Map<String, String> data = new HashMap<>();
        data.put("villagesid", String.valueOf(mDistrictAuthInfo.district.id));
        ApiUtils.post(getActivity(),
                URLConstants.USER_DOOR_KEYS, data, DoorKeyListResult.class, new Response.Listener<DoorKeyListResult>() {
                    @Override
                    public void onResponse(DoorKeyListResult response) {

                        CustomDialogFragment.dismissDialog();
                        if (O2OUtils.checkResponse(getActivity(), response)) {
                            mDistrictDoor.setDoorList(response.data);
                            if (!ArraysUtils.isEmpty(response.data)) {
                                initDoorListData(response.data);
                            } else {
                                ToastUtils.show(getActivity(), R.string.footer_load_end);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(getActivity(), R.string.msg_error);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_open_shake:
                ToggleButton cb = mViewFinder.find(R.id.cb_open_shake);
                setBluetoothOpen(cb.isChecked());
                break;
        }
    }

    public void initMiaodou() {
        // 初始化 Miaodou SDK
        MiaodouKeyAgent.init(getActivity());
        //开门之前需要先初始化蓝牙部分(开启蓝牙等操作)
        MiaodouKeyAgent.registerBluetooth(getActivity());
//        MiaodouKeyAgent.setNeedSensor(DoorMgr.getInstance().getDoorCount() > 0);
    }

    public void initAllDoorsFormCache() {
        List<DoorKeysInfo> list = DoorMgr.getInstance().getAllOpenDoors();
        List<MDVirtualKey> keyList = new ArrayList<MDVirtualKey>();
        if (ArraysUtils.isEmpty(list)) {
            return;
        }
        MDVirtualKey key = null;
        for (int i = 0; i < list.size(); i++) {
            DoorKeysInfo item = list.get(i);
            key = MiaodouKeyAgent.makeVirtualKey(getActivity(), String.valueOf(item.userid), item.keyname, item.community, item.keyid);
            keyList.add(key);
        }
        MiaodouKeyAgent.keyList = keyList;
    }

    public void onEventMainThread(DoorUpdateEvent event) {
        if (mDoorListAdapter != null) {
            mDoorListAdapter.updateItem(event.doorId, event.doorName);
        }
    }

    public void onEventMainThread(OpenDoorEvent event) {
        if (mIsOpening) {
            return;
        }
        try {
            openStart();
            MiaodouKeyAgent.openDoor(getActivity(), event.userId, event.keyName, event.community, event.keyId);

        } catch (Exception e) {
            e.printStackTrace();
            openOver();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MiaodouKeyAgent.setMDActionListener(this);
    }

    @Override
    protected void onDestroy() {
        MiaodouKeyAgent.setNeedSensor(false);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setBluetoothOpen(boolean bOpen) {
        mDistrictDoor.setAutoOpen(bOpen);
        MiaodouKeyAgent.setNeedSensor(bOpen);
    }

    protected void initMDKeyList() {
        List<MDVirtualKey> keyList = new ArrayList<MDVirtualKey>();
        if (mDoorListAdapter.getCount() <= 0) {
            return;
        }
        MDVirtualKey key = null;
        for (int i = 0; i < mDoorListAdapter.getCount(); i++) {
            DoorKeysInfo item = mDoorListAdapter.getItem(i);
            key = MiaodouKeyAgent.makeVirtualKey(getActivity(), String.valueOf(item.userid), item.keyname, item.community, item.keyid);
            keyList.add(key);
        }
        MiaodouKeyAgent.keyList = keyList;
        MiaodouKeyAgent.setNeedSensor(mDistrictDoor.isAutoOpen());
    }

    @Override
    public void scaningDevices() {
        if (!mIsOpening) {
            openStart();
        }
    }

    @Override
    public void findAvaliableKey(MDVirtualKey mdVirtualKey) {

    }

    private void openOver() {
        try {
            CustomDialogFragment.dismissDialog();
            mIsOpening = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openStart() {
        try {
            CustomDialogFragment.show(getActivity().getSupportFragmentManager(), R.string.open_door_opening, getClass().getName());
            mIsOpening = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 开门成功播放提示音
    protected void playTips() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), alert);
        r.play();
    }

    @Override
    public void onComplete(int i, MDVirtualKey mdVirtualKey) {
        try {

            openOver();
            int doorId = 0;
            if (mdVirtualKey != null && mDoorListAdapter != null) {
                DoorKeysInfo info = mDoorListAdapter.getItemByDoorId(mdVirtualKey.name);
                if (info != null) {
                    doorId = info.doorid;
                }
            }
            mCurrDoorId = 0;
            openDoorStat(1, doorId);
            if (i == MDAction.ACTION_OPEN_DOOR) {
                playTips();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    | | ERR_UNKNOWN | 0 | 未知 | | ERR_APP_KEY_MISS | -1000 | APP_ID 缺失 |
//            | ERR_DEVICE_ADDRESS_EMPTY | -2001 | 设备 mac 地址为空 |
//            | ERR_BLUETOOTH_DISABLE | -2002 | 蓝牙未开启 | | ERR_DEVICE_INVALID | -2003 | 设备失效 | | ERR_DEVICE_CONNECT_FAIL | -2004 | 与设备建立连接失败 |
//            | ERR_DEVICE_OPEN_FAIL | -2005 | 开门失败 | | ERR_DEVICE_DISCONNECT | -2006 | 与设备断开连接 |
//            | ERR_DEVICE_PARSE_RESPONSE_FAIL | -2007 | 解析数据失败 |
//            | ERR_APP_ID_MISMATCH | -2008 | APP_KEY 与应用不匹配 |
//            | ERR_NO_AVAILABLE_DEVICES | -2009 | 附近没有可用设备 |
    @Override
    public void onError(int i, int i1) {
        try {
            openOver();
            openDoorStat(1, mCurrDoorId);
            mCurrDoorId = 0;
            switch (i1) {
                case MDResCode.ERR_DEVICE_OPEN_FAIL: {
                    ToastUtils.show(getActivity(), R.string.err_device_open_fail);
                    break;
                }
                case MDResCode.ERR_DEVICE_CONNECT_FAIL:
                    ToastUtils.show(getActivity(), R.string.err_device_connect_fail);
                    break;
                case MDResCode.ERR_BLUETOOTH_DISABLE:
                    ToastUtils.show(getActivity(), R.string.err_bluetooth_disable);
                    break;
                case MDResCode.ERR_DEVICE_ADDRESS_EMPTY: {
                    ToastUtils.show(getActivity(), R.string.err_device_address_empty);
                }
                break;

                case MDResCode.ERR_DEVICE_DISCONNECT:
                    ToastUtils.show(getActivity(), R.string.err_device_disconnect);
                    break;
                case MDResCode.ERR_DEVICE_PARSE_RESPONSE_FAIL:
                    ToastUtils.show(getActivity(), R.string.err_device_parse_response_fail);
                    break;
                case MDResCode.ERR_UNKNOWN: {
                    ToastUtils.show(getActivity(), R.string.err_unknown);
                }
                break;

                case MDResCode.ERR_DEVICE_INVALID: {
                    ToastUtils.show(getActivity(), R.string.err_device_invalid);
                }
                break;
                case MDResCode.ERR_NO_AVAILABLE_DEVICES: {
                    ToastUtils.show(getActivity(), R.string.err_no_available_devices);
                }
                break;
                case MDResCode.ERR_AUTHORIZE_INVALID: {
                    ToastUtils.show(getActivity(), R.string.err_authorize_invalid);
                }
                break;
                default:
                    ToastUtils.show(getActivity(), R.string.err_unknown);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDoorStat(int errorCode,
                              int doorId) {
        try {
            if (!NetworkUtils.isNetworkAvaiable(getActivity())) {
                return;
            }

            int districtId = 0, buildId = 0, roomId = 0;
            UserInfo userInfo = PreferenceUtils.getObject(getActivity(), UserInfo.class);
            if (userInfo != null) {
                districtId = userInfo.propertyUser.district.id;
                buildId = userInfo.propertyUser.build.id;
                roomId = userInfo.propertyUser.room.id;
            }


            Map<String, String> data = new HashMap<>();
            data.put("errorCode", String.valueOf(errorCode));
            data.put("districtId", String.valueOf(districtId));
            data.put("doorId", String.valueOf(doorId));
            data.put("buildId", String.valueOf(buildId));
            data.put("roomId", String.valueOf(roomId));
            ApiUtils.post(getActivity(),
                    URLConstants.USER_OPEN_DOOR_STAT, data, BaseResult.class, new Response.Listener<BaseResult>() {
                        @Override
                        public void onResponse(BaseResult response) {
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class DoorKeysCache {
        public int userId;
        public List<DoorKeysInfo> list;
    }


    public int getUserId() {
        UserInfo userInfo = PreferenceUtils.getObject(getActivity(), UserInfo.class);
        if (userInfo != null) {
            return userInfo.id;
        }
        return 0;
    }

    public static void start(Context context, DistrictAuthInfo authInfo) {
        Intent intent = new Intent(context, OpenDoorActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, authInfo);
        context.startActivity(intent);
    }

}
