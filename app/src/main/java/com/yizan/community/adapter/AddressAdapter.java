package com.yizan.community.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.R;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.fanwe.seallibrary.model.result.AddressResult;
import com.yizan.community.activity.AddressAddCommonActivity;
import com.yizan.community.utils.ApiUtils;
import com.yizan.community.utils.O2OUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地址列表 Adapter
 * Created by ztl on 2015/9/18.
 */
public class AddressAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater mInflater;
    private List<UserAddressInfo> mDatas;
    private int index = 0;
    private View.OnClickListener onClickListener;

    public AddressAdapter(Context context, List<UserAddressInfo> mDatas,View.OnClickListener onClickListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.onClickListener = onClickListener;
    }

    public void setList(List<UserAddressInfo> list){
        if (list != null&& list.size() > 0){
            mDatas.clear();
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView ==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_switch_addr,null);
            holder.name = (TextView)convertView.findViewById(R.id.item_switch_addr_name);
            holder.defaults = (TextView)convertView.findViewById(R.id.text_defaults);
            holder.defaults.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefault(mDatas.get(position).id);
                }
            });
            holder.edits = (TextView)convertView.findViewById(R.id.text_edits);
            holder.edits.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_DATA, mDatas.get(position));
                    intent.setClass(context, AddressAddCommonActivity.class);
                    intent.putExtra("isAdd",false);
                    context.startActivity(intent);
                }
            });
            holder.deletes = (TextView)convertView.findViewById(R.id.text_deletes);
            holder.deletes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initDialog(position);
                }
            });
            holder.mobile = (TextView)convertView.findViewById(R.id.item_switch_addr_mobile);
            holder.address = (TextView)convertView.findViewById(R.id.item_switch_addr_text);
            holder.top = (ImageView)convertView.findViewById(R.id.item_switch_top);
            holder.bottom = (ImageView)convertView.findViewById(R.id.item_switch_bottom);
            holder.img_defaults = (ImageView)convertView.findViewById(R.id.img_defaults);
            holder.img_defaults.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefault(mDatas.get(position).id);
                }
            });
            holder.img_edits = (ImageView)convertView.findViewById(R.id.img_edits);
            holder.img_edits.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_DATA, mDatas.get(position));
                    intent.setClass(context, AddressAddCommonActivity.class);
                    intent.putExtra("isAdd",false);
                    context.startActivity(intent);
                }
            });
            holder.img_deletes = (ImageView)convertView.findViewById(R.id.img_deletes);
            holder.img_deletes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initDialog(position);
                }
            });
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        UserAddressInfo item = mDatas.get(position);
        if (!("").equals(item.name)){
            holder.name.setText(item.name);
        }else{
            holder.name.setText("");
        }
        if (!("").equals(item.mobile)){
            holder.mobile.setText(item.mobile);
        }else{
            holder.mobile.setText("");
        }
        if (!("").equals(item.address)){
            holder.address.setText(item.address);
        }else{
            holder.address.setText("");
        }
        if (item.isDefault){
            holder.top.setVisibility(View.VISIBLE);
            holder.img_defaults.setImageResource(R.drawable.addr_isdefault);
            holder.bottom.setVisibility(View.VISIBLE);
        }else{
            holder.top.setVisibility(View.GONE);
            holder.img_defaults.setImageResource(R.drawable.addr_nodefault);
            holder.bottom.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void initDialog(final int position) {
        AlertDialog.Builder db = new AlertDialog.Builder(context).setTitle("温馨提示").setMessage("确认要删除该地址吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setDelete(mDatas.get(position).id);
                        mDatas.remove(position);
                        notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
        db.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
            }
        });
        db.create().show();
    }

    class ViewHolder{
        public TextView name,defaults,edits,deletes;
        public TextView mobile;
        public TextView address;
        public ImageView top,img_defaults,img_edits,img_deletes;
        public ImageView bottom;
    }

    private void setDefault(int id){
        if (!NetworkUtils.isNetworkAvaiable(context)) {
            ToastUtils.show(context, R.string.msg_error_network);
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(id));
        ApiUtils.post(context, URLConstants.USERADDRESSSETDEFAULT, data, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult baseResult) {
                getAddressList();
            }
        });
    }

    private void setDelete(int id){
        if (!NetworkUtils.isNetworkAvaiable(context)) {
            ToastUtils.show(context, R.string.msg_error_network);
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("id", String.valueOf(id));
        ApiUtils.post(context, URLConstants.USERADDRESSDELETE, data, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult baseResult) {
            }
        });
    }

    private void getAddressList(){
        if (!NetworkUtils.isNetworkAvaiable(context)) {
            ToastUtils.show(context, R.string.msg_error_network);
            return;
        }
        HashMap<String,String> map = new HashMap<>();
        ApiUtils.post(context, URLConstants.USERADDRESSLISTS, map, AddressResult.class, new Response.Listener<AddressResult>() {
            @Override
            public void onResponse(AddressResult addressResult) {
                if (O2OUtils.checkResponse(context, addressResult)){
                    if (addressResult.data != null){
                        mDatas.clear();
                        mDatas.addAll(addressResult.data);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
