package com.baidu.paddle.lite.demo.image_classification.Bean.Banner;

import java.util.ArrayList;
import java.util.List;

public class Classroom_Banner_Bean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

    public Classroom_Banner_Bean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public Classroom_Banner_Bean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }

    public static List<Classroom_Banner_Bean> getTestData() {
        List<Classroom_Banner_Bean> list = new ArrayList<>();
        list.add(new Classroom_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/a1.jpg", null, 1));
        list.add(new Classroom_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/a2.jpg", null, 1));
        list.add(new Classroom_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/a3.jpg", null, 1));
        list.add(new Classroom_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/a4.jpg", null, 1));
        return list;
    }

}

