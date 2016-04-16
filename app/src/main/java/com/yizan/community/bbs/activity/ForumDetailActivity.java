package com.yizan.community.bbs.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.ForumPosts;
import com.fanwe.seallibrary.model.UserAddressInfo;
import com.fanwe.seallibrary.model.event.ForumReplyEvent;
import com.fanwe.seallibrary.utils.DeviceUtils;
import com.yizan.community.R;
import com.yizan.community.action.BbsAction;
import com.yizan.community.activity.BaseActivity;
import com.yizan.community.activity.ViewImageActivity;
import com.yizan.community.bbs.adapter.BbsReplyListAdapter;
import com.yizan.community.dialog.ForumPopMenu;
import com.yizan.community.fragment.CustomDialogFragment;
import com.yizan.community.helper.ApiCallback;
import com.yizan.community.utils.ImgUrl;
import com.yizan.community.utils.O2OUtils;
import com.yizan.community.utils.TagManager;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.app.IntentUtils;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子详情Activity
 */
public class ForumDetailActivity extends BaseListActivity<ForumPosts> implements BaseActivity.TitleListener, View.OnClickListener {
    private ForumPosts mForumPosts;
    private BbsAction mAction;
    private int mIsLandlord = 0; // 1表示只看楼主 0表示所有
    private int mSort = 1; // （0 升序，1 降序）
    private View mForumDetailView;

    private ForumPosts mHeaderForum;
    private ForumPosts mReplyForum;
    private EditText mReplyEdit;

