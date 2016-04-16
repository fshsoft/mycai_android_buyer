package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class ArticleInfo implements Serializable {
    private static final long serialVersionUID = -5785577506398029021L;

    public int id;
    public int sellerId;
    public String title;
    public String content;
    public String readTime;
    public String createTime;
    public String url;
}
