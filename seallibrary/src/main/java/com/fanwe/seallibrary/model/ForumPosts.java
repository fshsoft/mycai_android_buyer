package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-12-29
 * Time: 17:48
 * 功能: 帖子
 */
public class ForumPosts implements Parcelable{
    public int id; //编号
    public int pid;// 0 表示帖子， 大于0回复编号为pid的帖子
    public UserInfo user;// 创建者
    public String title; //标题
    public String content; //内容
    public List<String> imagesArr;// 图片数组
    public int top;// 0不顶置，1顶置
    public int hot;//0不热门，1热门
    public int rateNum; //好评数量
    public int goodNum;//点赞数量
    public String createTimeStr; //创建时间
    public UserAddressInfo address;//地址对象
    public ForumPlate plate;//板块信息
    public int isCheck;//是否通过审核
    public List<ForumPosts> childs;//帖子回复列表
    public int isPraise; // 是否点赞
    public int flood; // 楼层
    public int userId;
    public ForumPosts replyPosts; // 回复的帖子
    public String replyContent; // 回复的内容
    public ForumPosts() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.pid);
        dest.writeSerializable(this.user);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeStringList(this.imagesArr);
        dest.writeInt(this.top);
        dest.writeInt(this.hot);
        dest.writeInt(this.rateNum);
        dest.writeInt(this.goodNum);
        dest.writeString(this.createTimeStr);
        dest.writeSerializable(this.address);
        dest.writeParcelable(this.plate, 0);
        dest.writeInt(this.isCheck);
        dest.writeTypedList(childs);
        dest.writeInt(this.isPraise);
        dest.writeInt(this.flood);
        dest.writeInt(this.userId);
        dest.writeParcelable(this.replyPosts, 0);
        dest.writeString(this.replyContent);
    }

    protected ForumPosts(Parcel in) {
        this.id = in.readInt();
        this.pid = in.readInt();
        this.user = (UserInfo) in.readSerializable();
        this.title = in.readString();
        this.content = in.readString();
        this.imagesArr = in.createStringArrayList();
        this.top = in.readInt();
        this.hot = in.readInt();
        this.rateNum = in.readInt();
        this.goodNum = in.readInt();
        this.createTimeStr = in.readString();
        this.address = (UserAddressInfo) in.readSerializable();
        this.plate = in.readParcelable(ForumPlate.class.getClassLoader());
        this.isCheck = in.readInt();
        this.childs = in.createTypedArrayList(ForumPosts.CREATOR);
        this.isPraise = in.readInt();
        this.flood = in.readInt();
        this.userId = in.readInt();
        this.replyPosts = in.readParcelable(ForumPosts.class.getClassLoader());
        this.replyContent = in.readString();
    }

    public static final Creator<ForumPosts> CREATOR = new Creator<ForumPosts>() {
        public ForumPosts createFromParcel(Parcel source) {
            return new ForumPosts(source);
        }

        public ForumPosts[] newArray(int size) {
            return new ForumPosts[size];
        }
    };
}
