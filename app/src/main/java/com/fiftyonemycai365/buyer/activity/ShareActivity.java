package com.fiftyonemycai365.buyer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.ShareInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.ShareResult;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.SaveImgFile;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享
 */
public class ShareActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener, PlatformActionListener {
    private ImageView weibo,wechat_friends,wechat,qq;
    private NetworkImageView share_image;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setTitleListener(this);
        try {
            file =  SaveImgFile.saveMyBitmap(this,"icon");
        } catch (IOException e) {
            e.printStackTrace();
        }
        share_image = (NetworkImageView) findViewById(R.id.share_image);
        weibo = (ImageView) findViewById(R.id.share_weibo);
        wechat = (ImageView) findViewById(R.id.share_wechat);
        wechat_friends = (ImageView) findViewById(R.id.share_wechat_friends);
        qq = (ImageView) findViewById(R.id.share_qq);
        weibo.setOnClickListener(this);
        wechat_friends.setOnClickListener(this);
        wechat.setOnClickListener(this);
        qq.setOnClickListener(this);
        loadShare();

        ShareSDK.initSDK(this);


    }

   private void shareWeibo(){
       ShareParams sp = new ShareParams();
       sp.setText("My菜，我是你的菜，对你有真爱"+shareUrl);
       sp.setImagePath(file.getPath());
//       sp.setImageUrl(shareUrl);//需要开通高级付费接口才能添加网络图片
//       sp.setUrl(shareUrl);
       Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
       weibo.SSOSetting(false);
       // 设置分享事件回调
       weibo.setPlatformActionListener(this);
       // 执行图文分享
       weibo.share(sp);
   }

    private void shareQQ(){
        ShareParams sp = new ShareParams();
        sp.setText("我是你的菜，对你有真爱");
        sp.setTitle("My菜");
        sp.setImageUrl(file.getPath());
        sp.setTitleUrl(shareUrl);
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        // 设置分享事件回调
        qq.setPlatformActionListener(this);
        // 执行图文分享
        qq.share(sp);
    }

    private void shareWechat(){
        ShareParams sp = new ShareParams();
        sp.setText("我是你的菜，对你有真爱");
        sp.setTitle("My菜");
        sp.setImagePath(file.getPath());
        sp.setShareType(Platform.SHARE_WEBPAGE);//一个链接的收藏
        sp.setUrl(shareUrl);

        Platform wx= ShareSDK.getPlatform(Wechat.NAME);
        wx.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        wx.share(sp);


    }

    private void shareWechatFriends(){
        ShareParams sp = new ShareParams();
        Platform.ShareParams spf = new Platform.ShareParams();
        spf.setTitle("My菜");
        spf.setImagePath(file.getPath());
        spf.setText("我是你的菜，对你有真爱");
        spf.setShareType(Platform.SHARE_WEBPAGE);//链接分享至朋友圈
        spf.setUrl(shareUrl);

        Platform wxf= ShareSDK.getPlatform(WechatMoments.NAME);
        wxf.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        wxf.share(spf);

    }



    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getResources().getString(R.string.my_shall));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_weibo:
                shareWeibo();
                break;
            case R.id.share_wechat:
                shareWechat();
                break;
            case R.id.share_wechat_friends:
                shareWechatFriends();
                break;
            case R.id.share_qq:
                shareQQ();
                break;
        }
    }

    String shareUrl;
    String qrcodeUrl;
    private void initViewData(ShareInfo shareInfo){
        if(null==shareInfo){
            return;
        }
        qrcodeUrl = shareInfo.qrcodeUrl;
        shareUrl= shareInfo.shareUrl;
        setImageUrl(share_image,qrcodeUrl,R.drawable.no_headportaint);
    }

    private void setImageUrl(NetworkImageView imageView, String url, int res) {
        imageView.setDefaultImageResId(res);
        imageView.setErrorImageResId(res);
        imageView.setImageUrl(url, RequestManager.getImageLoader());
    }

    private void loadShare() {
        if (NetworkUtils.isNetworkAvaiable(ShareActivity.this)) {
            UserInfo user = PreferenceUtils.getObject(ShareActivity.this, UserInfo.class);
            Map<String, String> map = new HashMap<>();
            map.put("token", Constants.TOKEN);
            map.put("userId",String.valueOf(user.id));
            map.put("NotEncrypterData","true");
            ApiUtils.post(getApplicationContext(), URLConstants.WY_SHARE,map, ShareResult.class, new Response.Listener<ShareResult>() {
                @Override
                public void onResponse(ShareResult response) {
                    CustomDialogFragment.dismissDialog();
                    if (O2OUtils.checkResponse(ShareActivity.this, response)) {
                        initViewData(response.data);
                    }

                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    CustomDialogFragment.dismissDialog();
                }
            });
        }else {
            ToastUtils.show(ShareActivity.this, R.string.label_check_mobile);
        }

    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        //回调的地方是子线程，进行UI操作要用handle处理
        if (platform.getName().equals(SinaWeibo.NAME)||
                platform.getName().equals(Wechat.NAME)||
                platform.getName().equals(WechatMoments.NAME)||
                platform.getName().equals(QQ.NAME)) {// 判断成功的平台
            handler.sendEmptyMessage(1);
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        //回调的地方是子线程，进行UI操作要用handle处理
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 3;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        //回调的地方是子线程，进行UI操作要用handle处理
        handler.sendEmptyMessage(2);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };



}
