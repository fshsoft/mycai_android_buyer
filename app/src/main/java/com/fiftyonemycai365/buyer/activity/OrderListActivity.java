package com.fiftyonemycai365.buyer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.utils.TagManager;

public class OrderListActivity extends BaseActivity implements BaseActivity.TitleListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        setTitleListener(this);
        setPageTag(TagManager.ORDER_LIST_ACTIVITY);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_order_list);
    }
}
