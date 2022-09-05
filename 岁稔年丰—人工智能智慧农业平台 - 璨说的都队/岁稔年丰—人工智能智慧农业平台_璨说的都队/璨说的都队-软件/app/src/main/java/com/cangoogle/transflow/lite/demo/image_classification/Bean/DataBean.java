package com.baidu.paddle.lite.demo.image_classification.Bean;

import java.util.ArrayList;
import java.util.List;

public class DataBean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

    public DataBean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public DataBean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }

    public static List<DataBean> getTestData() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean("https://www.twoyl.cn/xczx/1.png", null, 1));
        list.add(new DataBean("https://www.twoyl.cn/xczx/2.png", null, 1));
        list.add(new DataBean("https://www.twoyl.cn/xczx/3.png", null, 1));
        list.add(new DataBean("https://www.twoyl.cn/xczx/4.png", null, 1));
        return list;
    }

}

