package com.fiftyonemycai365.buyer.bbs.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.model.ForumMessage;
import com.fiftyonemycai365.buyer.R;
import com.fiftyonemycai365.buyer.activity.BaseActivity;
import com.fiftyonemycai365.buyer.action.BbsAction;
import com.fiftyonemycai365.buyer.bbs.adapter.BbsMsgAdapter;
import com.fiftyonemycai365.buyer.fragment.CustomDialogFragment;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fiftyonemycai365.buyer.utils.TagManager;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class BbsMsgActivity extends BaseListActivity<List<ForumMessage>> implements BaseActivity.TitleListener, BbsMsgAdapter.IOpMessage {
    private BbsAction mAction;

    @Override
    protected void initView() {
        setPageTag(TagManager.A_BBS_MSG);
        mAction = new BbsAction(getActivity());
        setTitleListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 100);
    }

    protected void delMessage(final int id){
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.forumMsgDel(id, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getActivity(), R.string.msg_error_delete_ok);
                ((BbsMsgAdapter) mAdapter).removeById(id);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                ToastUtils.show(getActivity(), message);
                CustomDialogFragment.dismissDialog();
            }
        });
    }
    @Override
    protected CommonAdapter setAdapter() {
        return new BbsMsgAdapter(getActivity(), new ArrayList<ForumMessage>(), this);
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<List<ForumMessage>> callback) {
        mAction.forumMsgList(currentPage, callback);
    }

    @Override
    protected List deliveryResult(List<ForumMessage> result) {
        return result;
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_bbs_msg);
    }

    @Override
    public void onDel(ForumMessage forumMessage, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setTitle(R.string.hint)
                .setMessage(R.string.msg_is_delete_msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                        R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                delMessage(((ForumMessage)mAdapter.getItem(position)).id);

                            }
                        }).setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onRead(ForumMessage forumMessage, int position) {
        mAction.forumMsgRead(forumMessage.id, null);
    }
}
