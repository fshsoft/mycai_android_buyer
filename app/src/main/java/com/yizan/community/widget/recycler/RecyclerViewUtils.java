package com.yizan.community.widget.recycler;

import android.content.Context;

/**
 * RecyclerView 使用DataBinding工具类
 * Created by atlas on 15/10/18.
 */
public class RecyclerViewUtils {
    /**
     *
     * @param context
     * @param resId
     * @param formatArgs
     * @return
     */
    public static String getString(Context context,int resId,Object... formatArgs){
        return context.getString(resId,formatArgs);
    }
}
