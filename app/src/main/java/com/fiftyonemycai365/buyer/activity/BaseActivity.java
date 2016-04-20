package com.fiftyonemycai365.buyer.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.fiftyonemycai365.buyer.R;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.platform.ZYStatConfig;
import com.zongyou.library.util.LogUtils;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.util.ViewFinder;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-17
 * Time: 10:15
 * FIXME
 */
public class BaseActivity extends FragmentActivity {
    public ImageButton mTitleLeft,mTitleLeft2;
    public RelativeLayout mTitleContainer;
    public TextView mTitle,mTitleText;
    public RelativeLayout mTitleRight3;
    public View mTitleRight, mTitleRight2,title2,mTitleBottom;
    private TitleListener mTitleSetListener;
    private EditTitleListener editTitleListener;
    private ContainerTitleListener containerTitleListener;
    protected ViewFinder mViewFinder;

    protected String mPageTag;

    protected void onResume() {
        super.onResume();
        ZYStatConfig.onPageResume(this, getPageTag());
    }

    protected void onPause() {
        super.onPause();
        ZYStatConfig.onPagePause(this, getPageTag());
    }

    protected void setPageTag(String pageTag){
        mPageTag = pageTag;
    }

    public String getPageTag() {
        if (TextUtils.isEmpty(mPageTag) && ZYStatConfig.isNeedStat()) {
            LogUtils.e("STAT_TAG", "NET SEET TAG ====>>: " + this.getClass().getName());
        }
        return mPageTag;
    }

    public interface TitleListener {
        public void setTitle(TextView title, ImageButton left, View right);
    }

    public interface ContainerTitleListener{
        public void setTitle(TextView title, ImageButton left, View right,RelativeLayout viewGroup);
    }

    public interface EditTitleListener {
        public void setTitle(View title, ImageButton left, View right);
    }

    public void setTitleListener(TitleListener listener) {
        setTitleListener(listener, R.layout.titlebar);
    }

    public void setTitleListener_RightImage(TitleListener listener) {
        setTitleListener_RightImage(listener, R.layout.titlebar);
    }

    public void setTitleListener_CenterEdit(EditTitleListener listener){
        setTitleListener_CenterEdit(listener, R.layout.titlebar);
    }

    public void setTitleListener_Container(ContainerTitleListener listener, int titleLayoutRes) {
        setCustomTitle(titleLayoutRes);
        initTitle();
        containerTitleListener = listener;
        mTitleRight2.setVisibility(View.VISIBLE);
        containerTitleListener.setTitle(mTitle,mTitleLeft,mTitleRight2,mTitleContainer);
    }

    public void setTitleListener(TitleListener listener, int titleLayoutRes) {
        setCustomTitle(titleLayoutRes);
        initTitle();
        mTitleSetListener = listener;
        mTitleContainer.setBackgroundResource(R.drawable.mine_title_top_empty);
        mTitleSetListener.setTitle(mTitle, mTitleLeft, mTitleRight);
    }

    public void setTitleListener_RightImage(TitleListener listener, int titleLayoutRes) {
        setCustomTitle(titleLayoutRes);
        initTitle();
        mTitleSetListener = listener;
        mTitleRight2.setVisibility(View.VISIBLE);
        mTitleContainer.setBackgroundResource(R.drawable.mine_title_top_empty);
        mTitleSetListener.setTitle(mTitle, mTitleLeft, mTitleRight2);
    }

    public void setTitleListener_CenterEdit(EditTitleListener listener,int titleLayoutRes){
        setCustomTitle(titleLayoutRes);
        initTitle();
        editTitleListener = listener;
        title2.setVisibility(View.VISIBLE);
        mTitleContainer.setBackgroundResource(R.drawable.mine_title_top_empty);
        editTitleListener.setTitle(title2,mTitleLeft,mTitleRight);
    }

    public void setCustomTitle(int titleRes) {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, titleRes);
    }

    public void initTitle() {
        mTitleLeft2 = (ImageButton)findViewById(R.id.ib_left);
        mTitleBottom = findViewById(R.id.view_least);
        mTitleContainer = (RelativeLayout) findViewById(R.id.title_container);
        mTitleLeft = (ImageButton) findViewById(R.id.title_left);
        mTitle = (TextView) findViewById(R.id.title);
        mTitleRight = findViewById(R.id.title_right);
        mTitleRight2 = findViewById(R.id.ib_right);
        mTitleRight3 = (RelativeLayout)findViewById(R.id.title_right3);
        title2 = findViewById(R.id.title2);
        mTitleText = (TextView)findViewById(R.id.title_right_text);
        if (mTitleLeft != null) {
            mTitleLeft.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finishActivity();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        AppUtils.hideSoftInput(BaseActivity.this);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mViewFinder = new ViewFinder(this);
    }

    @Override
    protected void onDestroy() {
        mViewFinder = null;
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    public void finishActivity() {
        finish();
    }

    public View setViewText(int layoutId, String value) {
        TextView v = (TextView) findViewById(layoutId);
        v.setText(value);
        return v;
    }

    public View setViewText(int layoutId, Spanned value) {
        TextView v = (TextView) findViewById(layoutId);
        v.setText(value);
        return v;
    }

    public View setViewText(int layoutId, int res) {
        TextView v = (TextView) findViewById(layoutId);
        v.setText(res);
        return v;
    }

    public View setViewClickListener(int layoutId, View.OnClickListener listener) {
        View v = findViewById(layoutId);
        v.setOnClickListener(listener);
        return v;
    }

    public View setViewVisible(int layoutId, int visibility) {
        View v = findViewById(layoutId);
        v.setVisibility(visibility);
        return v;
    }

    public View setViewImage(int layoutId, String url, int defaultImage) {
        NetworkImageView v = (NetworkImageView) findViewById(layoutId);
        if (defaultImage > 0) {
            v.setDefaultImageResId(defaultImage);
            v.setErrorImageResId(defaultImage);
        }
        v.setImageUrl(url, RequestManager.getImageLoader());
        return v;
    }

    public View setViewImage(int layoutId, int imageId) {
        ImageView v = (ImageView) findViewById(layoutId);
        if (imageId > 0) {
            v.setImageResource(imageId);
        }
        return v;
    }

    protected void hideSoftInputView() {
        try {
            if (this.getCurrentFocus() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(this.getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if(mTitle != null){
            mTitle.setText(title);
        }
    }

    public FragmentActivity getActivity(){
        return this;
    }
}
