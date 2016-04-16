package com.yizan.community.wy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.DistrictInfo;
import com.fanwe.seallibrary.model.Repair;
import com.yizan.community.R;
import com.yizan.community.action.PropertyAction;
import com.yizan.community.activity.BaseActivity;
import com.yizan.community.bbs.activity.BaseListActivity;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.TagManager;
import com.yizan.community.wy.adapter.RepairListAdapter;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;


public class RepairListActivity extends BaseListActivity<List<Repair>> implements View.OnClickListener, BaseActivity.TitleListener {
    protected static final int ADD_REQUEST_CODE = 0x110;
    private PropertyAction mAction;
    private DistrictInfo mDistrictInfo;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_repair_list;
    }

    @Override
    protected void initView() {
        setPageTag(TagManager.A_REPAIR_LIST);
        mAction = new PropertyAction(getActivity());
        mDistrictInfo = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if(mDistrictInfo == null){
            finishActivity();
            return;
        }
        mViewFinder.onClick(R.id.btn_commit, this);
        setTitleListener(this);
        initData();

    }

    @Override
    protected CommonAdapter setAdapter() {
        return new RepairListAdapter(getActivity(), new ArrayList<Repair>());
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<List<Repair>> callback) {
        mAction.reapirList(mDistrictInfo.id, currentPage, callback);
    }

    @Override
    protected List deliveryResult(List<Repair> result) {
        return result;
    }

    public static void start(Context context, DistrictInfo districtInfo){
        Intent intent = new Intent(context, RepairListActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, districtInfo);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                RepairAddActivity.start(getActivity(), mDistrictInfo, ADD_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("故障报修");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case ADD_REQUEST_CODE:
                initData();
                break;
        }
    }
}
