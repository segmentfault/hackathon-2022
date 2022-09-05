package com.baidu.paddle.lite.demo.image_classification.Bean.Banner;

import java.util.ArrayList;
import java.util.List;

public class Craft_Banner_Bean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

    public Craft_Banner_Bean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public Craft_Banner_Bean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }

    public static List<Craft_Banner_Bean> getTestData() {
        List<Craft_Banner_Bean> list = new ArrayList<>();
        list.add(new Craft_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/xc_py.png", null, 1));
        list.add(new Craft_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/1.jpg", null, 1));
        list.add(new Craft_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/2.jpg", null, 1));
        list.add(new Craft_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/3.jpg", null, 1));
        list.add(new Craft_Banner_Bean("http://www.twoyl.cn/xczxpt/banner/4.jpg", null, 1));
        return list;
    }

}

