package com.fanwe.seallibrary.model;

import java.io.Serializable;

/**
 * 消息实体
 * Created by zzl on 2015/9/22.
 */
public class MessageInfo implements Serializable{
    public int id;
    public String content;
    public String title;
    public String createTime;
    public int isRead;//0 未读  1已读
    public int type;//1：普通消息 2：html页面，args为url  3：订单消息，args为订单id 4：佣金消息,点击进入佣金界面
    public String args;
    public boolean  isChick=false;
    public int status;

}
