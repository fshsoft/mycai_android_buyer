package com.fiftyonemycai365.buyer.action;

import android.content.Context;

import com.fanwe.seallibrary.ForumIndex;
import com.fanwe.seallibrary.comm.Constants;
import com.fanwe.seallibrary.model.ForumMessage;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fiftyonemycai365.buyer.action.result.ForumIndexResult;
import com.fiftyonemycai365.buyer.action.result.ForumMessgeListResult;
import com.fiftyonemycai365.buyer.action.result.ForumPlateListResult;
import com.fiftyonemycai365.buyer.action.result.ForumPostListResult;
import com.fiftyonemycai365.buyer.action.result.ForumPostResult;
import com.fiftyonemycai365.buyer.helper.ApiCallback;
import com.fanwe.seallibrary.comm.URLConstants;
import com.fanwe.seallibrary.model.ForumPlate;
import com.fanwe.seallibrary.model.ForumPosts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 18:53
 * 功能:
 */
public class BbsAction extends BaseAction {
    public BbsAction(Context context) {
        super(context);
    }

    /**
     * 生活圈首页
     * @param callback
     */
    public void index(final ApiCallback<ForumIndex> callback) {

        call(URLConstants.BBS_HOME_FORUM_INDEX, null, ForumIndexResult.class, callback);
    }

    /**
     * 帖子列表
     * @param plateId
     * @param page
     * @param callback
     */
    public void list(int plateId, int page, ApiCallback<List<ForumPosts>> callback){
            Map<String, Object> data = new HashMap<>();
            data.put("plateId", plateId);
            data.put("page", page);
        data.put("pageSize", Constants.PAGE_SIZE);
        call(URLConstants.BBS_FORUM_LIST, data, ForumPostListResult.class, callback);
    }
    /**
     * 帖子列表
     * @param keywords
     * @param page
     * @param callback
     */
    public void search(String keywords, int page, ApiCallback<List<ForumPosts>> callback){
            Map<String, Object> data = new HashMap<>();
            data.put("keywords", keywords);
            data.put("page", page);
        data.put("pageSize", Constants.PAGE_SIZE);
        call(URLConstants.BBS_FORUM_SEARCH, data, ForumPostListResult.class, callback);
    }

    /**
     * 帖子版块
     * @param callback
     */
    public void plateList(ApiCallback<List<ForumPlate>> callback){
        call(URLConstants.BBS_FORUM_PLATE, null, ForumPlateListResult.class, callback);
    }

    /**
     * 创建/修改 帖子
     * @param id    创建传入0/编辑传入帖子编号
     * @param plateId   板块编号
     * @param title
     * @param content
     * @param images    图片数组
     * @param addressId 地址编号
     */
    public void postSave(int id,
                         int plateId,
                         String title,
                         String content,
                         List<String> images,
                         int addressId,
                         ApiCallback<ForumPosts> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("plateId", plateId);
        data.put("title", title);
        data.put("content", content);
        data.put("images", images);
        data.put("addressId", addressId);

        call(URLConstants.BBS_FORUM_POSTS_SAVE, data, ForumPostResult.class, callback);
    }


    /**
     * 论坛消息列表
     * @param page
     * @param callback
     */
    public void forumMsgList(int page, ApiCallback<List<ForumMessage>> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("page", page);
        data.put("pageSize", Constants.PAGE_SIZE);

        call(URLConstants.BBS_FORUM_MESSAGE_LIST, data, ForumMessgeListResult.class, callback);
    }

    /**
     * 论坛消息删除
     * @param id
     * @param callback
     */
    public void forumMsgDel(int id, ApiCallback<Void> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);

        call(URLConstants.BBS_FORUM_MESSAGE_DEL, data, BaseResult.class, callback);
    }

    /**
     * 我的帖子
     * @param type 1 我的帖子，2 我回复的帖子，3 我点赞的帖子
     * @param page 页码
     * @param callback
     */
    public void userForumList(int type, int page, ApiCallback<List<ForumPosts>> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("page", page);
        data.put("pageSize", Constants.PAGE_SIZE);
        call(URLConstants.BBS_USER_FORUM_LIST, data, ForumPostListResult.class, callback);
    }

    /**
     * 帖子回复
     * @param id
     * @param content
     * @param callback
     */
    public void replyForum(int id, String content, ApiCallback<ForumPosts> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("content", content);
        call(URLConstants.BBS_FORUM_REPLY, data, ForumPostResult.class, callback);
    }

    /**
     * 删除帖子
     * @param id
     * @param callback
     */
    public void forumDel(int id, ApiCallback<Void> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        call(URLConstants.BBS_FORUM_DELETE, data, BaseResult.class, callback);
    }

    /**
     * 帖子举报
     * @param id
     * @param content
     * @param callback
     */
    public void forumComplain(int id, String content, ApiCallback<Void> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("content", content);
        call(URLConstants.BBS_FORUM_COMPLAIN, data, BaseResult.class, callback);
    }

    /**
     * 帖子点赞
     * @param id
     * @param callback
     */
    public void forumPraise(int id, ApiCallback<Void> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("postsId", id);
        call(URLConstants.BBS_FORUM_PRAISE, data, BaseResult.class, callback);
    }


    /**
     * 论坛消息阅读
     * @param id
     * @param callback
     */
    public void forumMsgRead(int id, ApiCallback<Void> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        call(URLConstants.BBS_FORUM_MESSAGE_READ, data, BaseResult.class, callback);
    }

    /**
     * 帖子详情
     * @param id
     * @param isLandlord 1表示只看楼主 0表示所有
     * @param sort 根据时间 （0 升序，1 降序） 排列
     * @param page
     * @param callback
     */
    public void forumDetail(int id, int isLandlord, int sort, int page, ApiCallback<ForumPosts> callback){
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("isLandlord", isLandlord);
        data.put("sort", sort);
        data.put("page", page);
        data.put("pageSize", Constants.PAGE_SIZE);
        call(URLConstants.BBS_FORUM_POSTS_DETAIL, data, ForumPostResult.class, callback);
    }
}
