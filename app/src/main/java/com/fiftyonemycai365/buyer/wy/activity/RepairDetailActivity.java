package com.fiftyonemycai365.buyer.wy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.Repair;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.activity.ViewImageActivity;
import com.fiftyonemycai365.buyer.utils.ImgUrl;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.ArrayList;

public class RepairDetailActivity extends BaseActivity implements BaseActivity.TitleListener {
    private Repair mRepair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_detail);
        mRepair = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        setPageTag(TagManager.A_REPAIR_DETAIL);
        if (mRepair == null) {
            finishActivity();
            return;
        }
        setTitleListener(this);
        mViewFinder.setText(R.id.tv_time, mRepair.createTime);
        mViewFinder.setText(R.id.tv_status, mRepair.statusStr);
        mViewFinder.setText(R.id.tv_type, mRepair.repairType);
        mViewFinder.setText(R.id.tv_content, mRepair.content);

        LinearLayout layout = mViewFinder.find(R.id.ll_img_container);
        if (!ArraysUtils.isEmpty(mRepair.images)) {
            for (int i = 0; i < mRepair.images.size(); i++) {
                NetworkImageView iv = new NetworkImageView(getActivity());

                iv.setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_small));
                iv.setDefaultImageResId(R.drawable.ic_default_square);
                iv.setTag(String.valueOf(i));
                iv.setImageUrl(ImgUrl.formatUrl(-1, -1, mRepair.images.get(i)), RequestManager.getImageLoader());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                iv.setLayoutParams(layoutParams);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = Integer.parseInt(v.getTag().toString());
                        ViewImageActivity.show(RepairDetailActivity.this, new ArrayList<String>(mRepair.images), index);
                    }
                });
                layout.addView(iv);
            }
        }

    }

    public static void start(Context context, Repair repair) {
        Intent intent = new Intent(context, RepairDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, repair);
        context.startActivity(intent);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("报修记录");
    }
}
