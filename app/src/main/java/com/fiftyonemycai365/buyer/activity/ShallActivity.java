package com.fiftyonemycai365.buyer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiftyonemycai365.buyer.R;

/**
 * Created by fengshuai on 16/5/16.
 */
public class ShallActivity extends BaseActivity implements BaseActivity.TitleListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shall);
        setTitleListener(this);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getResources().getString(R.string.my_shall));
    }



}
