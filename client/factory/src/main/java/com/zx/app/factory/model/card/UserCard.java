package com.zx.app.factory.model.card;

import com.zx.app.factory.model.Author;
import com.zx.app.factory.model.db.User;

import java.sql.Date;


/**
 * author Afton
 * date 2020/3/5
 * 用户卡片，用于接收服务器数据
 */
public class UserCard implements Author {
    private String id;
    private String name;
    private String phone;
    private String portrait;
    private String desc;
    private int sex = 0;
    /*用户最后的更新时间*/
    private Date modifyAt;
    /*用户关注的人*/
    private int follows;
    /*用户粉丝数量*/
    private int following;
    /*我是否关注该用户*/
    private boolean isFollowing;

    // 缓存一个对应的User, 不能被GSON框架解析使用ø
    private transient User user;

    public User build() {
        if (user == null) {
            User user = new User();
            user.setId(id);
            user.setName(name);
            user.setPhone(phone);
            user.setPortrait(portrait);
            user.setDesc(desc);
            user.setSex(sex);
            user.setModifyAt(modifyAt);
            user.setFollows(follows);
            user.setFollowing(following);
            user.setFollow(isFollowing);
            this.user = user;
        }
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
