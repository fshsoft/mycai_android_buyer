package com.yizan.community.helper;

import android.app.Activity;

import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
import com.hzblzx.miaodou.sdk.core.bluetooth.MDActionListener;
import com.yizan.community.BuildConfig;
import com.yizan.community.YizanApp;
import com.yizan.community.utils.O2OUtils;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-28
 * Time: 15:55
 * 功能:
 */
public class MiaodouHelper {
    private MDActionListener mMDActionListener;
    protected MiaodouHelper(MDActionListener listener){
        mMDActionListener = listener;
    }

    static public MiaodouHelper getInstance(MDActionListener listener){
        return new MiaodouHelper(listener);
    }

    public void init(Activity activity){

        // 初始化 Miaodou SDK
        MiaodouKeyAgent.init(YizanApp.getInstance());
        //开门之前需要先初始化蓝牙部分(开启蓝牙等操作)
        MiaodouKeyAgent.registerBluetooth(activity);
    }

    public void uninit(){
        if (!O2OUtils.isOpenProperty()) {
            return;
        }
        MiaodouKeyAgent.unregisterMiaodouAgent();
    }

    public void setMDActionListener(MDActionListener listener){
        if (!O2OUtils.isOpenProperty()) {
            return;
        }
        MiaodouKeyAgent.setMDActionListener(listener);
    }

    public void reflashNeedSensor() {
        if (!O2OUtils.isOpenProperty()) {
            return;
        }
        if (!O2OUtils.isLogin(YizanApp.getInstance())) {
            MiaodouKeyAgent.setNeedSensor(false);
            return;
        }

        if ( DoorMgr.getInstance().getDoorCount() > 0) {
            MiaodouKeyAgent.setNeedSensor(true);
        } else {
            MiaodouKeyAgent.setNeedSensor(false);
        }
    }
}
