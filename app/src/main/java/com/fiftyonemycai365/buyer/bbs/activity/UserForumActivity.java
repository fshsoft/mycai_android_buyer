package com.fiftyonemycai365.buyer.bbs.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.bbs.fragment.UserForumsFragment;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.fiftyonemycai365.buyer.widget.PagerSlidingTabStrip;

public class UserForumActivity extends BaseActivity implements BaseActivity.TitleListener {
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forum);
        setPageTag(TagManager.A_USER_FORUM);
        setTitleListener(this);
        mViewPager = mViewFinder.find(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mViewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.margin_small));
        //向ViewPager绑定PagerSlidingTabStrip
        mPagerSlidingTabStrip = mViewFinder.find(R.id.tabs);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("我的帖子");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] title = {getResources().getString(R.string.tab_bbs_add), getResources().getString(R.string.tab_bbs_reply), getResources().getString(R.string.tab_bbs_praise)};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                    return UserForumsFragment.newInstance(position+1);

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }
}
