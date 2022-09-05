package com.baidu.paddle.lite.demo.image_classification.entity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;

public class t_community extends BmobObject {
    private User user;
    private String content;
    private String img_url;
    private String username;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }


    public String getImg_url() {
        return img_url;
    }

    public User getUser() {
        return user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }


    public void setUser(User user) {
        this.user = user;
    }
}
