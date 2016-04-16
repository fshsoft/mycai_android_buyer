package com.yizan.community.wy.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.model.DistrictInfo;
import com.yizan.community.action.PropertyAction;
import com.yizan.community.activity.BaseActivity;
import com.yizan.community.bbs.activity.BaseListActivity;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.TagManager;
import com.yizan.community.wy.adapter.UserDistrictListAdapter;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserDistrictActivity extends BaseListActivity<List<DistrictInfo>> implements BaseActivity.TitleListener, UserDistrictListAdapter.IGoProperty {
    public static final String ARG_PROPERTY = "form_property";
    private PropertyAction mAction;

    @Override
    protected void initView() {
        setPageTag(TagManager.A_USER_DISTRICT);
        setTitleListener(this);
        mAction = new PropertyAction(this);
        initData();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDistrictListAdapter adapter = getAdapter();
                DistrictDetailActivity.start(getActivity(), adapter.getItem(position));
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }

    @Override
    protected CommonAdapter setAdapter() {
        return new UserDistrictListAdapter(getActivity(), new ArrayList<DistrictInfo>(), this);
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<List<DistrictInfo>> callback) {
        mAction.districtList(callback);
    }

    @Override
    protected List deliveryResult(List<DistrictInfo> result) {
        return result;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("我的小区");
        if(right instanceof TextView){
            ((TextView)right).setText("添加");
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserDistrictActivity.this, DistrictListActivity.class);
                    startActivityForResult(intent, DistrictListActivity.REQUEST_CODE);
                }
            });
        }
    }

    @Override
    public void gotoProperty(DistrictInfo info) {

//        Intent intent = new Intent();
//        intent.putExtra(Constants.EXTRA_DATA, info);
//        if(mFromProperty){
//            setResult(Activity.RESULT_OK, intent);
//            finishActivity();
//        }else{
//            intent.setClass(getActivity(), PropertyActivity.class);
//            startActivity(intent);
//        }
        PropertyActivity.start(getActivity(), info);
        finishActivity();
    }

    public static void start(Activity context){
        Intent intent = new Intent(context, UserDistrictActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }
}
