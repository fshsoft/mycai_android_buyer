package com.yizan.community.wy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.BuildingInfo;
import com.fanwe.seallibrary.model.DistrictInfo;
import com.fanwe.seallibrary.model.ForumPosts;
import com.fanwe.seallibrary.model.RepairType;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.yizan.community.R;
import com.yizan.community.action.PropertyAction;
import com.yizan.community.activity.BaseActivity;
import com.yizan.community.activity.SwitchAddressActivity;
import com.yizan.community.adapter.PhotoGridAdapter;
import com.yizan.community.adapter.PopBuildingListAdapter;
import com.yizan.community.bbs.activity.PlateListActivity;
import com.yizan.community.fragment.CustomDialogFragment;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.OSSUtils;
import com.yizan.community.utils.PhotoUtils;
import com.yizan.community.utils.TagManager;
import com.yizan.community.widget.ImageSwitcherPopupWindow;
import com.yizan.community.wy.adapter.RepairTypeListAdapter;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class RepairAddActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener, PhotoUtils.PhotoHandler {
    private final String POP_TAG = "photo";

    private DistrictInfo mDistrictInfo;

    private PhotoGridAdapter mPhotoGridAdapter;

    private ImageSwitcherPopupWindow mPopupWinddow;
    private PopupWindow mRepairTypeWindow;
    private PhotoUtils.PhotoParams mPhotoParams;
    private ArrayList<String> mPhotoUris = new ArrayList<String>();
    private PropertyAction mAction;
    private TextView mRepairTypeView;
    private List<RepairType> mRepairTypeList;
    private RepairType mRepairType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_add);
        setPageTag(TagManager.A_REPAIR_ADD);
        OSSUtils.init(this);
        mDistrictInfo = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if (mDistrictInfo == null) {
            finishActivity();
            return;
        }
        setTitleListener(this);
        initPhotoView();
        mViewFinder.onClick(R.id.btn_commit, this);
        mViewFinder.setText(R.id.tv_name, mDistrictInfo.name);
        mAction = new PropertyAction(this);
        mRepairTypeView = mViewFinder.find(R.id.tv_repair_type);
        mRepairTypeView.setOnClickListener(this);

        loadData();
    }

    protected void initReapirType(RepairType repairType) {
        mRepairType = repairType;
        if (mRepairType != null) {
            mRepairTypeView.setText(mRepairType.name);
        } else {
            mRepairTypeView.setText("");
        }
    }

    protected void loadData() {
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.repairTypeList(new ApiCallback<List<RepairType>>() {
            @Override
            public void onSuccess(List<RepairType> data) {
                mRepairTypeList = data;
                CustomDialogFragment.dismissDialog();
                if (!ArraysUtils.isEmpty(mRepairTypeList)) {
                    initReapirType(mRepairTypeList.get(0));
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getContext(), message);
            }
        });
    }

    protected void initRepairTypeWindow() {

        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_list_layout, null);
        ListView listView = (ListView) contentView.findViewById(R.id.lv_list);
        if (mRepairTypeList == null) {
            mRepairTypeList = new ArrayList<>();
        }
        RepairTypeListAdapter popAdapter = new RepairTypeListAdapter(getApplicationContext(), mRepairTypeList);
        listView.setAdapter(popAdapter);

        mRepairTypeWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mRepairTypeWindow.setOutsideTouchable(true);
        mRepairTypeWindow.setFocusable(true);
        mRepairTypeWindow.setTouchable(true);

        mRepairTypeWindow.setBackgroundDrawable(new BitmapDrawable());
        int xoffset = 0;
        int yoffset = (int) getResources().getDimension(R.dimen.margin);
        backgroundAlpha(0.8f);
        mRepairTypeWindow.showAsDropDown(mRepairTypeView, xoffset, yoffset);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initReapirType(mRepairTypeList.get(position));
                mRepairTypeWindow.dismiss();
            }
        });
        mRepairTypeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                selPopState(mRepairTypeView, false);
                backgroundAlpha(1f);
            }
        });
        selPopState(mRepairTypeView, true);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void selPopState(TextView tv, boolean popShow) {
        if (popShow) {
            tv.setTextColor(getResources().getColor(R.color.theme_main_text));
            Drawable arrowDown = getResources().getDrawable(R.drawable.ic_menu_arrow_up);
            arrowDown.setBounds(0, 0, arrowDown.getMinimumWidth(), arrowDown.getMinimumHeight());
            tv.setCompoundDrawables(null, null, arrowDown, null);
        } else {

            tv.setTextColor(getResources().getColor(R.color.theme_black_text));
            Drawable arrowDown = getResources().getDrawable(R.drawable.down_arrow);
            arrowDown.setBounds(0, 0, arrowDown.getMinimumWidth(), arrowDown.getMinimumHeight());
            tv.setCompoundDrawables(null, null, arrowDown, null);
        }
    }

    protected void initPhotoView() {
        mPhotoParams = new PhotoUtils.PhotoParams();
        mPhotoParams.outputX = 600;
        mPhotoParams.outputY = 800;

        mPhotoGridAdapter = new PhotoGridAdapter(getActivity(), new ArrayList<String>(), 4, true);
        GridView gridView = mViewFinder.find(R.id.gv_pics);
        gridView.setAdapter(mPhotoGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftInputView();
                PhotoGridAdapter adapter = (PhotoGridAdapter) parent.getAdapter();
                if (TextUtils.isEmpty(adapter.getItem(position))) {
                    mPopupWinddow = new ImageSwitcherPopupWindow(
                            getActivity(), RepairAddActivity.this,
                            POP_TAG);
                    mPopupWinddow.show(parent);
                    return;
                }
            }
        });
    }


    public static void start(Activity context, DistrictInfo districtInfo, int requestCode) {
        Intent intent = new Intent(context, RepairAddActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, districtInfo);

        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item1:
                mPopupWinddow.dismiss();
                startActivityForResult(PhotoUtils.buildCaptureIntent(mPhotoParams.uri), PhotoUtils.REQUEST_CAMERA);
                break;
            case R.id.item2:
                mPopupWinddow.dismiss();
                startActivityForResult(PhotoUtils.buildGalleryIntent(), PhotoUtils.REQUEST_GALLERY);
                break;
            case R.id.item3:
                mPopupWinddow.dismiss();
                break;
            case R.id.btn_commit:
                commitData();
                break;
            case R.id.tv_repair_type:
                initRepairTypeWindow();
                break;

        }
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
        return this;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("我要报修");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoUtils.handleResult(this, requestCode, resultCode, data);
    }

    private void commitData() {
        if (!NetworkUtils.isNetworkAvaiable(getContext())) {
            ToastUtils.show(getContext(), R.string.loading_err_net);
            return;
        }
        if(mRepairType == null){
            ToastUtils.show(getContext(), "请选择故障类型");
            return;
        }

        EditText et = mViewFinder.find(R.id.et_content);
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            ToastUtils.show(getContext(), "请输入故障描述");
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
            if (PhotoGridAdapter.isLocalFile(path)) {
                uploadSize += 1;
            }
        }

        CustomDialogFragment.show(getSupportFragmentManager(),
                R.string.msg_loading, getClass().getName());
        if (uploadSize > 0) {

            final int size = mPhotoUris.size();

            for (int i = 0; i < size; i++) {
                String path = mPhotoUris.get(i);
                if (!PhotoGridAdapter.isLocalFile(path)) {
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
                        if (mUploadSucc == finalUploadSize) {
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

    protected void uploadData() {
        EditText et = mViewFinder.find(R.id.et_content);
        String content = et.getText().toString().trim();

        mAction.createRepair(mDistrictInfo.id, mRepairType.id, mPhotoUris, content, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtils.show(getActivity(), "添加成功");
                CustomDialogFragment.dismissDialog();
                setResult(Activity.RESULT_OK);
                finishActivity();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                ToastUtils.show(getActivity(), message);
                CustomDialogFragment.dismissDialog();
            }
        });

    }
}
