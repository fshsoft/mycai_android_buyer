package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 17:48
 * 功能: 论坛消息
 */
public class ForumMessage {
    public int id; //编号
    public int type; //类型 1:系统消息 2:其他 (后续待增加)
    public String title; //标题
    public String content; //内容
    public String sendTime; // 推送时间
    public String readTime; //
    public ForumPosts posts; // 帖子对象
    public UserInfo user; // 接收者
    public UserInfo relatedUser; // 关联会员
}
