package com.fiftyonemycai365.buyer.wy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.DistrictAuthInfo;
import com.fanwe.seallibrary.model.DistrictInfo;
import com.fanwe.seallibrary.model.PropertyFunc;
import com.fanwe.seallibrary.model.UserInfo;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.action.PropertyAction;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.activity.MainActivity;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.ImgUrl;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.fiftyonemycai365.buyer.wy.adapter.PropertyListAdapter;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 物业管理相关功能
 */
public class PropertyActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    public static final int DISTRICT_REQUEST_CODE = 0x210;
    public static final int AUTH_REQUEST_CODE = 0x211;
    private FragmentActivity mTmpActivity;
    private PropertyAction mAction;
    private DistrictAuthInfo mAuth;
    private boolean mIsLoadFailed;
    private DistrictInfo mDistrictInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        setPageTag(TagManager.A_PROPERTY_HOME);
        setTitleListener(this);
        mAction = new PropertyAction(getActivity());
        mDistrictInfo = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        initView();
        loadData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mDistrictInfo = intent.getParcelableExtra(Constants.EXTRA_DATA);
        loadData();
    }

    @Override
    public FragmentActivity getActivity() {
        if (mTmpActivity != null) {
            return mTmpActivity;
        }
        return super.getActivity();
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_property);
        if (right instanceof TextView) {
            ((TextView) right).setText("切换");
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectDistrict();
                }
            });
        }
    }

    private void loadData() {
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        int districtId = -1;
        if(mDistrictInfo != null){
            districtId = mDistrictInfo.id;
        }
        mAction.getDistrict(districtId, new ApiCallback<DistrictAuthInfo>() {
            @Override
            public void onSuccess(DistrictAuthInfo data) {
                CustomDialogFragment.dismissDialog();
                mAuth = data;
                mIsLoadFailed = false;
                initFailedState(data);
                initPropertyInfo(data);
                initTitleText();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getActivity(), message);
                mAuth = null;
                mIsLoadFailed = true;
                initFailedState(null);
                initPropertyInfo(null);
                initTitleText();
            }
        });
    }

    private void initTitleText(){
        if(mAuth != null && mAuth.district != null) {
            setTitle(mAuth.district.sellerName);
        }else{
            setTitle(R.string.title_activity_property);
        }
    }
    private void initPropertyInfo(DistrictAuthInfo auth) {

        if(auth == null ||
                auth.isCheck != 0 ||
                auth.isProperty != 0 ||
                auth.isVerify != 0){
            mViewFinder.find(R.id.rl_detail).setVisibility(View.GONE);

            return;
        }
        mViewFinder.find(R.id.rl_detail).setVisibility(View.VISIBLE);
        NetworkImageView iv = mViewFinder.find(R.id.iv_head);
        UserInfo userInfo = PreferenceUtils.getObject(getActivity(), UserInfo.class);
        iv.setImageUrl(ImgUrl.squareUrl(R.dimen.image_height_big, userInfo.avatar), RequestManager.getImageLoader());

        mViewFinder.setText(R.id.tv_user_name, auth.name);
        mViewFinder.setText(R.id.tv_user_tel, auth.mobile);
        mViewFinder.setText(R.id.tv_user_unit, auth.build.name + "#" + auth.room.roomNum);
    }

    private void initFailedState(DistrictAuthInfo auth) {
        mViewFinder.find(R.id.ll_status).setVisibility(View.VISIBLE);
        Button btn = mViewFinder.find(R.id.btn_commit);
        TextView tv = mViewFinder.find(R.id.tv_status);
        if (auth == null) {
            if (!mIsLoadFailed) {
                tv.setText("您需要先选择小区才可以申请物业");
                btn.setText("马上去选择");
            } else {
                tv.setText("当前网络不可用，请稍后重试");
                btn.setText("刷新重试");
            }

        } else if (auth.isProperty != 0) {
            tv.setText("很抱歉，" + auth.district.name + "未开通物业版块");
            btn.setText("重新选择小区");
        } else if (auth.isVerify != 0) {
            tv.setText("您未进行身份验证");
            btn.setText("去验证");
        } else if (auth.isCheck != 0) {
            tv.setText("您的身份信息已提交审核，请耐心等待");
            btn.setText("去首页逛逛");
        } else {
            mViewFinder.find(R.id.ll_status).setVisibility(View.INVISIBLE);
        }

    }

    private void initView() {
        mViewFinder.onClick(R.id.ll_owner, this);
        List<PropertyFunc> list = new ArrayList<>();
        list.add(new PropertyFunc(0, R.string.item_property_0, R.drawable.ic_property_0));
//        list.add(new PropertyFunc(1, R.string.item_property_1, R.drawable.ic_property_1));
//        list.add(new PropertyFunc(2, R.string.item_property_2, R.drawable.ic_property_2));
        list.add(new PropertyFunc(1, R.string.item_property_3, R.drawable.ic_property_3));
        list.add(new PropertyFunc(2, R.string.item_property_5, R.drawable.ic_property_5));
        if(O2OUtils.isOpenProperty()) {
            list.add(new PropertyFunc(3, R.string.item_property_4, R.drawable.ic_property_4));
        }
        final PropertyListAdapter adapter = new PropertyListAdapter(this, list);
        GridView gv = mViewFinder.find(R.id.gv_list);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PropertyFunc func = adapter.getItem(position);
                switch (func.id){
                    case 0:
                        NoticeListActivity.start(getActivity(), mAuth.district.sellerId);
                        break;
                    case 1:
                        RepairListActivity.start(getActivity(), mAuth.district);
                        break;
                    case 2:
                        PropertyDetailActivity.start(getActivity(), mAuth.district.id);
                        break;
                    case 3:
                        openDoor();
                        break;

                }

            }
        });
        mViewFinder.onClick(R.id.btn_commit, this);
    }

    public static PropertyActivity getInstance(FragmentActivity activity) {
        PropertyActivity propertyActivity = new PropertyActivity();
        propertyActivity.mTmpActivity = activity;
        return propertyActivity;
    }


    public void openDoor() {
        if(mAuth == null){
            return;
        }

        switch (mAuth.accessStatus) {
            case 1: // 通过
                OpenDoorActivity.start(getActivity(), mAuth);
                break;
            default:
                chooseAuthOpenDoor();
                break;

        }
    }

    private void chooseAuthOpenDoor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(R.string.msg_open_door_apply_title);
        builder.setMessage(R.string.msg_open_door_apply_desc);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        applyOpenDoor();
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        builder.show();
    }

    private void applyOpenDoor() {
        CustomDialogFragment.show(getActivity().getSupportFragmentManager(), R.string.loading, getActivity().getClass().getName());
        mAction.openDoorApply(mAuth.district.id, new ApiCallback<DistrictAuthInfo>() {
            @Override
            public void onSuccess(DistrictAuthInfo data) {
                CustomDialogFragment.dismissDialog();
                if (data != null) {
                    mAuth = data;
                    ToastUtils.show(getActivity(), R.string.msg_submit_guard_ok);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getActivity(), message);
            }
        });
        if (!NetworkUtils.isNetworkAvaiable(getActivity())) {
            ToastUtils.show(getActivity(), R.string.msg_error_network);
        }

    }

    private void startAuthActivity(DistrictAuthInfo info) {
        Intent intent = new Intent(getActivity(), IdentityAuthActivity.class);
        if (info != null) {
            intent.putExtra(Constants.EXTRA_DATA, info);
        }
        getActivity().startActivityForResult(intent, AUTH_REQUEST_CODE);
    }

    private void selectDistrict(){
        UserDistrictActivity.start(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                if(mAuth == null){
                    if(mIsLoadFailed){
                        loadData();
                    }else{
                        // 选择小区
                        selectDistrict();
                    }
                }else if(mAuth.isProperty != 0){
                    // 选择小区
                    selectDistrict();
                }else if(mAuth.isVerify != 0){
                    // 去验证
                    startAuthActivity(mAuth);
                }else if(mAuth.isCheck != 0){
                    // 去逛逛
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.ll_owner:
                OwnerDetailActivity.start(getActivity(), mAuth);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case DISTRICT_REQUEST_CODE:
                if(data != null){
                    mDistrictInfo = data.getParcelableExtra(Constants.EXTRA_DATA);
                    loadData();
                }
                break;
            case AUTH_REQUEST_CODE:
                loadData();
                break;
        }
    }

    public static void start(Context context, DistrictInfo info){
        if(O2OUtils.turnLogin(context)){
            ToastUtils.show(context, R.string.msg_error_not_login);
            return;
        }
        Intent intent = new Intent(context, PropertyActivity.class);
        if(info != null) {
            intent.putExtra(Constants.EXTRA_DATA, info);
        }
        context.startActivity(intent);
    }
}
