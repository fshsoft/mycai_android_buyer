package com.fiftyonemycai365.buyer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.model.AdvInfo;
import com.fiftyonemycai365.buyer.BuildConfig;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.utils.ImgUrl;
import com.zongyou.library.volley.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-09-17
 * Time: 17:49
 * FIXME
 */
public class BannerPagerAdapter extends PagerAdapter {
    private List<AdvInfo> mList;
    private Context mContext;
    private IItemClick mIItemClick;
    private int mHeight = -1;

    public BannerPagerAdapter(Context context, List<AdvInfo> list, IItemClick iItemClick) {
        mList = list;
        mIItemClick = iItemClick;
        if (mList == null) {
            mList = new ArrayList<AdvInfo>();
        }
    }


    public BannerPagerAdapter(Context context, List<AdvInfo> list, IItemClick iItemClick, int height) {
        mList = list;
        mIItemClick = iItemClick;
         if (mList == null) {
            mList = new ArrayList<AdvInfo>();
        }
        mHeight = height;
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = getView(container, position);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIItemClick != null) {
                    mIItemClick.onPageItemClick(v, mList.get(position));
                }
            }
        });
        container.addView(itemView);
        return itemView;
    }


    public View getView(ViewGroup container, int position) {
        NetworkImageView view = new NetworkImageView(container.getContext());
        if (mHeight != -1) {
            view.setImageUrl(ImgUrl.formatUrl(ImgUrl.getScreenWidth(), mHeight, mList.get(position).image), RequestManager.getImageLoader());
        } else if (BuildConfig.GOOD_DETAIL_SQUARE) {
            view.setImageUrl(ImgUrl.formatUrl(ImgUrl.getScreenWidth(), ImgUrl.getScreenWidth(), mList.get(position).image), RequestManager.getImageLoader());
        } else {
            view.setImageUrl(ImgUrl.heightUrl(R.dimen.banner_height, mList.get(position).image), RequestManager.getImageLoader());
        }
        //        view.setImageUrl(mList.get(position).image, RequestManager.getImageLoader());
//        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //客户提示要显示整个图片，不做裁剪
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public interface IItemClick {
        void onPageItemClick(View v, Object data);
    }
}
