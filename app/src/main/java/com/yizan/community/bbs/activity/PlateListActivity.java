package com.yizan.community.bbs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.ForumPlate;
import com.yizan.community.R;
import com.yizan.community.activity.BaseActivity;
import com.yizan.community.action.BbsAction;
import com.yizan.community.bbs.adapter.PlateListAdapter;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.O2OUtils;
import com.yizan.community.utils.TagManager;
import com.yizan.community.wy.activity.PropertyActivity;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ToastUtils;

import java.util.List;

public class PlateListActivity extends BaseActivity implements BaseActivity.TitleListener, AdapterView.OnItemClickListener {
    public final static String ARG_DATA = "data";
    private BbsAction mAction;
    private PlateListAdapter mAdapter;
    private boolean mNeedRet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_activity_plate_list);
        setPageTag(TagManager.A_BBS_PLATE);
        setTitleListener(this);
        mAction = new BbsAction(this);
        mNeedRet = getIntent().getBooleanExtra(ARG_DATA, false);
        loadData();
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        setTitle(R.string.title_plate_list);
    }

    protected void loadData(){
        CustomDialogFragment.getInstance(getSupportFragmentManager(), getClass().getName());
        final ListView lv = mViewFinder.find(R.id.lv_list);
        lv.setEmptyView(mViewFinder.find(android.R.id.empty));
        lv.setOnItemClickListener(this);
        mAction.plateList(new ApiCallback<List<ForumPlate>>() {
            @Override
            public void onSuccess(List<ForumPlate> data) {
                if (data != null) {
                    mAdapter = new PlateListAdapter(getApplicationContext(), data);
                    lv.setAdapter(mAdapter);
                }

                CustomDialogFragment.dismissDialog();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                ToastUtils.show(getApplicationContext(), message);
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mAdapter == null || mAdapter.getCount() <= position){
            return;
        }
        if(mNeedRet){
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_DATA, mAdapter.getItem(position));
            setResult(Activity.RESULT_OK, intent);
            finishActivity();
        }else {
            ForumPlate forumPlate = mAdapter.getItem(position);
            if(forumPlate.id == 1) {
                if(!O2OUtils.turnLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), PropertyActivity.class));
                }else{
                    ToastUtils.show(getActivity(), R.string.msg_not_login);
                }
            }else {
                BbsListActivity.start(getActivity(), forumPlate);
            }
        }
    }

    public static void start(Activity context, int requestCode){
        Intent intent = new Intent(context, PlateListActivity.class);
        if(requestCode > 0){
            intent.putExtra(ARG_DATA, true);
            context.startActivityForResult(intent, requestCode);
        }else{
            context.startActivity(intent);
        }

    }
}
