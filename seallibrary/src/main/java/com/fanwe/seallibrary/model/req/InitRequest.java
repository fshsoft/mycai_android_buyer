package com.fanwe.seallibrary.model.req;

import android.content.Context;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.utils.DeviceUtils;

import java.io.Serializable;

public class InitRequest implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    //设备信息
    public String systemInfo;
    //设备类型
    public String deviceType;
    //操作系统版本
    public String systemVersion;
    //当前app版本号
    public String appVersion;

    public InitRequest(Context context) {
        super();
        this.systemInfo = DeviceUtils.getDeviceId(context);
        this.deviceType = Constants.DEVICE_TYPE;
        this.systemVersion = android.os.Build.VERSION.RELEASE;
        this.appVersion = DeviceUtils.getPackageInfo(context).versionName;
    }
}
