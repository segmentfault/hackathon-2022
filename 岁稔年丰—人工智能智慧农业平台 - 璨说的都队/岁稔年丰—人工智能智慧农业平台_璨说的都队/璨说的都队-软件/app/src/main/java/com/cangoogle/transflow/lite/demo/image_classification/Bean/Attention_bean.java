package com.baidu.paddle.lite.demo.image_classification.Bean;

public class Attention_bean {

    private String name,title,like;

    public Attention_bean(String name, String title, String like) {
        this.name = name;
        this.title = title;
        this.like = like;
    }

    public String getLike() {
        return like;
    }


    public void setLike(String like) {
        this.like = like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
