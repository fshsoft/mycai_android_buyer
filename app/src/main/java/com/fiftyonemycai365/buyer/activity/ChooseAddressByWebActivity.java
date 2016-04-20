package com.fiftyonemycai365.buyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.PoiInfo;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.AddressListResult;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.fiftyonemycai365.buyer.BuildConfig;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.adapter.ChoosePoiAdapter;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.utils.ApiUtils;
import com.fiftyonemycai365.buyer.utils.TagManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 兴趣点搜索Activity
 * Created by admin on 2016/1/26.
 */
public class ChooseAddressByWebActivity extends BaseActivity implements BaseActivity.EditTitleListener{

    private ListView mListview;
    private ChoosePoiAdapter adapter;
    private List<PoiInfo> listData = new ArrayList<>();
    private TextView use_map_choose_point;
    private UserAddressInfo mAddrInfo;
    private boolean isAdd;
    private RelativeLayout list_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_choose_list);
        isAdd = getIntent().getBooleanExtra("isAdd",false);
        setPageTag(TagManager.CHOOSE_ADDRESS_BY_WEB_ACTIVITY);
        mAddrInfo = (UserAddressInfo) getIntent().getSerializableExtra(Constants.EXTRA_DATA);
        setTitleListener_CenterEdit(this);
        initView();
    }

    private void initView() {
        list_empty = mViewFinder.find(R.id.list_empty);
        adapter = new ChoosePoiAdapter(this,listData);
        mListview = mViewFinder.find(R.id.list);
        mListview.setAdapter(adapter);
//        mListview.setEmptyView(findViewById(android.R.id.empty));
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ChooseAddressByWebActivity.this, AddressAddCommonActivity.class);
                intent.putExtra("mapPoint", listData.get(i).mapPoint);
                intent.putExtra(Constants.EXTRA_DATA,mAddrInfo);
                intent.putExtra("address", listData.get(i).title);
                intent.putExtra("isAdd",isAdd);
                startActivity(intent);
                finishActivity();
                NewAddressAddActivity.activity.finishActivity();
                AddressAddCommonActivity.activity.finishActivity();
            }
        });
        use_map_choose_point = mViewFinder.find(R.id.use_map_choose_point);
        use_map_choose_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
    }


    @Override
    public void setTitle(View title, ImageButton left, View right) {
        ((EditText)title).setVisibility(View.VISIBLE);
        ((EditText) title).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString()))
                    getListData(editable.toString(), BuildConfig.MAP_ID);
            }
        });
    }

    private void getListData(String keyword,String key) {
        CustomDialogFragment.show(getSupportFragmentManager(),R.string.loading,ChooseAddressByWebActivity.class.getName());
        HashMap<String, String> params = new HashMap<>();
        try {
            params.put("keyword", URLEncoder.encode(keyword, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("key", key);
        ApiUtils.get(URLConstants.POI_SUGGESTION, params, AddressListResult.class, new Response.Listener<AddressListResult>() {
            @Override
            public void onResponse(AddressListResult addressListResult) {
                CustomDialogFragment.dismissDialog();
                if (addressListResult.data.size() > 0){
                    List<PoiInfo> list = new ArrayList<PoiInfo>();
                    for (int i=0;i<addressListResult.data.size();i++){
                        PoiInfo info = new PoiInfo();
                        info.title = addressListResult.data.get(i).title;
                        info.address = addressListResult.data.get(i).address;
                        info.mapPoint = addressListResult.data.get(i).location.lat+","+addressListResult.data.get(i).location.lng;
                        list.add(info);
                    }
                    listData.clear();
                    listData.addAll(list);
                    mListview.setVisibility(View.VISIBLE);
                    list_empty.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }else{
                    listData.clear();
                    mListview.setVisibility(View.GONE);
                    list_empty.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomDialogFragment.dismissDialog();
            }
        });
    }
}
