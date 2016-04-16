package com.fanwe.seallibrary;

import com.fanwe.seallibrary.model.ForumPlate;
import com.fanwe.seallibrary.model.ForumPosts;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2016-01-04
 * Time: 17:06
 * 功能:
 */
public class ForumIndex {
    public List<ForumPlate> plates; // 表示板块
    public List<ForumPosts> posts; // 表示帖子
    public int postsnum; // 我的帖子数量
    public int messagenum; // 我的消息数量
}
