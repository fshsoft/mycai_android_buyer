package com.fanwe.seallibrary.model.event;

import com.fanwe.seallibrary.model.ForumPosts;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-08
 * Time: 15:46
 * 功能: 指定回复某一条评论
 */
public class ForumReplyEvent {
    public ForumPosts forumPosts;
    public ForumReplyEvent(ForumPosts posts){
        this.forumPosts = posts;
    }
}
