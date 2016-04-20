package com.fiftyonemycai365.buyer.utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2014-12-8 下午3:24:40
 */
public final class ShareUtils {
    public static final String[] PLATFORMS = new String[]{ WechatMoments.NAME, Wechat.NAME};

    public static void share(Context context, String platform, String logoUrl, String title, String text, String url, PlatformActionListener listener) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(url);
        sp.setText(text);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Log.e("SHARE", "share url: " + url);
        sp.setUrl(url);
//        sp.setUrl("http://wap.sq.test.o2o.fanwe.cn/UserCenter/obtaincoupon");
        sp.setImageUrl(logoUrl);
        Platform plat = ShareSDK.getPlatform(platform);
        plat.setPlatformActionListener(listener);
        plat.share(sp);
    }

    private static int index = 0;

    /**
     * 多平台分享
     *
     * @param context
     * @param platforms
     * @param title
     * @param text
     * @param url
     */
    public static synchronized void share(final Context context, final String[] platforms, final String logoUrl, final String title, final String text, final String url) {
        // 正在分享
        if (index != 0)
            return;

        PlatformActionListener listener = new PlatformActionListener() {

            @Override
            public void onCancel(Platform arg0, int arg1) {
                if (index < platforms.length)
                    ShareUtils.share(context, platforms[index++], logoUrl, title, text, url, this);
                else
                    index = 0;
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                if (index < platforms.length) {
                    ShareUtils.share(context, platforms[index++], logoUrl, title, text, url, this);
                } else
                    index = 0;
            }

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO 判断错误码
                if (index < platforms.length) {
                    ShareUtils.share(context, platforms[index++], logoUrl, title, text, url, this);
                } else
                    index = 0;
            }
        };
        ShareUtils.share(context, platforms[index++], logoUrl, title, text, url, listener);
    }
}