    @Override
    protected void initView() {
        setPageTag(TagManager.A_FORUM_DETAIL);
        mForumPosts = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if (mForumPosts == null) {
            finishActivity();
            return;
        }
        setTitleListener_RightImage(this);
        mAction = new BbsAction(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        mForumDetailView = layoutInflater.inflate(R.layout.layout_forum_detail, null);
        mGridView.addHeaderView(mForumDetailView);
        initData();
        mGridView.setEmptyView(null);
        mViewFinder.find(android.R.id.empty).setVisibility(View.GONE);
        mViewFinder.onClick(R.id.btn_reply, this);
        mReplyEdit = mViewFinder.find(R.id.et_reply);
        EventBus.getDefault().register(this);

        mGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mReplyEdit.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void initContract(UserAddressInfo addressInfo) {
        if (addressInfo == null) {
            mViewFinder.find(R.id.ll_contract).setVisibility(View.GONE);
            mViewFinder.find(R.id.tv_contract_addr).setVisibility(View.GONE);
            return;
        }
        mViewFinder.find(R.id.ll_contract).setVisibility(View.VISIBLE);
        mViewFinder.find(R.id.tv_contract_addr).setVisibility(View.VISIBLE);
        mViewFinder.setText(R.id.tv_user_name, addressInfo.name);
        if (TextUtils.isEmpty(addressInfo.mobile)) {
            mViewFinder.find(R.id.tv_user_tel).setVisibility(View.INVISIBLE);
        } else {
            mViewFinder.find(R.id.tv_user_tel).setVisibility(View.VISIBLE);
            mViewFinder.setText(R.id.tv_user_tel, addressInfo.mobile);
        }
        if (!TextUtils.isEmpty(addressInfo.address)) {
            mViewFinder.setText(R.id.tv_contract_addr, "地址：" + addressInfo.address);
        }
        mViewFinder.onClick(R.id.tv_user_tel, this);

    }

    private int getUserId() {
        if (mForumPosts == null) {
            return 0;
        }
        if (mForumPosts.user != null) {
            return mForumPosts.user.id;
        }
        return mForumPosts.userId;
    }

    private void initListHeader(ForumPosts forumPosts) {
        if (forumPosts != null) {
            ((TextView) mForumDetailView.findViewById(R.id.tv_heart)).setText(String.valueOf(forumPosts.goodNum));
            ((TextView) mForumDetailView.findViewById(R.id.tv_reply)).setText(String.valueOf(forumPosts.rateNum));
        }
        if (mHeaderForum != null) {
            mHeaderForum = forumPosts;
            return;
        }
        mHeaderForum = forumPosts;
        mForumPosts = mHeaderForum;

        NetworkImageView imageView = (NetworkImageView) mForumDetailView.findViewById(R.id.iv_head);

        imageView.setDefaultImageResId(R.drawable.ic_default_circle);
        imageView.setErrorImageResId(R.drawable.ic_default_circle);
        if (mHeaderForum.user != null) {
            ((TextView) mForumDetailView.findViewById(R.id.tv_name)).setText(mHeaderForum.user.name);
            if (!TextUtils.isEmpty(mHeaderForum.user.avatar))
                imageView.setImageUrl(ImgUrl.squareUrl(R.dimen.image_height_small, mHeaderForum.user.avatar), RequestManager.getImageLoader());
        }
        ((TextView) mForumDetailView.findViewById(R.id.tv_time)).setText(mHeaderForum.createTimeStr);
        if (mHeaderForum.plate != null) {
            ((TextView) mForumDetailView.findViewById(R.id.tv_addr)).setText("来自" + mHeaderForum.plate.name);
        } else {
            ((TextView) mForumDetailView.findViewById(R.id.tv_addr)).setText("");
        }
        ((TextView) mForumDetailView.findViewById(R.id.tv_title)).setText(mHeaderForum.title);
        ((TextView) mForumDetailView.findViewById(R.id.tv_content)).setText(mHeaderForum.content);

        if (ArraysUtils.isEmpty(mHeaderForum.imagesArr)) {
            mForumDetailView.findViewById(R.id.ll_img_container).setVisibility(View.GONE);
        } else {
            LinearLayout layout = (LinearLayout) mForumDetailView.findViewById(R.id.ll_img_container);
            layout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mHeaderForum.imagesArr.size(); i++) {
                NetworkImageView iv = new NetworkImageView(getActivity());

                iv.setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.margin_small));
                iv.setDefaultImageResId(R.drawable.ic_default_square);
                iv.setTag(String.valueOf(i));
                iv.setImageUrl(ImgUrl.formatUrl(-1, -1, mHeaderForum.imagesArr.get(i)), RequestManager.getImageLoader());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                iv.setLayoutParams(layoutParams);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = Integer.parseInt(v.getTag().toString());
                        ViewImageActivity.show(ForumDetailActivity.this, new ArrayList<String>(mHeaderForum.imagesArr), index);
                    }
                });
                layout.addView(iv);
            }

        }

        mViewFinder.onClick(R.id.tv_reply, this);
        mViewFinder.onClick(R.id.tv_heart, this);
        initPraiseNum();
        initContract(mHeaderForum.address);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forum_detail;
    }

    @Override
    protected CommonAdapter setAdapter() {
        return new BbsReplyListAdapter(getActivity(), new ArrayList<ForumPosts>(), getUserId());
    }

    @Override
    protected void requestData(int currentPage, ApiCallback<ForumPosts> callback) {
        mAction.forumDetail(mForumPosts.id, mIsLandlord, mSort, currentPage, callback);
    }

    @Override
    protected List deliveryResult(ForumPosts result) {
        if (result != null) {
            initListHeader(result);
            return result.childs;
        }
        return null;

    }

    public static void start(Context context, ForumPosts forumPosts) {
        if (forumPosts == null) {
            return;
        }
        Intent intent = new Intent(context, ForumDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DATA, forumPosts);
        context.startActivity(intent);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(mForumPosts.title);
        if (right instanceof ImageView) {
            ((ImageView) right).setImageResource(R.drawable.ic_title_more);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(getActivity(), v);
                }
            });
        }
    }


    private void showPopupMenu(final Context context, View ancher) {
        ForumPopMenu popupMenu = new ForumPopMenu(context, this, mIsLandlord, mSort, mForumPosts.isPraise);
//        popupMenu.showAsDropDown(ancher, (int) getResources().getDimension(R.dimen.margin_micro), 0, Gravity.RIGHT|Gravity.TOP);
        popupMenu.showAtLocation(ancher, Gravity.RIGHT | Gravity.TOP, (int) getResources().getDimension(R.dimen.margin_micro), ancher.getHeight() + DeviceUtils.getStatusBarHeight(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_view:
                mIsLandlord = mIsLandlord == 1 ? 0 : 1;
                initData();
                break;
            case R.id.action_sort:
                mSort = mSort == 1 ? 0 : 1;
                initData();
                break;
            case R.id.action_heart:
                praiseForums();
                break;
            case R.id.action_complain:
                Intent intent = new Intent(this, ForumComplainActivity.class);
                intent.putExtra(Constants.EXTRA_DATA, mForumPosts.id);
                startActivity(intent);
                break;
            case R.id.btn_reply:
                replayForum();
                break;
            case R.id.tv_reply:
                doReply(null);
                break;
            case R.id.tv_heart:
                praiseForums();
                break;
            case R.id.tv_user_tel:
                if (mForumPosts != null && mForumPosts.address != null) {
                    try {
                        IntentUtils.dial(this, mForumPosts.address.mobile.replace("-", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void initPraiseNum() {
        int resId = R.drawable.bg_bbs_heart_gray;
        if (mForumPosts.isPraise > 0) {
            resId = R.drawable.bg_bbs_heart_red;
        }
        TextView tv = (TextView) mForumDetailView.findViewById(R.id.tv_heart);
        tv.setBackgroundResource(resId);
        tv.setText("" + mForumPosts.goodNum);
    }

    private void praiseForums() {
        if (O2OUtils.turnLogin(this)) {
            ToastUtils.show(getActivity(), R.string.msg_not_login);
            return;
        }
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.forumPraise(mForumPosts.id, new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomDialogFragment.dismissDialog();
                if (mForumPosts.isPraise == 0) {
                    mForumPosts.isPraise = 1;
                    mForumPosts.goodNum += 1;
                } else {
                    mForumPosts.isPraise = 0;
                    mForumPosts.goodNum -= 1;
                    if (mForumPosts.goodNum < 0) {
                        mForumPosts.goodNum = 0;
                    }
                }
                initPraiseNum();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                ToastUtils.show(getActivity(), message);
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    private void replayForum() {
        if (O2OUtils.turnLogin(this)) {
            ToastUtils.show(getActivity(), R.string.msg_not_login);
            return;
        }
        String value = mReplyEdit.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            ToastUtils.show(getActivity(), "请输入回复内容");
            return;
        }
        int replyId = mForumPosts.id;
        if (mReplyForum != null) {
            replyId = mReplyForum.id;
            value = getReplyHeader() + value;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mReplyEdit.getWindowToken(), 0);
        CustomDialogFragment.show(getSupportFragmentManager(), R.string.loading, getClass().getName());
        mAction.replyForum(replyId, value, new ApiCallback<ForumPosts>() {
            @Override
            public void onSuccess(ForumPosts data) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(getActivity(), "回复成功");
//                ((BbsReplyListAdapter) mAdapter).addFirst(data);
                mReplyForum = null;
                mReplyEdit.setText("");
                initData();

            }

            @Override
            public void onFailure(int errorCode, String message) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(ForumDetailActivity.this, message);
            }
        });
    }

    public void onEventMainThread(ForumReplyEvent event) {
        if (event.forumPosts == null) {
            return;
        }
        doReply(event.forumPosts);
    }

    private void doReply(ForumPosts replyForum) {
        mReplyForum = replyForum;
        if (mReplyEdit != null) {
            mReplyEdit.setHint(getReplyHint());
            mReplyEdit.requestFocus();
            Selection.setSelection(mReplyEdit.getText(), mReplyEdit.getText().length());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mReplyEdit, InputMethodManager.SHOW_FORCED);
        }
    }

    private String getReplyHeader() {
        if (mReplyForum != null && mReplyForum.user != null) {
            return String.format(getResources().getString(R.string.bbs_reply_header), mReplyForum.user.name);
        }
        return "";
    }

    private String getReplyHint() {
        String header = getReplyHeader();
        if (TextUtils.isEmpty(header)) {
            return getString(R.string.bbs_reply_hint);
        }
        return header;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
