package com.baidu.paddle.lite.demo.image_classification.entity;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private String forbid;

    public String getForbid() {
        return forbid;
    }

    public void setForbid(String forbid) {
        this.forbid = forbid;
    }
}
