package com.fiftyonemycai365.buyer.bbs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.ForumPlate;
import com.fanwe.seallibrary.model.ForumPosts;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.activity.SwitchAddressActivity;
import com.fiftyonemycai365.buyer.adapter.PhotoGridAdapter;
import com.fiftyonemycai365.buyer.action.BbsAction;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.O2OUtils;
import com.fiftyonemycai365.buyer.utils.OSSUtils;
import com.fiftyonemycai365.buyer.utils.PhotoUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.fiftyonemycai365.buyer.widget.ImageSwitcherPopupWindow;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class BbsAddActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener, PhotoUtils.PhotoHandler {
    public static final int REQUEST_CODE = 0x199;
    public static final int LOC_REQUEST_CODE = 0x299;
    private final String POP_TAG = "photo";

    protected final static String ARG_PLATE = "plate";
    protected final static String ARG_POST = "post";

    private PhotoGridAdapter mPhotoGridAdapter;

    private ImageSwitcherPopupWindow mPopupWinddow;
    private PhotoUtils.PhotoParams mPhotoParams;
    private ForumPosts mForumPosts;
    private ForumPlate mForumPlate;
    private UserAddressInfo mUserAddressInfo;
    private BbsAction mAction;

    private ArrayList<String> mPhotoUris = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_add);
        setPageTag(TagManager.A_BBS_ADD);
        mForumPlate = getIntent().getParcelableExtra(ARG_PLATE);
        mForumPosts = getIntent().getParcelableExtra(ARG_POST);
        mAction = new BbsAction(getContext());
        OSSUtils.init(getContext());
        if(mForumPosts != null){
            mForumPlate = mForumPosts.plate;
            mUserAddressInfo = mForumPosts.address;
        }
        setTitleListener(this);

        initView();
    }

    protected void initView(){

        mViewFinder.onClick(R.id.ll_plate, this);
        mViewFinder.onClick(R.id.tv_add_contract, this);
        mViewFinder.onClick(R.id.iv_del_contract, this);
        mViewFinder.onClick(R.id.ll_contract, this);
        initPhotoView();
        initViewData();
    }
    protected void initPhotoView(){
        mPhotoParams = new PhotoUtils.PhotoParams();
        mPhotoParams.outputX = 600;
        mPhotoParams.outputY = 800;

        mPhotoGridAdapter = new PhotoGridAdapter(getContext(), new ArrayList<String>(), 4, true);
        GridView gridView = mViewFinder.find(R.id.gv_pics);
        gridView.setAdapter(mPhotoGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftInputView();
                PhotoGridAdapter adapter = (PhotoGridAdapter) parent.getAdapter();
                if (TextUtils.isEmpty(adapter.getItem(position))) {
                    mPopupWinddow = new ImageSwitcherPopupWindow(
                            getActivity(), BbsAddActivity.this,
                            POP_TAG);
                    mPopupWinddow.show(parent);
                    return;
                }
            }
        });
    }
    protected void initForumPlate(){
        if(mForumPlate != null){
            mViewFinder.setText(R.id.tv_plate, mForumPlate.name);
        }
    }
    protected void initUserAddress(){
        if(mUserAddressInfo == null){
            mViewFinder.find(R.id.ll_contract).setVisibility(View.GONE);
            mViewFinder.find(R.id.tv_add_contract).setVisibility(View.VISIBLE);
        }else{
            mViewFinder.find(R.id.ll_contract).setVisibility(View.VISIBLE);
            mViewFinder.find(R.id.tv_add_contract).setVisibility(View.GONE);
            mViewFinder.setText(R.id.tv_user_name, mUserAddressInfo.name);
            mViewFinder.setText(R.id.tv_user_tel, mUserAddressInfo.mobile);
            mViewFinder.setText(R.id.tv_user_addr, mUserAddressInfo.address);
        }
    }
    protected void initViewData(){
        initForumPlate();
        initUserAddress();
        if(mForumPosts == null){
            return;
        }
        EditText et = mViewFinder.find(R.id.et_title);
        et.setText(mForumPosts.title);
        et = mViewFinder.find(R.id.et_content);
        et.setText(mForumPosts.content);
        if(!ArraysUtils.isEmpty(mForumPosts.imagesArr)){
            mPhotoGridAdapter.setList(mForumPosts.imagesArr);
        }

    }
    @Override
    public void setTitle(TextView title, ImageButton left, View right) {

        if(right instanceof TextView){
            if(mForumPosts == null) {
                ((TextView) right).setText(R.string.menu_bbs_add);
                title.setText(R.string.title_bbs_add);
            }else{
                ((TextView) right).setText(R.string.menu_bbs_edit);
                title.setText(R.string.title_bbs_edit);
            }
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSoftInputView();
                    commitData();
                }
            });
        }
    }
    public static void start(Context context, ForumPlate forumPlate, ForumPosts forumPosts){
        if(O2OUtils.turnLogin(context)){
            ToastUtils.show(context, R.string.msg_not_login);
            return;
        }
        Intent intent = new Intent(context, BbsAddActivity.class);
        if(forumPlate != null){
            intent.putExtra(ARG_PLATE, forumPlate);
        }
        if(forumPosts != null){
            intent.putExtra(ARG_POST, forumPosts);
        }
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        hideSoftInputView();
        switch (v.getId()) {

            case R.id.item1:
                mPopupWinddow.dismiss();
//                mPhotoParams.uri = FileCache.createCacheUri(getContext());
                startActivityForResult(PhotoUtils.buildCaptureIntent(mPhotoParams.uri), PhotoUtils.REQUEST_CAMERA);
                break;
            case R.id.item2:
                mPopupWinddow.dismiss();
                startActivityForResult(PhotoUtils.buildGalleryIntent(), PhotoUtils.REQUEST_GALLERY);
                break;
            case R.id.item3:
                mPopupWinddow.dismiss();
                break;
            case R.id.ll_plate:
                PlateListActivity.start(getActivity(), REQUEST_CODE);
                break;
            case R.id.ll_contract:
            case R.id.tv_add_contract:
                Intent intent = new Intent(getActivity(), SwitchAddressActivity.class);
                intent.putExtra("isLocate", "false");
                startActivityForResult(intent, LOC_REQUEST_CODE);
                break;
            case R.id.iv_del_contract:
                mUserAddressInfo = null;
                initUserAddress();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK && data != null){
                mForumPlate = data.getParcelableExtra(Constants.EXTRA_DATA);
                initForumPlate();
            }
            return;
        }else if(requestCode == LOC_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK && data != null){
                mUserAddressInfo = (UserAddressInfo)data.getSerializableExtra(Constants.EXTRA_DATA);
                initUserAddress();
            }
            return;
        }
        PhotoUtils.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onPhotoTaked(Uri uri) {
        mPhotoGridAdapter.addItem(uri.getPath());
    }

    @Override
    public void onPhotoCancel() {

    }

    @Override
    public void onPhotoFailed(String message) {

    }

    @Override
    public PhotoUtils.PhotoParams getPhotoParams() {
        return mPhotoParams;
    }

    @Override
    public Activity getContext() {
        return getActivity();
    }

    private void commitData(){
        if(!NetworkUtils.isNetworkAvaiable(getContext())){
            ToastUtils.show(getContext(), R.string.loading_err_net);
            return;
        }
        if(mForumPlate == null){
            ToastUtils.show(getContext(), R.string.bbs_save_plate_hint);
            return;
        }
        EditText et = mViewFinder.find(R.id.et_title);
        if(TextUtils.isEmpty(et.getText().toString().trim())){
            ToastUtils.show(getContext(), R.string.bbs_save_title_hint);
            return;
        }
        et = mViewFinder.find(R.id.et_content);
        if(TextUtils.isEmpty(et.getText().toString().trim())){
            ToastUtils.show(getContext(), R.string.bbs_save_content_hint);
            return;
        }
        uploadPics();
    }
    private int mUploadSucc = 0;
    private void uploadPics() {

        int uploadSize = 0;
        mUploadSucc = 0;
        mPhotoUris.clear();
        List<String> datas = mPhotoGridAdapter.getDatas();
        for (int i = 0; i < datas.size(); i++) {
            if (TextUtils.isEmpty(datas.get(i))) {
                continue;
            }
            String path = Uri.parse(datas.get(i)).getPath();
            mPhotoUris.add(path);
            if(PhotoGridAdapter.isLocalFile(path)){
                uploadSize += 1;
            }
        }

        CustomDialogFragment.show(getSupportFragmentManager(),
                R.string.msg_loading, getClass().getName());
        if (uploadSize > 0) {

            final int size = mPhotoUris.size();

            for (int i = 0; i < size; i++) {
                String path = mPhotoUris.get(i);
                if(!PhotoGridAdapter.isLocalFile(path)){
                    continue;
                }
                // upload image
                final int index = i;
                final int finalUploadSize = uploadSize;
                OSSUtils.save(path, new SaveCallback() {
                    @Override
                    public void onProgress(String arg0, int arg1, int arg2) {
                    }

                    @Override
                    public void onSuccess(String arg0) {
                        mPhotoUris.set(index, arg0);
                        mUploadSucc += 1;
                        if(mUploadSucc == finalUploadSize){
                            mHandler.sendEmptyMessage(0);
                        }

                    }

                    @Override
                    public void onFailure(String arg0, OSSException arg1) {
                        mHandler.sendEmptyMessage(1);
                    }
                });
            }
        } else {
            mHandler.sendEmptyMessage(0);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getContext(),
                        R.string.msg_failed_add);
            } else {
                uploadData();
            }
        }

    };

    protected void uploadData(){
        EditText et = mViewFinder.find(R.id.et_title);
        String title = et.getText().toString().trim();
        et = mViewFinder.find(R.id.et_content);
        String content = et.getText().toString().trim();
        int id = (mForumPosts == null)?0:mForumPosts.id;
        int addrId = (mUserAddressInfo == null)?0:mUserAddressInfo.id;
        mAction.postSave(id, mForumPlate.id, title, content, mPhotoUris, addrId, new ApiCallback<ForumPosts>() {
            @Override
            public void onSuccess(ForumPosts data) {
                CustomDialogFragment.dismissDialog();

                AlertDialog.Builder builder = new AlertDialog.Builder(BbsAddActivity.this, R.style.MyDialog);
                builder.setMessage(R.string.bbs_save_succ);
                builder.setTitle("成功提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        BbsAddActivity.this.finishActivity();
                    }
                });
                dialog.show();

            }

            @Override
            public void onFailure(int errorCode, String message) {
                ToastUtils.show(getContext(), message);
                CustomDialogFragment.dismissDialog();
            }
        });
    }
}
