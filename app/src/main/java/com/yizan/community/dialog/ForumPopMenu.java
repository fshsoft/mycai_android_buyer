package com.yizan.community.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.seallibrary.model.ForumPosts;
import com.yizan.community.R;
import com.yizan.community.utils.ImgUrl;

import java.awt.font.TextAttribute;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-07
 * Time: 18:05
 * 功能:
 */
public class ForumPopMenu extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    public ForumPopMenu(Context context, View.OnClickListener listener, int isLandlord, int sort, int isPraise) {
        super(context);
        mContext = context;
        View contentView = LayoutInflater.from(context)
                .inflate(R.layout.pop_forums_detail, null);
        setContentView(contentView);
        setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setTouchable(true);
        setOutsideTouchable(true);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mOnClickListener = listener;
        contentView.findViewById(R.id.action_view).setOnClickListener(this);
        contentView.findViewById(R.id.action_sort).setOnClickListener(this);
        contentView.findViewById(R.id.action_heart).setOnClickListener(this);
        contentView.findViewById(R.id.action_complain).setOnClickListener(this);
        TextView textView = (TextView) contentView.findViewById(R.id.action_view);
        if (isLandlord != 1) {
            textView.setText("只看楼主");
        } else {
            textView.setText("查看全部");
        }
        textView = (TextView) contentView.findViewById(R.id.action_sort);
        if (sort == 1) {
            textView.setText("升序查看");
        } else {
            textView.setText("降序查看");
        }
        textView = (TextView) contentView.findViewById(R.id.action_heart);
        int resId = R.drawable.ic_menu_red_heart;
        if (isPraise == 1) {
            textView.setText("取消喜欢");
        } else {
            textView.setText("喜欢");
            resId = R.drawable.ic_menu_gray_heart;
        }

        Drawable heart = context.getResources().getDrawable(resId);
        heart.setBounds(0, 0, heart.getMinimumWidth(), heart.getMinimumHeight());
        textView.setCompoundDrawables(heart, null, null, null);

//        setBackgroundDrawable(contentView.getContext().getResources().getDrawable(R.color.theme_background));
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        setBackgroundDrawable(dw);
        update();
    }


    @Override
    public void onClick(View v) {
        if (mOnClickListener != null){
            mOnClickListener.onClick(v);
        }
        dismiss();
    }
}
